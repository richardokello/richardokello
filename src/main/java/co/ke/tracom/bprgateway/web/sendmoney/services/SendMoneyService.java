package co.ke.tracom.bprgateway.web.sendmoney.services;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.agenttransactions.services.AgentTransactionService;
import co.ke.tracom.bprgateway.web.bankbranches.entity.BPRBranches;
import co.ke.tracom.bprgateway.web.bankbranches.service.BPRBranchService;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.responses.RRATINValidationResponse;
import co.ke.tracom.bprgateway.web.sendmoney.data.requests.ReceiveMoneyRequest;
import co.ke.tracom.bprgateway.web.sendmoney.data.requests.SendMoneyRequest;
import co.ke.tracom.bprgateway.web.sendmoney.data.response.SendMoneyResponse;
import co.ke.tracom.bprgateway.web.sendmoney.data.response.SendMoneyResponseData;
import co.ke.tracom.bprgateway.web.sendmoney.entity.MoneySend;
import co.ke.tracom.bprgateway.web.sendmoney.repository.MoneySendRepository;
import co.ke.tracom.bprgateway.web.smsscheduled.entities.ScheduledSMS;
import co.ke.tracom.bprgateway.web.smsscheduled.repository.ScheduledSMSRepository;
import co.ke.tracom.bprgateway.web.switchparameters.repository.XSwitchParameterRepository;
import co.ke.tracom.bprgateway.web.t24communication.services.T24Channel;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import co.ke.tracom.bprgateway.web.util.services.UtilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

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

    private final XSwitchParameterRepository xSwitchParameterRepository;
    private final MoneySendRepository moneySendRepository;
    private final ScheduledSMSRepository scheduledSMSRepository;

    @Value("${merchant.account.validation}")
    private String agentValidation;


    public SendMoneyResponse processSendMoneyRequest(SendMoneyRequest request, String transactionRRN) {

        Optional<AuthenticateAgentResponse> optionalAuthenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials(), agentValidation);
        if (optionalAuthenticateAgentResponse.isEmpty()) {
            log.info(
                    "Send Money transaction :[Failed] Missing agent information %n");
            return SendMoneyResponse.builder()
                    .status("117")
                    .message("Missing agent information")
                    .data(null)
                    .build();
        } else if (optionalAuthenticateAgentResponse.get().getCode() != HttpStatus.OK.value()) {
            return SendMoneyResponse
                    .builder()
                    .status(String.valueOf(
                            optionalAuthenticateAgentResponse.get().getCode()))
                    .message(optionalAuthenticateAgentResponse.get().getMessage())
                    .build();
        }
        AuthenticateAgentResponse authenticateAgentResponse = optionalAuthenticateAgentResponse.get();

        BPRBranches branch = branchService.fetchBranchAccountsByBranchCode(authenticateAgentResponse.getData().getAccountNumber());
        if (null == branch.getCompanyName()) {
            return SendMoneyResponse.builder()
                    .status("065")
                    .message("Missing agent branch details")
                    .data(null)
                    .build();
        }

        String branchAccountID = branch.getId();
        if (branchAccountID.isEmpty()) {
            return SendMoneyResponse.builder()
                    .status("065")
                    .message("Missing agent branch account id")
                    .data(null)
                    .build();
        }

        long agentFloatAccountBalance =
                agentTransactionService.fetchAgentAccountBalanceOnly(authenticateAgentResponse.getData().getAccountNumber());

        try {

            /**
             * "Debit Agent float Acc - Credit Agent Suspense Acc, Dr Agent - Cr Agent Fees, Debit Agent
             * Fees - Credit Agent Tax Acc"
             */
            String charges = fetchSendMoneyTransactionCharges(authenticateAgentResponse.getData().getAccountNumber(),
                    branchAccountID, transactionRRN, request.getAmount());
            long chargesLong = Long.parseLong(charges);

            System.out.printf(
                    "%n Transaction %s T24 Charges (%s) and agent float account balance (%d) against send money amount (%f) %n",
                    transactionRRN, charges, agentFloatAccountBalance, request.getAmount());

            if (request.getAmount() > (agentFloatAccountBalance + chargesLong)) {
                return SendMoneyResponse.builder()
                        .status("117")
                        .message("Insufficient agent float balance")
                        .data(null)
                        .build();
            }


            String senderMobileNo = request.getSenderMobileNo().trim();
            if (senderMobileNo.length() != 10) {
                return SendMoneyResponse.builder()
                        .status("145")
                        .message("Missing sender mobile no")
                        .data(null)
                        .build();
            }

            String receiverMobile = request.getRecipientMobileNo().trim();
            if (receiverMobile.equalsIgnoreCase(senderMobileNo)) {
                return SendMoneyResponse.builder()
                        .status("144")
                        .message("Invalid or missing recipient mobile no")
                        .data(null)
                        .build();
            }

            String configuredSendMoneySuspenseAccount = xSwitchParameterRepository.findByParamName("SENDMONEYSUSPENSE").get().getParamValue();

            String firstTransactionPaymentDetails = senderMobileNo + " / " + receiverMobile;
            String secondTransactionPaymentDetails = authenticateAgentResponse.getData().getNames() + " " + authenticateAgentResponse.getData().getAccountNumber();
            String thirdTransactionPaymentDetails = "SEND MONEY";

            String cardlesssendOFS =
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
                            + authenticateAgentResponse.getData().getAccountNumber()
                            + ","
                            + "DEBIT.AMOUNT::="
                            + request.getAmount()
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

            System.out.println("send money OFS  " + cardlesssendOFS);
            String tot24str = String.format("%04d", cardlesssendOFS.length()) + cardlesssendOFS;

            //TODO CHECK IF TIN CAN SEND ID Type
//      String nationalIdDocumentName = senderrecepient[3].equals("0") ? "NID" : "OTHERS";
            String nationalIdDocumentName = "NID";

            System.out.println("transactionReferenceNo : " + transactionRRN);
            System.out.println("sendMoneyAmount : " + request.getAmount());
            System.out.println("receiverMobile : " + receiverMobile);
            System.out.println("senderMobileNo : " + senderMobileNo);

            //TODO find TID
            String tid = "PC";
            System.out.println("tid : " + tid);

            String t24Reference = "";

            T24TXNQueue tot24 = new T24TXNQueue();
            tot24.setTid(tid);
            tot24.setRequestleg(tot24str);
            tot24.setStarttime(System.currentTimeMillis());

            String channel = "1510";
            tot24.setTxnchannel(channel);
            tot24.setGatewayref(transactionRRN);
            tot24.setPostedstatus("1");
            //TODO Define P Codes
            tot24.setProcode("410000");
            tot24.setDebitacctno(authenticateAgentResponse.getData().getAccountNumber());
            tot24.setCreditacctno(configuredSendMoneySuspenseAccount);

            final String t24Ip = xSwitchParameterRepository.findByParamName("T24_IP").get().getParamValue();
            final String t24Port = xSwitchParameterRepository.findByParamName("T24_PORT").get().getParamValue();

            t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);
            transactionService.updateT24TransactionDTO(tot24);

            t24Reference = tot24.getT24reference() == null ? "NA" : tot24.getT24reference();
            if ((tot24.getT24responsecode().equalsIgnoreCase("1"))) {

                SendMoneyResponseData data = SendMoneyResponseData.builder().build();
                try {
                    charges = tot24.getTotalchargeamt();
                    System.out.println("Charges " + charges);
                    String formattedcharge = charges.replace("RWF", "");
                    System.out.println("Transaction charge : " + formattedcharge);
                    //send money commission save

                    String collectionCommissionAccount = xSwitchParameterRepository.findByParamName("SENDMONEYCOMMISSIONCOLLECTIONACC").get().getParamValue();
                    this.saveSendMoneyCommission(formattedcharge, tid, transactionRRN, authenticateAgentResponse.getData().getAccountNumber(),
                            firstTransactionPaymentDetails, secondTransactionPaymentDetails,
                            thirdTransactionPaymentDetails, branchAccountID, collectionCommissionAccount);

                    String isoamount = String.format("%012d", Integer.parseInt(formattedcharge));
                    data.setCharges(Double.parseDouble(isoamount));

                    SendMoneyResponseData sendMoneyResponseData = SendMoneyResponseData.builder()
                            .T24Reference(t24Reference)
                            .charges(Double.parseDouble(formattedcharge))
                            .rrn(transactionRRN)
                            .build();

                    SendMoneyResponse.builder()
                            .status("00")
                            .message("Transaction processing successful.")
                            .data(sendMoneyResponseData)
                            .build();

                    tot24.setT24reference(t24Reference);
                    transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "SEND MONEY", "1200",
                            request.getAmount() , "000");
                } catch (Exception e) {
                    transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "SEND MONEY", "1200",
                            request.getAmount() , "000");
                    System.out.println(
                            "Unable to get charges for send money transaction reference " + transactionRRN);
                    e.printStackTrace();
                }
                data.setT24Reference(t24Reference);
                try {

                    String vbin = xSwitchParameterRepository.findByParamName("CARDLESSTXNBIN").get().getParamValue();
                    String CARDLESSTXNBIN = "123456";
                    String VIRTUALCARDBIN = vbin.equals("") ? CARDLESSTXNBIN : vbin;
                    String cno = bprCreditCardNumberGenerator.generate(VIRTUALCARDBIN, 12);
                    System.out.println("cnoenc **************************************** " + cno);
                    String cnoenc = desUtil.encryptPlainText(cno);

                    if (cnoenc != null || !Objects.requireNonNull(cnoenc).isEmpty()) {
                        Random generator = new Random();
                        String token = String.format("%06d", 100000 + generator.nextInt(899999));
                        System.out.println("token ======================= " + token);
                        MoneySend ms = new MoneySend();
                        ms.setChannel("POS");
                        ms.setAmount(String.valueOf(request.getAmount()));
                        ms.setSendernumber(senderMobileNo);
                        ms.setRecevernumber(receiverMobile);
                        ms.setAgentid(authenticateAgentResponse.getData().getAccountNumber());
                        ms.setMstoken(token);
                        ms.setCno(cnoenc);
                        ms.setFulfilmentstatus(0);

                        String senderNationalID = request.getSenderNationalID();
                        ms.setSendernationalid(senderNationalID);
                        ms.setTypeofid(nationalIdDocumentName);
                        ms.setSendmoneylegt24ref(t24Reference);

                        moneySendRepository.save(ms);

                        String recipientSMSMessage =
                                "You have received RWF "
                                        + request.getAmount()
                                        + " from "
                                        + senderMobileNo
                                        + " to "
                                        + "withdraw at a BPR agent. Provide VCARD no: "
                                        + cno
                                        + ", "
                                        + "Sender will share the passcode. MURAKOZE";

                        String SMScontent = recipientSMSMessage;
                        int ti = 0;
                        while (SMScontent.length() > 0) {

                            ScheduledSMS scheduledSMSTransaction = new ScheduledSMS();
                            scheduledSMSTransaction.setSentstatus(0);
                            scheduledSMSTransaction.setAttempts(0);
                            scheduledSMSTransaction.setReceiverphone(receiverMobile);
                            scheduledSMSTransaction.setTxnref(transactionRRN);

                            if (SMScontent.length() <= 160) {
                                scheduledSMSTransaction.setMessage(utilityService.encryptSensitiveData(SMScontent));
                                SMScontent = "";
                            } else {
                                scheduledSMSTransaction.setMessage(
                                        utilityService.encryptSensitiveData(SMScontent.substring(0, 160)));
                                SMScontent = SMScontent.substring(160);
                            }
                            scheduledSMSRepository.save(scheduledSMSTransaction);
                            ++ti;
                        }

                        String smssender1 =
                                "You have successfully sent RWF"
                                        + request.getAmount()
                                        + " to "
                                        + receiverMobile
                                        + " "
                                        + "via BPR Cardless transfer. Kindly share passcode with the recipient."
                                        + "passcode : "
                                        + token
                                        + ". Thanks for banking with us.";

                        // Insert Second SMS
                        String SMSContent = utilityService.encryptText(smssender1);
                        ScheduledSMS scheduledSMS = new ScheduledSMS();
                        scheduledSMS.setSentstatus(0);
                        scheduledSMS.setAttempts(0);
                        scheduledSMS.setMessage(SMSContent);
                        scheduledSMS.setReceiverphone(senderMobileNo);
                        scheduledSMS.setTxnref(transactionRRN);
                        scheduledSMSRepository.save(scheduledSMS);

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
            } else {
                return SendMoneyResponse.builder()
                        .status("098")
                        .message("Transaction failed. Unable to post transaction to remote server.")
                        .data(null)
                        .build();
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

    public String fetchSendMoneyTransactionCharges(
            String agentFloatAccount, String branchAccountID, String originalTransactionReference, double amount) {

        String validationReferenceNo = RRNGenerator.getInstance("BP").getRRN();
        System.out.printf(
                "Send Money Charges generated transaction ID %s for POS request transaction ID %s %n",
                validationReferenceNo, originalTransactionReference);

        try {

            /**
             * "Debit Agent float Acc - Credit Agent Suspense Acc, Dr Agent - Cr Agent Fees, Debit Agent
             * Fees - Credit Agent Tax Acc"
             */
            String configuredSendMoneySuspenseAccount = xSwitchParameterRepository.findByParamName("SENDMONEYSUSPENSE").get().getParamValue();

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

            final String t24Ip = xSwitchParameterRepository.findByParamName("T24_IP").get().getParamValue();
            final String t24Port = xSwitchParameterRepository.findByParamName("T24_PORT").get().getParamValue();

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
                        return charges.replace("RWF", "");

                    } else {
                        System.out.printf(
                                "Send Money Charges Request [Failed]: Transaction %s has no transaction charge %s from T24 Processing. %n",
                                validationReferenceNo, charges);
                        return String.format("%012d", Integer.parseInt("0"));
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
        return String.format("%012d", Integer.parseInt("0"));
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

            final String t24Ip = xSwitchParameterRepository.findByParamName("T24_IP").get().getParamValue();
            final String t24Port = xSwitchParameterRepository.findByParamName("T24_PORT").get().getParamValue();

            t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);
            transactionService.updateT24TransactionDTO(tot24);

            String t24ref = tot24.getT24reference() == null ? "NA" : tot24.getT24reference();
            String t24responsecode =
                    tot24.getT24responsecode() == null ? "NA" : tot24.getT24responsecode();
            if (!t24responsecode.equals("1")) {
                tot24.getT24failnarration();
                log.info("Commission applied for send money transaction " + gatewayref);
            } else {
                log.info("Commission applied for send money transaction " + gatewayref + ". Commission transaction gateway reference " + refno + " and T24 Response: " + t24ref);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SendMoneyResponse processReceiveMoneyRequest(ReceiveMoneyRequest request, String transactionRRN) {

        Optional<AuthenticateAgentResponse> optionalAuthenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials(), agentValidation);
        if (optionalAuthenticateAgentResponse.isEmpty()) {
            log.info(
                    "Send Money transaction :[Failed] Missing agent information %n");
            return SendMoneyResponse.builder()
                    .status("117")
                    .message("Missing agent information")
                    .data(null)
                    .build();
        } else if (optionalAuthenticateAgentResponse.get().getCode() != HttpStatus.OK.value()) {
            return SendMoneyResponse
                    .builder()
                    .status(String.valueOf(
                            optionalAuthenticateAgentResponse.get().getCode()))
                    .message(optionalAuthenticateAgentResponse.get().getMessage())
                    .build();
        }

        AuthenticateAgentResponse authenticateAgentResponse = optionalAuthenticateAgentResponse.get();
        long agentFloatAccountBalance = agentTransactionService.fetchAgentAccountBalanceOnly(authenticateAgentResponse.getData().getAccountNumber());

        if (agentFloatAccountBalance < request.getAmount()) {
            return SendMoneyResponse.builder()
                    .status("098")
                    .message("Insufficient agent account balance")
                    .data(null)
                    .build();
        }

        String channel = "1510";
        String posRRN = transactionRRN;
        String sendMoneySuspense = xSwitchParameterRepository.findByParamName("SENDMONEYSUSPENSE").get().getParamValue();

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

        String paymentdetail3 =
                new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(new java.util.Date());
        String paymentdetail2 = authenticateAgentResponse.getData().getAccountNumber();

        try {

            String tid = "PC";
            String narr = "RECEIVEMONEY";
            Optional<MoneySend> optionalMoneySend = getSendMoneyTxn(recipientPhoneNo, passcode, String.valueOf(request.getAmount()));

            if (!optionalMoneySend.isPresent()) {
                return SendMoneyResponse.builder()
                        .status("139")
                        .message("Transaction failed. Missing original send money record.")
                        .data(null)
                        .build();
            }

            MoneySend sendMoneyTxn = optionalMoneySend.get();
            String storedVCard = "";
            try {
                storedVCard = desUtil._decrypt(sendMoneyTxn.getCno());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                System.out.println("Error encrypting card no for send money txn ref ~ " + posRRN);
                return SendMoneyResponse.builder()
                        .status("116")
                        .message("Transaction failed. Contact administrator")
                        .data(null)
                        .build();
            }

            if (!storedVCard.equalsIgnoreCase(request.getVCard())) {
                return SendMoneyResponse.builder()
                        .status("116")
                        .message("Transaction failed. Invalid vCard. Please try again.")
                        .data(null)
                        .build();
            }

            String paymentdetail1 = recipientPhoneNo;
            paymentdetail3 = narr;
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
                            + posRRN
                            + ",ORDERING.BANK::='BPR',"
                            + "PAYMENT.DETAILS:1:= "
                            + utilityService.sanitizePaymentDetails(paymentdetail1.trim(), "Send Money Withdrawal").trim()
                            + ","
                            + "PAYMENT.DETAILS:2:="
                            + paymentdetail2.trim()
                            + ","
                            + "PAYMENT.DETAILS:3:="
                            + paymentdetail3.trim();

            System.out.println("Receive money OFS " + smwOFS);

            String tot24str = String.format("%04d", smwOFS.length()) + smwOFS;
            Long moneysendid_or = sendMoneyTxn.getMoneysendid();
            String t24ref = "";
            T24TXNQueue tot24 = new T24TXNQueue();
            // base 64 encode request in db

            tot24.setRequestleg(tot24str);
            tot24.setStarttime(System.currentTimeMillis());
            tot24.setTxnchannel(channel);
            tot24.setGatewayref(posRRN);
            tot24.setPostedstatus("0");
            tot24.setAttempts(0);
            tot24.setTid(tid);
            tot24.setCreditacctno(authenticateAgentResponse.getData().getAccountNumber());
            tot24.setDebitacctno(sendMoneySuspense);

            final String t24Ip = xSwitchParameterRepository.findByParamName("T24_IP").get().getParamValue();
            final String t24Port = xSwitchParameterRepository.findByParamName("T24_PORT").get().getParamValue();

            t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);
            transactionService.updateT24TransactionDTO(tot24);
            t24ref = tot24.getT24reference() == null ? "NA" : tot24.getT24reference();


            if (tot24.getT24responsecode().equalsIgnoreCase("1")) {

                sendMoneyTxn.setFulfilmentref(posRRN);
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
                smssched.setTxnref(posRRN);
                scheduledSMSRepository.save(smssched);


                tot24.setT24reference(tot24.getT24reference());
                transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "RECEIVE MONEY", "1200",
                        request.getAmount() , "000");


                SendMoneyResponseData sendMoneyResponseData = SendMoneyResponseData.builder()
                        .T24Reference(tot24.getT24reference())
                        .charges(0.0)
                        .rrn(transactionRRN)
                        .build();

                return SendMoneyResponse.builder()
                        .status("00")
                        .message("Transaction processed successfully")
                        .data(sendMoneyResponseData)
                        .build();

            } else {

                tot24.setT24reference(tot24.getT24reference());
                transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "RECEIVE MONEY", "1200",
                        request.getAmount() , "118");
                return SendMoneyResponse.builder()
                        .status("118")
                        .message("Transaction failed. " + tot24.getT24failnarration())
                        .data(null)
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
