package co.ke.tracom.bprgateway.web.sendmoneyBulkpayment.service;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.Data;
import co.ke.tracom.bprgateway.web.agenttransactions.services.AgentTransactionService;
import co.ke.tracom.bprgateway.web.bankbranches.entity.BPRBranches;
import co.ke.tracom.bprgateway.web.bankbranches.service.BPRBranchService;
import co.ke.tracom.bprgateway.web.exceptions.custom.InsufficientAccountBalanceException;
import co.ke.tracom.bprgateway.web.exceptions.custom.InvalidAgentCredentialsException;
import co.ke.tracom.bprgateway.web.sendmoney.data.response.SendMoneyResponse;
import co.ke.tracom.bprgateway.web.sendmoney.data.response.SendMoneyResponseData;
import co.ke.tracom.bprgateway.web.sendmoney.entity.MoneySend;
import co.ke.tracom.bprgateway.web.sendmoney.repository.MoneySendRepository;
import co.ke.tracom.bprgateway.web.sendmoney.services.BPRCreditCardNumberGenerator;
import co.ke.tracom.bprgateway.web.sendmoney.services.DesUtil;
import co.ke.tracom.bprgateway.web.sendmoney.services.SendMoneyService;
import co.ke.tracom.bprgateway.web.sendmoneyBulkpayment.data.AgentUsername;
import co.ke.tracom.bprgateway.web.sendmoneyBulkpayment.data.SendMoneydata;
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
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import co.ke.tracom.bprgateway.web.util.services.UtilityService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static co.ke.tracom.bprgateway.web.sms.dto.SMSRequest.SMS_FUNCTION_RECEIVER;
import static co.ke.tracom.bprgateway.web.sms.dto.SMSRequest.SMS_FUNCTION_SENDER;
import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.*;


@RequiredArgsConstructor
@Service
@Slf4j

public class BulkSendMoneyService  {
    public final String CURRENCY_ISO_FORMAT = "%012d";
    private final String SEND_MONEY_TRANSACTION_LOG_LABEL = "Send money transaction [";
    private final String SEND_MONEY_SUSPENSE_ACC = "SENDMONEYSUSPENSE";
    private final String SEND_MONEY_LABEL = "SEND MONEY";

    private final Long SENDMONEY_TRANSACTION_LIMIT_ID = 1L;
    private final Long RECEIVEMONEY_TRANSACTION_LIMIT_ID = 2L;
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
    @Autowired
    private SendMoneyService sendMoneyService;
    private long messageId=0;


    //public CompletableFuture<List<SendMoneyRequest>>


//    public Authentication principal = (Authentication) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    String username = principal.getName();
//public MerchantAuthInfo getUserDetails(MerchantAuthInfo authInfo){
//    MerchantAuthInfo merchantAuthInfo=new MerchantAuthInfo();
//    merchantAuthInfo.setPassword(authInfo.getPassword());
//    merchantAuthInfo.setUsername(authInfo.getUsername());
//    return merchantAuthInfo;
//
//    }
//
//       // return currentUserName;
//
//    //create methode to read and pasrse the data from the file.
//     public List<SendMoneydata>parseCSV(File file) throws IOException {
//      List<SendMoneydata>requests=new ArrayList<>();
//      CsvSchema schema = CsvSchema.emptySchema().withHeader();
//        ObjectReader objectReader = new CsvMapper()
//            .readerFor(SendMoneydata.class)
//            .with(schema);
//        MappingIterator<SendMoneydata> userMappingIterator = objectReader.readValues((DataInput) file);
//        return userMappingIterator.readAll();
// }

//public List<String>fetchIDs(SendMoneyRequest request){
//
//    List<String>ids=new ArrayList<>();
//    ids.add(request.getSenderMobileNo());
//    return ids;
//
//    }

