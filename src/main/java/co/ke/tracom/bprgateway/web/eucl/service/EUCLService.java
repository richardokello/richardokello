package co.ke.tracom.bprgateway.web.eucl.service;

import co.ke.tracom.bprgateway.web.eucl.dto.request.MeterNoValidation;
import co.ke.tracom.bprgateway.web.eucl.dto.response.MeterNoData;
import co.ke.tracom.bprgateway.web.eucl.dto.response.MeterNoValidationResponse;
import co.ke.tracom.bprgateway.web.switchparameters.entities.XSwitchParameter;
import co.ke.tracom.bprgateway.web.switchparameters.repository.XSwitchParameterRepository;
import co.ke.tracom.bprgateway.web.t24communication.services.T24Channel;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_PASSWORD;
import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_USERNAME;

@Slf4j
@RequiredArgsConstructor
@Service
public class EUCLService {
    private final T24Channel t24Channel;
    private final TransactionService transactionService;

    private final XSwitchParameterRepository xSwitchParameterRepository;

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


            final String t24Ip = xSwitchParameterRepository.findByParamName("T24_IP").get().getParamValue();
            final String t24Port = xSwitchParameterRepository.findByParamName("T24_PORT").get().getParamValue();

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
}
