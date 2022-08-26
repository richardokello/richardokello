package co.ke.tracom.bprgateway.servers.tcpserver;

import co.ke.tracom.bprgateway.core.config.CustomObjectMapper;
import co.ke.tracom.bprgateway.core.util.AppConstants;
import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.servers.tcpserver.data.academicBridge.AcademicBridgeValidation;
import co.ke.tracom.bprgateway.servers.tcpserver.data.billMenu.BillMenuRequest;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.*;
import co.ke.tracom.bprgateway.web.VisionFund.data.*;
import co.ke.tracom.bprgateway.web.VisionFund.data.custom.CustomVerificationRequest;
import co.ke.tracom.bprgateway.web.VisionFund.data.custom.CustomVerificationResponse;
import co.ke.tracom.bprgateway.web.VisionFund.service.VisionFundService;
import co.ke.tracom.bprgateway.web.academicbridge.data.studentdetails.GetStudentDetailsResponse;
import co.ke.tracom.bprgateway.web.academicbridge.services.AcademicBridgeT24;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.billMenus.data.BillMenuResponse;
import co.ke.tracom.bprgateway.web.billMenus.service.BillMenusService;
import co.ke.tracom.bprgateway.web.eucl.dto.request.EUCLPaymentRequest;
import co.ke.tracom.bprgateway.web.eucl.dto.request.MeterNoValidation;
import co.ke.tracom.bprgateway.web.eucl.dto.response.EUCLPaymentResponse;
import co.ke.tracom.bprgateway.web.eucl.dto.response.MeterNoData;
import co.ke.tracom.bprgateway.web.eucl.dto.response.MeterNoValidationResponse;
import co.ke.tracom.bprgateway.web.eucl.dto.response.PaymentResponseData;
import co.ke.tracom.bprgateway.web.eucl.service.EUCLService;
import co.ke.tracom.bprgateway.web.exceptions.custom.InvalidAgentCredentialsException;
import co.ke.tracom.bprgateway.web.exceptions.custom.UnprocessableEntityException;
import co.ke.tracom.bprgateway.web.ltss.data.LTSSRequest;
import co.ke.tracom.bprgateway.web.ltss.data.nationalIDValidation.NationalIDValidationRequest;
import co.ke.tracom.bprgateway.web.ltss.data.nationalIDValidation.NationalIDValidationResponse;
import co.ke.tracom.bprgateway.web.ltss.data.paymentContribution.LTSSPaymentResponse;
import co.ke.tracom.bprgateway.web.ltss.service.LtssService;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.CustomerProfileResponse;
import co.ke.tracom.bprgateway.web.wasac.service.WASACService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class BillRequestHandler {
    private final WASACService wasacService;
    private final EUCLService euclService;
    private static final String NO_DATA="Nodata";
    private static final String CLIENT_POST_NAME= "ClientPostName";
    private final AcademicBridgeT24 academicBridgeT24Service;
    private final TransactionService transactionService;
    private final BaseServiceProcessor baseServiceProcessor;
    private final static String billMenuRequestData="BILL MENU REQUEST DATA: {}";
    private final VisionFundService visionFundService;

    private final LtssService ltssService;
    public void menu(String requestString, BillMenusService billMenusService, NetSocket socket)
            throws JsonProcessingException, UnprocessableEntityException {
        CustomObjectMapper mapper = new CustomObjectMapper();
        /* Parse request string into bill menu request object */
        BillMenuRequest billMenuRequest = mapper.readValue(requestString, BillMenuRequest.class);
        String tnxType = billMenuRequest.getTnxType();

        if (tnxType != null && tnxType.equals("fetch-menu")) {
            BillMenuResponse billMenuResponse;
            if (billMenuRequest.getLang().equalsIgnoreCase("en")) {
                billMenuResponse = billMenusService.fetchEnglishMenus();
                log.info(billMenuRequestData, billMenuResponse.toString());
                writeResponseToTCPChannel(socket, mapper.writeValueAsString(billMenuResponse));
            } else if (billMenuRequest.getLang().equalsIgnoreCase("rw")) {
                billMenuResponse = billMenusService.fetchKinyarwandaMenus();
                log.info(billMenuRequestData, billMenuResponse.toString());
                writeResponseToTCPChannel(socket, mapper.writeValueAsString(billMenuResponse));
            }

        } else {
            log.error("TCP SERVER - BAD REQUEST DATA FOR FETCHING MENU: {} ", requestString);
            throw new UnprocessableEntityException("Entity cannot be processed");
        }
    }



    public AcademicBridgeValidation validation(ValidationRequest genericRequest, NetSocket socket)
            throws JsonProcessingException, UnprocessableEntityException {
        CustomObjectMapper mapper = new CustomObjectMapper();

        List<TransactionData> data = genericRequest.getData();
        try {
         baseServiceProcessor.authenticateAgentUsernamePassword(
                    new MerchantAuthInfo(genericRequest.getCredentials().getUsername(), genericRequest.getCredentials().getPassword()));
        } catch (InvalidAgentCredentialsException e) {
            throw new RuntimeException(e);
        }



        CustomerProfileResponse customerProfileResponse =new CustomerProfileResponse();
        AcademicBridgeValidation response = AcademicBridgeValidation.builder().build();

        MeterNoValidationResponse euclValidationResponse;
        MeterNoValidation euclValidation = new MeterNoValidation();


        switch (genericRequest.getSvcCode()) {

            case "01.1":
            case "01.2":

                if (genericRequest.getField()!=null&&genericRequest.getField().equalsIgnoreCase("Student Registration ID Number")) {

                    customerProfileResponse = (academicBridgeT24Service.validateStudentId(genericRequest.getValue()));
                    List<TransactionData> transactionData = new ArrayList<>();

                    if(customerProfileResponse != null && customerProfileResponse.getData().getSchool_ide()>1) {
                        response.setResponseCode("00");

                        response.setResponseMessage("Transaction successful");


                        GetStudentDetailsResponse customerProfileResponseData = customerProfileResponse.getData();
                        transactionData.add(TransactionData.builder()
                                .name("Billnumber")
                                .value(customerProfileResponseData.getStudent_reg_number())
                                .build()
                        );
                        transactionData.add(TransactionData.builder()
                                .name("StudentName")
                                .value(customerProfileResponseData.getStudent_name())
                                .build()
                        );
                        transactionData.add(TransactionData.builder()
                                .name("SchoolName")
                                .value(customerProfileResponseData.getSchool_name())
                                .build()
                        );
                        transactionData.add(TransactionData.builder()
                                .name("SchoolAccount")
                                .value(customerProfileResponseData.getSchool_account_number())
                                .build()
                        );
                        transactionData.add(TransactionData.builder()
                                .name("SchoolId")
                                .value(String.valueOf(customerProfileResponseData.getSchool_ide()))
                                .build()
                        );

                        response.setData(transactionData);
                    }else{
                        response.setResponseCode("10");

                        response.setResponseMessage("Something went wrong");
                        assert customerProfileResponse != null;
                        transactionData.add(TransactionData.builder()
                                .name("Data")
                                .value(String.valueOf(customerProfileResponse.getData().getStudent_name()))
                                .build()
                        );
                        response.setData(transactionData);


                    }

                } else {
                    log.info("Wrong svc code and field combination :" + genericRequest.getField());


                    customerProfileResponse.setMessage("Wrong svc code and field name combination");
                    customerProfileResponse.setStatus("09");
                    customerProfileResponse.setData(null);
                }
                break;


            //Validation for wasac customer profile.
            case "02.1":
            case "02.2":
                {
                        if (genericRequest.getBill() == null){
                            genericRequest.setBill("bill");
                        }
                        response = wasacService.validateWaterAccount(genericRequest, "POS");
                }
                break;

            //EUCL validation
            case "03.1":
            case "03.2":

                //Extract data from validation request object to local variables only when some data has been sent
                if (!data.isEmpty()){
                    String amount= data.get(0).getValue();
                //    String phoneNumber=data.get(1).getValue();
                    String meterNo = data.size()>1? data.get(1).getValue():"00";

                    euclValidation.setAmount(amount);
                    euclValidation.setCredentials(
                            new MerchantAuthInfo(
                                    genericRequest.getCredentials().getUsername(),
                                    genericRequest.getCredentials().getPassword()
                            )
                    );
                    euclValidation.setMeterNo(meterNo);

                    String requestRefNo = RRNGenerator.getInstance("EV").getRRN();
                    euclValidationResponse = euclService.validateEUCLMeterNo(euclValidation, requestRefNo);


                    //Extract data from service response object to the generic response object
                    response.setResponseCode(euclValidationResponse.getStatus());
                    response.setResponseMessage(euclValidationResponse.getMessage());

                    List<TransactionData> euclTransactionData = new ArrayList<>();
                    MeterNoData euclValidationResponseData = euclValidationResponse.getData();

                    euclTransactionData.add(TransactionData.builder().name("Meter number").value(euclValidationResponseData.getMeterNo()).build());
                 //  euclTransactionData.add(TransactionData.builder().name("rrn").value(euclValidationResponseData.getRrn()).build());

                    response.setData(euclTransactionData);
                }

                break;
            case "04.1": {
                VisionFundRequest visionFundRequest = new VisionFundRequest();
                VisionFundResponse fundResponse = new VisionFundResponse();
                if (genericRequest.getData().size() == 0) {
                    fundResponse.setResponseCode("05");
                    fundResponse.setResponseMessage("Mobile number missing");
                    BeanUtils.copyProperties(fundResponse, response);
                    log.error("Mobile number missing");
                    //writeResponseToTCPChannel(socket, mapper.writeValueAsString(fundResponse));
                    break;
                } else {
                    BeanUtils.copyProperties(genericRequest, visionFundRequest);
                    CustomVerificationRequest verificationRequest = new CustomVerificationRequest();
                    verificationRequest.setAccountNumber(visionFundRequest.getData().get(1).getValue());
                    verificationRequest.setMobileNumber(visionFundRequest.getData().get(0).getValue());

                    verificationRequest.setCredentials(visionFundRequest.getCredentials());
                    verificationRequest.setTnxType(visionFundRequest.getTnxType());

                    CustomVerificationResponse verificationResponse = visionFundService.verifyCustomer(verificationRequest);
                    fundResponse.setResponseCode(verificationResponse.getResponseCode());
                    fundResponse.setResponseMessage(verificationResponse.getResponseString());

                    List<TransactionData> transactionData = new ArrayList<>();
                    transactionData.add(TransactionData.builder()
                            .name("custNo").value(verificationResponse.getCustNo())
                            .build());
                    transactionData.add(TransactionData.builder()
                            .name("custNm").value(verificationResponse.getCustNm())
                            .build());
                    transactionData.add(TransactionData.builder()
                            .name("branchId").value(verificationResponse.getBranchId())
                            .build());

                    fundResponse.setData(transactionData);
                    BeanUtils.copyProperties(fundResponse, response);
                    log.error("<<<<\nVision Fund Verification Completed");
                    log.error("\n[\nVision Fund Response Object\n{}\n",verificationResponse);
                    log.error("\nVision Fund RETURNED Response Object\n{}\n]\n>>>>",fundResponse);
                }
            }
            break;
            //Ejo Heza
            case "05.1":{
                NationalIDValidationRequest nationalIDValidationRequest = new NationalIDValidationRequest();
                if (genericRequest.getData().size() > 0){
                    nationalIDValidationRequest.setIdentification(genericRequest.getData().get(0).getValue());
                }else {
                    nationalIDValidationRequest.setIdentification(genericRequest.getValue());
                }
                NationalIDValidationResponse nationalIDValidationResponse = ltssService.validateNationalID(nationalIDValidationRequest);
                if (nationalIDValidationResponse.getName().isEmpty()&& nationalIDValidationResponse.getIdentification().isEmpty()){
                    response.setResponseCode("05");
                    response.setResponseMessage("No Response from NID API");
                }
                if (!nationalIDValidationResponse.getName().isBlank() && !nationalIDValidationResponse.getIdentification().isBlank()){
                    response.setResponseCode("00");
                    response.setResponseMessage("Customer data fetched successfully!");
                    List<TransactionData> transactionData = new ArrayList<>();
                    transactionData.add(TransactionData.builder()
                            .name("identification").value(nationalIDValidationResponse.getIdentification())
                            .build());
                    transactionData.add(TransactionData.builder()
                            .name("name").value(nationalIDValidationResponse.getName())
                            .build());

                    response.setData(transactionData);
                }else {
                    response.setResponseCode("05");
                    response.setResponseMessage("Unable to fetch customer data. Try again later!");
                }
            }
            break;

        }
        if(!(socket ==null)) {
            writeResponseToTCPChannel(socket, mapper.writeValueAsString(response));
        }
        return response;
    }

    private void writeResponseToTCPChannel(NetSocket socket, String s) {
        Buffer outBuffer = Buffer.buffer();
        outBuffer.appendString(s);
        socket.write(outBuffer);
    }


    public BillPaymentResponse billPayment(String requestString, NetSocket socket)
            throws JsonProcessingException, UnprocessableEntityException, InvalidAgentCredentialsException {
        //Accadermic bill payment
        AuthenticateAgentResponse authenticateAgentResponse;
        HashMap<String, String> payment = new HashMap<>();
        BillPaymentResponse billPaymentResponse = new BillPaymentResponse();
        CustomObjectMapper mapper = new CustomObjectMapper();

        BillPaymentRequest paymentRequest = mapper.readValue(requestString, BillPaymentRequest.class);


        List<TransactionData> data = paymentRequest.getData();


        //EUCL bill payment
        EUCLPaymentResponse euclPaymentResponse;
        EUCLPaymentRequest euclPaymentRequest = new EUCLPaymentRequest();


        switch (paymentRequest.getSvcCode()) {
            //Academic bridge Payment
            case "01.2":
                int ii = 0;
                for (TransactionData column : data) {
                    if (ii >= 0) {
                        payment.put(column.getName(), column.getValue());

                    }
                    ii++;

                }
                System.out.println("payment request+++++++++++ " + payment);
                MerchantAuthInfo auth = new MerchantAuthInfo();
                auth.setUsername(paymentRequest.getCredentials().getUsername());
                auth.setPassword(paymentRequest.getCredentials().getPassword());

//
                try {
                    authenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(auth);

                    String OFS = academicBridgeT24Service.bootstrapAcademicBridgePaymentOFSMsg(
                            authenticateAgentResponse.getData().getAccountNumber(),
                            payment.get("Credit Account"),
                            Double.parseDouble(payment.get("Amount")),
                            payment.get("Payer's Name"),
                            payment.get("Mobile Number"),
                            payment.get("schoolId"),
                            payment.get("SchoolName"),
                            payment.get("StudentName"),
                            payment.get("Student Registration ID Number")

                    );

                    billPaymentResponse = (academicBridgeT24Service.academicBridgePayment(OFS));
                    billPaymentResponse = getResponse(billPaymentResponse,authenticateAgentResponse,payment.get("creditAccount"));
                } catch (InvalidAgentCredentialsException e) {
                    billPaymentResponse = bootStrapNoAgentAccount();

                }


                break;


            //WASAC bill payment
            case "02.2":

                if (paymentRequest.getBill() == null){
                   paymentRequest.setBill("bill");
                }
                if (paymentRequest.getBillSubCategory() == null){
                    paymentRequest.setBillSubCategory("billSubCategory");
                }
                if (paymentRequest.getData().size() == 0){
                    billPaymentResponse.setResponseCode("05");
                    billPaymentResponse.setResponseMessage("Missing Transaction data. Kindly provide all details and try again!");
                }
                else {
                    billPaymentResponse = wasacService.payWaterBill(paymentRequest);
                }
                break;
            //EUCL requests
            case "03.2":
                case"03.1":
                //Extract data from validation request object to local variables only when some data has been sent
                if (!data.isEmpty()) {
                    String amount = data.get(0).getValue();
                    String phoneNo = data.get(1).getValue();
                    String meterNo = data.size()>2? data.get(2).getValue():"00";
                    String meterLocation= data.size()>3 ?data.get(3).getValue():"No location";

                    euclPaymentRequest.setAmount(amount);
                    euclPaymentRequest.setCredentials(
                            new MerchantAuthInfo(
                                    paymentRequest.getCredentials().getUsername(),
                                    paymentRequest.getCredentials().getPassword()
                            )
                    );
                    euclPaymentRequest.setMeterNo(meterNo);
                    euclPaymentRequest.setPhoneNo(phoneNo);
                    euclPaymentRequest.setMeterLocation(meterLocation);

                    String requestRefNo = RRNGenerator.getInstance("EV").getRRN();

                    euclPaymentResponse = euclService.purchaseEUCLTokens(euclPaymentRequest, requestRefNo);

                    billPaymentResponse.setResponseCode(euclPaymentResponse.getStatus()); //Assuming status is same as response code
                    billPaymentResponse.setResponseMessage(euclPaymentResponse.getMessage());

                    List<TransactionData> ueclTransactionData = new ArrayList<>();

                    //Extract response data from response object and set transaction data
                    PaymentResponseData euclPaymentResponseData = euclPaymentResponse.getData();
                    ueclTransactionData.add(TransactionData.builder().name("t24Reference")
                            .value(euclPaymentResponseData.getT24Reference()).build());
                    ueclTransactionData.add(TransactionData.builder().name("token")
                            .value(euclPaymentResponseData.getToken()).build());
                    ueclTransactionData.add(TransactionData.builder().name("unitsInKW")
                            .value(euclPaymentResponseData.getUnitsInKW()).build());

                    ueclTransactionData.add(TransactionData.builder().name("charges")
                            .value(euclPaymentResponseData.getCharges()).build());

                    //Set transaction data
                    billPaymentResponse.setData(ueclTransactionData);
                }
                break;
                /*
                * Vision Fund
                * Account deposit
                * Cash Withdraw
                * */
            case "04.1":
            case "04.2":
                    {
                        VisionFundResponse fundResponse = visionFundTransaction(requestString);
                        BeanUtils.copyProperties(fundResponse,billPaymentResponse);
                    }

                break;
            //Ejo Heza contributions
            case "05.1":
            case "05.2":
            {
                LTSSRequest ltssRequest = new LTSSRequest();
                ltssRequest.setCredentials(paymentRequest.getCredentials());

                LTSSPaymentResponse paymentContributionResponse;
                if(data.isEmpty()){
                    billPaymentResponse.setResponseCode("05");
                    billPaymentResponse.setResponseMessage("Transaction data missing");
                }
                else
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    ltssRequest.setPaymentDate(formatter.format(new Date()));
                    ltssRequest.setAmount(paymentRequest.getData().get(1).getValue());
                    ltssRequest.setDescription("Ejo Heza contribution payment");
                    ltssRequest.setIntermediary("Tracom Services Limited");
                    //Generate RRn
                    ltssRequest.setExtRefNo(RRNGenerator.getInstance("EH").getRRN());
                    ltssRequest.setBeneficiary(ltssRequest.getBeneficiary());
                    NationalIDValidationResponse response=new NationalIDValidationResponse();
                    NationalIDValidationRequest nationalIDValidationRequest = new NationalIDValidationRequest();
                    nationalIDValidationRequest.setIdentification(paymentRequest.getData().get(0).getValue());
                    nationalIDValidationRequest.setName(paymentRequest.getData().get(1).getValue());
                    System.out.println("nationalIDValidationRequest = " + nationalIDValidationRequest);



                    ltssRequest.setIdentification(nationalIDValidationRequest.getIdentification());

                     paymentContributionResponse = ltssService.makeContributionPayment(ltssRequest);

                    if (paymentContributionResponse.getData().getRefNo().isEmpty()) {
                        billPaymentResponse.setResponseCode("05");
                        billPaymentResponse.setResponseMessage("Transaction failed. Try again later!");
                    } else {
                        billPaymentResponse.setResponseCode("00");
                        billPaymentResponse.setResponseMessage("Transaction completed successfully!");

                        List<TransactionData> transactionData = new ArrayList<>();
                        transactionData.add(
                                TransactionData.builder()
                                        .name("identification")
                                        .value(paymentRequest.getData().get(0).getValue())
                                        .build());
                        transactionData.add(
                                TransactionData.builder()
                                        .name("name")
                                        .value(response.getName())
                                        .build());
                        transactionData.add(
                                TransactionData.builder()
                                        .name("amount")
                                        .value(paymentContributionResponse.getData().getAmount())
                                        .build());
                        transactionData.add(
                                TransactionData.builder()
                                        .name("intermediary")
                                        .value(paymentContributionResponse.getData().getIntermediary())
                                        .build());
                        transactionData.add(
                                TransactionData.builder()
                                        .name("extReferenceNo")
                                        .value(paymentContributionResponse.getData().getExtReferenceNo())
                                        .build());
                        transactionData.add(
                                TransactionData.builder()
                                        .name("refNo")
                                        .value(paymentContributionResponse.getData().getRefNo())
                                        .build());
                        transactionData.add(
                                TransactionData.builder()
                                        .name("paymentDate")
                                        .value(paymentContributionResponse.getData().getPaymentDate())
                                        .build());
                        transactionData.add(
                                TransactionData.builder()
                                        .name("description")
                                        .value(paymentContributionResponse.getData().getDescription())
                                        .build());

                        billPaymentResponse.setData(transactionData);
                    }
                }
            }
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + paymentRequest.getSvcCode());

        }

        Buffer outBuffer = Buffer.buffer();
        if(!(socket ==null)){
            outBuffer.appendString(mapper.writeValueAsString(billPaymentResponse));
            socket.write(outBuffer);
        }
        return billPaymentResponse;
    }

    private BillPaymentResponse getBillPaymentResponse() {
        List<TransactionData> data = new ArrayList<>();
        data.add(
                TransactionData.builder()
                        .name("T24 Reference")
                        .value(RRNGenerator.getInstance("BP").getRRN())
                        .build());
        data.add(
                TransactionData.builder()
                        .name("Reference No")
                        .value(RRNGenerator.getInstance("BP").getRRN())
                        .build());
        data.add(TransactionData.builder().name("Amount").value("15,004").build());
        data.add(TransactionData.builder().name("PaidTo").value("Tracom Academy").build());
        data.add(TransactionData.builder().name("customerName").value("Paul Wainaina").build());
        data.add(TransactionData.builder().name("StudentNo").value("COM/0510/10").build());

        data.add(TransactionData.builder().name("Type Of Payment").value("Tuition").build());

        return BillPaymentResponse.builder()
                .responseCode("00")
                .responseMessage("Transaction successful")
                .data(data)
                .build();
    }

    public void billStatus(NetSocket socket)
            throws JsonProcessingException, UnprocessableEntityException {

        BillPaymentResponse billPaymentResponse = getBillPaymentResponse();

        Buffer outBuffer = Buffer.buffer();
        CustomObjectMapper mapper = new CustomObjectMapper();
        outBuffer.appendString(mapper.writeValueAsString(billPaymentResponse));
        socket.write(outBuffer);
    }


    private BillPaymentResponse bootStrapNoAgentAccount() {

        return BillPaymentResponse.builder()
                .responseCode("098")
                .responseMessage("No Agent account found")
                .data(null)

                .build();
}
    public VisionFundResponse visionFundTransaction(String requestString)
            throws JsonProcessingException, UnprocessableEntityException {
        CustomObjectMapper mapper = new CustomObjectMapper();

        VisionFundRequest fundRequest = mapper.readValue(requestString, VisionFundRequest.class);

        VisionFundResponse fundResponse = new VisionFundResponse();
        List<TransactionData> data = fundRequest.getData();

        switch (fundRequest.getSvcCode()) {
            /*
             * Account deposit
             * */
            case "04.1": {
                AccountDepositRequest depositRequest = new AccountDepositRequest();
                depositRequest.setAmount(Long.valueOf(data.get(3).getValue()));
                depositRequest.setAccountNumber(data.get(1).getValue());
                depositRequest.setMobileNumber(data.get(0).getValue());
                depositRequest.setNationalID(data.get(2).getValue());
                depositRequest.setTranDesc("Account Deposit");

                depositRequest.setCredentials(fundRequest.getCredentials());
                depositRequest.setTnxType(fundRequest.getTnxType());

                AccountDepositResponse depositResponse = visionFundService.makeDeposit(depositRequest);
                fundResponse.setResponseMessage(depositResponse.getResponseString());
                fundResponse.setResponseCode(depositResponse.getResponseCode());

                List<TransactionData> responseData = new ArrayList<>();
                responseData.add(TransactionData.builder()
                        .name("availBal").value(String.valueOf(depositResponse.getAvailBalance()))
                        .build());
                responseData.add(TransactionData.builder()
                        .name("txnReference").value(depositResponse.getTxnReference())
                        .build());
                responseData.add(TransactionData.builder()
                        .name("txnDateTime").value(String.valueOf(depositResponse.getTxnDateTime()))
                        .build());
                fundResponse.setData(responseData);

                log.error("[\nVISION FUND ACCOUNT DEPOSIT RESPONSE OBJECT: \n{}\n]\n>>>>", fundResponse);
            }
            break;

            case "04.2": {
                CashWithdrawalRequest withdrawalRequest = new CashWithdrawalRequest();
                withdrawalRequest.setToken(data.get(0).getValue());
                withdrawalRequest.setAmount(Long.parseLong(data.get(2).getValue()));
                withdrawalRequest.setNationalID(data.get(1).getValue());
                withdrawalRequest.setMobileNumber(data.get(3).getValue());
                withdrawalRequest.setAccountNumber(data.get(4).getValue());
                withdrawalRequest.setAccountName(data.get(5).getValue());
                withdrawalRequest.setTranDesc("Cash Withdrawal");

                withdrawalRequest.setCredentials(fundRequest.getCredentials());
                withdrawalRequest.setTnxType(fundRequest.getTnxType());

                log.error("<<<<\n[\nVISION FUND CASH WITHDRAWAL REQUEST OBJECT: \n{}\n]", withdrawalRequest);

                CashWithdrawalResponse withdrawalResponse = visionFundService.doWithdraw(withdrawalRequest);
                fundResponse.setResponseMessage(withdrawalResponse.getResponseString());
                fundResponse.setResponseCode(withdrawalResponse.getResponseCode());

                List<TransactionData> responseData = new ArrayList<>();
                responseData.add(TransactionData.builder()
                        .name("availBal").value(String.valueOf(withdrawalResponse.getAvailBalance()))
                        .build());
                responseData.add(TransactionData.builder()
                        .name("txnReference").value(withdrawalResponse.getTxnReference())
                        .build());
                responseData.add(TransactionData.builder()
                        .name("txnDateTime").value(String.valueOf(withdrawalResponse.getTxnDateTime()))
                        .build());
                fundResponse.setData(responseData);

                log.error("[\nVISION FUND CASH WITHDRAWAL RESPONSE OBJECT: \n{}\n]\n>>>>", fundResponse);
            }
            break;
        }

        return fundResponse;
    }


    private BillPaymentResponse getResponse(BillPaymentResponse billPaymentResponse,
                                            AuthenticateAgentResponse authenticateAgentResponse,
                                            String account){
        BillPaymentResponse response;
        T24TXNQueue tot24 = new T24TXNQueue();
        String processingStatus;
        String amount;


        List<TransactionData> data = new ArrayList<>();
        if(billPaymentResponse.getResponseMessage().equalsIgnoreCase("No Agent account found")){
            data.add(
                    TransactionData.builder()
                            .name("Client Post Name")
                            .value("POSTest").build());
            data.add(
                    TransactionData.builder().name("No data").value(billPaymentResponse.getResponseMessage()).build());
            getAcademicBridgePayment(data, AppConstants.ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR.value(), AppConstants.ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR.getReasonPhrase());
        }

        List<AcademicTransactionData> list = new ArrayList<>(billPaymentResponse.getPaymentData());

        if(!list.isEmpty()) {
            tot24.setT24responsecode(list.get(0).getT24responsecode());
            System.out.println("t24 response code to be saved "+tot24.getT24responsecode());
            if (list.get(0).getT24responsecode().equalsIgnoreCase("1")) {
                System.out.println("Response code from t24 after setting  " + list.get(0).getT24reference());
                tot24.setT24reference(list.get(0).getT24reference());



                if (!list.isEmpty()) {
                    System.out.println("Response data setting");
                  data.add(
                            TransactionData.builder().name("ABStudentName").value(list.get(0).getAbStudentName()).build());
                    data.add(
                            TransactionData.builder().name("BPRSenderName").value(list.get(0).getBprSenderName()).build());
                    data.add(
                            TransactionData.builder().name("ChargeAmount").value(list.get(0).getLocalCahrgeAmount()).build());
                    data.add(
                            TransactionData.builder().name("ABSchoolName").value(list.get(0).getAbSchoolName()).build());
                      data.add(
                            TransactionData.builder().name("BillNo").value(list.get(0).getAbBillNo()).build());
                      tot24.setDebitacctno(authenticateAgentResponse.getData().getAccountNumber());

                    tot24.setCreditacctno(account);
                    amount = list.get(0).getCreditAmount();

                    response = getAcademicBridgePayment(data, AppConstants.TRANSACTION_SUCCESS_STANDARD.value(), AppConstants.TRANSACTION_SUCCESS_STANDARD.getReasonPhrase());

                    processingStatus = "000";
                } else {
                    data.add(
                            TransactionData.builder()
                                    .name(CLIENT_POST_NAME)
                                    .value("POSTest").build());
                    data.add(
                            TransactionData.builder().name(NO_DATA).value("Something went wrong during the transaction").build());
                    tot24.setDebitacctno(authenticateAgentResponse.getData().getAccountNumber());

                    tot24.setCreditacctno(null);
                    amount = "0";

                    response = getAcademicBridgePayment(data, AppConstants.ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR.value(), AppConstants.ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR.getReasonPhrase());


                    processingStatus = "10";
                }
            } else {
                System.out.println("Transaction not successful >> "+list.get(0).getT24responsecode());
                data.add(
                        TransactionData.builder()
                                .name(CLIENT_POST_NAME)
                                .value("POSTest").build());
                data.add(
                        TransactionData.builder().name(NO_DATA).value(list.get(0).getError()).build());

                amount = "0";
                processingStatus = "10";
                response = getAcademicBridgePayment(data, AppConstants.ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR.value(), AppConstants.ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR.getReasonPhrase());

            }
        }else{

            tot24.setT24responsecode("-1");
            data.add(
                    TransactionData.builder()
                            .name(CLIENT_POST_NAME)
                            .value("POSTest").build());
            data.add(
                    TransactionData.builder().name(NO_DATA).value(list.get(0).getError()).build());

            amount = "0";
            processingStatus = "10";
            response = getAcademicBridgePayment(data, AppConstants.ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR.value(), AppConstants.ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR.getReasonPhrase());

        }



        try {
            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "ACADEMIC BRIDGE", "1200",
                    Double.parseDouble(amount), processingStatus,
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());//Replace with actua data after successfull login credentials fro POS


        }catch (Exception e){
            e.printStackTrace();
        }

        log.error("[\nACADEMIC BRIDGE PAYMENT RESPONSE : \n{}\n]\n>>>>", response);

        return response;
    }

    private BillPaymentResponse getAcademicBridgePayment(List<TransactionData> validationData, String value, String reasonPhrase) {
        return BillPaymentResponse.builder()
                .responseCode(value)
                .responseMessage(reasonPhrase)
                .data(validationData)
                .build();
    }
}
