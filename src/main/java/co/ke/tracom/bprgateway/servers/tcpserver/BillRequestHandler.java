package co.ke.tracom.bprgateway.servers.tcpserver;

import co.ke.tracom.bprgateway.core.util.AppConstants;
import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.BillPaymentRequest;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.BillPaymentResponse;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.TransactionData;
import co.ke.tracom.bprgateway.servers.tcpserver.data.academicBridge.AcademicBridgeValidation;
import co.ke.tracom.bprgateway.servers.tcpserver.data.billMenu.BillMenuRequest;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.ValidationRequest;
import co.ke.tracom.bprgateway.web.academicbridge.data.studentdetails.GetStudentDetailsResponse;
import co.ke.tracom.bprgateway.web.academicbridge.services.AcademicBridgeService;
import co.ke.tracom.bprgateway.web.academicbridge.services.AcademicBridgeT24;
import co.ke.tracom.bprgateway.web.billMenus.data.BillMenuResponse;
import co.ke.tracom.bprgateway.web.billMenus.service.BillMenusService;
import co.ke.tracom.bprgateway.core.config.CustomObjectMapper;
import co.ke.tracom.bprgateway.web.exceptions.custom.UnprocessableEntityException;
import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.CustomerProfileRequest;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.CustomerProfileResponse;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.Response;
import co.ke.tracom.bprgateway.web.wasac.service.WASACService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillRequestHandler {
    private final WASACService wasacService;
    private  final AcademicBridgeT24 academicBridgeT24Service;

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

        ValidationRequest genericRequest = mapper.readValue(requestString, ValidationRequest.class);
        log.info("Validation REQUEST OBJECT: {}", genericRequest);

        //List<TransactionData> data = genericRequest.getCredentials().getData();
        String data = genericRequest.getCredentials().getData();


        CustomerProfileResponse customerProfileResponse = new CustomerProfileResponse();

        customerProfileResponse.setMessage("Success");
        customerProfileResponse.setStatus("200");
        customerProfileResponse.setData(null);
//        switch (genericRequest.getCredentials().getSvcCode()){
        switch (genericRequest.getSvcCode()){
            case "01.1":
            case "01.2":
                if(genericRequest.getField().equalsIgnoreCase("billNumber")) {
                    System.out.println("bill number is : "+genericRequest.getValue());
                    customerProfileResponse = (academicBridgeT24Service.validateStudentId(genericRequest.getValue()));
                    System.out.println("In case 01.1");
                    System.out.println(genericRequest.getCredentials().getBill());
                    System.out.println("T24 response is : " + customerProfileResponse.getMessage());
                }else {
                    System.out.println("Wrong svc code and field combination :"+genericRequest.getField());
                }
                break;

            case "02.1":
                System.out.println("In case 02.1");
                 /*customerProfileResponse = null;
                        wasacService.fetchCustomerProfile(
                                CustomerProfileRequest.builder().customerId(data.get(0).getValue()).credentials(
                                        new MerchantAuthInfo(genericRequest.getCredentials().getUsername()
                                                ,genericRequest.getCredentials().getPassword())).build());*/
                break;

            case "03.1":
                System.out.println("In case 03.1");
               // customerProfileResponse = null;
                break;


        }


        GetStudentDetailsResponse validationResponse = customerProfileResponse.getData();
//        Response validationResponse = customerProfileResponse.getData();
        System.out.println("sch name "+validationResponse.getSchool_name());
        System.out.println("sch id "+validationResponse.getSchool_ide());
        System.out.println("sch acc name "+validationResponse.getSchool_account_name());
        System.out.println("sch acc num "+validationResponse.getSchool_account_number());
        System.out.println(" student name "+validationResponse.getStudent_name());
        System.out.println("student id "+validationResponse.getStudent_reg_number());


        if (customerProfileResponse
                .getStatus()
                .equalsIgnoreCase(AppConstants.EXCEPTION_OCCURRED_ON_EXTERNAL_HTTP_REQUEST.value())) {

            AcademicBridgeValidation response = getBridgeValidation(customerProfileResponse);

            writeResponseToTCPChannel(socket, mapper.writeValueAsString(response));
        }

        String schoolId = String.valueOf(validationResponse.getSchool_ide());
        List<TransactionData> validationData = new ArrayList<>();
        validationData.add(
                TransactionData.builder()
                        .name("Client Post Name")
                        .value("POSTest").build());
        validationData.add(
                TransactionData.builder().name("School Name").value(validationResponse.getSchool_name()).build());
        validationData.add(
                TransactionData.builder().name("School Id").value(String.valueOf(validationResponse.getSchool_ide())).build());
        /*validationData.add(
                TransactionData.builder().name("School Account name").value(validationResponse.getSchool_account_name()).build());*/
        validationData.add(
                TransactionData.builder().name("School Account number").value(validationResponse.getSchool_account_number()).build());
        validationData.add(
                TransactionData.builder().name("Student Name").value(validationResponse.getStudent_name()).build());
        validationData.add(
                TransactionData.builder().name("Student Id").value(validationResponse.getStudent_reg_number()).build());

       /* validationData.add(
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
                        .value("02315").build());*/

        AcademicBridgeValidation response = getAcademicBridgeValidation(validationData, AppConstants.TRANSACTION_SUCCESS_STANDARD.value(), AppConstants.TRANSACTION_SUCCESS_STANDARD.getReasonPhrase());
        writeResponseToTCPChannel(socket, mapper.writeValueAsString(response));
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
        //Accadermic bill payment
        BillPaymentResponse billPaymentResponse = null;
        CustomerProfileResponse customerProfileResponse = null;

        //kelvin
        CustomObjectMapper mapper = new CustomObjectMapper();
        BillPaymentRequest genericRequest = mapper.readValue(requestString, BillPaymentRequest.class);
        log.info("Validation REQUEST OBJECT: {}", genericRequest);
        switch(genericRequest.getSvcCode()){
            case "01.2":
               // billPaymentResponse = getBillPaymentResponse();
                List data = genericRequest.getData();
                System.out.println(data.size());
                System.out.println(data.stream().findFirst());
               // String OFS = academicBridgeT24Service.bootstrapAcademicBridgePaymentOFSMsg(
                       /* genericRequest.getPayment().getDebitAccount(),
                        genericRequest.getPayment().getCreditAccount(),
                        genericRequest.getPayment().getAmount(),
                        genericRequest.getPayment().getSenderName(),
                        genericRequest.getPayment().getMobileNumber(),
                        String.valueOf(genericRequest.getPayment().getSchoolId()),
                        genericRequest.getPayment().getSchoolName(),
                        genericRequest.getPayment().getStudentName(),
                        genericRequest.getPayment().getBillNumber()*/

                       /* genericRequest.getDebitAccount(),
                        genericRequest.getCreditAccount(),
                        genericRequest.getAmount(),
                        genericRequest.getSenderName(),
                        genericRequest.getMobileNumber(),
                        String.valueOf(genericRequest.getSchoolId()),
                        genericRequest.getSchoolName(),
                        genericRequest.getStudentName(),
                        genericRequest.getBillNumber()*/
              //  );
              //  System.out.println("Request OFS : "+OFS);
               // customerProfileResponse = ( academicBridgeT24Service.validateStudentId(OFS));
               // customerProfileResponse = (academicBridgeT24Service.academicBridgePayment(OFS));
                System.out.println("In case 01.1");
                break;

            case "02.2":
                billPaymentResponse = getBillPaymentResponse();
                break;
        }



        Buffer outBuffer = Buffer.buffer();
        //CustomObjectMapper mapper = new CustomObjectMapper();
       // outBuffer.appendString(mapper.writeValueAsString(billPaymentResponse));
        outBuffer.appendString(mapper.writeValueAsString(customerProfileResponse));
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
}
