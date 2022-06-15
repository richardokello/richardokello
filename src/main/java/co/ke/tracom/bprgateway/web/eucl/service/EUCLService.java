package co.ke.tracom.bprgateway.web.eucl.service;

import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.eucl.dto.request.EUCLPaymentRequest;
import co.ke.tracom.bprgateway.web.eucl.dto.request.MeterNoValidation;
import co.ke.tracom.bprgateway.web.eucl.dto.response.EUCLPaymentResponse;
import co.ke.tracom.bprgateway.web.eucl.dto.response.MeterNoData;
import co.ke.tracom.bprgateway.web.eucl.dto.response.MeterNoValidationResponse;
import co.ke.tracom.bprgateway.web.eucl.dto.response.PaymentResponseData;
import co.ke.tracom.bprgateway.web.eucl.entities.EUCLElectricityTxnLogs;
import co.ke.tracom.bprgateway.web.eucl.repository.EUCLElectricityTxnLogsRepository;
import co.ke.tracom.bprgateway.web.exceptions.custom.InvalidAgentCredentialsException;
import co.ke.tracom.bprgateway.web.switchparameters.XSwitchParameterService;
import co.ke.tracom.bprgateway.web.t24communication.services.T24Channel;
import co.ke.tracom.bprgateway.web.transactionLimits.TransactionLimitManagerService;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_PASSWORD;
import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_USERNAME;

@Slf4j
@RequiredArgsConstructor
@Service
public class EUCLService {
    private final Long EUCL_TRANSACTION_LIMIT_ID= 8L;
    private final T24Channel t24Channel;
    private final TransactionService transactionService;
    private final BaseServiceProcessor baseServiceProcessor;

    private final XSwitchParameterService xSwitchParameterService;
    private final EUCLElectricityTxnLogsRepository euclElectricityTxnLogsRepository;
    private final TransactionLimitManagerService limitManagerService;
    @SneakyThrows
    public MeterNoValidationResponse validateEUCLMeterNo(MeterNoValidation request, String referenceNo) {
        // Validate agent credentials

        AuthenticateAgentResponse optionalAuthenticateAgentResponse = null;
        T24TXNQueue tot24 = new T24TXNQueue();
        try{optionalAuthenticateAgentResponse=baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials());

        }
        catch (InvalidAgentCredentialsException e)
        {
            transactionService.saveFailedUserPasswordTransactions("Failed Logins PC module transactions","Agent logins",request.getCredentials().getUsername(),
                    "AgentValidation","FAILED","ipAddress");
        }

        try {
         MeterNoValidationResponse responses=new MeterNoValidationResponse();

            TransactionLimitManagerService.TransactionLimit limitValid = limitManagerService.isLimitValid(EUCL_TRANSACTION_LIMIT_ID, Double.parseDouble(request.getAmount()));
            if (!limitValid.isValid()) {
                transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "EUCL ELECTRICITY", "1200",
                        Double.valueOf(request.getAmount()), "061",
                        optionalAuthenticateAgentResponse.getData().getTid(), optionalAuthenticateAgentResponse.getData().getMid());
                responses.setStatus("061");
                responses.setMessage("Amount should be between"+ limitValid.getLower()+ " and " + limitValid.getUpper());
                return responses;
            }

            String channel = "PC";
            String txnref = referenceNo;
            String tid = "PCTID";

            Long amount = Long.valueOf(request.getAmount());
            String meterNo = request.getMeterNo();
            String phone = request.getPhoneNo();
            String EUCLBranch = xSwitchParameterService.fetchXSwitchParamValue("DEFAULT_EUCL_BRANCH");
            String newt24tem =
                    "0000AENQUIRY.SELECT,,"
                            + MASKED_T24_USERNAME
                            + "/"
                            + MASKED_T24_PASSWORD
                            + "/"
                            + EUCLBranch
                            + ",BPR.EUCL.GET.DATA,METER.NO:EQ="
                            + meterNo
                            + ",TXN.AMT:EQ="
                            + amount;
            String tot24str = String.format("%04d", newt24tem.length()) + newt24tem;


            // create a table or function to generate T24 messages

            tot24.setMeterno(meterNo);
            // base 64 encode request in db
            tot24.setRequestleg(tot24str);
            tot24.setStarttime(System.currentTimeMillis());
            tot24.setTxnchannel(channel);
            tot24.setGatewayref(txnref);
            tot24.setPostedstatus("0");
            tot24.setProcode("460001");
            tot24.setTxnmti("1100");
            tot24.setTid(tid);

            final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue("T24_IP");
            final String t24Port = xSwitchParameterService.fetchXSwitchParamValue("T24_PORT");

