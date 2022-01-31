package co.ke.tracom.bprgateway.web.sendmoney.services;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.Data;
import co.ke.tracom.bprgateway.web.agenttransactions.services.AgentTransactionService;
import co.ke.tracom.bprgateway.web.bankbranches.entity.BPRBranches;
import co.ke.tracom.bprgateway.web.bankbranches.service.BPRBranchService;
import co.ke.tracom.bprgateway.web.exceptions.custom.InsufficientAccountBalanceException;
import co.ke.tracom.bprgateway.web.exceptions.custom.InvalidAgentCredentialsException;
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
import co.ke.tracom.bprgateway.web.transactionLimits.TransactionLimitManagerService;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import co.ke.tracom.bprgateway.web.util.TransactionISO8583ProcessingCode;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import co.ke.tracom.bprgateway.web.util.services.UtilityService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

import static co.ke.tracom.bprgateway.web.sms.dto.SMSRequest.SMS_FUNCTION_RECEIVER;
import static co.ke.tracom.bprgateway.web.sms.dto.SMSRequest.SMS_FUNCTION_SENDER;
import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class SendMoneyService {
    public final String CURRENCY_ISO_FORMAT = "%012d";
    private final String SEND_MONEY_TRANSACTION_LOG_LABEL = "Send money transaction [";
    private final String SEND_MONEY_SUSPENSE_ACC = "SENDMONEYSUSPENSE";
    private final String SEND_MONEY_LABEL = "SEND MONEY";

    private final Long SENDMONEY_TRANSACTION_LIMIT_ID=1L;
    private final Long RECEIVEMONEY_TRANSACTION_LIMIT_ID=2L;
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
    private final TransactionLimitManagerService limitManagerService;

    @SneakyThrows
    public SendMoneyResponse processSendMoneyRequest(SendMoneyRequest request, String transactionRRN) {
        T24TXNQueue toT24= new T24TXNQueue();

        AuthenticateAgentResponse authenticateAgentResponse = null;

        try {
            authenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials());
        }catch (InvalidAgentCredentialsException e){
//            transactionService.saveFailedPasswordTransactions(toT24, "SEND_MONEY_LABEL", "1200",request.getAmount(),
//                   "117");
            return new SendMoneyResponse("117",e.getMessage(), SendMoneyResponseData.builder().build());

        }
        log.info(SEND_MONEY_TRANSACTION_LOG_LABEL + transactionRRN + "] processing begins. Request " + request);

    Data agentAuthData = authenticateAgentResponse.getData();




        long agentFloatAccountBalance = agentTransactionService.fetchAgentAccountBalanceOnly(agentAuthData.getAccountNumber());

        try {




            SendMoneyResponse responses=new SendMoneyResponse();

            TransactionLimitManagerService.TransactionLimit limitValid = limitManagerService.isLimitValid(SENDMONEY_TRANSACTION_LIMIT_ID, (long) request.getAmount());
            if (!limitValid.isValid()) {
                transactionService.saveCardLessTransactionToAllTransactionTable(toT24, SEND_MONEY_LABEL, "1200",
                        request.getAmount(), "061",
                        authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());
                responses.setStatus("061");
                responses.setMessage("Amount should be between"+ limitValid.getLower()+ " and " + limitValid.getUpper());
                return responses;
            }


            BPRBranches branch = branchService.fetchBranchAccountsByBranchCode(agentAuthData.getAccountNumber());
            String branchAccountID = branch.getId();
            System.out.println(" agent authentication data = " + agentAuthData);
            log.info("Account data fetched ---->[]", branch);

            doesAgentHaveSufficientBalance(request, transactionRRN, agentAuthData, agentFloatAccountBalance, branchAccountID);

            String senderMobileNo = request.getSenderMobileNo().trim();
            validateMobileNumberLength(transactionRRN, senderMobileNo.length());

            String receiverMobile = request.getRecipientMobileNo().trim();




            compareSenderRecipientMobileNumbers(transactionRRN, receiverMobile.equalsIgnoreCase(senderMobileNo));

            String configuredSendMoneySuspenseAccount = xSwitchParameterService.fetchXSwitchParamValue(SEND_MONEY_SUSPENSE_ACC);






            String[] paymentDetails = new String[3];

            paymentDetails[0] = senderMobileNo + "/" + receiverMobile;
            paymentDetails[1] = agentAuthData.getNames() + " " + agentAuthData.getAccountNumber();
            paymentDetails[2] = SEND_MONEY_LABEL;

            String sendMoneyOFSMsg = bootstrapSendMoneyOFSMsg(request.getAmount(), transactionRRN, agentAuthData, branchAccountID,
                    configuredSendMoneySuspenseAccount, paymentDetails);
            String tot24str = String.format("%04d", sendMoneyOFSMsg.length()) + sendMoneyOFSMsg;
            log.info(SEND_MONEY_TRANSACTION_LOG_LABEL + transactionRRN + "] OFS is ready. " + tot24str);

            String nationalIdDocumentName = request.getSenderNationalIDType().equals("0") ? "NID" : "OTHERS";
            String tid = "PC";

            T24TXNQueue tot24 = prepareT24Transaction(transactionRRN, agentAuthData, configuredSendMoneySuspenseAccount, tot24str, tid);

            processSendMoneyTransaction(tot24);

            if ((tot24.getT24responsecode().equalsIgnoreCase("1"))) {
                return processSuccessfulSendMoneyT24Transaction(request, transactionRRN, agentAuthData, branchAccountID,
                        paymentDetails, nationalIdDocumentName, tid, tot24, authenticateAgentResponse);
            } else {
                return processFailedSendMoneyT24Transaction(request,transactionRRN, agentAuthData, tot24.getT24reference());
            }
        } catch (Exception w) {
            w.printStackTrace();
            log.info("Send money transaction error : " + w.getMessage());
        }
        transactionService.saveCardLessTransactionToAllTransactionTable(toT24, SEND_MONEY_LABEL, "1200",
                request.getAmount(), "116",
                authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());

        return SendMoneyResponse.builder()
                .status("116")
                .message("Transaction processing failed. Please try again")
                .data(null)
                .build();
    }
