package co.ke.tracom.bprgateway.web.wasac.service;

import co.ke.tracom.bprgateway.core.config.CustomObjectMapper;
import co.ke.tracom.bprgateway.core.tracomchannels.json.RestHTTPService;
import co.ke.tracom.bprgateway.core.util.AppConstants;
import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.servers.tcpserver.data.academicBridge.AcademicBridgeValidation;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.BillPaymentRequest;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.BillPaymentResponse;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.TransactionData;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.ValidationRequest;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.Data;
import co.ke.tracom.bprgateway.web.agenttransactions.services.AgentTransactionService;
import co.ke.tracom.bprgateway.web.switchparameters.XSwitchParameterService;
import co.ke.tracom.bprgateway.web.t24communication.services.T24Channel;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.CustomerProfileRequest;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.CustomerProfileResponse;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.Response;
import co.ke.tracom.bprgateway.web.wasac.data.payment.WasacPaymentResponse;
import co.ke.tracom.bprgateway.web.wasac.data.WascWaterTxnLog;
import co.ke.tracom.bprgateway.web.wasac.repository.WascWaterTxnLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.*;
import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_PASSWORD;

@Service
@Slf4j
@RequiredArgsConstructor
public class WASACService {

    private final CustomObjectMapper mapper = new CustomObjectMapper();

    private final RestHTTPService restHTTPService;

    @Value("${wasac.customer-profile.request-base-url}")
    private String WASACBaseURL;

    @Value("${wasac.customer-profile.request-suffix-url}")
    private String WASACProfileURL;

    @Value("${wasac.payment.advise-url}")
    private String WASACSPaymentAdviseURL;


    private final XSwitchParameterService xSwitchParameterService;
    private final BaseServiceProcessor baseServiceProcessor;
    private final AgentTransactionService agentTransactionService;

    private CustomerProfileResponse profileResponse1;
    private final T24Channel t24Channel;
    private final TransactionService transactionService;

    @Autowired
    private  final WascWaterTxnLogRepository repository;

    /**
     * Fetch customer data given Customer ID from remote API. URL:
     * https://dev.api.wasac.rw/<customerid>/profile
     *
     * <p>postname: client postname name: client name zone: zone(location) mobile: mobile phone number
     * email: clients email phone: clients' fixed phone personnalid: National ID branch: WASAC branch
     * balance: Due balance meterid: Water Meter Number customerid: Unique customer identifier
     *
     * @param profileRequest
     */
    public CustomerProfileResponse fetchCustomerProfile(CustomerProfileRequest profileRequest) {
        CustomerProfileResponse profileResponse =
                CustomerProfileResponse.builder()
                        .status(AppConstants.EXCEPTION_OCCURRED_ON_EXTERNAL_HTTP_REQUEST.value()).build();
        try {
            String requestURL = WASACBaseURL + "/" + profileRequest.getCustomerId() + WASACProfileURL;
            String results = restHTTPService.sendGetOKHTTPRequest(requestURL);
            log.info("WASAC SERVICE RESPONSE: {}", results);
            profileResponse =
                    mapper.readValue(results, CustomerProfileResponse.class);
            profileResponse.setStatus(AppConstants.TRANSACTION_SUCCESS_STANDARD.value());

            profileResponse.setMessage("Customer profile fetched successfully");

            //profileResponse1 = profileResponse;
        } catch (Exception e) {
            e.printStackTrace();
            logError(e);
        }
        return profileResponse;
    }

    public void fetchProfile() {


        List<TransactionData> data = new ArrayList<>();
        data.add(TransactionData.builder().name("school name").value("Tracom Academy").build());
        data.add(TransactionData.builder().name("school Account No").value("115627393").build());
        data.add(TransactionData.builder().name("school account name").value("Tracom Academy").build());
        data.add(TransactionData.builder().name("Student Reg").value("COM/0510/10").build());
        data.add(TransactionData.builder().name("Type Of Payment").value("Tuition").build());

        AcademicBridgeValidation response =
                AcademicBridgeValidation.builder()
                        .responseCode("00")
                        .responseMessage("Transaction processed successfully")
                        .data(data)
                        .build();
    }


