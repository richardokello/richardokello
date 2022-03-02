package co.ke.tracom.bprgateway.servers.tcpserver;

import co.ke.tracom.bprgateway.core.config.CustomObjectMapper;
import co.ke.tracom.bprgateway.core.util.AppConstants;
import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.*;

import co.ke.tracom.bprgateway.servers.tcpserver.data.academicBridge.AcademicBridgeValidation;
import co.ke.tracom.bprgateway.servers.tcpserver.data.billMenu.BillMenuRequest;
import co.ke.tracom.bprgateway.web.academicbridge.data.studentdetails.GetStudentDetailsResponse;


import co.ke.tracom.bprgateway.servers.tcpserver.data.academicBridge.AcademicBridgeValidation;
import co.ke.tracom.bprgateway.servers.tcpserver.data.billMenu.BillMenuRequest;
import co.ke.tracom.bprgateway.web.academicbridge.data.studentdetails.GetStudentDetailsResponse;

import co.ke.tracom.bprgateway.servers.tcpserver.data.academicBridge.AcademicBridgeValidation;
import co.ke.tracom.bprgateway.servers.tcpserver.data.billMenu.BillMenuRequest;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.BillPaymentRequest;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.BillPaymentResponse;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.TransactionData;
import co.ke.tracom.bprgateway.web.VisionFund.data.*;
import co.ke.tracom.bprgateway.web.VisionFund.data.custom.CustomVerificationRequest;
import co.ke.tracom.bprgateway.web.VisionFund.data.custom.CustomVerificationResponse;
import co.ke.tracom.bprgateway.web.VisionFund.service.VisionFundService;
import co.ke.tracom.bprgateway.web.academicbridge.services.AcademicBridgeService;
import co.ke.tracom.bprgateway.web.academicbridge.services.AcademicBridgeT24;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.billMenus.data.BillMenuResponse;
import co.ke.tracom.bprgateway.web.billMenus.service.BillMenusService;
import co.ke.tracom.bprgateway.core.config.CustomObjectMapper;

import co.ke.tracom.bprgateway.web.eucl.dto.request.EUCLPaymentRequest;
import co.ke.tracom.bprgateway.web.eucl.dto.request.MeterNoValidation;
import co.ke.tracom.bprgateway.web.eucl.dto.response.EUCLPaymentResponse;
import co.ke.tracom.bprgateway.web.eucl.dto.response.MeterNoData;
import co.ke.tracom.bprgateway.web.eucl.dto.response.MeterNoValidationResponse;
import co.ke.tracom.bprgateway.web.eucl.dto.response.PaymentResponseData;
import co.ke.tracom.bprgateway.web.eucl.service.EUCLService;

