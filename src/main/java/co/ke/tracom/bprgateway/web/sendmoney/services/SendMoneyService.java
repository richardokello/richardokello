package co.ke.tracom.bprgateway.web.sendmoney.services;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.Data;
import co.ke.tracom.bprgateway.web.agenttransactions.services.AgentTransactionService;
import co.ke.tracom.bprgateway.web.bankbranches.entity.BPRBranches;
import co.ke.tracom.bprgateway.web.bankbranches.service.BPRBranchService;
import co.ke.tracom.bprgateway.web.exceptions.custom.InsufficientAccountBalanceException;
import co.ke.tracom.bprgateway.web.exceptions.custom.InvalidMobileNumberException;
import co.ke.tracom.bprgateway.web.sendmoney.data.requests.ReceiveMoneyRequest;
import co.ke.tracom.bprgateway.web.sendmoney.data.requests.SendMoneyRequest;
import co.ke.tracom.bprgateway.web.sendmoney.data.response.SendMoneyResponse;
import co.ke.tracom.bprgateway.web.sendmoney.data.response.SendMoneyResponseData;
import co.ke.tracom.bprgateway.web.sendmoney.entity.MoneySend;
import co.ke.tracom.bprgateway.web.sendmoney.repository.MoneySendRepository;
import co.ke.tracom.bprgateway.web.sms.dto.SMSRequest;
import co.ke.tracom.bprgateway.web.sms.dto.SMSResponse;
import co.ke.tracom.bprgateway.web.sms.services.SMSService;
import co.ke.tracom.bprgateway.web.smsscheduled.entities.ScheduledSMS;
import co.ke.tracom.bprgateway.web.smsscheduled.repository.ScheduledSMSRepository;
import co.ke.tracom.bprgateway.web.switchparameters.XSwitchParameterService;
import co.ke.tracom.bprgateway.web.t24communication.services.T24Channel;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import co.ke.tracom.bprgateway.web.util.TransactionISO8583ProcessingCode;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import co.ke.tracom.bprgateway.web.util.services.UtilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

import static co.ke.tracom.bprgateway.web.sms.dto.SMSRequest.SMS_FUNCTION_RECEIVER;
import static co.ke.tracom.bprgateway.web.sms.dto.SMSRequest.SMS_FUNCTION_SENDER;
import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_PASSWORD;
import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_USERNAME;

@Slf4j
@RequiredArgsConstructor
@Service
public class SendMoneyService {
    private final AgentTransactionService agentTransactionService;
    private final BaseServiceProcessor baseServiceProcessor;
    private final BPRBranchService branchService;
    private final UtilityService utilityService;
    private final T24Channel t24Channel;
    private final TransactionService transactionService;
    private final BPRCreditCardNumberGenerator bprCreditCardNumberGenerator;
    private final DesUtil desUtil;
    private final SMSService smsService;

    private final XSwitchParameterService xSwitchParameterService;
    private final MoneySendRepository moneySendRepository;
    private final ScheduledSMSRepository scheduledSMSRepository;


    public SendMoneyResponse processSendMoneyRequest(SendMoneyRequest request, String transactionRRN) {
        AuthenticateAgentResponse authenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials());

        log.info("Send money transaction [" + transactionRRN + "] processing begins. Request " + request);

        Data agentAuthData = authenticateAgentResponse.getData();
        long agentFloatAccountBalance = agentTransactionService.fetchAgentAccountBalanceOnly(agentAuthData.getAccountNumber());