    @SneakyThrows
    public BillPaymentResponse payWaterBill(BillPaymentRequest request)  {
        WascWaterTxnLog waterTxnLog = new WascWaterTxnLog();
        WasacPaymentResponse paymentResponse =
            new WasacPaymentResponse()
                    .setStatus(AppConstants.EXCEPTION_OCCURRED_ON_EXTERNAL_HTTP_REQUEST.value());
        BillPaymentResponse billPaymentResponse;
        List<TransactionData> transactionData = new ArrayList<>();
        //try {
            //payment impl
            AuthenticateAgentResponse authenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(
                    new MerchantAuthInfo(request.getCredentials().getUsername(),request.getCredentials().getPassword()));

            if (authenticateAgentResponse.getCode() != 200) {
                WasacPaymentResponse wasacPaymentResponse = new WasacPaymentResponse();
                wasacPaymentResponse.setStatus("Invalid credentials");
                billPaymentResponse = getPaymentResponse(wasacPaymentResponse);
                return billPaymentResponse;
            }
            Data agentAuthData = authenticateAgentResponse.getData();
            long agentFloatAccountBalance = agentTransactionService.fetchAgentAccountBalanceOnly(agentAuthData.getAccountNumber());

            String waitTime = xSwitchParameterService.fetchXSwitchParamValue("T24_INQUIRY_WAIT_TIME_MILLISECONDS");
            waitTime = waitTime.isEmpty() ? "30000" : waitTime;
            /*profileResponse1 = fetchCustomerProfile(
                    CustomerProfileRequest.builder()
                        .customerId("CUSTOMER_ID")
                        .credentials(
                                MerchantAuthInfo.builder()
                                    .username(request.getCredentials().getUsername())
                                    .password(request.getCredentials().getPassword()
                                ).build()
                    ).build());*/
            //Response profileResponse1Data = profileResponse1.getData();

            //extracting data from the request payload
            List<TransactionData> transactionDataList = request.getData();
            //List<TransactionData> transactionDataList = request.getCredentials().getData();
            long customerId;
            String account;
            Long amount;
            String description;
            if (transactionDataList != null && transactionDataList.size() > 0) {
                customerId = Long.parseLong(transactionDataList.get(0).getValue());
                account = transactionDataList.get(1).getValue();
                amount = Long.parseLong(transactionDataList.get(2).getValue());
                description = transactionDataList.get(3).getValue();
            }else {
                customerId = 89565565;
                account = agentAuthData.getAccountNumber();
                amount = 1500L;
            }

            String RRN = RRNGenerator.getInstance("BP").getRRN();

            String meterNo = "211610021";

            waterTxnLog.setAmount(String.valueOf(amount));
            waterTxnLog.setCustomerName(agentAuthData.getNames());
            waterTxnLog.setMeterNo(/*profileResponse1Data.getMeterid()*/meterNo);
            waterTxnLog.setPosRef(RRN);
            waterTxnLog.setMid(agentAuthData.getMid());
            waterTxnLog.setTid(agentAuthData.getTid());

            String tot24str = getT24OFS(/*profileResponse1Data.getMeterid()*/meterNo, account, amount,
                    /*profileResponse1Data.getName()*/agentAuthData.getNames(),request.getCredentials().getTid(),
                    agentAuthData.getMid(), agentFloatAccountBalance);


            T24TXNQueue tot24 = new T24TXNQueue();

            tot24.setMeterno(/*profileResponse1Data.getMeterid()*/meterNo);
            // base 64 encode request in db
            tot24.setRequestleg(tot24str);
            tot24.setStarttime(System.currentTimeMillis());
            //tot24.setTxnmti(isomsg.getMTI());
            tot24.setTxnchannel("PC");
            tot24.setGatewayref(RRN);
            tot24.setPostedstatus("0");
            //tot24.setProcode(isomsg.getString(3));
            tot24.setTid(agentAuthData.getTid());
            tot24.setDebitacctno(account);
            tot24.setTotalchargeamt("0.0");

        final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue("T24_IP");
        final String t24Port = xSwitchParameterService.fetchXSwitchParamValue("T24_PORT");

            t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);

