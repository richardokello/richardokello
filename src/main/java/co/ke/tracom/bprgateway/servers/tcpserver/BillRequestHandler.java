package co.ke.tracom.bprgateway.servers.tcpserver;

import co.ke.tracom.bprgateway.core.config.CustomObjectMapper;
import co.ke.tracom.bprgateway.core.util.RRNGenerator;
<<<<<<< HEAD
import co.ke.tracom.bprgateway.servers.tcpserver.dto.BillPaymentRequest;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.BillPaymentResponse;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.TransactionData;
=======
<<<<<<< HEAD
import co.ke.tracom.bprgateway.servers.tcpserver.dto.*;
>>>>>>> efcbb2b65c6e0f7d10ac63d3583f9325e0a4220c
import co.ke.tracom.bprgateway.servers.tcpserver.data.academicBridge.AcademicBridgeValidation;
import co.ke.tracom.bprgateway.servers.tcpserver.data.billMenu.BillMenuRequest;
import co.ke.tracom.bprgateway.web.academicbridge.data.studentdetails.GetStudentDetailsResponse;
=======
>>>>>>> 4d3ba2bf7dd9775ed88dea1da280a0678992792e
import co.ke.tracom.bprgateway.servers.tcpserver.data.academicBridge.AcademicBridgeValidation;
import co.ke.tracom.bprgateway.servers.tcpserver.data.billMenu.BillMenuRequest;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.BillPaymentRequest;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.BillPaymentResponse;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.TransactionData;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.ValidationRequest;
import co.ke.tracom.bprgateway.web.VisionFund.data.*;
import co.ke.tracom.bprgateway.web.VisionFund.data.custom.CustomVerificationRequest;
import co.ke.tracom.bprgateway.web.VisionFund.data.custom.CustomVerificationResponse;
import co.ke.tracom.bprgateway.web.VisionFund.service.VisionFundService;
>>>>>>> b4f8b51aa7de15d115450a31e54228f463d48fa6
import co.ke.tracom.bprgateway.web.academicbridge.services.AcademicBridgeService;
import co.ke.tracom.bprgateway.web.academicbridge.services.AcademicBridgeT24;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.billMenus.data.BillMenuResponse;
import co.ke.tracom.bprgateway.web.billMenus.service.BillMenusService;
<<<<<<< HEAD
import co.ke.tracom.bprgateway.core.config.CustomObjectMapper;
<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> 4d3ba2bf7dd9775ed88dea1da280a0678992792e
>>>>>>> b4f8b51aa7de15d115450a31e54228f463d48fa6
import co.ke.tracom.bprgateway.web.eucl.dto.request.EUCLPaymentRequest;
import co.ke.tracom.bprgateway.web.eucl.dto.request.MeterNoValidation;
import co.ke.tracom.bprgateway.web.eucl.dto.response.EUCLPaymentResponse;
import co.ke.tracom.bprgateway.web.eucl.dto.response.MeterNoData;
import co.ke.tracom.bprgateway.web.eucl.dto.response.MeterNoValidationResponse;
<<<<<<< HEAD
=======
import co.ke.tracom.bprgateway.web.eucl.dto.response.PaymentResponseData;
>>>>>>> 4d3ba2bf7dd9775ed88dea1da280a0678992792e
import co.ke.tracom.bprgateway.web.eucl.service.EUCLService;
=======
import co.ke.tracom.bprgateway.web.exceptions.custom.InvalidAgentCredentialsException;
>>>>>>> efcbb2b65c6e0f7d10ac63d3583f9325e0a4220c
import co.ke.tracom.bprgateway.web.exceptions.custom.UnprocessableEntityException;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
<<<<<<< HEAD
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.CustomerProfileRequest;
=======
>>>>>>> b4f8b51aa7de15d115450a31e54228f463d48fa6
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class BillRequestHandler {
    private final WASACService wasacService;
<<<<<<< HEAD
    private final EUCLService euclService;
<<<<<<< HEAD
=======
    private final AcademicBridgeT24 academicBridgeT24Service;
    private final TransactionService transactionService;
    private final BaseServiceProcessor baseServiceProcessor;
>>>>>>> efcbb2b65c6e0f7d10ac63d3583f9325e0a4220c
=======
<<<<<<< HEAD
=======
    private final VisionFundService visionFundService;
>>>>>>> 4d3ba2bf7dd9775ed88dea1da280a0678992792e
>>>>>>> b4f8b51aa7de15d115450a31e54228f463d48fa6

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

<<<<<<< HEAD
        List<TransactionData> data = genericRequest.getData();
        //List<TransactionData> data = genericRequest.getCredentials().getData();
        //System.out.println(""+data.get(0).getValue());

        CustomerProfileResponse customerProfileResponse = null;
<<<<<<< HEAD
        MeterNoValidation meterNoValidation=new MeterNoValidation();
        MeterNoValidationResponse meterNoValidationResponse=new MeterNoValidationResponse();
=======
        List<TransactionData> validationData = new ArrayList<>();
>>>>>>> 4d3ba2bf7dd9775ed88dea1da280a0678992792e
        AcademicBridgeValidation response = AcademicBridgeValidation.builder().build();

        MeterNoValidationResponse euclValidationResponse = MeterNoValidationResponse.builder().build();
        MeterNoValidation euclValidation = new MeterNoValidation();


<<<<<<< HEAD
        switch (genericRequest.getSvcCode() /*genericRequest.getCredentials().getSvcCode()*/){
=======
        //List<TransactionData> data = genericRequest.getCredentials().getData();
        String data = genericRequest.getCredentials().getData();


        CustomerProfileResponse customerProfileResponse = new CustomerProfileResponse();

        customerProfileResponse.setMessage("Success");
        customerProfileResponse.setStatus("200");
        customerProfileResponse.setData(null);
//        switch (genericRequest.getCredentials().getSvcCode()){
        String billNumber = null;
        switch (genericRequest.getSvcCode()) {
>>>>>>> efcbb2b65c6e0f7d10ac63d3583f9325e0a4220c
=======
        switch (genericRequest.getSvcCode() /*genericRequest.getCredentials().getSvcCode()*/) {
>>>>>>> b4f8b51aa7de15d115450a31e54228f463d48fa6
            case "01.1":
            case "01.2":

                if (genericRequest.getField().equalsIgnoreCase("billNumber")) {
                    System.out.println("bill number is : " + genericRequest.getValue());
                    customerProfileResponse = (academicBridgeT24Service.validateStudentId(genericRequest.getValue()));
                    billNumber = genericRequest.getValue();
                    log.info("In case 01.1");

                } else {
                    log.info("Wrong svc code and field combination :" + genericRequest.getField());
                    customerProfileResponse.setData(null);
                    customerProfileResponse.setMessage("Wrong svc code and field name combination");
                    customerProfileResponse.setStatus("09");
                }
                break;


            //Validation for wasac customer profile.
            case "02.1":
<<<<<<< HEAD
<<<<<<< HEAD
                //If there is data sent then it's a profile validation else it's a  field validation
                if (data.size() > 0){
                    response = wasacService.validateWaterAccount(genericRequest, "POS");
                   /* validationData.add(TransactionData.builder()
                                    .name("Client POS name")responseValidation = {AcademicBridgeValidation@14727} "AcademicBridgeValidation(responseCode=05, responseMessage=Transaction processing failed. Please try again, data=[])"
                                    .value("")
                            .build());*/
=======
            case "02.2":
                {
                        if (genericRequest.getBill() == null){
                            genericRequest.setBill("bill");
                        }
                        response = wasacService.validateWaterAccount(genericRequest, "POS");
>>>>>>> b4f8b51aa7de15d115450a31e54228f463d48fa6
                }
                break;

<<<<<<< HEAD
                //fetch profile and return response
                 customerProfileResponse =
                        wasacService.fetchCustomerProfile(
                                CustomerProfileRequest.builder().customerId(genericRequest.getValue()).credentials(
                                        new MerchantAuthInfo(genericRequest.getCredentials().getUsername()
                                                ,genericRequest.getCredentials().getPassword())).build());
=======

                 /*customerProfileResponse = null;
                        wasacService.fetchCustomerProfile(
                                CustomerProfileRequest.builder().customerId(data.get(0).getValue()).credentials(
                                        new MerchantAuthInfo(genericRequest.getCredentials().getUsername()
                                                ,genericRequest.getCredentials().getPassword())).build());*/
                break;
>>>>>>> efcbb2b65c6e0f7d10ac63d3583f9325e0a4220c
=======
                //EUCL validation
            case "03.1":
<<<<<<< HEAD
               if(!data.isEmpty()){
                   String meterNo=data.get(0).getValue();
                   String phone =data.get(1).getValue();
                   String amount =data.get(2).getValue();
                   Long amounts= Long.parseLong(amount);
                   meterNoValidation.setMeterNo(meterNo);
                   meterNoValidation.setAmount(amount);
                   meterNoValidation.setPhoneNo(phone);
                   meterNoValidation.setCredentials(new MerchantAuthInfo(genericRequest.getCredentials().getUsername(),
                           genericRequest.getCredentials().getPassword()));
                   String euclRRN=RRNGenerator.getInstance("ECLV").getRRN();
                   meterNoValidationResponse=euclService.validateEUCLMeterNo(meterNoValidation,euclRRN);

                   //setting response service response to generic response
                 response.setResponseMessage(meterNoValidationResponse.getMessage());
                 response.setResponseCode(meterNoValidationResponse.getStatus());
                  List<TransactionData> eucldata=new ArrayList<>();
                   MeterNoData meterNoData= meterNoValidationResponse.getData();
                   eucldata.add(TransactionData.builder().name("Meter Number").value(meterNoData.getMeterNo()).build());
                   eucldata.add(TransactionData.builder().name("Account Name").value(meterNoData.getAccountName()).build());
                   eucldata.add(TransactionData.builder().name("Meter Location").value(meterNoData.getMeterLocation()).build());
                   eucldata.add(TransactionData.builder().name("RRN").value(meterNoData.getRrn()).build());

                   response.setData(eucldata);

                   //writing response to the output stream..
                   writeResponseToTCPChannel(socket, mapper.writeValueAsString(response));


               }
                break;
>>>>>>> b4f8b51aa7de15d115450a31e54228f463d48fa6


<<<<<<< HEAD
        }
=======
>>>>>>> 4d3ba2bf7dd9775ed88dea1da280a0678992792e

                //Extract data from validation request object to local variables only when some data has been sent
                if (!data.isEmpty()) {
                    String meterNo = data.get(0).getValue();
                    String phoneNo = data.get(1).getValue();
                    long amount = Long.parseLong(data.get(2).getValue());
=======
            //EUCL validation
            case "03.1":
<<<<<<< HEAD
            case "03.2":

                //Extract data from validation request object to local variables only when some data has been sent
                if (!data.isEmpty()){
                    String  meterNo = data.get(2).getValue();
                    String phoneNo = data.get(1).getValue();
                    String amount = data.get(0).getValue();
>>>>>>> b3145de46f0a14cf9880671dbd2fab347bdf95fb

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

                    // todo writing response to the output stream

                    //for field validation
                    customerProfileResponse = null;
                }

=======

                // customerProfileResponse = null;
>>>>>>> efcbb2b65c6e0f7d10ac63d3583f9325e0a4220c
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

<<<<<<< HEAD
        }


        GetStudentDetailsResponse validationResponse = customerProfileResponse.getData();
//        Response validationResponse = customerProfileResponse.getData();
        if (validationResponse != null) {

<<<<<<< HEAD
        /*Response validationResponse = customerProfileResponse.getData();
=======
        } else {
            System.out.println("No data in the response");
        }
>>>>>>> efcbb2b65c6e0f7d10ac63d3583f9325e0a4220c
=======
                    BeanUtils.copyProperties(fundResponse, response);
                    log.error("<<<<\nVision Fund Verification Completed");
                    log.error("\n[\nVision Fund Response Object\n{}\n",verificationResponse);
                    log.error("\nVision Fund RETURNED Response Object\n{}\n]\n>>>>",fundResponse);
                }
            }
            break;
>>>>>>> b4f8b51aa7de15d115450a31e54228f463d48fa6

        }


<<<<<<< HEAD
          response = getBridgeValidation(customerProfileResponse);
=======
>>>>>>> 4d3ba2bf7dd9775ed88dea1da280a0678992792e

<<<<<<< HEAD

<<<<<<< HEAD
        List<TransactionData> validationData = new ArrayList<>();
        validationData.add(
                TransactionData.builder()
                        .name("Client Post Name")
                        .value("POSTest").build());
       /* validationData.add(
                TransactionData.builder().name("Name").value(validationResponse.getName()).build());
        validationData.add(
                TransactionData.builder().name("Zone").value(validationResponse.getZone()).build());
        validationData.add(
                TransactionData.builder().name("Mobile No").value(validationResponse.getMobile()).build());
        validationData.add(
                TransactionData.builder().name("Email").value(validationResponse.getEmail()).build());
        validationData.add(
                TransactionData.builder().name("Phone").value(validationResponse.getPhone()).build());
        validationData.add(*/

        validationData.add(
                TransactionData.builder().name("Name").value("wanjohi").build());
        validationData.add(
                TransactionData.builder().name("Zone").value("Nairobi").build());
        validationData.add(
                TransactionData.builder().name("Mobile No").value("0712890098").build());
        validationData.add(
                TransactionData.builder().name("Email").value("test@gmail.com").build());
        validationData.add(
                TransactionData.builder().name("Phone").value("123654878").build());
        validationData.add(
                TransactionData.builder()
                        .name("National ID")
                        .value("03214569").build());
        validationData.add(
                TransactionData.builder().name("Branch").value("tracom").build());
        validationData.add(
                TransactionData.builder().name("Balance").value("120").build());
        validationData.add(
                TransactionData.builder().name("Meter No").value("0002222111555").build());
        validationData.add(
                TransactionData.builder()
                        .name("Customer Id")
                        .value("02315").build());

        response = getAcademicBridgeValidation(validationData, AppConstants.TRANSACTION_SUCCESS_STANDARD.value(), AppConstants.TRANSACTION_SUCCESS_STANDARD.getReasonPhrase());
=======
>>>>>>> 4d3ba2bf7dd9775ed88dea1da280a0678992792e
        writeResponseToTCPChannel(socket, mapper.writeValueAsString(response));
=======
        // String schoolId = String.valueOf(validationResponse.getSchool_ide());
        List<TransactionData> validationData = new ArrayList<>();
        if (validationResponse != null) {
            System.out.println("Response data setting");
            if (validationResponse.getStudent_name().equalsIgnoreCase("No record found")) {
                validationData.add(
                        TransactionData.builder()
                                .name("Client Post Name")
                                .value("POSTest").build());
                validationData.add(
                        TransactionData.builder().name("Student Data").value("No records were found for the student").build());
                validationData.add(
                        TransactionData.builder().name("Student Id").value(billNumber).build());

                posResponse = getAcademicBridgeValidation(validationData, "10", "Student data not found");
            }else if(validationResponse.getStudent_name().startsWith("OFSERROR")){
                validationData.add(
                        TransactionData.builder()
                                .name("Client Post Name")
                                .value("POSTest").build());
                validationData.add(
                        TransactionData.builder().name("Error in processing").value(validationResponse.getStudent_name()).build());

                posResponse = getAcademicBridgeValidation(validationData, "09", "T24 Error");
            }
            else {
                validationData.add(
                        TransactionData.builder()
                                .name("Client Post Name")
                                .value("POSTest").build());
                validationData.add(
                        TransactionData.builder().name("School Name").value(validationResponse.getSchool_name()).build());
                validationData.add(
                        TransactionData.builder().name("School Id").value(String.valueOf(validationResponse.getSchool_ide())).build());

                validationData.add(
                        TransactionData.builder().name("Credit Account").value(validationResponse.getSchool_account_number()).build());
                validationData.add(
                        TransactionData.builder().name("Student Name").value(validationResponse.getStudent_name()).build());
                validationData.add(
                        TransactionData.builder().name("Student Id").value(validationResponse.getStudent_reg_number()).build());

                posResponse = getAcademicBridgeValidation(validationData, AppConstants.TRANSACTION_SUCCESS_STANDARD.value(), AppConstants.TRANSACTION_SUCCESS_STANDARD.getReasonPhrase());
            }


        } else {
            System.out.println("No data set for response");
            validationData = null;
            posResponse = getAcademicBridgeValidation(validationData, "05", "Unexpected Error during processing");
        }

        //AcademicBridgeValidation response = getAcademicBridgeValidation(validationData, AppConstants.TRANSACTION_SUCCESS_STANDARD.value(), AppConstants.TRANSACTION_SUCCESS_STANDARD.getReasonPhrase());
        writeResponseToTCPChannel(socket, mapper.writeValueAsString(posResponse));

>>>>>>> efcbb2b65c6e0f7d10ac63d3583f9325e0a4220c
    }

    private void writeResponseToTCPChannel(NetSocket socket, String s) {
        System.out.println("writing response");
        Buffer outBuffer = Buffer.buffer();
        outBuffer.appendString(s);
        System.out.println(s);
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
<<<<<<< HEAD
        //Accadermic bill payment
<<<<<<< HEAD
        BillPaymentResponse billPaymentResponse = null;
        ObjectMapper objectMapper =new ObjectMapper();
        BillPaymentRequest paymentRequest=objectMapper.readValue(requestString,BillPaymentRequest.class);
     
        System.out.println("==========paymentRequest =========== " + paymentRequest);

        List<TransactionData> transactionData=new ArrayList<>();
        EUCLPaymentRequest euclPaymentRequest=new EUCLPaymentRequest();
        EUCLPaymentResponse euclPaymentResponse=EUCLPaymentResponse.builder().build();



        //kelvin
        switch(requestString){
=======
<<<<<<< HEAD
        BillPaymentResponse billPaymentResponse = BillPaymentResponse.builder().build();
=======
        BillPaymentResponse billPaymentResponse = new BillPaymentResponse();
>>>>>>> c4cf274d52dc57c9c471df137663e8e1c761fc2f
        CustomObjectMapper mapper = new CustomObjectMapper();

        BillPaymentRequest paymentRequest = mapper.readValue(requestString, BillPaymentRequest.class);
        log.info("BILL PAYMENT REQUEST OBJECT: {}", paymentRequest);

        List<TransactionData> data = paymentRequest.getData();

        //EUCL bill payment
        EUCLPaymentResponse euclPaymentResponse = EUCLPaymentResponse.builder().build();
        EUCLPaymentRequest euclPaymentRequest = new EUCLPaymentRequest();

        //kelvin
        switch (paymentRequest.getSvcCode()) {
>>>>>>> 4d3ba2bf7dd9775ed88dea1da280a0678992792e
            case "01.1":
                billPaymentResponse = getBillPaymentResponse();
                break;

<<<<<<< HEAD
            case "01.2":
                billPaymentResponse = getBillPaymentResponse();
                break;
            case "01.3":

            if(!transactionData.isEmpty()){
                String meterNo= transactionData.get(0).getValue();
                String phoneNumber=transactionData.get(1).getValue();
                String amount=transactionData.get(2).getValue();
                String meterLocation= transactionData.get(3).getValue();


                euclPaymentRequest.setAmount(amount);
                euclPaymentRequest.setCredentials(new MerchantAuthInfo(paymentRequest.getCredentials().getUsername(),
                        paymentRequest.getCredentials().getPassword()));

            }
            break;
        }
=======
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
                    break;
                }
                billPaymentResponse = wasacService.payWaterBill(paymentRequest);
                break;
            //EUCL requests
            case "03.2":
                //billPaymentResponse = getBillPaymentResponse();

                //Extract data from validation request object to local variables only when some data has been sent
<<<<<<< HEAD
                if (!data.isEmpty()) {
                    String meterNo = data.get(0).getValue();
=======
                if (!data.isEmpty()){
                    String  meterNo = data.get(2).getValue();
>>>>>>> b3145de46f0a14cf9880671dbd2fab347bdf95fb
                    String phoneNo = data.get(1).getValue();
                    String amount = data.get(0).getValue();
                    String meterLocation = data.get(3).getValue();

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
>>>>>>> 4d3ba2bf7dd9775ed88dea1da280a0678992792e

                break;
<<<<<<< HEAD
=======

        BillPaymentResponse billPaymentResponse = null;
        String processingStatus = null;
        String amount = "0";
        AuthenticateAgentResponse authenticateAgentResponse = null;
        HashMap<String, String> payment = new HashMap<String, String>();
        CustomObjectMapper mapper = new CustomObjectMapper();
        T24TXNQueue tot24 = new T24TXNQueue();
        BillPaymentRequest genericRequest = mapper.readValue(requestString, BillPaymentRequest.class);
        log.info("Validation REQUEST OBJECT: {}", genericRequest);
        switch (genericRequest.getSvcCode()) {
            case "01.2":
               // try {




                List<TransactionData> data = genericRequest.getData();
                int ii = 0;
                for (TransactionData column : data) {
                    if (ii >= 0) {
                        payment.put(column.getName(), column.getValue());

                    }
                    ii++;

                }
                MerchantAuthInfo auth = new MerchantAuthInfo();
                auth.setUsername(genericRequest.getCredentials().getUsername());
                auth.setPassword(genericRequest.getCredentials().getPassword());
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
                } catch (InvalidAgentCredentialsException e) {
                    billPaymentResponse = bootStrapNoAgentAccount();
                    e.printStackTrace();
                }

               /* if(authenticateAgentResponse !=null) {


                }else{
                    billPaymentResponse = bootStrapNoAgentAccount();
                }*/

               /* }catch (Exception ex){
                    log.info("Exception {}",ex);
                }*/

                break;

            case "02.2":
                billPaymentResponse = getBillPaymentResponse();
                break;
>>>>>>> efcbb2b65c6e0f7d10ac63d3583f9325e0a4220c
=======
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
>>>>>>> b4f8b51aa7de15d115450a31e54228f463d48fa6
        }

        AcademicBridgeValidation response = null;
        List<TransactionData> data = new ArrayList<>();
        if(billPaymentResponse.getResponseMessage().equalsIgnoreCase("No Agent account found")){
            data.add(
                    TransactionData.builder()
                            .name("Client Post Name")
                            .value("POSTest").build());
            data.add(
                    TransactionData.builder().name("No data").value(billPaymentResponse.getResponseMessage()).build());
            response = getAcademicBridgeValidation(data, AppConstants.ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR.value(), AppConstants.ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR.getReasonPhrase());
        }

        List<AcademicTransactionData> list = billPaymentResponse.getPaymentData().stream().collect(Collectors.toList());
       // System.out.println("First list element : " + list.get(0).getAbStudentName());

       // List<TransactionData> data = new ArrayList<>();
        if(!list.isEmpty()) {
            tot24.setT24responsecode(list.get(0).getT24responsecode());
            System.out.println("t24 response code to be saved "+tot24.getT24responsecode());
            if (list.get(0).getT24responsecode().equalsIgnoreCase("1")) {
                System.out.println("Response code from t24 after setting  " + list.get(0).getT24reference());
                tot24.setT24reference(list.get(0).getT24reference());


//        if (list != null) {
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
                   /* data.add(
                            TransactionData.builder().name("Delivery Out Ref").value(list.get(0).getDeliveryOutRef()).build());*/
                    tot24.setDebitacctno(authenticateAgentResponse.getData().getAccountNumber());
                    // tot24.setT24reference("Ref123");
                    tot24.setCreditacctno(payment.get("creditAccount"));
                    amount = list.get(0).getCreditAmount();

                    response = getAcademicBridgeValidation(data, AppConstants.TRANSACTION_SUCCESS_STANDARD.value(), AppConstants.TRANSACTION_SUCCESS_STANDARD.getReasonPhrase());

                    processingStatus = "000";
                } else {
                    data.add(
                            TransactionData.builder()
                                    .name("Client Post Name")
                                    .value("POSTest").build());
                    data.add(
                            TransactionData.builder().name("No data").value("Something went wrong during the transaction").build());
                    tot24.setDebitacctno(authenticateAgentResponse.getData().getAccountNumber());
                    //tot24.setT24reference("Ref123");
                    tot24.setCreditacctno(null);
                    amount = "0";

                    response = getAcademicBridgeValidation(data, AppConstants.ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR.value(), AppConstants.ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR.getReasonPhrase());


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
                response = getAcademicBridgeValidation(data, AppConstants.ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR.value(), AppConstants.ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR.getReasonPhrase());

<<<<<<< HEAD
            }
        }else{
           // System.out.println("Transaction not successful in last else >> "+list.get(0).getError());
            tot24.setT24responsecode("-1");
            data.add(
                    TransactionData.builder()
                            .name("Client Post Name")
                            .value("POSTest").build());
            data.add(
                    TransactionData.builder().name("No data").value(list.get(0).getError()).build());

            amount = "0";
            processingStatus = "10";
            response = getAcademicBridgeValidation(data, AppConstants.ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR.value(), AppConstants.ACADEMIC_BRIDGE_PAYMENT_EXTERNAL_SERVER_ERROR.getReasonPhrase());

        }

=======
>>>>>>> b4f8b51aa7de15d115450a31e54228f463d48fa6
        Buffer outBuffer = Buffer.buffer();
<<<<<<< HEAD
        outBuffer.appendString(mapper.writeValueAsString(billPaymentResponse));
        socket.write(outBuffer);
=======
        CustomObjectMapper mappe = new CustomObjectMapper();
        outBuffer.appendString(mappe.writeValueAsString(billPaymentResponse));
        //outBuffer.appendString(mappe.writeValueAsString(customerProfileResponse));
        System.out.println("Response to POS: " + outBuffer);
        // writeResponseToTCPChannel(socket, mapper.writeValueAsString(billPaymentResponse));
        // socket.write(outBuffer);

        /*tot24.setDebitacctno(authenticateAgentResponse.getData().getAccountNumber());
        tot24.setT24reference("Ref123");
        tot24.setCreditacctno(payment.get("creditAccount"));*/

        try {
            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "ACADEMIC BRIDGE", "1200",
                    Double.valueOf(amount), processingStatus,
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());//Replace with actua data after successfull login credentials fro POS


        }catch (Exception e){
            e.printStackTrace();
        }
         // AcademicBridgeValidation response = getAcademicBridgeValidation(data, AppConstants.TRANSACTION_SUCCESS_STANDARD.value(), AppConstants.TRANSACTION_SUCCESS_STANDARD.getReasonPhrase());
        writeResponseToTCPChannel(socket, mapper.writeValueAsString(response));
       // writeResponseToTCPChannel(socket, mapper.writeValueAsString(billPaymentResponse));
>>>>>>> efcbb2b65c6e0f7d10ac63d3583f9325e0a4220c
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

<<<<<<< HEAD
    private BillPaymentResponse bootStrapNoAgentAccount() {
        List<AcademicTransactionData> paymentData = new ArrayList<>();
        BillPaymentResponse billPaymentResponse =
                BillPaymentResponse.builder()
                        .responseCode("09")
                        .responseMessage("No Agent account found")
                        .data(null)
                        .paymentData(paymentData)
                        .build();
        return billPaymentResponse;
=======
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
>>>>>>> b4f8b51aa7de15d115450a31e54228f463d48fa6
    }
}