@SneakyThrows
    private SendMoneyResponse processSuccessfulSendMoneyT24Transaction(SendMoneyRequest request, String transactionRRN,
                                                                       Data agentAuthData, String branchAccountID,
                                                                       String[] paymentDetails,
                                                                       String nationalIdDocumentName, String tid,
                                                                       T24TXNQueue tot24, AuthenticateAgentResponse authenticateAgentResponse) {
        String senderMobileNo = request.getSenderMobileNo();
        String receiverMobile = request.getRecipientMobileNo();

        SendMoneyResponseData data = SendMoneyResponseData.builder().build();

        try {

//
            String charges = tot24.getTotalchargeamt();
            System.err.println("Charges " + charges);
            String formattedCharge = charges.replace("RWF", "");
            System.err.println("Transaction charge : " + formattedCharge);
            //send money commission save

            String collectionCommissionAccount = xSwitchParameterService.fetchXSwitchParamValue("SENDMONEYCOMMISSIONCOLLECTIONACC");
            this.saveSendMoneyCommission(formattedCharge, tid, transactionRRN, agentAuthData.getAccountNumber(),
                    paymentDetails, branchAccountID, collectionCommissionAccount);

            data.setCharges(Double.parseDouble(formattedCharge));
            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, SEND_MONEY_LABEL, "1200",
                    request.getAmount(), "000",
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());
        } catch (Exception e) {
            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, SEND_MONEY_LABEL, "1200",
                    request.getAmount(), "098",
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());
            System.err.println(
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

                saveSendMoneyTransaction(request, agentAuthData, nationalIdDocumentName, tot24.getT24reference(), generatedCardNo, passCode,transactionRRN);

                SMSRequest recipientMessage = saveRecipientMessage(request, transactionRRN, receiverMobile, senderMobileNo, vCardNo);
                SMSRequest senderMessage = saveSenderMessage(request, transactionRRN, receiverMobile, senderMobileNo, passCode);

                String fdiSMSAPIAuthToken = smsService.getFDISMSAPIAuthToken();

                SMSResponse recipientResponse = smsService.sendFDISMSRequest(fdiSMSAPIAuthToken, recipientMessage);
                log.info("Recipient SMS Response[" + transactionRRN + "]: " + recipientResponse.toString());
                SMSResponse senderResponse = smsService.sendFDISMSRequest(fdiSMSAPIAuthToken, senderMessage);
                log.info("Sender SMS Response[" + transactionRRN + "]: " + senderResponse.toString());

                return SendMoneyResponse.builder()
                        .status("00")
                        .message("Send money transaction processed successfully.")
                        .data(data)
                        .build();

            } else {
                System.err.println(
                        "Card less vcard generation error for rrn " + transactionRRN);
                transactionService.saveCardLessTransactionToAllTransactionTable(tot24, SEND_MONEY_LABEL, "1200",
                        request.getAmount(), "118",
                        authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());
                return SendMoneyResponse.builder()
                        .status("118")
                        .message("Transaction failed. Unable to generate virtual card. Please try again.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=================================================" + e.getMessage());
            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, SEND_MONEY_LABEL, "1200",
                    request.getAmount(), "118",
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());
            return SendMoneyResponse.builder()
                    .status("118")
                    .message("Transaction failed. Please try again.")
                    .data(null)
                    .build();
        }
    }

    private SMSRequest saveRecipientMessage(SendMoneyRequest request, String transactionRRN,
                                            String receiverMobile, String senderMobile, String vCardNo) {

        String recipientMessage = recipientMessage(request, senderMobile, vCardNo);

        SMSRequest fdismsRequest = getFDISMSRequest(recipientMessage, receiverMobile, SMS_FUNCTION_RECEIVER);

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
        return fdismsRequest;
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
     * @deprecated OFS message no longer viable
     */
    @Deprecated(since = "v1.6", forRemoval = true)
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


    private SMSRequest getFDISMSRequest(String message, String smsRecipientNo, String smsFunctionReceiver) {

        System.err.println("message = " + smsFunctionReceiver + " : " + message);
        return SMSRequest.builder()
                .recipient(smsRecipientNo)
                .SMSFunction(smsFunctionReceiver)
                .message(message)
                .build();
    }

    private SMSRequest saveSenderMessage(SendMoneyRequest request, String transactionRRN,
                                         String recipientMobile, String senderMobileNo, String passCode) {

        String senderMessage = senderMessage(request, recipientMobile, passCode);
        SMSRequest fdismsRequest = getFDISMSRequest(senderMessage, senderMobileNo, SMS_FUNCTION_SENDER);
        // Insert Second SMS
        String SMSContent = utilityService.encryptText(senderMessage);
        ScheduledSMS scheduledSMS = new ScheduledSMS();
        scheduledSMS.setSentstatus(1);
        scheduledSMS.setAttempts(0);
        scheduledSMS.setMessage(SMSContent);
        scheduledSMS.setReceiverphone(senderMobileNo);
        scheduledSMS.setTxnref(transactionRRN);
        scheduledSMSRepository.save(scheduledSMS);

        return fdismsRequest;
    }

    private String senderMessage(SendMoneyRequest request, String recipientMobile, String passCode) {
        return "You have successfully sent RWF"
                + request.getAmount()
                + " to "
                + recipientMobile
                + " via BPR Cardless transfer. Kindly share passcode with the recipient. Passcode: "
                + passCode
                + ". Thanks for banking with us.";
    }

    private String recipientMessage(SendMoneyRequest request, String sendMobile, String cardNo) {
        return "You have received RWF"
                + request.getAmount()
                + " from "
                + sendMobile
                + " to withdraw at a BPR agent. Provide VCARD no: "
                + cardNo
                + " ,Sender will share the passcode. MURAKOZE";
    }

    private void saveSendMoneyTransaction(SendMoneyRequest request, Data agentAuthData, String nationalIdDocumentName,
                                          String t24Reference, String generatedCardNo, String token, String transactionRRN) {
        String senderMobileNo = request.getSenderMobileNo();
        String receiverMobile = request.getRecipientMobileNo();

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

        //walter
        //Save RRN, tokenstarttime, tokenexoirytime
        ms.setTransactionRRN(transactionRRN);

        Instant now = Instant.now();
        long timeNow = now.toEpochMilli();
        ms.setSendmoneytokenstarttime(timeNow);

        long expiryTime = now.plus(Duration.ofHours(72)).toEpochMilli();
        ms.setSendmoneytokenexpiretime(expiryTime);
        moneySendRepository.save(ms);
    }

    @SneakyThrows
    private SendMoneyResponse processFailedSendMoneyT24Transaction(SendMoneyRequest request, String transactionRRN, Data agentAuthData, String t24Reference) {
        T24TXNQueue tot24 = new T24TXNQueue();
        AuthenticateAgentResponse authenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials());

        SendMoneyResponseData sendMoneyResponseData = SendMoneyResponseData.builder()
                .T24Reference(t24Reference)
                .charges(0)
                .rrn(transactionRRN)
                .build();
        sendMoneyResponseData.setRrn(transactionRRN);
        sendMoneyResponseData.setUsername(agentAuthData.getUsername());
        sendMoneyResponseData.setNames(agentAuthData.getNames());
        sendMoneyResponseData.setBusinessName(agentAuthData.getBusinessName());
        sendMoneyResponseData.setLocation(agentAuthData.getLocation());
        transactionService.saveCardLessTransactionToAllTransactionTable(tot24, SEND_MONEY_LABEL, "1200",
                request.getAmount(), "098",
                authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());

        return SendMoneyResponse.builder()
                .status("098")
                .message("Transaction failed. Unable to post transaction to remote server.")
                .data(sendMoneyResponseData)
                .build();
    }

    private void processSendMoneyTransaction(T24TXNQueue tot24) {
        final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue(T24_IP);
        final String t24Port = xSwitchParameterService.fetchXSwitchParamValue(T24_PORT);
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

    private String bootstrapSendMoneyOFSMsg(double amount, String transactionRRN, Data agentAuthData, String branchAccountID,
                                            String configuredSendMoneySuspenseAccount, String[] paymentDetails) {
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
                + ",PAYMENT.DETAILS:1:= "
                + utilityService.sanitizePaymentDetails(paymentDetails[0], SEND_MONEY_LABEL).trim()
                + ","
                + "PAYMENT.DETAILS:2:="
                + paymentDetails[1].trim()
                + ","
                + "PAYMENT.DETAILS:3:="
                + paymentDetails[2].trim();
    }

    private void compareSenderRecipientMobileNumbers(String RRN, boolean sameMobileNo) {
        if (sameMobileNo) {
            log.info(SEND_MONEY_TRANSACTION_LOG_LABEL + RRN + "] failed. Sender mobile number and recipient mobile number are the same.");
            throw new InvalidMobileNumberException("Transaction failed. Sender mobile number and recipient mobile number are the same.");
        }
    }

    private void validateMobileNumberLength(String RRN, int length) {
        if (length != 10) {
            log.info(SEND_MONEY_TRANSACTION_LOG_LABEL + RRN + "] failed. Invalid mobile number length.");
            throw new InvalidMobileNumberException("Invalid mobile number length");
        }
    }

    private void doesAgentHaveSufficientBalance(SendMoneyRequest request, String transactionRRN, Data agentAuthData, long agentFloatAccountBalance, String branchAccountID) {
        long chargesLong = fetchSendMoneyTransactionCharges(agentAuthData.getAccountNumber(), branchAccountID, transactionRRN, request.getAmount());
        System.err.printf(
                "%n Transaction %s T24 Charges (%d) and agent float account balance (%d) against send money amount (%f) %n",
                transactionRRN, chargesLong, agentFloatAccountBalance, request.getAmount());

        if (request.getAmount() > (agentFloatAccountBalance + chargesLong)) {
            throw new InsufficientAccountBalanceException("Insufficient agent balance to process this transaction. Top up agent float to continue.");
        }
    }

    public long fetchSendMoneyTransactionCharges(String agentFloatAccount, String branchAccountID,
                                                 String originalTransactionReference, double amount) {

        String validationReferenceNo = RRNGenerator.getInstance("BP").getRRN();
        System.err.printf(
                "Send Money Charges generated transaction ID %s for POS request transaction ID %s %n",
                validationReferenceNo, originalTransactionReference);

        try {
            String configuredSendMoneySuspenseAccount = xSwitchParameterService.fetchXSwitchParamValue(SEND_MONEY_SUSPENSE_ACC);
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

            final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue(T24_IP);
            final String t24Port = xSwitchParameterService.fetchXSwitchParamValue(T24_PORT);
            t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);
            transactionService.updateT24TransactionDTO(tot24);

            System.err.printf(
                    "Send Money Charges Request : Transaction %s has been queued for T24 Processing. %n",
                    validationReferenceNo);

            if (tot24.getT24responsecode().equalsIgnoreCase("1")) {

                return extractChargesFromResponse(validationReferenceNo, tot24);

            }

        } catch (Exception w) {
            System.err.printf(
                    "Send Money Charges Request [Error]: Transaction %s has failed T24 Processing. Error message %s %n",
                    validationReferenceNo, w.getMessage());
            w.printStackTrace();
        }
        return Long.parseLong(String.format(CURRENCY_ISO_FORMAT, Integer.parseInt("0")));
    }

    private long extractChargesFromResponse(String validationReferenceNo, T24TXNQueue tot24) {
        try {
            String charges = tot24.getTotalchargeamt();
            if (null != charges) {
                System.err.printf(
                        "Send Money Charges Request [Success]: Transaction %s has transaction charge %s from T24 Processing. %n",
                        validationReferenceNo, charges);
                return Long.parseLong(charges.replace("RWF", ""));

            } else {
                System.err.printf(
                        "Send Money Charges Request [Failed]: Transaction %s has no transaction charges from T24 Processing. %n",
                        validationReferenceNo);
                return Long.parseLong(String.format(CURRENCY_ISO_FORMAT, Integer.parseInt("0")));
            }
        } catch (Exception e) {
            System.err.printf(
                    "Send Money Charges Request [Error]: Transaction %s has failed T24 Processing. Error message %s %n",
                    validationReferenceNo, e.getMessage());
            e.printStackTrace();
            return Long.parseLong(String.format(CURRENCY_ISO_FORMAT, Integer.parseInt("0")));
        }
    }

    private void saveSendMoneyCommission(String charges, String tid, String gatewayref,
                                         String debitAccount, String[] paymentDetails,
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
                            + utilityService.sanitizePaymentDetails(paymentDetails[0], "Send Money Commission").trim()
                            + ","
                            + "PAYMENT.DETAILS:2:="
                            + paymentDetails[1].trim()
                            + ","
                            + "PAYMENT.DETAILS:3:="
                            + paymentDetails[2].trim();

            System.err.println("Send money commission OFS for transaction [" + gatewayref + "] : " + commissionOFS);

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

            final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue(T24_IP);
            final String t24Port = xSwitchParameterService.fetchXSwitchParamValue(T24_PORT);

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

    @SneakyThrows
    public SendMoneyResponse processReceiveMoneyRequest(ReceiveMoneyRequest request, String transactionRRN) {
        T24TXNQueue tot24 = new T24TXNQueue();
        AuthenticateAgentResponse authenticateAgentResponse = null;
        try {
            authenticateAgentResponse=baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials());
        }catch(InvalidAgentCredentialsException e) {
            transactionService.saveFailedUserPasswordTransactions("Failed Logins","Agent logins",request.getCredentials().getUsername(),
                    "AgentValidation","FAILED","ipAddress");
        }

        long agentFloatAccountBalance = agentTransactionService.fetchAgentAccountBalanceOnly(authenticateAgentResponse.getData().getAccountNumber());

        if (agentFloatAccountBalance < request.getAmount()) {
            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "RECEIVE MONEY", "1200",
                    request.getAmount(), "098",
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());

            return SendMoneyResponse.builder()
                    .status("098")
                    .message("Insufficient agent account balance")
                    .data(null)
                    .build();
        }

        SendMoneyResponse responses=new SendMoneyResponse();

        TransactionLimitManagerService.TransactionLimit limitValid = limitManagerService. isLimitValid(RECEIVEMONEY_TRANSACTION_LIMIT_ID, (long) request.getAmount());
        if (!limitValid.isValid()) {
            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "RECEIVE MONEY", "1200",
                    request.getAmount(), "061",
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());

            responses.setStatus("061");
            responses.setMessage("Amount should be between"+ limitValid.getLower()+ " and " + limitValid.getUpper());
            return responses;
        }

        String channel = "1510";
        String sendMoneySuspense = xSwitchParameterService.fetchXSwitchParamValue(SEND_MONEY_SUSPENSE_ACC);

        // this must be set
        if (sendMoneySuspense.isEmpty()) {
            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "RECEIVE MONEY", "1200",
                    request.getAmount(), "098",
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());

            return SendMoneyResponse.builder()
                    .status("098")
                    .message("Missing send money suspense account")
                    .data(null)
                    .build();
        }

        String recipientPhoneNo = request.getReceiverMobileNo().trim();
        String passcode = request.getPasscode().trim();

        // No need to validate agent account is being debited
        BPRBranches branch =
                branchService.fetchBranchAccountsByBranchCode(sendMoneySuspense);
        if (null == branch.getCompanyName()) {

            System.err.println(
                    "Receive money suspense account details could not be verified for account "
                            + sendMoneySuspense);
            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "RECEIVE MONEY", "1200",
                    request.getAmount(), "065",
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());

            return SendMoneyResponse.builder()
                    .status("065")
                    .message("Transaction failed. Missing receive money suspense account information. Contact BPR")
                    .data(null)
                    .build();
        }

        String accountbranchid = branch.getId();
        if (accountbranchid.isEmpty()) {
            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "RECEIVE MONEY", "1200",
                    request.getAmount(), "065",
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());

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
            Optional<MoneySend> optionalMoneySend = getSendMoneyTxn(recipientPhoneNo, passcode);

            if (optionalMoneySend.isEmpty()) {
                transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "RECEIVE MONEY", "1200",
                        request.getAmount(), "139",
                        authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());

                return SendMoneyResponse.builder()
                        .status("139")
                        .message("Transaction failed. Missing original send money record.")
                        .data(null)
                        .build();
            }

            MoneySend sendMoneyTxn = optionalMoneySend.get();

            if (sendMoneyTxn.getFulfilmentstatus() == 1) {
                // todo what to log here: log.info("Trying to fulfill an already completed transaction, RRN {} - passcode {} ", sendMoneyTxn.getRecevernumber(), request.getAmount());
                return SendMoneyResponse.builder()
                        .status("059")
                        .message("Transaction failed. Already fulfilled.")
                        .data(null)
                        .build();
            }

            if (Double.parseDouble(sendMoneyTxn.getAmount()) != request.getAmount()) {
                log.info("Wrong receive amount entered, original {} - current {} ", sendMoneyTxn.getAmount(), request.getAmount());
                transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "RECEIVE MONEY", "1200",
                        request.getAmount(), "139",
                        authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());

                return SendMoneyResponse.builder()
                        .status("139")
                        .message("Transaction failed. Incorrect amount was entered.")
                        .data(null)
                        .build();
            }

            //walter
            /* ======= start =======  */
            // check if token expired
            if (Instant.ofEpochMilli(sendMoneyTxn.getSendmoneytokenexpiretime()).isBefore(Instant.now())
            /*sendMoneyTxn.getSendmoneytokenexpiretime() < Instant.now().toEpochMilli()*/) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");

                //check whether is the sender withdrawing
                if (!request.getReceiverMobileNo().trim().equals(sendMoneyTxn.getSendernumber())) {
                    log.info("Send money token expired at {} ", formatter.format(new Date(sendMoneyTxn.getSendmoneytokenexpiretime())));
                    transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "RECEIVE MONEY", "1200",
                            request.getAmount(), "139",
                            authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());

                    return SendMoneyResponse.builder()
                            .status("139")
                            .message("Transaction failed. Token expired!")
                            .data(null)
                            .build();
                }else{
                    //check the regenerated token
                    if (Instant.ofEpochMilli(sendMoneyTxn.getSendmoneytokenexpiretime2()).isBefore(Instant.now())
                        /*sendMoneyTxn.getSendmoneytokenexpiretime2() < Instant.now().toEpochMilli()*/){
                        log.info("Send money token expired at {} ", formatter.format(new Date(sendMoneyTxn.getSendmoneytokenexpiretime2())));
                        transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "RECEIVE MONEY", "1200",
                                request.getAmount(), "139",
                                authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());

                        return SendMoneyResponse.builder()
                                .status("139")
                                .message("Transaction failed. Token expired!")
                                .data(null)
                                .build();
                    }
                }
            }
            /* ======= end =======  */

            if (compareRequestTokenWithStoredToken(request, transactionRRN, sendMoneyTxn)) {
                transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "RECEIVE MONEY", "1200",
                        request.getAmount(), "116",
                        authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());

                return SendMoneyResponse.builder()
                        .status("116")
                        .message("Transaction failed. Invalid vCard. Please try again.")
                        .data(null)
                        .build();
            }

            System.err.println(
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

            System.err.println("Receive money OFS " + smwOFS);

            String tot24str = String.format("%04d", smwOFS.length()) + smwOFS;


            tot24.setRequestleg(tot24str);
            tot24.setStarttime(System.currentTimeMillis());
            tot24.setTxnchannel(channel);
            tot24.setGatewayref(transactionRRN);
            tot24.setPostedstatus("0");
            tot24.setAttempts(0);
            tot24.setTid(tid);
            tot24.setCreditacctno(authenticateAgentResponse.getData().getAccountNumber());
            tot24.setDebitacctno(sendMoneySuspense);

            final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue(T24_IP);
            final String t24Port = xSwitchParameterService.fetchXSwitchParamValue(T24_PORT);

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

    private boolean compareRequestTokenWithStoredToken(ReceiveMoneyRequest request, String transactionRRN, MoneySend sendMoneyTxn) {
        try {
            String storedVCard = desUtil._decrypt(sendMoneyTxn.getCno());

            if (!storedVCard.equalsIgnoreCase(request.getVCard())) {
                return true;
            }

        } catch (Exception e1) {
            e1.printStackTrace();
            System.err.println("Error encrypting card no for send money txn ref ~ " + transactionRRN);
        }
        return false;
    }

    public Optional<MoneySend> getSendMoneyTxn(String receiverPhone, String msToken) {
        return moneySendRepository.findByRecevernumberAndMstoken(receiverPhone, msToken);
    }
}
