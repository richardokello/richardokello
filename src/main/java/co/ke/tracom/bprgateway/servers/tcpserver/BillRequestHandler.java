package co.ke.tracom.bprgateway.servers.tcpserver;

import co.ke.tracom.bprgateway.core.config.CustomObjectMapper;
import co.ke.tracom.bprgateway.core.util.RRNGenerator;
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
import co.ke.tracom.bprgateway.web.academicbridge.services.AcademicBridgeService;
import co.ke.tracom.bprgateway.web.billMenus.data.BillMenuResponse;
import co.ke.tracom.bprgateway.web.billMenus.service.BillMenusService;
import co.ke.tracom.bprgateway.web.eucl.dto.request.EUCLPaymentRequest;
import co.ke.tracom.bprgateway.web.eucl.dto.request.MeterNoValidation;
import co.ke.tracom.bprgateway.web.eucl.dto.response.EUCLPaymentResponse;
import co.ke.tracom.bprgateway.web.eucl.dto.response.MeterNoData;
import co.ke.tracom.bprgateway.web.eucl.dto.response.MeterNoValidationResponse;
import co.ke.tracom.bprgateway.web.eucl.dto.response.PaymentResponseData;
import co.ke.tracom.bprgateway.web.eucl.service.EUCLService;
import co.ke.tracom.bprgateway.web.exceptions.custom.UnprocessableEntityException;
import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.CustomerProfileResponse;
import co.ke.tracom.bprgateway.web.wasac.service.WASACService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class BillRequestHandler {
    private final WASACService wasacService;
    private final EUCLService euclService;
    private final VisionFundService visionFundService;

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


        switch (genericRequest.getSvcCode() /*genericRequest.getCredentials().getSvcCode()*/) {
            case "01.1":
                customerProfileResponse = null;
                break;


            //Validation for wasac customer profile.
            case "02.1":{
                    response = wasacService.validateWaterAccount(genericRequest, "POS");
            }
                break;

                //EUCL validation
            case "03.1":

                //Extract data from validation request object to local variables only when some data has been sent
                if (!data.isEmpty()) {
                    String meterNo = data.get(0).getValue();
                    String phoneNo = data.get(1).getValue();
                    long amount = Long.parseLong(data.get(2).getValue());

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
        BillPaymentResponse billPaymentResponse = new BillPaymentResponse();
        CustomObjectMapper mapper = new CustomObjectMapper();

        BillPaymentRequest paymentRequest = mapper.readValue(requestString, BillPaymentRequest.class);
        log.info("BILL PAYMENT REQUEST OBJECT: {}", paymentRequest);

        List<TransactionData> data = paymentRequest.getData();

        //EUCL bill payment
        EUCLPaymentResponse euclPaymentResponse = EUCLPaymentResponse.builder().build();
        EUCLPaymentRequest euclPaymentRequest = new EUCLPaymentRequest();

        //kelvin
        switch (paymentRequest.getSvcCode()) {
            case "01.1":
                billPaymentResponse = getBillPaymentResponse();
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
                    break;
                }
                billPaymentResponse = wasacService.payWaterBill(paymentRequest);
                break;
            //EUCL requests
            case "03.2":
                //billPaymentResponse = getBillPaymentResponse();

                //Extract data from validation request object to local variables only when some data has been sent
                if (!data.isEmpty()) {
                    String meterNo = data.get(0).getValue();
                    String phoneNo = data.get(1).getValue();
                    String amount = data.get(2).getValue();
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
}