    public CompletableFuture<List<SendMoneyResponse>> bulkSendMoneyProcess(List<SendMoneydata> request, String transactionRRN, String username) {

        SendMoneydata finalRequest = request.iterator().next();
        // SendMoneydata finalRequest = (SendMoneydata) request;
//        request.forEach(data->{
//        });
        request.toArray();
        List<SendMoneyResponse> responses=new ArrayList<>();
        for (SendMoneydata sendMoneydata : request) {

            String senderNo = sendMoneydata.getSenderMobileNo();
            String receiverNo = sendMoneydata.getRecipientMobileNo();
            String senderNID = sendMoneydata.getSenderNationalID();
            String senderIdType = sendMoneydata.getSenderNationalIDType();
            double amount = sendMoneydata.getAmount();


            T24TXNQueue toT24 = new T24TXNQueue();
            AgentUsername usernames = new AgentUsername();
            usernames.setUsername(username);
            AuthenticateAgentResponse authenticateAgentResponse;
            responses = new ArrayList<>();
            try {
                authenticateAgentResponse = baseServiceProcessor.bulkSendMoneyAuth(usernames);
            } catch (InvalidAgentCredentialsException e) {
                //    transactionService.saveFailedPasswordTransactions(toT24, "SEND_MONEY_LABEL", "1200",request.getAmount(),
//                  "117");
                for (SendMoneyResponse resp : responses)
                {
                    resp.setData(null);
                resp.setStatus("118");
                resp.setMessage(e.getMessage());
                responses=Collections.singletonList(resp);
            }
                return CompletableFuture.completedFuture(responses);
            }

            log.info(SEND_MONEY_TRANSACTION_LOG_LABEL + transactionRRN + "] processing begins. Request " + finalRequest);

            Data agentAuthData = authenticateAgentResponse.getData();


            long agentFloatAccountBalance = agentTransactionService.fetchAgentAccountBalanceOnly(agentAuthData.getAccountNumber());
            System.out.println("agentFloatAccountBalance =============== " + agentFloatAccountBalance);

            try {


//                if (agentFloatAccountBalance < amount) {
//                    transactionService.saveCardLessTransactionToAllTransactionTable(toT24, "RECEIVE MONEY", "1200",
//                            amount, "117",
//                            authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid(), "", "");

//                return SendMoneyResponse.builder()
//                        .status("117")
//                        .message("Transaction could not be processed. Insufficient Float balance")
//                        .data(null)
//                        .build();
                // }


//                TransactionLimitManagerService.TransactionLimit limitValid = limitManagerService.isLimitValid(SENDMONEY_TRANSACTION_LIMIT_ID, (long) amount);
//                if (!limitValid.isValid()) {
//                    transactionService.saveCardLessTransactionToAllTransactionTable(toT24, SEND_MONEY_LABEL, "1200",
//                            amount, "061",
//                            authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid(), "", "");
//                    responses.setStatus("061");
//                    responses.setMessage("Amount should be between" + limitValid.getLower() + " and " + limitValid.getUpper());
//                    return responses;
//                }
                System.out.println("Agent Auth data is ========" + authenticateAgentResponse.getData().getTid());
                System.out.println("Agent Auth data is ========" + authenticateAgentResponse.getData().getAccountNumber());
                BPRBranches branch = branchService.fetchBranchAccountsByBranchCode(authenticateAgentResponse.getData().getAccountNumber());

                String branchAccountID = branch.getId();
                doesAgentHaveSufficientBalance(finalRequest, transactionRRN, agentAuthData, agentFloatAccountBalance, branchAccountID);

                // String senderMobileNo = finalRequest.getSenderMobileNo().trim();
                String senderMobileNo = senderNo;
                sendMoneyService.validateMobileNumberLength(transactionRRN, senderMobileNo.length());
                // String receiverMobile = finalRequest.getRecipientMobileNo().trim();
                String receiverMobile = receiverNo;
                sendMoneyService.compareSenderRecipientMobileNumbers(transactionRRN, receiverMobile.equalsIgnoreCase(senderMobileNo));
                String configuredSendMoneySuspenseAccount = xSwitchParameterService.fetchXSwitchParamValue(SEND_MONEY_SUSPENSE_ACC);
                String[] paymentDetails = new String[3];
                paymentDetails[0] = senderMobileNo + "/" + receiverMobile;
                paymentDetails[1] = agentAuthData.getNames() + " " + agentAuthData.getAccountNumber();
                paymentDetails[2] = SEND_MONEY_LABEL;

                String sendMoneyOFSMsg = bootstrapBulksendMoneyOFS(amount, transactionRRN, agentAuthData, branchAccountID,
                        configuredSendMoneySuspenseAccount, paymentDetails);
                String tot24str = String.format("%04d", sendMoneyOFSMsg.length()) + sendMoneyOFSMsg;
                log.info(SEND_MONEY_TRANSACTION_LOG_LABEL + transactionRRN + "] OFS is ready. " + tot24str);
                String nationalIdDocumentName = senderIdType.equals("0") ? "NID" : "OTHERS";
                String tid = "PC";
                T24TXNQueue tot24 = sendMoneyService.prepareT24Transaction(transactionRRN, agentAuthData, configuredSendMoneySuspenseAccount, tot24str, tid);
                processBulkSendMoneyTransaction(tot24);

                if ((tot24.getT24responsecode().equalsIgnoreCase("1"))) {
                    return processSuccessfulSendMoneyT24Transaction(finalRequest, transactionRRN, agentAuthData, branchAccountID,
                            paymentDetails, nationalIdDocumentName, tid, tot24, authenticateAgentResponse);
                } else {
                    return processFailedSendMoneyT24Transaction(finalRequest, transactionRRN, username, agentAuthData, tot24.getT24reference());
                }

            } catch (Exception w) {
                w.printStackTrace();
                log.info("Send money transaction error : " + w.getMessage());
            }
            transactionService.saveCardLessTransactionToAllTransactionTable(toT24, SEND_MONEY_LABEL, "1200",
                    amount, "116",
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid(), "", "");
        }
        for (SendMoneyResponse resp : responses
        ) {
            resp.setStatus("118");
            resp.setMessage("Transaction failed. Unable to generate virtual card. Please try again..");
            resp.setData(null);
            responses=Collections.singletonList(resp);
            return CompletableFuture.completedFuture(responses);
        }

        return CompletableFuture.completedFuture(responses);


//        return SendMoneyResponse.builder()
//                .status("116")
//                .message("Transaction processing failed. Please try again")
//                .data(null)
//                .build();

    }


