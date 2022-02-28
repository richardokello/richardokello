package co.ke.tracom.bprgateway.servers.tcpserver;

import co.ke.tracom.bprgateway.core.util.AppConstants;
import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.*;
import co.ke.tracom.bprgateway.servers.tcpserver.data.academicBridge.AcademicBridgeValidation;
import co.ke.tracom.bprgateway.servers.tcpserver.data.billMenu.BillMenuRequest;
import co.ke.tracom.bprgateway.web.academicbridge.data.studentdetails.GetStudentDetailsResponse;
import co.ke.tracom.bprgateway.web.academicbridge.services.AcademicBridgeService;
import co.ke.tracom.bprgateway.web.academicbridge.services.AcademicBridgeT24;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.billMenus.data.BillMenuResponse;
import co.ke.tracom.bprgateway.web.billMenus.service.BillMenusService;
import co.ke.tracom.bprgateway.core.config.CustomObjectMapper;
import co.ke.tracom.bprgateway.web.exceptions.custom.InvalidAgentCredentialsException;
import co.ke.tracom.bprgateway.web.exceptions.custom.UnprocessableEntityException;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.CustomerProfileRequest;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.CustomerProfileResponse;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.Response;
import co.ke.tracom.bprgateway.web.wasac.service.WASACService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final AcademicBridgeT24 academicBridgeT24Service;
    private final TransactionService transactionService;
    private final BaseServiceProcessor baseServiceProcessor;

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

        //List<TransactionData> data = genericRequest.getCredentials().getData();
        String data = genericRequest.getCredentials().getData();


        CustomerProfileResponse customerProfileResponse = new CustomerProfileResponse();

        customerProfileResponse.setMessage("Success");
        customerProfileResponse.setStatus("200");
        customerProfileResponse.setData(null);
//        switch (genericRequest.getCredentials().getSvcCode()){
        String billNumber = null;
        switch (genericRequest.getSvcCode()) {
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

            case "02.1":

                 /*customerProfileResponse = null;
                        wasacService.fetchCustomerProfile(
                                CustomerProfileRequest.builder().customerId(data.get(0).getValue()).credentials(
                                        new MerchantAuthInfo(genericRequest.getCredentials().getUsername()
                                                ,genericRequest.getCredentials().getPassword())).build());*/
                break;

            case "03.1":

                // customerProfileResponse = null;
                break;


        }


        GetStudentDetailsResponse validationResponse = customerProfileResponse.getData();
//        Response validationResponse = customerProfileResponse.getData();
        if (validationResponse != null) {

        } else {
            System.out.println("No data in the response");
        }

        if (customerProfileResponse
                .getStatus()
                .equalsIgnoreCase(AppConstants.EXCEPTION_OCCURRED_ON_EXTERNAL_HTTP_REQUEST.value())) {

            AcademicBridgeValidation response = getBridgeValidation(customerProfileResponse);

            writeResponseToTCPChannel(socket, mapper.writeValueAsString(response));
        }

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

        Buffer outBuffer = Buffer.buffer();
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
        List<AcademicTransactionData> paymentData = new ArrayList<>();
        BillPaymentResponse billPaymentResponse =
                BillPaymentResponse.builder()
                        .responseCode("09")
                        .responseMessage("No Agent account found")
                        .data(null)
                        .paymentData(paymentData)
                        .build();
        return billPaymentResponse;
    }
}