            t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);

            //TODO check this
            transactionService.updateT24TransactionDTO(tot24);

            String accname = tot24.getCustomerName() == null ? "" : tot24.getCustomerName();
            if (!accname.isEmpty()) {
                MeterNoData data = MeterNoData.builder()
                        .meterNo(request.getMeterNo())
                        .accountName(accname)
                        .meterLocation(tot24.getMeterLocation())
                        .rrn(referenceNo)
                        .build();

                MeterNoValidationResponse response = MeterNoValidationResponse
                        .builder()
                        .status("00")
                        .message("Meter no validation successful")
                        .data(data)
                        .build();
                log.info("EUCL Validation successful. Transaction [" + referenceNo + "] ");

                return response;

            } else {
                MeterNoValidationResponse response;
                String tot24narration=tot24.getT24failnarration()==null?"no response":tot24.getT24failnarration().replace("\"", "");
                //String tot24Isnull = Objects.isNull(tot24.getT24failnarration())?"response has no fail narration":tot24.getT24failnarration().replace("\"", "");
                MeterNoData data = MeterNoData.builder()
                        .rrn(referenceNo)
                        .build();

                    log.info("EUCL Validation failed. Transaction [" + referenceNo + "] " + tot24narration);

                transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "EUCL ELECTRICITY", "1200",
                        Double.valueOf(request.getAmount()), "98",
                        optionalAuthenticateAgentResponse.getData().getTid(), optionalAuthenticateAgentResponse.getData().getMid());


                response = MeterNoValidationResponse
                        .builder()
                        .status("98")
                        .message("EUCL Validation failed. Transaction [" + referenceNo + "] " )
                        .data(data)
                        .build();
                return response;
            }

        } catch (Exception e) {
            e.printStackTrace();
            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "EUCL ELECTRICITY", "1200",
                    Double.valueOf(request.getAmount()), "098",
                    optionalAuthenticateAgentResponse.getData().getTid(), optionalAuthenticateAgentResponse.getData().getMid());
            MeterNoData data = MeterNoData.builder()
                    .rrn(referenceNo)
                    .build();
            log.info("EUCL Validation failed. Transaction [" + referenceNo + "] Error: " + e.getMessage());
            MeterNoValidationResponse response = MeterNoValidationResponse
                    .builder()
                    .status("098")
                    .message("EUCL Validation failed. Contact administrator")
                    .data(data)
                    .build();
            return response;
        }

    }

    @SneakyThrows
    public EUCLPaymentResponse purchaseEUCLTokens(EUCLPaymentRequest request, String transactionReferenceNo) {
        AuthenticateAgentResponse authenticateAgentResponse = null;
        try{
            authenticateAgentResponse= baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials());
        }
        catch(InvalidAgentCredentialsException e){
          transactionService.saveFailedUserPasswordTransactions("Failed Logins PC module transactions","Agent logins",request.getCredentials().getUsername(),
                  "AgentValidation","FAILED","ipAddress");
        }


        T24TXNQueue tot24 = new T24TXNQueue();
        EUCLElectricityTxnLogs elecTxnLogs = new EUCLElectricityTxnLogs();
        try {

            elecTxnLogs.setAmount(request.getAmount());
            String channel = "PC";
            String tid = "PC";

            String meterNo = request.getMeterNo();
            String phone = request.getPhoneNo();
            String customerName = request.getPhoneNo();
            String customerAddress = request.getMeterLocation();
            String mid = authenticateAgentResponse.getData().getUsername();

            Long amount = Long.valueOf(request.getAmount());

            String euclBranch = xSwitchParameterService.fetchXSwitchParamValue("DEFAULT_EUCL_BRANCH");

            elecTxnLogs.setCustomer_name(customerName);
            elecTxnLogs.setMeterno(meterNo);
            elecTxnLogs.setPosref(transactionReferenceNo);
            elecTxnLogs.setMid(mid);
            elecTxnLogs.setTid(tid);

            // create t24 string
            String t24 =
                    "0000AFUNDS.TRANSFER,BPR.MIB.EUCL.PAY/I/PROCESS,"
                            + MASKED_T24_USERNAME
                            + "/"
                            + MASKED_T24_PASSWORD
                            + "/"
                            + euclBranch
                            + ",,"
                            + "METER.NO::="
                            + meterNo
                            + ","
                            + "DEBIT.ACCT.NO::="
                            + authenticateAgentResponse.getData().getAccountNumber()
                            + ","
                            + "DEBIT.CURRENCY::=RWF,"
                            + "ORDERING.BANK::=BNK,"
                            + "DEBIT.AMOUNT::="
                            + amount
                            + ","
                            + "MOBILE.NO::="
                            + phone
                            + ","
                            + "PAYMENT.DETAILS:1:1=ELECTRICITY PAYMENT BY USER,"
                            + "PAYMENT.DETAILS:2:="
                            + tid
                            + " "
                            + mid
                            + ","
                            + "PAYMENT.DETAILS:3:="
                            + transactionReferenceNo
                            + ","
                            + "CHANNEL::=AGB";

            String tot24str = String.format("%04d", t24.length()) + t24;
            System.out.println("String to t24==>" + tot24str);


            tot24.setMeterno(meterNo);
            // base 64 encode request in db
            tot24.setRequestleg(tot24str);
            tot24.setStarttime(System.currentTimeMillis());
            //TODO Define this in enum
            tot24.setTxnmti("1200");
            tot24.setTxnchannel(channel);
            tot24.setGatewayref(transactionReferenceNo);
            tot24.setPostedstatus("0");
            //TODO Define this in enum
            tot24.setProcode("460001");
            tot24.setTid(tid);
            tot24.setDebitacctno(authenticateAgentResponse.getData().getAccountNumber());

            final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue("T24_IP");
            final String t24Port = xSwitchParameterService.fetchXSwitchParamValue("T24_PORT");
            t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);
            transactionService.updateT24TransactionDTO(tot24);

            String accname = tot24.getCustomerName() == null ? "" : tot24.getCustomerName();
            String errorMessage =
                    tot24.getT24failnarration() == null ? "" : tot24.getT24failnarration();
            if (errorMessage.isEmpty()) {
                boolean manual =false;
                if (tot24.getT24responsecode() == null && tot24.getResponseleg().length() > 150){
                    //extract data  manually
                    manualExtraction(tot24,tot24.getResponseleg());
                    manual =true;
                }

                if (manual || tot24.getT24responsecode().equals("1")) {

                    elecTxnLogs.setGateway_t24postingstatus("1");
                    elecTxnLogs.setToken_no(tot24.getTokenNo());
                    euclElectricityTxnLogsRepository.save(elecTxnLogs);

                    String charges = "0.0";
                    if (null != tot24.getTotalchargeamt()) {
                        charges = tot24.getTotalchargeamt().replace("RWF", "");
                    }

                    PaymentResponseData paymentResponseData = new PaymentResponseData()
                            .setToken(tot24.getTokenNo())
                            .setT24Reference(tot24.getT24reference())
                            .setUnitsInKW(tot24.getUnitsKw())
                            .setCharges(charges)
                            .setRrn(transactionReferenceNo);

                    paymentResponseData.setUsername(authenticateAgentResponse.getData().getUsername());
                    paymentResponseData.setNames(authenticateAgentResponse.getData().getNames());
                    paymentResponseData.setBusinessName(authenticateAgentResponse.getData().getBusinessName());
                    paymentResponseData.setLocation(authenticateAgentResponse.getData().getLocation());

                    paymentResponseData.setTid(authenticateAgentResponse.getData().getTid());
                    paymentResponseData.setMid(authenticateAgentResponse.getData().getMid());

                    EUCLPaymentResponse euclPaymentResponse = EUCLPaymentResponse.builder()
                            .status("00")
                            .message("EUCL Transaction successful")
                            .data(paymentResponseData).build();


                    transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "EUCL ELECTRICITY", "1200",
                            Double.valueOf(request.getAmount()), "000",
                            authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());

                    return euclPaymentResponse;
                } else {
                    String charges = "0.0";
                    if (null != tot24.getTotalchargeamt()) {
                        charges = tot24.getTotalchargeamt().replace("RWF", "");
                    }

                    PaymentResponseData paymentResponseData = new PaymentResponseData()
                            .setCharges(charges)
                            .setRrn(transactionReferenceNo);
                    transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "EUCL ELECTRICITY", "1200",
                            Double.valueOf(request.getAmount()), "098",
                            authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());
                    log.info("EUCL Transaction [] failed " + errorMessage);
                    return EUCLPaymentResponse
                            .builder()
                            .data(paymentResponseData)
                            .status("098")
                            .message("EUCL Transaction failed.")
                            .build();
                }
            } else {
                elecTxnLogs.setGateway_t24postingstatus("0");
                euclElectricityTxnLogsRepository.save(elecTxnLogs);

                transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "EUCL ELECTRICITY", "1200",
                        Double.valueOf(request.getAmount()), "135",
                        authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());

                return EUCLPaymentResponse.builder()
                        .status("135")
                        .message(tot24.getT24failnarration().replace("\"", ""))
                        .data(null).build();
            }

        } catch (Exception e) {
            PaymentResponseData paymentResponseData = new PaymentResponseData()

                    .setRrn(transactionReferenceNo);
            e.printStackTrace();
            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "EUCL ELECTRICITY", "1200",
                    Double.valueOf(request.getAmount()), "098",
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());
            log.info("EUCL transaction [" + transactionReferenceNo + "] failed. Error " + e.getMessage());
            return EUCLPaymentResponse.builder()
                    .status("098")
                    .message("EUCL transaction failed. An exception occurred")
                    .data(paymentResponseData).build();
        }
    }

    private void manualExtraction(T24TXNQueue tot24, String responseleg) {
        String[] split = responseleg.split(",");
        tot24.setT24reference(split[0].split("/")[0]);
        String[] token = split[27].split(":");
        tot24.setTokenNo(token[3].split("-")[0]);
        tot24.setUnitsKw(token[4]);
        tot24.setGatewayref(split[15].split("=")[1]);
    }
}
