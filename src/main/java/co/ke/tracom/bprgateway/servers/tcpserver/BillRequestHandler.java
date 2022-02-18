package co.ke.tracom.bprgateway.servers.tcpserver;

import co.ke.tracom.bprgateway.core.util.AppConstants;
import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.BillPaymentRequest;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.BillPaymentResponse;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.TransactionData;
import co.ke.tracom.bprgateway.servers.tcpserver.data.academicBridge.AcademicBridgeValidation;
import co.ke.tracom.bprgateway.servers.tcpserver.data.billMenu.BillMenuRequest;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.ValidationRequest;
import co.ke.tracom.bprgateway.web.academicbridge.services.AcademicBridgeService;
import co.ke.tracom.bprgateway.web.billMenus.data.BillMenuResponse;
import co.ke.tracom.bprgateway.web.billMenus.service.BillMenusService;
import co.ke.tracom.bprgateway.core.config.CustomObjectMapper;
import co.ke.tracom.bprgateway.web.eucl.dto.request.EUCLPaymentRequest;
import co.ke.tracom.bprgateway.web.eucl.dto.request.MeterNoValidation;
import co.ke.tracom.bprgateway.web.eucl.dto.response.EUCLPaymentResponse;
import co.ke.tracom.bprgateway.web.eucl.dto.response.MeterNoData;
import co.ke.tracom.bprgateway.web.eucl.dto.response.MeterNoValidationResponse;
import co.ke.tracom.bprgateway.web.eucl.service.EUCLService;
import co.ke.tracom.bprgateway.web.exceptions.custom.UnprocessableEntityException;
import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.CustomerProfileRequest;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.CustomerProfileResponse;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.Response;
import co.ke.tracom.bprgateway.web.wasac.service.WASACService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final EUCLService euclService;

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

        List<TransactionData> data = genericRequest.getData();
        System.out.println(""+data.get(0).getValue());

        CustomerProfileResponse customerProfileResponse = null;
        MeterNoValidation meterNoValidation=new MeterNoValidation();
        MeterNoValidationResponse meterNoValidationResponse=new MeterNoValidationResponse();
        AcademicBridgeValidation response = AcademicBridgeValidation.builder().build();

        switch (genericRequest.getSvcCode()){
            case "01.1":
                customerProfileResponse = null;
                break;

            case "02.1":
                 customerProfileResponse =
                        wasacService.fetchCustomerProfile(
                                CustomerProfileRequest.builder().customerId(/*data.get(0).getValue()*/"").credentials(
                                        new MerchantAuthInfo(genericRequest.getCredentials().getUsername()
                                                ,genericRequest.getCredentials().getPassword())).build());
                break;

            case "03.1":
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


        }



        Response validationResponse = customerProfileResponse.getData();

        if (customerProfileResponse
                .getStatus()
                .equalsIgnoreCase(AppConstants.EXCEPTION_OCCURRED_ON_EXTERNAL_HTTP_REQUEST.value())) {

          response = getBridgeValidation(customerProfileResponse);

            writeResponseToTCPChannel(socket, mapper.writeValueAsString(response));
        }

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
        BillPaymentResponse billPaymentResponse = null;
        ObjectMapper objectMapper =new ObjectMapper();
        BillPaymentRequest paymentRequest=objectMapper.readValue(requestString,BillPaymentRequest.class);
     
        System.out.println("==========paymentRequest =========== " + paymentRequest);

        List<TransactionData> transactionData=new ArrayList<>();
        EUCLPaymentRequest euclPaymentRequest=new EUCLPaymentRequest();
        EUCLPaymentResponse euclPaymentResponse=EUCLPaymentResponse.builder().build();



        //kelvin
        switch(requestString){
            case "01.1":
                billPaymentResponse = getBillPaymentResponse();
                break;

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



        Buffer outBuffer = Buffer.buffer();
        CustomObjectMapper mapper = new CustomObjectMapper();
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
}