            transactionService.updateT24TransactionDTO(tot24);


            //todo to be continued

            System.out.println(".Gateway ref.. " + RRN + " txn queued for t24 posting !!");

            String accname = tot24.getCustomerName() == null ? "" : tot24.getCustomerName();
            String errorMessage =
                    tot24.getT24failnarration() == null ? "" : tot24.getT24failnarration();
            if (errorMessage.isEmpty()) {
                if (tot24.getT24responsecode().equals("3")) {
                    //isomsg.set(60, "Transaction failed due to timeout");
                    //isomsg.set(61, "");
                    //isomsg.set(39, "098");

                    transactionData.add(addValidationResponse("Transaction failure","Transaction failed due to timeout"));
                    transactionData.add(addValidationResponse("Action code","098"));
                    billPaymentResponse = getBillPaymentResponse(transactionData, AppConstants.TRANSACTION_SUCCESS_STANDARD.value(),
                            AppConstants.TRANSACTION_SUCCESS_STANDARD.getReasonPhrase());
                } else {

             /*       isomsg.set(60, f60);
                    //                    isomsg.set(
//                            60,
//                            meterNo
//                                    + "#"
//                                    + phone
//                                    + "#"
//                                    + accname
//                                    + "#"
//                                    + customerAddress
//                                    + "#"
//                                    + tot24.getTokenNo()
//                                    + "#"
//                                    + tot24.getUnitsKw()
//                                    + "#");
                    isomsg.set(61, tot24.getT24reference());
                    isomsg.set(39, "000");
                    isomsg.set(5, tot24.getTotalchargeamt());*/

                    waterTxnLog.setGatewayT24PostingStatus("1");
                    // todo set token respondent elecTxnLogs.setToken_no(tot24.getTokenNo());
                    insertWascTxnLogs(waterTxnLog);

                    transactionData.add(addValidationResponse("T24 Reference",RRN));
                    transactionData.add(addValidationResponse("Reference No",RRN));
                    transactionData.add(addValidationResponse("name",agentAuthData.getNames()));
                    transactionData.add(addValidationResponse("Phone",/*profileResponse1Data.getPhone()*/ "0710101010"));
                    transactionData.add(addValidationResponse("Token No",tot24.getTokenNo()));
                    transactionData.add(addValidationResponse("Units",tot24.getUnitsKw()));
                    transactionData.add(addValidationResponse("Meter No",/*profileResponse1Data.getMeterid()*/meterNo));
                    transactionData.add(addValidationResponse("Customer Id", Long.toString(customerId)));

                    transactionData.add(addValidationResponse("Action code","000"));
                    transactionData.add(addValidationResponse("Charge amount",tot24.getTotalchargeamt()));

                    billPaymentResponse = getBillPaymentResponse(transactionData, AppConstants.TRANSACTION_SUCCESS_STANDARD.value(),
                            AppConstants.TRANSACTION_SUCCESS_STANDARD.getReasonPhrase());
                }
            } else {
                transactionData.add(addValidationResponse("t24 fail narration",tot24.getT24failnarration()));
                transactionData.add(addValidationResponse("Action code","135"));
                billPaymentResponse = getBillPaymentResponse(transactionData, AppConstants.EXCEPTION_OCCURRED_ON_EXTERNAL_HTTP_REQUEST.value(),
                        AppConstants.EXCEPTION_OCCURRED_ON_EXTERNAL_HTTP_REQUEST.getReasonPhrase());
                //isomsg.set(60, tot24.getT24failnarration().replace("\"", ""));
                //isomsg.set(39, "135");

                waterTxnLog.setGatewayT24PostingStatus("0");
            }
            tot24.setT24reference(tot24.getT24reference());

            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, request.getTnxType(), "0200",amount ,tot24.getPostedstatus() ,
                    agentAuthData.getTid(), agentAuthData.getMid());

       /* } catch (Exception e) {
            e.printStackTrace();
        }
        return isomsg;*/

            /*
            ResponseEntity<String> response =
                    restHTTPService.postRequest(request, WASACSPaymentAdviseURL);
            log.info("WASAC SERVICE RESPONSE: {}", response);
            WasacPaymentResponse paymentAdviseResponse =
                    mapper.readValue(response.getBody(), WasacPaymentResponse.class);
            paymentAdviseResponse.setStatus(AppConstants.TRANSACTION_SUCCESS_STANDARD.value());
            */
        /*} catch (Exception e) {
            logError(e);
            e.printStackTrace();
            billPaymentResponse.setResponseCode("500");
            billPaymentResponse.setResponseMessage(e.getMessage());
        }*/
        return billPaymentResponse;
    }

    //construct t24 OFS message string
    private String getT24OFS(String meterNo, String agentFloatAccount, Long Amount, String customerName, String terminalId,
                             String mid, long _balance){
        String t24UserName="RAJ001";//########U
        String t24Password = "123456";//########A
        String euclBankBranch = "";//BPRFunctions.getParambyName("DEFAULT_EUCL_BRANCH");
        String euclBankBranch1 = xSwitchParameterService.fetchXSwitchParamValue("DEFAULT_EUCL_BRANCH");

        String waterBankBranch = "RW0010593"; // todo get from bpr functions
        String accountDetails = "22"; // todo get bank branch == is same as bank branch from enquiry leg
        String balance = String.valueOf(_balance); // todo get balance

        // create t24 string
        String t24 =
                "0000AFUNDS.TRANSFER,BPR.WASAC.PAYMENT.AGB/I/PROCESS,"
                        + t24UserName
                        + "/"
                        + t24Password
                        + "/"
                        + euclBankBranch1
                        + ",,"
                        + "TRANSACTION.TYPE::=ACWB,BPR.ID.NUMBER::="
                        + meterNo
                        + ","
                        + "DEBIT.ACCT.NO::="
                        + agentFloatAccount
                        + ","
                        + "DEBIT.CURRENCY::=RWF,"
                        + "ORDERING.BANK::=BNK,"
                        + "DEBIT.VALUE.DATE::="
                        + getFormattedDate("yyyyMMdd")
                        + ","
                        + "DEBIT.AMOUNT::="
                        + Amount.doubleValue()
                        + ","
                        + "INVOICE.DETAILS::= "
                        + accountDetails
                        + ","
                        + "AB.SCHOOL.ID::="
                        + balance
                        + ","
                        + "AB.STU.NAME::="
                        + customerName
                        + ","
                        + "CREDIT.CURRENCY::=RWF,"
                        + "PAYMENT.DETAILS:1:1=WATER BILL PAYMENT," +
                        "PAYMENT.DETAILS:2:1="
                        + /*"terminalId*/ "PC"
                        + " "
                        + mid
                        + " "
                        + /*waterBankBranch*/"RW0010593";

                        String t24str ="0000AFUNDS.TRANSFER,BPR.WASAC.PAYMENT.AGB/I/PROCESS,RAJ001/123456/RW0010593,,TRANSACTION.TYPE::=ACWB,BPR.ID.NUMBER::=202213074,DEBIT.ACCT.NO::=408102653810194,DEBIT.CURRENCY::=RWF,ORDERING.BANK::=BNK,DEBIT.VALUE.DATE::=20210831,DEBIT.AMOUNT::=11000,INVOICE.DETAILS::=22.0,AB.SCHOOL.ID::=22,AB.STU.NAME::=UWIMANA LIBERATHA,CREDIT.CURRENCY::=RWF,PAYMENT.DETAILS:1:1=WATER BILL PAYMENT,PAYMENT.DETAILS:2:1=PO400003 000000 RW001000";
        return String.format("%04d", t24.length()) + t24str;
    }

    private static String getFormattedDate(String pattern ) {
        pattern = pattern.isEmpty() ? "yyyyMMdd" : pattern;
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(new Date());
    }


    private void logError(Exception ex) {
        log.error("WASAC SERVICE: {}", ex.getMessage());
    }

    public AcademicBridgeValidation validateWaterAccount(ValidationRequest validationRequest, String channel) {
        AcademicBridgeValidation response = AcademicBridgeValidation.builder().build();
        List<TransactionData> validationData = new ArrayList<>();
        try {
            AuthenticateAgentResponse authenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(
                    new MerchantAuthInfo(validationRequest.getCredentials().getUsername(), validationRequest.getCredentials().getPassword()));

            if (authenticateAgentResponse.getCode() != 200) {
                CustomerProfileResponse wasacPaymentResponse = new CustomerProfileResponse();
                wasacPaymentResponse.setStatus("Invalid credentials");
                response = getBridgeValidation(wasacPaymentResponse);
                return response;
            }
            Data agentAuthData = authenticateAgentResponse.getData();

            String waitTime = xSwitchParameterService.fetchXSwitchParamValue("T24_INQUIRY_WAIT_TIME_MILLISECONDS");
            waitTime = waitTime.isEmpty() ? "30000" : waitTime;
            //Response profileResponse1Data = profileResponse1.getData();

            String t24usn = "TRUSER1";//########U
            String t24pwd = "123456";//########A

            String euclBankBranch = "RW0010593";//xSwitchParameterService.fetchXSwitchParamValue("DEFAULT_EUCL_BRANCH");

            String meterNo ="213315124"; //"202213074";

            //if transaction data has been sent
            if (validationRequest.getData().size() > 0) {
                meterNo = validationRequest.getData().get(0).getValue().isBlank() ? /*profileResponse1Data.getMeterid()*/"89565565" : validationRequest.getData().get(0).getValue();
            }

            String newt24tem =
                    "0000AENQUIRY.SELECT,,"
                            + t24usn
                            + "/"
                            + t24pwd
                            + "/"
                            + euclBankBranch
                            + ",BPR.ENQ.WASAC.GET.DET,CUSTOMER.NO:EQ="
                            + meterNo;

            // TRANS.TYPE.ID:EQ= BALENQ
            String tot24str = String.format("%04d", newt24tem.length()) + newt24tem;

            String t24ref = RRNGenerator.getInstance("BP").getRRN();
            boolean postingsuccesst24 = false;

            // create a table or function to generate T24 messages
            T24TXNQueue tot24 = new T24TXNQueue();
            tot24.setMeterno(meterNo);
            // base 64 encode request in db
            tot24.setRequestleg(tot24str);
            tot24.setStarttime(System.currentTimeMillis());
            tot24.setTxnchannel(channel);
            tot24.setGatewayref(t24ref);
            tot24.setPostedstatus("0");
            tot24.setProcode("500000");
            //tot24.setTxnmti("1100");
            tot24.setTid(/*validationRequest.getCredentials().getTid() agentAuthData.getTid()*/ channel);

            final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue("T24_IP");
            final String t24Port = xSwitchParameterService.fetchXSwitchParamValue("T24_PORT");

            //long agentFloatAccountBalance = agentTransactionService.fetchAgentAccountBalanceOnly(agentAuthData.getAccountNumber());

            t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);

            System.out.println(">>>>> processing transaction >>>>> ");

            transactionService.updateT24TransactionDTO(tot24);


            //todo to be continued
            

            String accname = tot24.getCustomerName() == null ? "" : tot24.getCustomerName();
            System.out.println("transaction response >>>>>> " + tot24);
            if (!accname.isEmpty()) {
                validationData.add(addValidationResponse("Client post name",/*profileResponse1Data.getPostname()*/ tot24.getAccountname()));
                validationData.add(addValidationResponse("name",/*profileResponse1Data.getName()*/ tot24.getCustomerName()));
                validationData.add(addValidationResponse("zone",/*profileResponse1Data.getZone()*/ agentAuthData.getLocation()));
                validationData.add(addValidationResponse("Mobile No",/*profileResponse1Data.getMobile()*/ ""));
                validationData.add(addValidationResponse("Email",/*profileResponse1Data.getEmail()*/tot24.getCustemail()));
                validationData.add(addValidationResponse("Phone",/*profileResponse1Data.getPhone()*/tot24.getPhone()));
                validationData.add(addValidationResponse("National ID",/*profileResponse1Data.getPersonnalid()*/ ""));
                validationData.add(addValidationResponse("Branch",/*profileResponse1Data.getBranch()*/ ""));
                validationData.add(addValidationResponse("Balance",/*profileResponse1Data.getBalance()*/ tot24.getBaladvise()));
                validationData.add(addValidationResponse("Meter No",/*profileResponse1Data.getMeterid()*/ tot24.getMeterno()));
                validationData.add(addValidationResponse("Customer Id",/*profileResponse1Data.getCustomerid()*/ tot24.getCustid()));

                response = getAcademicBridgeValidation(validationData, AppConstants.TRANSACTION_SUCCESS_STANDARD.value(), AppConstants.TRANSACTION_SUCCESS_STANDARD.getReasonPhrase());
                
                /*isomsg.set(
                        60,
                        customerWASCId + "#" + phone + "#" + accname + "#" + tot24.getMeterno() + "#" + tot24.getBaladvise() + "#");
                isomsg.set(39, "000");
                isomsg.set(4, tot24.getBaladvise());*/
                
            } else {
                validationData.add(addValidationResponse("T24failnarration",tot24.getT24failnarration()));
                validationData.add(addValidationResponse("Action code","135"));
                response = getAcademicBridgeValidation(validationData, AppConstants.EXCEPTION_OCCURRED_ON_EXTERNAL_HTTP_REQUEST.value(), AppConstants.EXCEPTION_OCCURRED_ON_EXTERNAL_HTTP_REQUEST.getReasonPhrase());
                /*isomsg.set(60, tot24.getT24failnarration().replace("\"", ""));
                isomsg.set(39, "135");*/
            }

            transactionService.saveCardLessTransactionToAllTransactionTable(
                    tot24, validationRequest.getTnxType(), "WASC ACCOUNT VALIDATION",0,AppConstants.TRANSACTION_SUCCESS_STANDARD.value(), agentAuthData.getTid(), agentAuthData.getMid());

        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode("500");
            response.setResponseMessage(e.getMessage());
        }
        return response;
    }

    private TransactionData addValidationResponse(String name, String value) {
        return TransactionData.builder()
                .name(name)
                .value(value)
                .build();
    }

    private AcademicBridgeValidation getAcademicBridgeValidation(List<TransactionData> validationData, String value, String reasonPhrase) {
        return AcademicBridgeValidation.builder()
                .responseCode(value)
                .responseMessage(reasonPhrase)
                .data(validationData)
                .build();
    }

    private AcademicBridgeValidation getBridgeValidation(CustomerProfileResponse customerProfileResponse) {
        return getAcademicBridgeValidation(new ArrayList<>(), customerProfileResponse.getStatus(), "Transaction processing failed. Please try again");
    }

    private BillPaymentResponse getBillPaymentResponse(List<TransactionData> validationData, String value, String reasonPhrase) {
        return BillPaymentResponse.builder()
                .responseCode(value)
                .responseMessage(reasonPhrase)
                .data(validationData)
                .build();
    }

    private BillPaymentResponse getPaymentResponse(WasacPaymentResponse wasacPaymentResponse) {
        return getBillPaymentResponse(new ArrayList<>(), wasacPaymentResponse.getStatus(), "Transaction processing failed. Please try again");
    }

    private void insertWascTxnLogs(WascWaterTxnLog wascWaterTxnLog) {
        try {
            repository.save(wascWaterTxnLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