import co.ke.tracom.bprgateway.web.exceptions.custom.InvalidAgentCredentialsException;
import co.ke.tracom.bprgateway.web.exceptions.custom.UnprocessableEntityException;
import co.ke.tracom.bprgateway.web.ltss.data.nationalIDValidation.NationalIDValidationRequest;
import co.ke.tracom.bprgateway.web.ltss.data.nationalIDValidation.NationalIDValidationResponse;
import co.ke.tracom.bprgateway.web.ltss.data.paymentContribution.PaymentContributionRequest;
import co.ke.tracom.bprgateway.web.ltss.data.paymentContribution.PaymentContributionResponse;
import co.ke.tracom.bprgateway.web.ltss.service.LtssService;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.CustomerProfileResponse;
import co.ke.tracom.bprgateway.web.wasac.service.WASACService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class BillRequestHandler {
    private final WASACService wasacService;
    private final EUCLService euclService;
    private final AcademicBridgeT24 academicBridgeT24Service;
    private final TransactionService transactionService;
    private final BaseServiceProcessor baseServiceProcessor;

    private final VisionFundService visionFundService;
    private String billNumber;

    private final LtssService ltssService;
    public void menu(String requestString, BillMenusService billMenusService, NetSocket socket)
            throws JsonProcessingException, UnprocessableEntityException {
        CustomObjectMapper mapper = new CustomObjectMapper();
        /* Parse request string into bill menu request object */
        BillMenuRequest billMenuRequest = mapper.readValue(requestString, BillMenuRequest.class);
        log.info("BILL MENU REQUEST DATA: {}", requestString);
        String tnxType = billMenuRequest.getTnxType();

        if (tnxType != null && tnxType.equals("fetch-menu")) {
            BillMenuResponse billMenuResponse = null;
            if (billMenuRequest.getLang().equalsIgnoreCase("en")) {
                billMenuResponse = billMenusService.fetchEnglishMenus();
                log.info("BILL MENU REQUEST DATA: {}", billMenuResponse.toString());
                writeResponseToTCPChannel(socket, mapper.writeValueAsString(billMenuResponse));
            } else if (billMenuRequest.getLang().equalsIgnoreCase("rw")) {
                billMenuResponse = billMenusService.fetchKinyarwandaMenus();
                log.info("BILL MENU REQUEST DATA: {}", billMenuResponse.toString());
                writeResponseToTCPChannel(socket, mapper.writeValueAsString(billMenuResponse));
            }

        } else {
            log.error("TCP SERVER - BAD REQUEST DATA FOR FETCHING MENU: {} ", requestString);
            throw new UnprocessableEntityException("Entity cannot be processed");
        }
    }

    public void academicBridge(
            String requestString, AcademicBridgeService academicBridgeService, NetSocket socket)
            throws JsonProcessingException {

        List<TransactionData> data = new ArrayList<>();
        data.add(TransactionData.builder().name("school name").value("Tracom Academy").build());
        data.add(TransactionData.builder().name("school Account No").value("115627393").build());
        data.add(TransactionData.builder().name("school account name").value("Tracom Academy").build());
        data.add(TransactionData.builder().name("Student Reg").value("COM/0510/10").build());
        data.add(TransactionData.builder().name("Type Of Payment").value("Tuition").build());

        AcademicBridgeValidation response = getAcademicBridgeValidation(data, "00", "Transaction processed successfully");

        Buffer outBuffer = Buffer.buffer();
        CustomObjectMapper mapper = new CustomObjectMapper();
        outBuffer.appendString(mapper.writeValueAsString(response));
        socket.write(outBuffer);
    }

    public void validation(String requestString, NetSocket socket)
            throws JsonProcessingException, UnprocessableEntityException {
        CustomObjectMapper mapper = new CustomObjectMapper();
        AcademicBridgeValidation posResponse = null;

        ValidationRequest genericRequest = mapper.readValue(requestString, ValidationRequest.class);
        log.info("Validation REQUEST OBJECT: {}", genericRequest);

        List<TransactionData> data = genericRequest.getData();
        //List<TransactionData> data = genericRequest.getCredentials().getData();
        //System.out.println(""+data.get(0).getValue());

        CustomerProfileResponse customerProfileResponse = null;
        MeterNoValidation meterNoValidation=new MeterNoValidation();
        MeterNoValidationResponse meterNoValidationResponse=new MeterNoValidationResponse();
        List<TransactionData> validationData = new ArrayList<>();
        AcademicBridgeValidation response = AcademicBridgeValidation.builder().build();

        MeterNoValidationResponse euclValidationResponse = MeterNoValidationResponse.builder().build();
        MeterNoValidation euclValidation = new MeterNoValidation();


        String billNumber = null;

        switch (genericRequest.getSvcCode() /*genericRequest.getCredentials().getSvcCode()*/) {

            case "01.1":
            case "01.2":

                if (genericRequest.getField().equalsIgnoreCase("billNumber")) {

                    customerProfileResponse = (academicBridgeT24Service.validateStudentId(genericRequest.getValue()));
                    billNumber = genericRequest.getValue();
                    response.setResponseCode("00");
                    response.setResponseMessage("Transaction successful");
                    List<TransactionData> transactionData = new ArrayList<>();
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
                            .name("StudentName")
                            .value(customerProfileResponseData.getStudent_name())
                            .build()
                    );
                    transactionData.add(TransactionData.builder()
                            .name("SchoolId")
                            .value(String.valueOf(customerProfileResponseData.getSchool_ide()))
                            .build()
                    );

                    response.setData(transactionData);


                } else {
                    log.info("Wrong svc code and field combination :" + genericRequest.getField());
                    customerProfileResponse.setData(null);
                    customerProfileResponse.setMessage("Wrong svc code and field name combination");
                    customerProfileResponse.setStatus("09");
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
                    String  meterNo = data.get(2).getValue();
                    String phoneNo = data.get(1).getValue();
                    String amount = data.get(0).getValue();

                    euclValidation.setAmount(amount);
                    euclValidation.setCredentials(
                            new MerchantAuthInfo(
                                    genericRequest.getCredentials().getUsername(),
                                    genericRequest.getCredentials().getPassword()
                            )
                    );
                    euclValidation.setMeterNo(meterNo);
                    euclValidation.setPhoneNo(phoneNo);
                    String requestRefNo = RRNGenerator.getInstance("EV").getRRN();
                    euclValidationResponse = euclService.validateEUCLMeterNo(euclValidation, requestRefNo);


                    //Extract data from service response object to the generic response object
                    response.setResponseCode(euclValidationResponse.getStatus());
                    response.setResponseMessage(euclValidationResponse.getMessage());

                    List<TransactionData> euclTransactionData = new ArrayList<>();
                    MeterNoData euclValidationResponseData = euclValidationResponse.getData();
                    euclTransactionData.add(TransactionData.builder().name("accountName").value(euclValidationResponseData.getAccountName())
                            .build());
                    euclTransactionData.add(TransactionData.builder().name("meterNo").value(euclValidationResponseData.getMeterNo()).build());
                    euclTransactionData.add(TransactionData.builder().name("meterLocation").value(euclValidationResponseData.getMeterLocation())
                            .build());
                    euclTransactionData.add(TransactionData.builder().name("rrn").value(euclValidationResponseData.getRrn()).build());

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



        writeResponseToTCPChannel(socket, mapper.writeValueAsString(response));

    }

    private void writeResponseToTCPChannel(NetSocket socket, String s) {
        Buffer outBuffer = Buffer.buffer();
        outBuffer.appendString(s);
        socket.write(outBuffer);
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


    public void billPayment(String requestString, NetSocket socket)
            throws JsonProcessingException, UnprocessableEntityException {
        //Accadermic bill payment
        AuthenticateAgentResponse authenticateAgentResponse = null;
        HashMap<String, String> payment = new HashMap<String, String>();
        BillPaymentResponse billPaymentResponse = new BillPaymentResponse();
        CustomObjectMapper mapper = new CustomObjectMapper();

        BillPaymentRequest paymentRequest = mapper.readValue(requestString, BillPaymentRequest.class);
        log.info("BILL PAYMENT REQUEST OBJECT: {}", paymentRequest);

        List<TransactionData> data = paymentRequest.getData();

        //EUCL bill payment
        EUCLPaymentResponse euclPaymentResponse = EUCLPaymentResponse.builder().build();
        EUCLPaymentRequest euclPaymentRequest = new EUCLPaymentRequest();


        switch (paymentRequest.getSvcCode()) {
            case "01.2":
               // billPaymentResponse = getBillPaymentResponse();
               // List<TransactionData> data = paymentRequest.getData();
                int ii = 0;
                for (TransactionData column : data) {
                    if (ii >= 0) {
                        payment.put(column.getName(), column.getValue());

                    }
                    ii++;

                }
                MerchantAuthInfo auth = new MerchantAuthInfo();
                /*auth.setUsername(genericRequest.getCredentials().getUsername());
                auth.setPassword(genericRequest.getCredentials().getPassword());*/

                auth.setUsername(paymentRequest.getCredentials().getUsername());
                auth.setPassword(paymentRequest.getCredentials().getPassword());

//                AuthenticateAgentResponse authenticateAgentResponse = null;
                try {
                    authenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(auth);
                    System.out.println("Agent account : " + authenticateAgentResponse.getData().getAccountNumber());
                    String OFS = academicBridgeT24Service.bootstrapAcademicBridgePaymentOFSMsg(
                            // payment.get("debitAccount"),
                            authenticateAgentResponse.getData().getAccountNumber(),
                            payment.get("creditAccount"),
                            Double.parseDouble(payment.get("amount")),
                            payment.get("senderName"),
                            payment.get("mobileNumber"),
                            payment.get("schoolId"),
                            payment.get("schoolName"),
                            payment.get("studentName"),
                            payment.get("billNumber")

                    );

                    billPaymentResponse = (academicBridgeT24Service.academicBridgePayment(OFS));
                    billPaymentResponse = getResponse(billPaymentResponse,authenticateAgentResponse,payment.get("creditAccount"));
                } catch (InvalidAgentCredentialsException e) {
                    billPaymentResponse = bootStrapNoAgentAccount();
                    e.printStackTrace();
                }


                break;
            /*case "01.2":
                billPaymentResponse = getBillPaymentResponse();
                break;*/
            case "01.3":

            if(!data.isEmpty()){
                String amount= data.get(0).getValue();
                String phoneNumber=data.get(1).getValue();
                String meterNo = data.size()>2? data.get(2).getValue():"00";
                String meterLocation= data.size()>3 ?data.get(3).getValue():"No location";


                euclPaymentRequest.setAmount(amount);
                euclPaymentRequest.setCredentials(new MerchantAuthInfo(paymentRequest.getCredentials().getUsername(),
                        paymentRequest.getCredentials().getPassword()));

            }
            break;
            //WASAC bill payment
            case "02.2":
                //billPaymentResponse = getBillPaymentResponse();
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
                    ueclTransactionData.add(TransactionData.builder().name("rrn")
                            .value(euclPaymentResponseData.getRrn()).build());
                    ueclTransactionData.add(TransactionData.builder().name("tid")
                            .value(euclPaymentResponseData.getTid()).build());
                    ueclTransactionData.add(TransactionData.builder().name("mid")
                            .value(euclPaymentResponseData.getMid()).build());
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
                        VisionFundResponse fundResponse = visionFundTransaction(requestString, socket);
                        BeanUtils.copyProperties(fundResponse,billPaymentResponse);
                    }

                break;
            //Ejo Heza contributions
            case "05.1":
            {
                PaymentContributionRequest paymentContributionRequest = new PaymentContributionRequest();
                if (paymentRequest.getData().size()==0){
                    billPaymentResponse.setResponseCode("05");
                    billPaymentResponse.setResponseMessage("Transaction data missing");
                }
                else
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    paymentContributionRequest.setPaymentDate(formatter.format(new Date()));
                    paymentContributionRequest.setAmount(paymentRequest.getData().get(1).getValue());
                    paymentContributionRequest.setDescription("Ejo Heza contribution payment");
                    paymentContributionRequest.setIntermediary("Tracom Services Limited");
                    //Generate RRn
                    paymentContributionRequest.setExtReferenceNo(RRNGenerator.getInstance("EH").getRRN());

                    NationalIDValidationRequest nationalIDValidationRequest = new NationalIDValidationRequest();
                    nationalIDValidationRequest.setIdentification(paymentRequest.getData().get(0).getValue());
                    paymentContributionRequest.setBeneficiary(nationalIDValidationRequest);

                    PaymentContributionResponse paymentContributionResponse = ltssService.sendPaymentContribution(paymentContributionRequest);

                    if (paymentContributionResponse.getRefNo().isBlank()){
                        billPaymentResponse.setResponseCode("05");
                        billPaymentResponse.setResponseMessage("Transaction failed. Try again later!");
                    }
                    else {
                        billPaymentResponse.setResponseCode("00");
                        billPaymentResponse.setResponseMessage("Transaction completed successfully!");

                        List<TransactionData> transactionData = new ArrayList<>();
                        transactionData.add(
                                TransactionData.builder()
                                        .name("identification")
                                        .value(paymentContributionResponse.getBeneficiary().getIdentification())
                                        .build());
                        transactionData.add(
                                TransactionData.builder()
                                        .name("name")
                                        .value(paymentContributionResponse.getBeneficiary().getName())
                                        .build());
                        transactionData.add(
                                TransactionData.builder()
                                        .name("amount")
                                        .value(paymentContributionResponse.getAmount())
                                        .build());
                        transactionData.add(
                                TransactionData.builder()
                                        .name("intermediary")
                                        .value(paymentContributionResponse.getIntermediary())
                                        .build());
                        transactionData.add(
                                TransactionData.builder()
                                        .name("extReferenceNo")
                                        .value(paymentContributionResponse.getExtReferenceNo())
                                        .build());
                        transactionData.add(
                                TransactionData.builder()
                                        .name("refNo")
                                        .value(paymentContributionResponse.getRefNo())
                                        .build());
                        transactionData.add(
                                TransactionData.builder()
                                        .name("paymentDate")
                                        .value(paymentContributionResponse.getPaymentDate().toString())
                                        .build());
                        transactionData.add(
                                TransactionData.builder()
                                        .name("description")
                                        .value(paymentContributionResponse.getDescription())
                                        .build());

                        billPaymentResponse.setData(transactionData);
                    }
                }
            }
            break;
        }

        //kelvin

        //kelvin


        Buffer outBuffer = Buffer.buffer();
        outBuffer.appendString(mapper.writeValueAsString(billPaymentResponse));
        socket.write(outBuffer);
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
        data.add(TransactionData.builder().name("Paid To").value("Tracom Academy").build());
        data.add(TransactionData.builder().name("customer Name").value("Paul Wainaina").build());
        data.add(TransactionData.builder().name("Student No").value("COM/0510/10").build());

        data.add(TransactionData.builder().name("Type Of Payment").value("Tuition").build());

        BillPaymentResponse billPaymentResponse =
                BillPaymentResponse.builder()
                        .responseCode("00")
                        .responseMessage("Transaction successful")
                        .data(data)
                        .build();

        return billPaymentResponse;
    }

    public void billStatus(String requestString, NetSocket socket)
            throws JsonProcessingException, UnprocessableEntityException {

        BillPaymentResponse billPaymentResponse = getBillPaymentResponse();

        Buffer outBuffer = Buffer.buffer();
        CustomObjectMapper mapper = new CustomObjectMapper();
        outBuffer.appendString(mapper.writeValueAsString(billPaymentResponse));
        socket.write(outBuffer);
    }


    private BillPaymentResponse bootStrapNoAgentAccount() {
        //List<AcademicTransactionData> paymentData = new ArrayList<>();
        BillPaymentResponse billPaymentResponse =
                BillPaymentResponse.builder()
                        .responseCode("09")
                        .responseMessage("No Agent account found")
                        .data(null)
                        //.paymentData(paymentData)
                        .build();
        return billPaymentResponse;
}
    public VisionFundResponse visionFundTransaction(String requestString, NetSocket socket)
            throws JsonProcessingException, UnprocessableEntityException {
        CustomObjectMapper mapper = new CustomObjectMapper();

        VisionFundRequest fundRequest = mapper.readValue(requestString, VisionFundRequest.class);
        log.info("VISION FUND REQUEST OBJECT: {}", fundRequest);

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

                log.error("<<<<\n[\nVISION FUND ACCOUNT DEPOSIT REQUEST OBJECT: \n{}\n]", depositRequest);

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
            /*
             * Cash Withdrawal
             * */
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

        /*Buffer outBuffer = Buffer.buffer();
        outBuffer.appendString(mapper.writeValueAsString(fundResponse));
        socket.write(outBuffer);*/
        return fundResponse;
    }


    private BillPaymentResponse getResponse(BillPaymentResponse billPaymentResponse,
                                            AuthenticateAgentResponse authenticateAgentResponse,
                                            String account){
        BillPaymentResponse response= null;
        T24TXNQueue tot24 = new T24TXNQueue();
        String processingStatus = null;
        String amount = "0";


        List<TransactionData> data = new ArrayList<>();
        if(billPaymentResponse.getResponseMessage().equalsIgnoreCase("No Agent account found")){
            data.add(
                    TransactionData.builder()
                            .name("Client Post Name")
                            .value("POSTest").build());
            data.add(
                    TransactionData.builder().name("No data").value(billPaymentResponse.getResponseMessage()).build());
            response = getAcademicBridgePayment(data, AppConstants.ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR.value(), AppConstants.ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR.getReasonPhrase());
        }

        List<AcademicTransactionData> list = billPaymentResponse.getPaymentData().stream().collect(Collectors.toList());

        if(!list.isEmpty()) {
            tot24.setT24responsecode(list.get(0).getT24responsecode());
            System.out.println("t24 response code to be saved "+tot24.getT24responsecode());
            if (list.get(0).getT24responsecode().equalsIgnoreCase("1")) {
                System.out.println("Response code from t24 after setting  " + list.get(0).getT24reference());
                tot24.setT24reference(list.get(0).getT24reference());



                if (!list.isEmpty()) {
                    System.out.println("Response data setting");
                    data.add(
                            TransactionData.builder()
                                    .name("Client Post Name")
                                    .value("POSTest").build());
                    data.add(
                            TransactionData.builder().name("Debit Customer").value(list.get(0).getDebitCustomer()).build());
                    data.add(
                            TransactionData.builder().name("Ordering Bank").value(String.valueOf(list.get(0).getOrderingBank())).build());
                    data.add(
                            TransactionData.builder().name("AB Student name").value(list.get(0).getAbStudentName()).build());
                    data.add(
                            TransactionData.builder().name("BPR Sender Name").value(list.get(0).getBprSenderName()).build());
                    data.add(
                            TransactionData.builder().name("Charge Amount").value(list.get(0).getLocalCahrgeAmount()).build());
                    data.add(
                            TransactionData.builder().name("AB School Id").value(list.get(0).getAbSchoolId()).build());
                    data.add(
                            TransactionData.builder().name("Credit Amount").value(list.get(0).getCreditAmount()).build());
                    data.add(
                            TransactionData.builder().name("Biller Id").value(list.get(0).getBillerId()).build());
                    data.add(
                            TransactionData.builder().name("AB School Name").value(list.get(0).getAbSchoolName()).build());
                    data.add(
                            TransactionData.builder().name("Mobile No").value(list.get(0).getMobileNo()).build());
                    data.add(
                            TransactionData.builder().name("DateTime").value(list.get(0).getDateTime()).build());
                    data.add(
                            TransactionData.builder().name("Debit Account").value(list.get(0).getDebitAcctNo()).build());
                   /* data.add(
                            TransactionData.builder().name("Credit Their Ref").value(list.get(0).getCreditTheirRef()).build());*/
                    data.add(
                            TransactionData.builder().name("Bill No").value(list.get(0).getAbBillNo()).build());
                    /*data.add(
                            TransactionData.builder().name("Delivery Out Ref").value(list.get(0).getDeliveryOutRef()).build());*/
                    tot24.setDebitacctno(authenticateAgentResponse.getData().getAccountNumber());
                    // tot24.setT24reference("Ref123");
                    tot24.setCreditacctno(account);
                    amount = list.get(0).getCreditAmount();

                    response = getAcademicBridgePayment(data, AppConstants.TRANSACTION_SUCCESS_STANDARD.value(), AppConstants.TRANSACTION_SUCCESS_STANDARD.getReasonPhrase());

                    processingStatus = "000";
                } else {
                    data.add(
                            TransactionData.builder()
                                    .name("Client Post Name")
                                    .value("POSTest").build());
                    data.add(
                            TransactionData.builder().name("No data").value("Something went wrong during the transaction").build());
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
                                .name("Client Post Name")
                                .value("POSTest").build());
                data.add(
                        TransactionData.builder().name("No data").value(list.get(0).getError()).build());

                amount = "0";
                processingStatus = "10";
                response = getAcademicBridgePayment(data, AppConstants.ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR.value(), AppConstants.ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR.getReasonPhrase());

            }
        }else{

            tot24.setT24responsecode("-1");
            data.add(
                    TransactionData.builder()
                            .name("Client Post Name")
                            .value("POSTest").build());
            data.add(
                    TransactionData.builder().name("No data").value(list.get(0).getError()).build());

            amount = "0";
            processingStatus = "10";
            response = getAcademicBridgePayment(data, AppConstants.ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR.value(), AppConstants.ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR.getReasonPhrase());

        }

        /*Buffer outBuffer = Buffer.buffer();
        CustomObjectMapper mappe = new CustomObjectMapper();
        outBuffer.appendString(mappe.writeValueAsString(billPaymentResponse));*/


        try {
            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "ACADEMIC BRIDGE", "1200",
                    Double.valueOf(amount), processingStatus,
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());//Replace with actua data after successfull login credentials fro POS


        }catch (Exception e){
            e.printStackTrace();
        }

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
