package co.ke.tracom.bprgateway.web.eucl.service;

import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.eucl.dto.request.EUCLPaymentRequest;
import co.ke.tracom.bprgateway.web.eucl.dto.request.MeterNoValidation;
import co.ke.tracom.bprgateway.web.eucl.dto.response.EUCLPaymentResponse;
import co.ke.tracom.bprgateway.web.eucl.dto.response.MeterNoData;
import co.ke.tracom.bprgateway.web.eucl.dto.response.MeterNoValidationResponse;
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
import org.springframework.beans.factory.annotation.Value;
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

        try {
            String channel = "1510";
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
                        .build();

                MeterNoValidationResponse response = MeterNoValidationResponse
                        .builder()
                        .status("00")
                        .message("Meter no validation successful")
                        .data(data)
                        .build();
                log.info("EUCL Validation successful. Transaction [" + referenceNo + "] ");


                transactionService.saveCardLessTransactionToAllTransactionTable(
                        tot24, "EUCL ACCOUNT VALIDATION");

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
        Optional<AuthenticateAgentResponse> optionalAuthenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials(), agentValidation);
        if (optionalAuthenticateAgentResponse.isEmpty()) {
            log.info(
                    "Agent Float Deposit:[Failed] Missing agent information.  Transaction RRN [" + transactionReferenceNo + "]");
            return EUCLPaymentResponse
                .builder()
                .status("117")
                .message("Missing agent information")
                .build();
        }
        AuthenticateAgentResponse authenticateAgentResponse = optionalAuthenticateAgentResponse.get();

        EUCLElectricityTxnLogs euclElectricityTxnLogs = new EUCLElectricityTxnLogs();
//        try {
//
//
//            Long amount = Long.valueOf(isomsg.getString(4));
//                      elecTxnLogs.setAmount(amount + "");
//
//            String channel = isomsg.getString(25);
//            String txnref = isomsg.getString(37);
//            String tid = isomsg.getString(41);
//            String f60 = isomsg.getString(60);
//
//            String[] data = f60.split("#");
//            String meterNo = data[0];
//            String phone = data[1];
//            String customerName = data[2];
//            String customerAddress = data[3];
//            String mid = isomsg.getString(42);
//            String euclBankBranch = BPRFunctions.getParambyName("DEFAULT_EUCL_BRANCH");
//
//            elecTxnLogs.setCustomer_name(customerName);
//            elecTxnLogs.setMeterno(meterNo);
//            elecTxnLogs.setPosref(txnref);
//            elecTxnLogs.setMid(mid);
//            elecTxnLogs.setTid(tid);
//
//            // create t24 string
//            String t24 =
//                    "0000AFUNDS.TRANSFER,BPR.MIB.EUCL.PAY/I/PROCESS,"
//                            + t24usn
//                            + "/"
//                            + t24pwd
//                            + "/"
//                            + euclBankBranch
//                            + ",,"
//                            + "METER.NO::="
//                            + meterNo
//                            + ","
//                            + "DEBIT.ACCT.NO::="
//                            + agentfloatacc
//                            + ","
//                            + "DEBIT.CURRENCY::=RWF,"
//                            + "ORDERING.BANK::=BNK,"
//                            + "DEBIT.AMOUNT::="
//                            + amount
//                            + ","
//                            + "MOBILE.NO::="
//                            + phone
//                            + ","
//                            + "PAYMENT.DETAILS:1:1=ELECTRICITY PAYMENT BY USER,"
//                            + "PAYMENT.DETAILS:2:="
//                            + tid
//                            + " "
//                            + mid
//                            + ","
//                            + "PAYMENT.DETAILS:3:="
//                            + txnref
//                            + ","
//                            + "CHANNEL::=AGB";
//
//            String tot24str = String.format("%04d", t24.length()) + t24;
//            System.out.println("String to t24==>" + tot24str);
//
//            String waittime = BPRFunctions.getParambyName("T24_INQUIRY_WAIT_TIME_MILLISECONDS");
//            waittime = waittime.isEmpty() ? "30000" : waittime;
//
//            T24TXNQueue tot24 = new T24TXNQueue();
//            tot24.setMeterno(meterNo);
//            // base 64 encode request in db
//            tot24.setRequestleg(tot24str);
//            tot24.setStarttime(System.currentTimeMillis());
//            tot24.setTxnmti(isomsg.getMTI());
//            tot24.setTxnchannel(channel);
//            tot24.setGatewayref(txnref);
//            tot24.setPostedstatus("0");
//            tot24.setProcode(isomsg.getString(3));
//            tot24.setTid(tid);
//            tot24.setDebitacctno(agentfloatacc);
//
//            final String t24Ip = SwitchFN.getParam_By_Name(db, "T24_IP");
//            final String t24Port = SwitchFN.getParam_By_Name(db, "T24_PORT");
//
//            T24HandlerProcessor.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);
//
//            T24HandlerProcessor.updateT24TransactionDTO(tot24);
//
//            System.out.println(".Gateway ref.. " + txnref + " txn queued for t24 posting !!");
//
//            String accname = tot24.getCustomerName() == null ? "" : tot24.getCustomerName();
//            String errorMessage =
//                    tot24.getT24failnarration() == null ? "" : tot24.getT24failnarration();
//            if (errorMessage.isEmpty()) {
//
//                if (tot24.getT24responsecode().equals("3")) {
//                    isomsg.set(60, "Transaction failed due to timeout");
//                    isomsg.set(61, "");
//                    isomsg.set(39, "098");
//                } else {
//                    isomsg.set(
//                            60,
//                            meterNo
//                                    + "#"
//                                    + phone
//                                    + "#"
//                                    + accname
//                                    + "#"
//                                    + customerAddress
//                                    + "#"
//                                    + tot24.getTokenNo()
//                                    + "#"
//                                    + tot24.getUnitsKw()
//                                    + "#");
//                    isomsg.set(61, tot24.getT24reference());
//                    isomsg.set(39, "000");
//
//                    elecTxnLogs.setGateway_t24postingstatus("1");
//                    elecTxnLogs.setToken_no(tot24.getTokenNo());
//                    insertEuclElecTxnLogs(elecTxnLogs, db);
//                }
//            } else {
//                isomsg.set(60, tot24.getT24failnarration().replace("\"", ""));
//                isomsg.set(39, "135");
//
//                elecTxnLogs.setGateway_t24postingstatus("0");
//            }
//            tot24.setT24reference(tot24.getT24reference());
//            SwitchFN.saveCardLessTransactionToAllTransactionTable(tot24, isomsg, "EUCL ELECTRICITY");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return isomsg;

        return null;

    }
}
