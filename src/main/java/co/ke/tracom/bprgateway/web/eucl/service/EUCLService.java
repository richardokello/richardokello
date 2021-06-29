package co.ke.tracom.bprgateway.web.eucl.service;

import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.depositmoney.data.response.DepositMoneyResult;
import co.ke.tracom.bprgateway.web.eucl.dto.request.EUCLPaymentRequest;
import co.ke.tracom.bprgateway.web.eucl.dto.request.MeterNoValidation;
import co.ke.tracom.bprgateway.web.eucl.dto.response.EUCLPaymentResponse;
import co.ke.tracom.bprgateway.web.eucl.dto.response.MeterNoData;
import co.ke.tracom.bprgateway.web.eucl.dto.response.MeterNoValidationResponse;
import co.ke.tracom.bprgateway.web.eucl.dto.response.PaymentResponseData;
import co.ke.tracom.bprgateway.web.eucl.entities.EUCLElectricityTxnLogs;
import co.ke.tracom.bprgateway.web.eucl.repository.EUCLElectricityTxnLogsRepository;
import co.ke.tracom.bprgateway.web.switchparameters.entities.XSwitchParameter;
import co.ke.tracom.bprgateway.web.switchparameters.repository.XSwitchParameterRepository;
import co.ke.tracom.bprgateway.web.t24communication.services.T24Channel;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_PASSWORD;
import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_USERNAME;

@Slf4j
@RequiredArgsConstructor
@Service
public class EUCLService {
    @Value("${merchant.account.validation}")
    private String agentValidation;

    private final T24Channel t24Channel;
    private final TransactionService transactionService;
    private final BaseServiceProcessor baseServiceProcessor;

    private final XSwitchParameterRepository xSwitchParameterRepository;
    private final EUCLElectricityTxnLogsRepository euclElectricityTxnLogsRepository;

    public MeterNoValidationResponse validateEUCLMeterNo(MeterNoValidation request, String referenceNo) {
        // Validate agent credentials
        AuthenticateAgentResponse optionalAuthenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials());

        try {
            String channel = "PC";
            String txnref = referenceNo;
            String tid = "PCTID";

            Long amount = Long.valueOf(request.getAmount());
            String meterNo = request.getMeterNo();
            String phone = request.getPhoneNo();
            String EUCLBranch = xSwitchParameterRepository.findByParamName("DEFAULT_EUCL_BRANCH").get().getParamValue();
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
            T24TXNQueue tot24 = new T24TXNQueue();
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

            Optional<XSwitchParameter> optionalT24IP = xSwitchParameterRepository.findByParamName("T24_IP");
            Optional<XSwitchParameter> optionalT24Port = xSwitchParameterRepository.findByParamName("T24_PORT");
            if (optionalT24IP.isEmpty() || optionalT24Port.isEmpty()) {
                return MeterNoValidationResponse.builder()
                        .status("098")
                        .message("Missing remote switch configuration. Contact administrator").build();
            }
            final String t24Ip = optionalT24IP.get().getParamValue();
            final String t24Port = optionalT24Port.get().getParamValue();

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
                log.info("EUCL Validation failed. Transaction [" + referenceNo + "] " + tot24.getT24failnarration().replace("\"", ""));
                MeterNoValidationResponse response = MeterNoValidationResponse
                        .builder()
                        .status("135")
                        .message(tot24.getT24failnarration().replace("\"", ""))
                        .data(null)
                        .build();
                return response;
            }

        } catch (Exception e) {
            e.printStackTrace();

            log.info("EUCL Validation failed. Transaction [" + referenceNo + "] Error: " + e.getMessage());
            MeterNoValidationResponse response = MeterNoValidationResponse
                    .builder()
                    .status("098")
                    .message("EUCL Validation failed. Contact administrator")
                    .data(null)
                    .build();
            return response;
        }

    }

    public EUCLPaymentResponse purchaseEUCLTokens(EUCLPaymentRequest request, String transactionReferenceNo) {
        AuthenticateAgentResponse authenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials());

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

            String euclBranch = xSwitchParameterRepository.findByParamName("DEFAULT_EUCL_BRANCH").get().getParamValue();

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

            T24TXNQueue tot24 = new T24TXNQueue();
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

            final String t24Ip = xSwitchParameterRepository.findByParamName("T24_IP").get().getParamValue();
            final String t24Port = xSwitchParameterRepository.findByParamName("T24_PORT").get().getParamValue();

            t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);
            transactionService.updateT24TransactionDTO(tot24);

            String accname = tot24.getCustomerName() == null ? "" : tot24.getCustomerName();
            String errorMessage =
                    tot24.getT24failnarration() == null ? "" : tot24.getT24failnarration();
            if (errorMessage.isEmpty()) {

                if (tot24.getT24responsecode().equals("1")) {

                    elecTxnLogs.setGateway_t24postingstatus("1");
                    elecTxnLogs.setToken_no(tot24.getTokenNo());
                    euclElectricityTxnLogsRepository.save(elecTxnLogs);

                    PaymentResponseData paymentResponseData = new PaymentResponseData()
                            .setToken(tot24.getTokenNo())
                            .setT24Reference(tot24.getT24reference())
                            .setUnitsInKW(tot24.getUnitsKw())
                            .setRrn(transactionReferenceNo);

                    paymentResponseData.setUsername(authenticateAgentResponse.getData().getUsername());
                    paymentResponseData.setNames(authenticateAgentResponse.getData().getNames());
                    paymentResponseData.setBusinessName(authenticateAgentResponse.getData().getBusinessName());
                    paymentResponseData.setLocation(authenticateAgentResponse.getData().getLocation());

                    EUCLPaymentResponse euclPaymentResponse = EUCLPaymentResponse.builder()
                            .status("00")
                            .message("EUCL Transaction successful")
                            .data(paymentResponseData).build();


                    transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "EUCL ELECTRICITY", "1200",
                            Double.valueOf(request.getAmount()), "000");

                    return euclPaymentResponse;
                } else {

                    transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "EUCL ELECTRICITY", "1200",
                            Double.valueOf(request.getAmount()), "098");
                    log.info("EUCL Transaction [] failed " + errorMessage);
                    return EUCLPaymentResponse
                            .builder()
                            .status("098")
                            .message("EUCL Transaction failed.")
                            .build();
                }
            } else {
                elecTxnLogs.setGateway_t24postingstatus("0");
                euclElectricityTxnLogsRepository.save(elecTxnLogs);

                transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "EUCL ELECTRICITY", "1200",
                        Double.valueOf(request.getAmount()), "135");

                return EUCLPaymentResponse.builder()
                        .status("135")
                        .message(tot24.getT24failnarration().replace("\"", ""))
                        .data(null).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("EUCL transaction [" + transactionReferenceNo + "] failed. Error " + e.getMessage());
            return EUCLPaymentResponse.builder()
                    .status("098")
                    .message("EUCL transaction failed. An exception occurred")
                    .data(null).build();
        }
    }
}
