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
import co.ke.tracom.bprgateway.web.eucl.dto.response.PaymentResponseData;
import co.ke.tracom.bprgateway.web.eucl.service.EUCLService;
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
        //List<TransactionData> data = genericRequest.getCredentials().getData();
        //System.out.println(""+data.get(0).getValue());

        CustomerProfileResponse customerProfileResponse = null;
        List<TransactionData> validationData = new ArrayList<>();
        AcademicBridgeValidation response = AcademicBridgeValidation.builder().build();

        MeterNoValidationResponse euclValidationResponse = MeterNoValidationResponse.builder().build();
        MeterNoValidation euclValidation = new MeterNoValidation();


        switch (genericRequest.getSvcCode() /*genericRequest.getCredentials().getSvcCode()*/){
            case "01.1":
                customerProfileResponse = null;
                break;


            //Validation for wasac customer profile.
            case "02.1":
                //If there is data sent then it's a profile validation else it's a  field validation
                if (data.size() > 0){
                    response = wasacService.validateWaterAccount(genericRequest, "POS");
                   /* validationData.add(TransactionData.builder()
                                    .name("Client POS name")responseValidation = {AcademicBridgeValidation@14727} "AcademicBridgeValidation(responseCode=05, responseMessage=Transaction processing failed. Please try again, data=[])"
                                    .value("")
                            .build());*/
                }

                //fetch profile and return response
                 customerProfileResponse =
                        wasacService.fetchCustomerProfile(
                                CustomerProfileRequest.builder().customerId(genericRequest.getValue()).credentials(
                                        new MerchantAuthInfo(genericRequest.getCredentials().getUsername()
                                                ,genericRequest.getCredentials().getPassword())).build());

                AcademicBridgeValidation responseValidation = getBridgeValidation(customerProfileResponse);

                writeResponseToTCPChannel(socket, mapper.writeValueAsString(responseValidation));
                return;
                //break;

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
                    euclValidationResponse = euclService.validateEUCLMeterNo(euclValidation,requestRefNo);


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

                break;


        }



        /*Response validationResponse = customerProfileResponse.getData();

        if (customerProfileResponse
                .getStatus()
                .equalsIgnoreCase(AppConstants.EXCEPTION_OCCURRED_ON_EXTERNAL_HTTP_REQUEST.value())) {

            AcademicBridgeValidation responseValidation = getBridgeValidation(customerProfileResponse);

            writeResponseToTCPChannel(socket, mapper.writeValueAsString(responseValidation));
        }*/


        /*validationData.add(
                TransactionData.builder()
                        .name("Client Post Name")
                        .value("POSTest").build());
       *//* validationData.add(
                TransactionData.builder().name("Name").value(validationResponse.getName()).build());
        validationData.add(
                TransactionData.builder().name("Zone").value(validationResponse.getZone()).build());
        validationData.add(
                TransactionData.builder().name("Mobile No").value(validationResponse.getMobile()).build());
        validationData.add(
                TransactionData.builder().name("Email").value(validationResponse.getEmail()).build());
        validationData.add(
                TransactionData.builder().name("Phone").value(validationResponse.getPhone()).build());
        validationData.add(*//*

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

        AcademicBridgeValidation response = getAcademicBridgeValidation(validationData, AppConstants.TRANSACTION_SUCCESS_STANDARD.value(), AppConstants.TRANSACTION_SUCCESS_STANDARD.getReasonPhrase());*/
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
        BillPaymentResponse billPaymentResponse = BillPaymentResponse.builder().build();
        CustomObjectMapper mapper = new CustomObjectMapper();

        BillPaymentRequest paymentRequest = mapper.readValue(requestString, BillPaymentRequest.class);
        log.info("BILL PAYMENT REQUEST OBJECT: {}", paymentRequest);

        List<TransactionData> data = paymentRequest.getData();

        //EUCL bill payment
        EUCLPaymentResponse euclPaymentResponse = EUCLPaymentResponse.builder().build();
        EUCLPaymentRequest euclPaymentRequest = new EUCLPaymentRequest();

        //kelvin
        switch(paymentRequest.getSvcCode()){
            case "01.1":
                billPaymentResponse = getBillPaymentResponse();
                break;

            //WASAC bill payment
            case "02.2":
                //billPaymentResponse = getBillPaymentResponse();
                billPaymentResponse = wasacService.payWaterBill(paymentRequest);
                break;
            //EUCL requests
            case "03.2":
                //billPaymentResponse = getBillPaymentResponse();

                //Extract data from validation request object to local variables only when some data has been sent
                if (!data.isEmpty()){
                    String  meterNo = data.get(2).getValue();
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

                    euclPaymentResponse = euclService.purchaseEUCLTokens(euclPaymentRequest,requestRefNo);

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
        }



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
}
