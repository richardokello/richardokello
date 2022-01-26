package co.ke.tracom.bprgateway.web.academicbridge.services;
import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.Data;
import co.ke.tracom.bprgateway.web.switchparameters.XSwitchParameterService;
import co.ke.tracom.bprgateway.web.t24communication.services.T24Channel;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.util.TransactionISO8583ProcessingCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.T24_IP;
import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.T24_PORT;

@Service
@Slf4j
public class AcademicBridgeT24 {

    private final XSwitchParameterService xSwitchParameterService;
    private final T24Channel t24Channel;
    public static final String MASKED_T24_USERNAME = "########U";
    public static final String MASKED_T24_PASSWORD = "########A";

    public AcademicBridgeT24(XSwitchParameterService xSwitchParameterService, T24Channel t24Channel) {
        this.xSwitchParameterService = xSwitchParameterService;
        this.t24Channel = t24Channel;
    }

    public String validateStudentId(String billNumber){
       // String sendMoneyOFSMsg = "0000AENQUIRY.SELECT,,INPUTT/123123/RW0010400,BPR.ACB.GET.DET.AGB,BILL.NO:EQ=1001190067-1";
        String sendMoneyOFSMsg = bootstrapAcademicBridgeGetDetailsOFSMsg(billNumber);
        String tot24str = String.format("%04d", sendMoneyOFSMsg.length()) + sendMoneyOFSMsg;
        Data agentAuthData = new Data();
        String transactionRRN = RRNGenerator.getInstance("SM").getRRN();
        //agentAuthData.setAccountNumber("1236544");
        T24TXNQueue tot24 = prepareT24Transaction(transactionRRN,
                agentAuthData,
                "1452365214",
                tot24str, "12369854");

        System.out.println("RRN is : "+transactionRRN);
        final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue(T24_IP);
        final String t24Port = xSwitchParameterService.fetchXSwitchParamValue(T24_PORT);
        System.out.println("IP an Port : "+t24Ip + "  "+t24Port);
        t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);
        //transactionService.updateT24TransactionDTO(tot24);

        System.err.printf(
                "Send Money Charges Request : Transaction %s has been queued for T24 Processing. %n",
                t24Ip);


        if (tot24.getT24responsecode().equalsIgnoreCase("1")) {


            //return extractChargesFromResponse(validationReferenceNo, tot24);
            return "success";

        }

        return "failed";
    }

    private T24TXNQueue prepareT24Transaction(String transactionRRN, Data agentAuthData, String configuredSendMoneySuspenseAccount, String tot24str, String tid) {
        T24TXNQueue tot24 = new T24TXNQueue();
        tot24.setTid(tid);//needed in validation?
        tot24.setRequestleg(tot24str);//our custom request
        tot24.setStarttime(System.currentTimeMillis());

        String channel = "1510";//what channel
        tot24.setTxnchannel(channel);
        tot24.setGatewayref(transactionRRN);
        tot24.setProcode(TransactionISO8583ProcessingCode.EUCL_BILL.getCode());//send money and validate
        tot24.setDebitacctno(agentAuthData.getAccountNumber());//validation any nned for this
        tot24.setCreditacctno(configuredSendMoneySuspenseAccount);// and this
        tot24.setTxnmti("1100");
        return tot24;
    }

    private String bootstrapAcademicBridgePaymentOFSMsg(String debitAcc, String creditAcc,double amount, String sender,
                                                        String phone, String schoolId, String schoolName, String studentName,
                                                        String billNumber) {
        return "0000AFUNDS.TRANSFER,BPR.ACB.PAY.AGB/I/PROCESS,INPUTT/123123/RW0010461,,TRANSACTION.TYPE::=ACAB,DEBIT.ACCT.NO::="
                + debitAcc
                + ","
                + "DEBIT.CURRENCY::=RWF,"

                + "ORDERING.BANK::=BNK,CREDIT.ACCT.NO::="
                + creditAcc
                + ","
                + "CREDIT.CURRENCY::=RWF,CREDIT.AMOUNT::="
                + amount  //check for extra decimal

                + ","
                + "BPR.SENDER.NAME::="
                + sender
                + ","
                + "MOBILE.NO::="
                + phone
                + ","
                + "CHANNEL::=AGB,AB.SCHOOL.ID::="
                + schoolId
                + ","
                + "AB.SCHL.NAME::="
                + schoolName

                + ","
                + "AB.STU.NAME::="
                + studentName
                + ","
                + "AB.BILL.NO::="
                + billNumber;
    }


    private String bootstrapAcademicBridgeGetDetailsOFSMsg(String billNumber) {
        return "0000AENQUIRY.SELECT,,INPUTT/123123/RW0010400,BPR.ACB.GET.DET.AGB,BILL.NO:EQ="+ billNumber;
    }
}