    public CompletableFuture<List<SendMoneyResponse>> processFailedSendMoneyT24Transaction(SendMoneydata request, String transactionRRN, String username, Data agentAuthData, String t24Reference) throws InvalidAgentCredentialsException {
        T24TXNQueue tot24 = new T24TXNQueue();
        List<SendMoneyResponse>  response= new ArrayList<>();
        AgentUsername usernames=new AgentUsername();
                usernames.setUsername(username);

        AuthenticateAgentResponse authenticateAgentResponse = baseServiceProcessor.bulkSendMoneyAuth(usernames);

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

                //TODO find the logged in username and use it to get the TID and MID.

                authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid(),"","");

//        return SendMoneyResponse.builder()
//                .status("098")
//                .message("Transaction failed. Unable to post transaction to remote server.")
//                .data(sendMoneyResponseData)
//                .build();
        for (SendMoneyResponse resp:response
        ) {
            resp.setStatus("118");
            resp.setMessage("Transaction failed. Unable to generate virtual card. Please try again..");
            resp.setData(null);
            response= Collections.singletonList(resp);

        }

        return CompletableFuture.completedFuture(response);
    }

    private void processBulkSendMoneyTransaction(T24TXNQueue tot24) {
        final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue(T24_IP);
        final String t24Port = xSwitchParameterService.fetchXSwitchParamValue(T24_PORT);
        t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);
        tot24.setPostedstatus("1");
        transactionService.updateT24TransactionDTO(tot24);
    }
    public void doesAgentHaveSufficientBalance(SendMoneydata request, String transactionRRN, Data agentAuthData, long agentFloatAccountBalance, String branchAccountID) {
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
            String channel = "PC Module";
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
            if (Objects.nonNull(tot24.getT24responsecode())) {
                if (tot24.getT24responsecode().equalsIgnoreCase("1")) {

                    return extractChargesFromResponse(validationReferenceNo, tot24);

                }
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

    private String bootstrapBulksendMoneyOFS(double amount, String transactionRRN, Data agentAuthData, String branchAccountID,
                                             String configuredSendMoneySuspenseAccount, String[] paymentDetails)
    {    return "0000AFUNDS.TRANSFER,SEND.MONEY/I//1/0,"
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


//    @Override
//    public SendMoneyResponse process(SendMoneyRequest request) throws Exception {
//        bulkSendMoneyProcess(request,null);
//        return null;
//    }


    @SneakyThrows
    public CompletableFuture<List<SendMoneyResponse>> processSuccessfulSendMoneyT24Transaction(SendMoneydata request, String transactionRRN,
                                                                                           Data agentAuthData, String branchAccountID,
                                                                                           String[] paymentDetails,
                                                                                           String nationalIdDocumentName, String tid,
                                                                                           T24TXNQueue tot24, AuthenticateAgentResponse authenticateAgentResponse) {
        String senderMobileNo = request.getSenderMobileNo();
        String receiverMobile = request.getRecipientMobileNo();
        List<SendMoneyResponse>  response= new ArrayList<>();
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
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid(),"Send Money","");
        } catch (Exception e) {
            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, SEND_MONEY_LABEL, "1200",
                    request.getAmount(), "098",
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid(),"","");
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

                saveSendMoneyTransaction(request, agentAuthData, nationalIdDocumentName, tot24.getT24reference(), generatedCardNo, passCode, transactionRRN);

                SMSRequest recipientMessage = saveRecipientMessage(request, transactionRRN, receiverMobile, senderMobileNo, vCardNo,passCode);
                SMSRequest senderMessage = saveSenderMessage(request, messageId, transactionRRN, receiverMobile, senderMobileNo);

                String fdiSMSAPIAuthToken = smsService.getFDISMSAPIAuthToken();

                SMSResponse recipientResponse = smsService.sendFDISMSRequest(fdiSMSAPIAuthToken, recipientMessage);
                log.info("Recipient SMS Response[" + transactionRRN + "]: " + recipientResponse.toString());
                SMSResponse senderResponse = smsService.sendFDISMSRequest(fdiSMSAPIAuthToken, senderMessage);
                log.info("Sender SMS Response[" + transactionRRN + "]: " + senderResponse.toString());

                for (SendMoneyResponse resp:response
                     ) {
                    resp.setStatus("00");
                    resp.setMessage("Send money transaction processed successfully.");
                    resp.setData(data);
                    response=Collections.singletonList(resp);
                    return CompletableFuture.completedFuture(response);
                }

//                return SendMoneyResponse.builder()
//                        .status("00")
//                        .message("Send money transaction processed successfully.")
//                        .data(data)
//                        .build();

            } else {
                System.err.println(
                        "Card less vcard generation error for rrn " + transactionRRN);
                transactionService.saveCardLessTransactionToAllTransactionTable(tot24, SEND_MONEY_LABEL, "1200",
                        request.getAmount(), "118",
                        authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid(),"","");
//                return SendMoneyResponse.builder()
//                        .status("118")
//                        .message("Transaction failed. Unable to generate virtual card. Please try again.")
//                        .data(null)
//                        .build();
                for (SendMoneyResponse resp:response
                ) {
                    resp.setStatus("118");
                    resp.setMessage("Transaction failed. Unable to generate virtual card. Please try again..");
                    resp.setData(null);
                    response=Collections.singletonList(resp);
                }
                return CompletableFuture.completedFuture(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=================================================" + e.getMessage());
            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, SEND_MONEY_LABEL, "1200",
                    request.getAmount(), "118",
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid(),"","");
//            return SendMoneyResponse.builder()
//                    .status("118")
//                    .message("Transaction failed. Please try again.")
//                    .data(null)
//                    .build();
            for (SendMoneyResponse resp:response
            ) {
                resp.setStatus("118");
                resp.setMessage("Transaction failed. Unable to generate virtual card. Please try again..");
                resp.setData(null);
                response=Collections.singletonList(resp);

            }
            return CompletableFuture.completedFuture(response);
        }
        return CompletableFuture.completedFuture(response);
    }
    private SMSRequest saveRecipientMessage(SendMoneydata request, String transactionRRN,
                                            String receiverMobile, String senderMobile, String vCardNo,String passCode) {

        String recipientMessage = recipientMessage(request, senderMobile, vCardNo,passCode);

        SMSRequest fdismsRequest = getFDISMSRequest(recipientMessage, receiverMobile, SMS_FUNCTION_RECEIVER);

        while (recipientMessage.length() > 0) {
            ScheduledSMS scheduledSMSTransaction = new ScheduledSMS();
            scheduledSMSTransaction.setSentstatus(1);
            scheduledSMSTransaction.setAttempts(0);
            scheduledSMSTransaction.setReceiverphone(receiverMobile);
            scheduledSMSTransaction.setTxnref(transactionRRN);
            scheduledSMSTransaction.setSendMoneyId(messageId);
            Long sendmoneytokenstarttime = 0L;
            if (moneySendRepository.findById(messageId).isPresent()){
                sendmoneytokenstarttime = moneySendRepository.findById(messageId).get().getSendmoneytokenstarttime();
            }
            scheduledSMSTransaction.setSendmoneytokenstarttime(sendmoneytokenstarttime);

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
    private SMSRequest getFDISMSRequest(String message, String smsRecipientNo, String smsFunctionReceiver) {

        System.err.println("message = " + smsFunctionReceiver + " : " + message);
        return SMSRequest.builder()
                .recipient(smsRecipientNo)
                .SMSFunction(smsFunctionReceiver)
                .message(message)
                .build();
    }

    private SMSRequest saveSenderMessage(SendMoneydata request, long messageId, String transactionRRN,
                                         String recipientMobile, String senderMobileNo) {

        String senderMessage = senderMessage(request, recipientMobile);
        SMSRequest fdismsRequest = getFDISMSRequest(senderMessage, senderMobileNo, SMS_FUNCTION_SENDER);
        // Insert Second SMS
        String SMSContent = utilityService.encryptText(senderMessage);
        ScheduledSMS scheduledSMS = new ScheduledSMS();
        scheduledSMS.setSentstatus(1);
        scheduledSMS.setAttempts(0);
        scheduledSMS.setMessage(SMSContent);
        scheduledSMS.setReceiverphone(senderMobileNo);
        scheduledSMS.setTxnref(transactionRRN);
        scheduledSMS.setSendMoneyId(messageId);
        Long sendmoneytokenstarttime = 0L;
        if (moneySendRepository.findById(messageId).isPresent()){
            sendmoneytokenstarttime = moneySendRepository.findById(messageId).get().getSendmoneytokenstarttime();
        }
        scheduledSMS.setSendmoneytokenstarttime(sendmoneytokenstarttime);
        scheduledSMSRepository.save(scheduledSMS);

        return fdismsRequest;
    }

    private String senderMessage(SendMoneydata request, String recipientMobile ) {
        return "You have successfully sent RWF"
                + request.getAmount()
                + " to "
                + recipientMobile
                + " via BPR Cardless transfer."
                + ". Thanks for banking with us.";
    }

    private String recipientMessage(SendMoneydata request, String sendMobile, String cardNo,String passCode) {
        return "You have received RWF"
                + request.getAmount()
                + " from "
                + sendMobile
                + " to withdraw at a BPR agent. Provide VCARD no: "
                + cardNo
                + " ,and PASSCODE: "
                +passCode;
    }
    private void saveSendMoneyTransaction(SendMoneydata request, Data agentAuthData, String nationalIdDocumentName,
                                          String t24Reference, String generatedCardNo, String token, String transactionRRN) {
        String senderMobileNo = request.getSenderMobileNo();
        String receiverMobile = request.getRecipientMobileNo();

        MoneySend ms = new MoneySend();
        ms.setChannel("PC Module");
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


       // ms.setSendmoneytokenstarttime(timeNow);

        //use the exact configuration name
     //   Duration initialDurationByConfigurationName = tokenDurationService.getInitialDurationByConfigurationName(tokenConfigurationName);

      //  long expiryTime = now.plus(initialDurationByConfigurationName).toEpochMilli();
      //  ms.setSendmoneytokenexpiretime(expiryTime);
        moneySendRepository.saveAndFlush(ms);

        //fetch messageId
        messageId =ms.getMoneysendid();
    }

    private void saveSendMoneyCommission(String charges, String tid, String gatewayref,
                                         String debitAccount, String[] paymentDetails,
                                         String branchAccountID, String creditAccount) {
        try {
            String commissionOFS ="0000AFUNDS.TRANSFER" +
            ",BPR.MIB.OTHAC/I/PROCESS"
                    +
                    //",TRUSER1/123456/RW0010400"
                    ","+MASKED_T24_USERNAME
                    +
                    "/"+MASKED_T24_PASSWORD
                    +
                    //",,DEBIT.ACCT.NO::=400418836810116"
                    ",,DEBIT.ACCT.NO::="
                    +debitAccount
                    +
                    ",DEBIT.CURRENCY::=RWF"
                    +
                    ",DEBIT.AMOUNT::="+charges
                    +
                    //",CREDIT.ACCT.NO::=RWF1021000290400"
                    ",CREDIT.ACCT.NO::="
                    +creditAccount
                    +
                    ",DEBIT.THEIR.REF::=FTTOBPRACCOUNT"
                    +
                    ",CREDIT.THEIR.REF::=FTTOBPRACCOUNT"
                    +
                    ",IBS.REF.NO::="
                    + gatewayref
                    +
                    ",CHANNEL::=AGB"
                    +
                    ",PAYMENT.DETAILS:1:=SB bulkpayment"
                    +
                    ",BILLER.ID::=AGBBULKAC\n";
//                    "0000AFUNDS.TRANSFER,SEND.MONEY/I//1/0,"
//                            + ""
//                            + MASKED_T24_USERNAME
//                            + "/"
//                            + MASKED_T24_PASSWORD
//                            + "/"
//                            + branchAccountID
//                            + ",,"
//                            + "TRANSACTION.TYPE::=ACMO,"
//                            + "DEBIT.ACCT.NO::="
//                            + debitAccount
//                            + ","
//                            + "DEBIT.AMOUNT::="
//                            + charges
//                            + ","
//                            + "CREDIT.ACCT.NO::="
//                            + creditAccount
//                            + ","
//                            + "DEBIT.CURRENCY::=RWF,"
//                            + "TCM.REF::="
//                            + gatewayref
//                            + ","
//                            + "PAYMENT.DETAILS:1:= "
//                            + utilityService.sanitizePaymentDetails(paymentDetails[0], "Send Money Commission").trim()
//                            + ","
//                            + "PAYMENT.DETAILS:2:="
//                            + paymentDetails[1].trim()
//                            + ","
//                            + "PAYMENT.DETAILS:3:="
//                            + paymentDetails[2].trim();

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

}