        try {
            BPRBranches branch = branchService.fetchBranchAccountsByBranchCode(agentAuthData.getAccountNumber());
            String branchAccountID = branch.getId();

            doesAgentHaveSufficientBalance(request, transactionRRN, agentAuthData, agentFloatAccountBalance, branchAccountID);

            String senderMobileNo = request.getSenderMobileNo().trim();
            validateMobileNumberLength(transactionRRN, senderMobileNo.length());

            String receiverMobile = request.getRecipientMobileNo().trim();
            compareSenderRecipientMobileNumbers(transactionRRN, receiverMobile.equalsIgnoreCase(senderMobileNo));

            String configuredSendMoneySuspenseAccount = xSwitchParameterService.fetchXSwitchParamValue("SENDMONEYSUSPENSE");
            String firstTransactionPaymentDetails = senderMobileNo + "/" + receiverMobile;
            String secondTransactionPaymentDetails = agentAuthData.getNames() + " " + agentAuthData.getAccountNumber();
            String thirdTransactionPaymentDetails = "SEND MONEY";

            String sendMoneyOFSMsg = bootstrapSendMoneyOFSMsg(request.getAmount(), transactionRRN, agentAuthData, branchAccountID, configuredSendMoneySuspenseAccount, firstTransactionPaymentDetails, secondTransactionPaymentDetails, thirdTransactionPaymentDetails);
            String tot24str = String.format("%04d", sendMoneyOFSMsg.length()) + sendMoneyOFSMsg;
            log.info("Send money transaction [" + transactionRRN + "] OFS is ready. " + tot24str);

            String nationalIdDocumentName = request.getSenderNationalIDType().equals("0") ? "NID" : "OTHERS";
            String tid = "PC";

            T24TXNQueue tot24 = prepareT24Transaction(transactionRRN, agentAuthData, configuredSendMoneySuspenseAccount, tot24str, tid);

            processSendMoneyTransaction(tot24);

            if ((tot24.getT24responsecode().equalsIgnoreCase("1"))) {
                return processSuccessfulSendMoneyT24Transaction(request, transactionRRN, agentAuthData, branchAccountID,
                        senderMobileNo, receiverMobile, firstTransactionPaymentDetails, secondTransactionPaymentDetails,
                        thirdTransactionPaymentDetails, nationalIdDocumentName, tid, tot24, authenticateAgentResponse);
            } else {
                return processFailedSendMoneyT24Transaction(transactionRRN, agentAuthData, tot24.getT24reference());
            }
        } catch (Exception w) {
            w.printStackTrace();
            log.info("Send money transaction error : " + w.getMessage());
        }
        return SendMoneyResponse.builder()
                .status("116")
                .message("Transaction processing failed. Please try again")
                .data(null)
                .build();
    }

    private SendMoneyResponse processSuccessfulSendMoneyT24Transaction(SendMoneyRequest request, String transactionRRN,
                                                                       Data agentAuthData, String branchAccountID,
                                                                       String senderMobileNo, String receiverMobile,
                                                                       String firstTransactionPaymentDetails,
                                                                       String secondTransactionPaymentDetails,
                                                                       String thirdTransactionPaymentDetails,
                                                                       String nationalIdDocumentName, String tid,
                                                                       T24TXNQueue tot24, AuthenticateAgentResponse authenticateAgentResponse) {
        SendMoneyResponseData data = SendMoneyResponseData.builder().build();
        try {
            String charges = tot24.getTotalchargeamt();
            System.out.println("Charges " + charges);
            String formattedCharge = charges.replace("RWF", "");
            System.out.println("Transaction charge : " + formattedCharge);
            //send money commission save

            String collectionCommissionAccount = xSwitchParameterService.fetchXSwitchParamValue("SENDMONEYCOMMISSIONCOLLECTIONACC");
            this.saveSendMoneyCommission(formattedCharge, tid, transactionRRN, agentAuthData.getAccountNumber(),
                    firstTransactionPaymentDetails, secondTransactionPaymentDetails,
                    thirdTransactionPaymentDetails, branchAccountID, collectionCommissionAccount);

            data.setCharges(Double.parseDouble(formattedCharge));
            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "SEND MONEY", "1200",
                    request.getAmount(), "000",
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());
        } catch (Exception e) {
            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "SEND MONEY", "1200",
                    request.getAmount(), "098",
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());
            System.out.println(
                    "Unable to get charges for send money transaction reference " + transactionRRN);
            e.printStackTrace();
        }
        data.setT24Reference(tot24.getT24reference());
        data.setRrn(transactionRRN);
        data.setUsername(agentAuthData.getUsername());
        data.setNames(agentAuthData.getNames());
        data.setBusinessName(agentAuthData.getBusinessName());
        data.setLocation(agentAuthData.getLocation());

        data.setTid(authenticateAgentResponse.getData().getTid());
        data.setMid(authenticateAgentResponse.getData().getMid());
        try {
            String virtualBIN = xSwitchParameterService.fetchXSwitchParamValue("CARDLESSTXNBIN");
            String CARDLESS_TXN_BIN = "123456";
            String virtualCardBIN = virtualBIN.equals("") ? CARDLESS_TXN_BIN : virtualBIN;
            String vCardNo = bprCreditCardNumberGenerator.generate(virtualCardBIN, 12);
            String generatedCardNo = desUtil.encryptPlainText(vCardNo);
            if (generatedCardNo != null) {
                Random generator = new Random();
                String passCode = String.format("%06d", 100000 + generator.nextInt(899999));

                saveSendMoneyTransaction(request, agentAuthData, senderMobileNo, receiverMobile, nationalIdDocumentName, tot24.getT24reference(), generatedCardNo, passCode);
                SMSRequest recipientMessage = saveRecipientMessage(request, transactionRRN, senderMobileNo, receiverMobile, passCode, vCardNo);
                SMSRequest senderMessage = saveSenderMessage(request, transactionRRN, senderMobileNo, receiverMobile, passCode, vCardNo);


                String fdiSMSAPIAuthToken = smsService.getFDISMSAPIAuthToken();
                SMSResponse recipientResponse = smsService.sendFDISMSRequest(fdiSMSAPIAuthToken, recipientMessage);
                SMSResponse senderResponse = smsService.sendFDISMSRequest(fdiSMSAPIAuthToken, senderMessage);

                return SendMoneyResponse.builder()
                        .status("00")
                        .message("Send money transaction processed successfully.")
                        .data(data)
                        .build();

            } else {
                System.out.println(
                        "Card less vcard generation error for rrn " + transactionRRN);
                return SendMoneyResponse.builder()
                        .status("118")
                        .message("Transaction failed. Unable to generate virtual card. Please try again.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=================================================" + e.getMessage());
            return SendMoneyResponse.builder()
                    .status("118")
                    .message("Transaction failed. Please try again.")
                    .data(null)
                    .build();
        }
    }

    private SMSRequest saveRecipientMessage(SendMoneyRequest request, String transactionRRN, String senderMobileNo,
                                            String receiverMobile, String passCode, String vCardNo) {
        String recipientMessage = recipientMessage(request, senderMobileNo, vCardNo);


        while (recipientMessage.length() > 0) {
            ScheduledSMS scheduledSMSTransaction = new ScheduledSMS();
            scheduledSMSTransaction.setSentstatus(1);
            scheduledSMSTransaction.setAttempts(0);
            scheduledSMSTransaction.setReceiverphone(receiverMobile);
            scheduledSMSTransaction.setTxnref(transactionRRN);

            if (recipientMessage.length() <= 160) {
                scheduledSMSTransaction.setMessage(utilityService.encryptSensitiveData(recipientMessage));
                recipientMessage = "";
            } else {
                scheduledSMSTransaction.setMessage(
                        utilityService.encryptSensitiveData(recipientMessage.substring(0, 160)));
                recipientMessage = recipientMessage.substring(160);
            }
            scheduledSMSRepository.save(scheduledSMSTransaction);
        }
//        SMSRequest smsObject = getNewSMSRequest(request, senderMobileNo, receiverMobile, passCode, vCardNo, SMS_FUNCTION_RECEIVER);
        return getFDISMSRequest(recipientMessage, receiverMobile, SMS_FUNCTION_RECEIVER);
    }

    /**
     * Added on 07/15/2021
     * BPR Switched SMS API and added a custom way of sending Message
     * Message body format:  AMOUNT/ PHONE NUMBER IN MESSAGE / PASSCODE / CARD  NO
     *
     * @param request
     * @param senderMobileNo
     * @param receiverMobile
     * @param passCode
     * @param vCardNo
     * @param smsFunctionReceiver
     * @return
     */
    @Deprecated
    private SMSRequest getNewSMSRequest(SendMoneyRequest request, String senderMobileNo, String receiverMobile, String passCode, String vCardNo, String smsFunctionReceiver) {
        return SMSRequest.builder()
                .recipient(receiverMobile)
                .SMSFunction(smsFunctionReceiver)
                .message(new StringBuilder().append(request.getAmount())
                        .append("/")
                        .append(senderMobileNo)
                        .append("/")
                        .append(passCode)
                        .append("/")
                        .append(vCardNo).toString())
                .build();
    }


    private SMSRequest getFDISMSRequest(String message, String receiverMobile, String smsFunctionReceiver) {
        return SMSRequest.builder()
                .recipient(receiverMobile)
                .SMSFunction(smsFunctionReceiver)
                .message(message)
                .build();
    }

    private SMSRequest saveSenderMessage(SendMoneyRequest request, String transactionRRN,
                                         String senderMobileNo, String receiverMobile, String passCode,
                                         String vCardNo) {

        String senderMessage = senderMessage(request, receiverMobile, passCode);
        // Insert Second SMS
        String SMSContent = utilityService.encryptText(senderMessage);
        ScheduledSMS scheduledSMS = new ScheduledSMS();
        scheduledSMS.setSentstatus(1);
        scheduledSMS.setAttempts(0);
        scheduledSMS.setMessage(SMSContent);
        scheduledSMS.setReceiverphone(senderMobileNo);
        scheduledSMS.setTxnref(transactionRRN);
        scheduledSMSRepository.save(scheduledSMS);

        return getFDISMSRequest(senderMessage, receiverMobile, SMS_FUNCTION_SENDER);
    }

    private String senderMessage(SendMoneyRequest request, String receiverMobile, String passCode) {
        return "You have successfully sent RWF"
                + request.getAmount()
                + " to "
                + receiverMobile
                + " "
                + "via BPR Cardless transfer. Kindly share passcode with the recipient."
                + "passcode : "
                + passCode
                + ". Thanks for banking with us.";
    }

    private String recipientMessage(SendMoneyRequest request, String senderMobileNo, String cardNo) {
        return "You have received RWF "
                + request.getAmount()
                + " from "
                + senderMobileNo
                + " to "
                + "withdraw at a BPR agent. Provide VCARD no: "
                + cardNo
                + ", "
                + "Sender will share the passcode. MURAKOZE";
    }

    private void saveSendMoneyTransaction(SendMoneyRequest request, Data agentAuthData, String senderMobileNo,
                                          String receiverMobile, String nationalIdDocumentName, String t24Reference,
                                          String generatedCardNo, String token) {
        MoneySend ms = new MoneySend();
        ms.setChannel("POS");
        ms.setAmount(String.valueOf(request.getAmount()));
        ms.setSendernumber(senderMobileNo);
        ms.setRecevernumber(receiverMobile);
        ms.setAgentid(agentAuthData.getAccountNumber());
        ms.setMstoken(token);
        ms.setCno(generatedCardNo);
        ms.setFulfilmentstatus(0);

        String senderNationalID = request.getSenderNationalID();
        ms.setSendernationalid(senderNationalID);
        ms.setTypeofid(nationalIdDocumentName);
        ms.setSendmoneylegt24ref(t24Reference);

        moneySendRepository.save(ms);
    }

    private SendMoneyResponse processFailedSendMoneyT24Transaction(String transactionRRN, Data agentAuthData, String t24Reference) {
        SendMoneyResponseData sendMoneyResponseData = SendMoneyResponseData.builder()
                .T24Reference(t24Reference)
                .charges(0)
                .rrn(transactionRRN)
                .build();

        sendMoneyResponseData.setUsername(agentAuthData.getUsername());
        sendMoneyResponseData.setNames(agentAuthData.getNames());
        sendMoneyResponseData.setBusinessName(agentAuthData.getBusinessName());
        sendMoneyResponseData.setLocation(agentAuthData.getLocation());

        return SendMoneyResponse.builder()
                .status("098")
                .message("Transaction failed. Unable to post transaction to remote server.")
                .data(sendMoneyResponseData)
                .build();
    }

    private void processSendMoneyTransaction(T24TXNQueue tot24) {
        final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue("T24_IP");
        final String t24Port = xSwitchParameterService.fetchXSwitchParamValue("T24_PORT");
        t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);
        tot24.setPostedstatus("1");
        transactionService.updateT24TransactionDTO(tot24);
    }

    private T24TXNQueue prepareT24Transaction(String transactionRRN, Data agentAuthData, String configuredSendMoneySuspenseAccount, String tot24str, String tid) {
        T24TXNQueue tot24 = new T24TXNQueue();
        tot24.setTid(tid);
        tot24.setRequestleg(tot24str);
        tot24.setStarttime(System.currentTimeMillis());

        String channel = "1510";
        tot24.setTxnchannel(channel);
        tot24.setGatewayref(transactionRRN);
        tot24.setProcode(TransactionISO8583ProcessingCode.SEND_MONEY.getCode());
        tot24.setDebitacctno(agentAuthData.getAccountNumber());
        tot24.setCreditacctno(configuredSendMoneySuspenseAccount);
        return tot24;
    }

    private String bootstrapSendMoneyOFSMsg(double amount, String transactionRRN, Data agentAuthData, String branchAccountID, String configuredSendMoneySuspenseAccount, String firstTransactionPaymentDetails, String secondTransactionPaymentDetails, String thirdTransactionPaymentDetails) {
        return "0000AFUNDS.TRANSFER,SEND.MONEY/I//1/0,"
                + ""
                + MASKED_T24_USERNAME
                + "/"
                + MASKED_T24_PASSWORD
                + "/"
                + branchAccountID
                + ",,"
                + "TRANSACTION.TYPE::=ACMO,"
                + "DEBIT.ACCT.NO::="
                + agentAuthData.getAccountNumber()
                + ","
                + "DEBIT.AMOUNT::="
                + amount
                + ","
                + "CREDIT.ACCT.NO::="
                + configuredSendMoneySuspenseAccount
                + ","
                + "DEBIT.CURRENCY::=RWF,"
                + "TCM.REF::="
                + transactionRRN
                + ","
                + "PAYMENT.DETAILS:1:= "
                + utilityService.sanitizePaymentDetails(firstTransactionPaymentDetails, "Send Money").trim()
                + ","
                + "PAYMENT.DETAILS:2:="
                + secondTransactionPaymentDetails.trim()
                + ","
                + "PAYMENT.DETAILS:3:="
                + thirdTransactionPaymentDetails.trim();
    }

    private void compareSenderRecipientMobileNumbers(String RRN, boolean sameMobileNo) {
        if (sameMobileNo) {
            log.info("Send money transaction [" + RRN + "] failed. Sender mobile number and recipient mobile number are the same.");
            throw new InvalidMobileNumberException("Transaction failed. Sender mobile number and recipient mobile number are the same.");
        }
    }

    private void validateMobileNumberLength(String RRN, int length) {
        if (length != 10) {
            log.info("Send money transaction [" + RRN + "] failed. Invalid mobile number length.");
            throw new InvalidMobileNumberException("Invalid mobile number length");
        }
    }

    private void doesAgentHaveSufficientBalance(SendMoneyRequest request, String transactionRRN, Data agentAuthData, long agentFloatAccountBalance, String branchAccountID) {
        long chargesLong = fetchSendMoneyTransactionCharges(agentAuthData.getAccountNumber(), branchAccountID, transactionRRN, request.getAmount());
        System.out.printf(
                "%n Transaction %s T24 Charges (%d) and agent float account balance (%d) against send money amount (%f) %n",
                transactionRRN, chargesLong, agentFloatAccountBalance, request.getAmount());

        if (request.getAmount() > (agentFloatAccountBalance + chargesLong)) {
            throw new InsufficientAccountBalanceException("Insufficient agent balance to process this transaction. Top up agent float to continue.");
        }
    }

    public long fetchSendMoneyTransactionCharges(String agentFloatAccount, String branchAccountID,
                                                 String originalTransactionReference, double amount) {

        String validationReferenceNo = RRNGenerator.getInstance("BP").getRRN();
        System.out.printf(
                "Send Money Charges generated transaction ID %s for POS request transaction ID %s %n",
                validationReferenceNo, originalTransactionReference);

        try {
            String configuredSendMoneySuspenseAccount = xSwitchParameterService.fetchXSwitchParamValue("SENDMONEYSUSPENSE");
            String tid = "PC";
            String cardlesssendOFS =
                    "0000AFUNDS.TRANSFER,SEND.MONEY/I/VALIDATE/1/0,"
                            + MASKED_T24_USERNAME
                            + "/"
                            + MASKED_T24_PASSWORD
                            + ""
                            + "/"
                            + branchAccountID
                            + ""
                            + ",,TRANSACTION.TYPE::=ACSM,"
                            + "DEBIT.ACCT.NO::="
                            + agentFloatAccount
                            + ","
                            + "DEBIT.AMOUNT::="
                            + amount
                            + ","
                            + "CREDIT.ACCT.NO::="
                            + configuredSendMoneySuspenseAccount
                            + ",DEBIT.CURRENCY::=RWF,"
                            + "TCM.REF::="
                            + validationReferenceNo
                            + ","
                            + "DEBIT.THEIR.REF::='SENDMONEY',"
                            + "CREDIT.THEIR.REF::='SENDMONEY'";

            String tot24str = String.format("%04d", cardlesssendOFS.length()) + cardlesssendOFS;

            T24TXNQueue tot24 = new T24TXNQueue();
            tot24.setRequestleg(tot24str);
            tot24.setStarttime(System.currentTimeMillis());
            String channel = "PC";
            tot24.setTxnchannel(channel);
            tot24.setGatewayref(validationReferenceNo);
            tot24.setPostedstatus("0");
            tot24.setProcode("410000");
            tot24.setTid(tid);

            final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue("T24_IP");
            final String t24Port = xSwitchParameterService.fetchXSwitchParamValue("T24_PORT");
            t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);
            transactionService.updateT24TransactionDTO(tot24);

            System.out.printf(
                    "Send Money Charges Request : Transaction %s has been queued for T24 Processing. %n",
                    validationReferenceNo);

            if (tot24.getT24responsecode().equalsIgnoreCase("1")) {
                try {

                    String charges = tot24.getTotalchargeamt();
                    if (null != charges) {
                        System.out.printf(
                                "Send Money Charges Request [Success]: Transaction %s has transaction charge %s from T24 Processing. %n",
                                validationReferenceNo, charges);
                        return Long.parseLong(charges.replace("RWF", ""));

                    } else {
                        System.out.printf(
                                "Send Money Charges Request [Failed]: Transaction %s has no transaction charges from T24 Processing. %n",
                                validationReferenceNo);
                        return Long.parseLong(String.format("%012d", Integer.parseInt("0")));
                    }
                } catch (Exception e) {
                    System.out.printf(
                            "Send Money Charges Request [Error]: Transaction %s has failed T24 Processing. Error message %s %n",
                            validationReferenceNo, e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (Exception w) {
            System.out.printf(
                    "Send Money Charges Request [Error]: Transaction %s has failed T24 Processing. Error message %s %n",
                    validationReferenceNo, w.getMessage());
            w.printStackTrace();
        }
        return Long.parseLong(String.format("%012d", Integer.parseInt("0")));
    }

    private void saveSendMoneyCommission(String charges, String tid, String gatewayref,
                                         String debitAccount, String firstTransactionPaymentDetails,
                                         String secondTransactionPaymentDetails, String thirdTransactionPaymentDetails,
                                         String branchAccountID, String creditAccount) {
        try {
            String commissionOFS =
                    "0000AFUNDS.TRANSFER,SEND.MONEY/I//1/0,"
                            + ""
                            + MASKED_T24_USERNAME
                            + "/"
                            + MASKED_T24_PASSWORD
                            + "/"
                            + branchAccountID
                            + ",,"
                            + "TRANSACTION.TYPE::=ACMO,"
                            + "DEBIT.ACCT.NO::="
                            + debitAccount
                            + ","
                            + "DEBIT.AMOUNT::="
                            + charges
                            + ","
                            + "CREDIT.ACCT.NO::="
                            + creditAccount
                            + ","
                            + "DEBIT.CURRENCY::=RWF,"
                            + "TCM.REF::="
                            + gatewayref
                            + ","
                            + "PAYMENT.DETAILS:1:= "
                            + utilityService.sanitizePaymentDetails(firstTransactionPaymentDetails, "Send Money Commission").trim()
                            + ","
                            + "PAYMENT.DETAILS:2:="
                            + secondTransactionPaymentDetails.trim()
                            + ","
                            + "PAYMENT.DETAILS:3:="
                            + thirdTransactionPaymentDetails.trim();

            System.out.println("Send money commission OFS for transaction [" + gatewayref + "] : " + commissionOFS);

            String refno = RRNGenerator.getInstance("SC").getRRN();
            String tot24str = String.format("%04d", commissionOFS.length()) + commissionOFS;

            T24TXNQueue tot24 = new T24TXNQueue();
            tot24.setAmountcredited(charges);
            tot24.setRequestleg(tot24str);
            tot24.setTid(tid);
            tot24.setPostedstatus("1");
            tot24.setStarttime(System.currentTimeMillis());
            tot24.setGatewayref(refno);
            tot24.setTxnchannel("Gateway");
            tot24.setRequestleg(tot24str);
            tot24.setDebitacctno(debitAccount);
            tot24.setCreditacctno(creditAccount);

            final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue("T24_IP");
            final String t24Port = xSwitchParameterService.fetchXSwitchParamValue("T24_PORT");

            t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);
            transactionService.updateT24TransactionDTO(tot24);

            String t24ref = tot24.getT24reference() == null ? "NA" : tot24.getT24reference();
            String t24responsecode =
                    tot24.getT24responsecode() == null ? "NA" : tot24.getT24responsecode();
            if (!t24responsecode.equals("1")) {
                log.info("Commission transaction for send money transaction [" + gatewayref + "] failed with error : " + tot24.getT24failnarration());
            } else {
                log.info("Commission applied for send money transaction " + gatewayref + ". Commission transaction gateway reference " + refno + " and T24 Response: " + t24ref);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SendMoneyResponse processReceiveMoneyRequest(ReceiveMoneyRequest request, String transactionRRN) {

        AuthenticateAgentResponse authenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials());

        long agentFloatAccountBalance = agentTransactionService.fetchAgentAccountBalanceOnly(authenticateAgentResponse.getData().getAccountNumber());

        if (agentFloatAccountBalance < request.getAmount()) {
            return SendMoneyResponse.builder()
                    .status("098")
                    .message("Insufficient agent account balance")
                    .data(null)
                    .build();
        }

        String channel = "1510";
        String sendMoneySuspense = xSwitchParameterService.fetchXSwitchParamValue("SENDMONEYSUSPENSE");

        // this must be set
        if (sendMoneySuspense.isEmpty()) {
            return SendMoneyResponse.builder()
                    .status("098")
                    .message("Missing send money suspense account")
                    .data(null)
                    .build();
        }

        String recipientPhoneNo = request.getReceiverMobileNo();
        String passcode = request.getPassCode();

        // No need to validate agent account is being debited
        BPRBranches branch =
                branchService.fetchBranchAccountsByBranchCode(sendMoneySuspense);
        if (null == branch.getCompanyName()) {
            System.out.println(
                    "Receive money suspense account details could not be verified for account "
                            + sendMoneySuspense);
            return SendMoneyResponse.builder()
                    .status("065")
                    .message("Transaction failed. Missing receive money suspense account information. Contact BPR")
                    .data(null)
                    .build();
        }

        String accountbranchid = branch.getId();
        if (accountbranchid.isEmpty()) {
            return SendMoneyResponse.builder()
                    .status("065")
                    .message("Transaction failed. Missing receive money suspense account information. Contact BPR")
                    .data(null)
                    .build();
        }


        String paymentdetail2 = authenticateAgentResponse.getData().getAccountNumber();

        try {
            String tid = "PC";
            String narration = "RECEIVEMONEY";
            Optional<MoneySend> optionalMoneySend = getSendMoneyTxn(recipientPhoneNo, passcode, String.valueOf(request.getAmount()));

            if (optionalMoneySend.isEmpty()) {
                return SendMoneyResponse.builder()
                        .status("139")
                        .message("Transaction failed. Missing original send money record.")
                        .data(null)
                        .build();
            }

            MoneySend sendMoneyTxn = optionalMoneySend.get();

            try {
                String storedVCard = desUtil._decrypt(sendMoneyTxn.getCno());

                if (!storedVCard.equalsIgnoreCase(request.getVCard())) {
                    return SendMoneyResponse.builder()
                            .status("116")
                            .message("Transaction failed. Invalid vCard. Please try again.")
                            .data(null)
                            .build();
                }

            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                System.out.println("Error encrypting card no for send money txn ref ~ " + transactionRRN);
                return SendMoneyResponse.builder()
                        .status("116")
                        .message("Transaction failed. Contact administrator")
                        .data(null)
                        .build();
            }

            System.out.println(
                    "Send Money Withdrawal Txn Found , Money send Id:  " + sendMoneyTxn.getMoneysendid());
            String smwOFS =
                    "0000AFUNDS.TRANSFER,SEND.MONEYWD/I/PROCESS/1/0,"
                            + ""
                            + MASKED_T24_USERNAME
                            + "/"
                            + MASKED_T24_PASSWORD
                            + "/"
                            + accountbranchid
                            + ","
                            + ",TRANSACTION.TYPE::=ACSS,"
                            + "DEBIT.ACCT.NO::="
                            + sendMoneySuspense
                            + ","
                            + "DEBIT.AMOUNT::="
                            + request.getAmount()
                            + ","
                            + "CREDIT.ACCT.NO::="
                            + authenticateAgentResponse.getData().getAccountNumber()
                            + ","
                            + "DEBIT.CURRENCY::=RWF,"
                            + "TCM.REF::="
                            + transactionRRN
                            + ",ORDERING.BANK::='BPR',"
                            + "PAYMENT.DETAILS:1:= "
                            + utilityService.sanitizePaymentDetails(recipientPhoneNo.trim(), "Send Money Withdrawal").trim()
                            + ","
                            + "PAYMENT.DETAILS:2:="
                            + paymentdetail2.trim()
                            + ","
                            + "PAYMENT.DETAILS:3:="
                            + narration.trim();

            System.out.println("Receive money OFS " + smwOFS);

            String tot24str = String.format("%04d", smwOFS.length()) + smwOFS;

            T24TXNQueue tot24 = new T24TXNQueue();
            tot24.setRequestleg(tot24str);
            tot24.setStarttime(System.currentTimeMillis());
            tot24.setTxnchannel(channel);
            tot24.setGatewayref(transactionRRN);
            tot24.setPostedstatus("0");
            tot24.setAttempts(0);
            tot24.setTid(tid);
            tot24.setCreditacctno(authenticateAgentResponse.getData().getAccountNumber());
            tot24.setDebitacctno(sendMoneySuspense);

            final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue("T24_IP");
            final String t24Port = xSwitchParameterService.fetchXSwitchParamValue("T24_PORT");

            t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);
            transactionService.updateT24TransactionDTO(tot24);

            if (tot24.getT24responsecode().equalsIgnoreCase("1")) {

                sendMoneyTxn.setFulfilmentref(transactionRRN);
                sendMoneyTxn.setFulfilmentreft24(tot24.getT24reference());
                sendMoneyTxn.setFulfilmentstatus(1);
                sendMoneyTxn.setFulfilmentagentid(tid);
                moneySendRepository.save(sendMoneyTxn);

                // use provided templates
                String smsrecipient =
                        "You have withdrawn RF "
                                + request.getAmount()
                                + " from  "
                                + tid
                                + " "
                                + "via BPR Card-less Transfer. Thank you for banking with BPR. ";
                ScheduledSMS smssched = new ScheduledSMS();
                smssched.setSentstatus(0);
                smssched.setAttempts(0);
                smssched.setMessage(utilityService.encryptText(smsrecipient));
                smssched.setReceiverphone(recipientPhoneNo);
                smssched.setTxnref(transactionRRN);
                scheduledSMSRepository.save(smssched);


                tot24.setT24reference(tot24.getT24reference());
                transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "RECEIVE MONEY", "1200",
                        request.getAmount(), "000",
                        authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());


                SendMoneyResponseData sendMoneyResponseData = SendMoneyResponseData.builder()
                        .T24Reference(tot24.getT24reference())
                        .charges(0.0)
                        .rrn(transactionRRN)
                        .build();

                sendMoneyResponseData.setUsername(authenticateAgentResponse.getData().getUsername());
                sendMoneyResponseData.setNames(authenticateAgentResponse.getData().getNames());
                sendMoneyResponseData.setBusinessName(authenticateAgentResponse.getData().getBusinessName());
                sendMoneyResponseData.setLocation(authenticateAgentResponse.getData().getLocation());
                sendMoneyResponseData.setTid(authenticateAgentResponse.getData().getTid());
                sendMoneyResponseData.setMid(authenticateAgentResponse.getData().getMid());


                return SendMoneyResponse.builder()
                        .status("00")
                        .message("Transaction processed successfully")
                        .data(sendMoneyResponseData)
                        .build();

            } else {

                tot24.setT24reference(tot24.getT24reference());
                transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "RECEIVE MONEY", "1200",
                        request.getAmount(), "118",
                        authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());

                SendMoneyResponseData sendMoneyResponseData = SendMoneyResponseData.builder()
                        .T24Reference(tot24.getT24reference())
                        .charges(0.0)
                        .rrn(transactionRRN)
                        .build();

                sendMoneyResponseData.setUsername(authenticateAgentResponse.getData().getUsername());
                sendMoneyResponseData.setNames(authenticateAgentResponse.getData().getNames());
                sendMoneyResponseData.setBusinessName(authenticateAgentResponse.getData().getBusinessName());
                sendMoneyResponseData.setLocation(authenticateAgentResponse.getData().getLocation());

                return SendMoneyResponse.builder()
                        .status("118")
                        .message("Transaction failed. " + tot24.getT24failnarration())
                        .data(sendMoneyResponseData)
                        .build();
            }

        } catch (Exception w) {
            w.printStackTrace();
            return SendMoneyResponse.builder()
                    .status("098")
                    .message("Transaction failed. Error encrypting vCard no.")
                    .data(null)
                    .build();
        }
    }

    public Optional<MoneySend> getSendMoneyTxn(String receiverPhone, String msToken, String amount) {
        return moneySendRepository.findByRecevernumberAndMstokenAndAmount(receiverPhone, msToken, amount);
    }
}
