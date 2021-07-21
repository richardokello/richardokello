package co.ke.tracom.bprgateway.web.sms.services;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.sms.dto.SMSRequest;
import co.ke.tracom.bprgateway.web.sms.dto.SMSResponse;
import co.ke.tracom.bprgateway.web.switchparameters.XSwitchParameterService;
import co.ke.tracom.bprgateway.web.t24communication.services.T24Channel;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_PASSWORD;
import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_USERNAME;

@Service
@Slf4j
@RequiredArgsConstructor
public class SMSService {
    private final XSwitchParameterService xSwitchParameterService;
    private final T24Channel t24Channel;

    public SMSResponse sendSMS(SMSRequest request) {
        final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue("T24_IP");
        final String t24Port = xSwitchParameterService.fetchXSwitchParamValue("T24_PORT");

        String tot24str = "0000AENQUIRY.SELECT,," + MASKED_T24_USERNAME + "/" + MASKED_T24_PASSWORD + "/RW0010400,BPR.AGB.PROCESS.SMS,SMS.MSG:EQ=" + request.getMessage() + ",CUS.NO:EQ=" + request.getRecipient() + ",MARKER:EQ=" + request.getSMSFunction();
        tot24str = String.format("%04d", tot24str.length()) + tot24str;

        return  packageRequest(t24Ip, t24Port, tot24str);
    }

    private SMSResponse packageRequest(String t24Ip, String t24Port, String tot24str) {
        T24TXNQueue t24TXNQueue = new T24TXNQueue();
        t24TXNQueue.setRequestleg(tot24str.trim());
        t24TXNQueue.setStarttime(System.currentTimeMillis());
        t24TXNQueue.setTxnchannel("PC");
        String transactionReferenceNo = RRNGenerator.getInstance("AD").getRRN();
        t24TXNQueue.setGatewayref(transactionReferenceNo);
        t24TXNQueue.setPostedstatus("0");
        t24TXNQueue.setProcode("500001");
        String tid = "PC";
        t24TXNQueue.setTid(tid);

        t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), t24TXNQueue);
        SMSResponse response = new SMSResponse();

        if (t24TXNQueue.getT24responsecode().equals("1")) {
            response.setStatus("00");
            response.setMessage(t24TXNQueue.getT24failnarration());
        } else {
            response.setStatus("01");
            response.setMessage(t24TXNQueue.getT24failnarration());
        }
        return response;
    }

}
