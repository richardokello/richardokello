package co.ke.tracom.bprgateway.t24OFS;

import co.ke.tracom.bprgateway.BPRGatewayGen2Application;
import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.switchparameters.XSwitchParameterService;
import co.ke.tracom.bprgateway.web.t24communication.services.T24Channel;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = BPRGatewayGen2Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("bpr")
@RunWith(SpringRunner.class)
public class SMSSender {
    public static final String MASKED_T24_USERNAME = "########U";
    public static final String MASKED_T24_PASSWORD = "########A";

    @Autowired
    private XSwitchParameterService xSwitchParameterService;
    @Autowired
    private T24Channel t24Channel;


    @Test
    public void simulateSendingSMS() {
        final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue("T24_IP");        final String t24Port = xSwitchParameterService.fetchXSwitchParamValue("T24_PORT");

       // Sender String tot24str = "0000AENQUIRY.SELECT,,"+MASKED_T24_USERNAME+"/"+MASKED_T24_PASSWORD+"/RW0010400,BPR.AGB.PROCESS.SMS,SMS.MSG:EQ=1000/250781467638/484778/913235267600,CUS.NO:EQ=250781467638,MARKER:EQ=SENDER";
        String tot24str = "0000AENQUIRY.SELECT,,"+MASKED_T24_USERNAME+"/"+MASKED_T24_PASSWORD+"/RW0010400,BPR.AGB.PROCESS.SMS,SMS.MSG:EQ=1000/250781467638/484778/913235267600,CUS.NO:EQ=250781467638,MARKER:EQ=RECEIVER";
        tot24str = String.format("%04d", tot24str.length()) + tot24str;

        String t24TXNQueueResponseLeg = packageRequest(t24Ip, t24Port, tot24str);

        System.out.println("response leg = " + t24TXNQueueResponseLeg);
        Assert.assertNotNull(t24TXNQueueResponseLeg);
    }

    @Test
    public void simulateBalanceInquiry() {
        final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue("T24_IP");        final String t24Port = xSwitchParameterService.fetchXSwitchParamValue("T24_PORT");

        String agentAccount = "401428327210176";

        String tot24str = String.format(
                "0000AENQUIRY.SELECT,,%s/%s,ECL.ENQUIRY.DETS,ID:EQ=%s,TRANS.TYPE.ID:EQ=CUSTDET",
                MASKED_T24_USERNAME, MASKED_T24_PASSWORD, agentAccount);

         tot24str = String.format("%04d", tot24str.length()) + tot24str;

        String t24TXNQueueResponseLeg = packageRequest(t24Ip, t24Port, tot24str);

        System.out.println("response leg = " + t24TXNQueueResponseLeg);
        Assert.assertNotNull(t24TXNQueueResponseLeg);

    }

    private String packageRequest(String t24Ip, String t24Port, String tot24str) {
        T24TXNQueue t24TXNQueue = new T24TXNQueue();
        t24TXNQueue.setRequestleg(tot24str.trim());
        t24TXNQueue.setStarttime(System.currentTimeMillis());
        t24TXNQueue.setTxnchannel("PC");
        String transactionReferenceNo = RRNGenerator.getInstance("AD").getRRN();
        t24TXNQueue.setGatewayref(transactionReferenceNo);
        t24TXNQueue.setPostedstatus("0");
        t24TXNQueue.setProcode("500000");
        String tid = "PC";
        t24TXNQueue.setTid(tid);

        t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), t24TXNQueue);
        return t24TXNQueue.getResponseleg();
    }



}
