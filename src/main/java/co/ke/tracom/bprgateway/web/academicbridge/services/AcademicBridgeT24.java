package co.ke.tracom.bprgateway.web.academicbridge.services;
import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.BillPaymentResponse;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.Credentials;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.Data;
import co.ke.tracom.bprgateway.web.exceptions.custom.InvalidAgentCredentialsException;
import co.ke.tracom.bprgateway.web.switchparameters.XSwitchParameterService;
import co.ke.tracom.bprgateway.web.t24communication.services.T24Channel;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import co.ke.tracom.bprgateway.web.util.TransactionISO8583ProcessingCode;
import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.CustomerProfileResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.T24_IP;
import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.T24_PORT;

@Service
@Slf4j
public class AcademicBridgeT24 {

    private final XSwitchParameterService xSwitchParameterService;
    private final TransactionService transactionService;
    private final T24Channel t24Channel;
    public static final String MASKED_T24_USERNAME = "########U";
    public static final String MASKED_T24_PASSWORD = "########A";
    private final BaseServiceProcessor baseServiceProcessor;

    public AcademicBridgeT24(XSwitchParameterService xSwitchParameterService, TransactionService transactionService, T24Channel t24Channel, BaseServiceProcessor baseServiceProcessor) {
        this.xSwitchParameterService = xSwitchParameterService;
        this.transactionService = transactionService;
        this.t24Channel = t24Channel;
        this.baseServiceProcessor = baseServiceProcessor;
    }

    @SneakyThrows
    public CustomerProfileResponse validateStudentId(String billNumber)  {
       // System.out.println("bill number "+billNumber);

        CustomerProfileResponse student = new CustomerProfileResponse();
      //  String sendMoneyOFSMsg = "0000AENQUIRY.SELECT,,INPUTT/123123/RW0010400,BPR.ACB.GET.DET.AGB,BILL.NO:EQ=1001190067-1";
       // String sendMoneyOFSMsg = "0000AFUNDS.TRANSFER,BPR.ACB.PAY.AGB/I/PROCESS,INPUTT/123123/RW0010461,,TRANSACTION.TYPE::=ACAB,DEBIT.ACCT.NO::=593412948060277,DEBIT.CURRENCY::=RWF,ORDERING.BANK::=BNK,CREDIT.ACCT.NO::=408430683210261,CREDIT.CURRENCY::=RWF,CREDIT.AMOUNT::=2000,BPR.SENDER.NAME::=TINASHE TEST,MOBILE.NO::=0789379839,AB.SCHOOL.ID::=1614240687,AB.SCHL.NAME::=DEMO SCHOOL,AB.STU.NAME::=GABRIEL IMANIKUZWE,AB.BILL.NO::=1001190067-1";
        String sendMoneyOFSMsg = bootstrapAcademicBridgeGetDetailsOFSMsg(billNumber);
       // System.out.println("OFS : "+sendMoneyOFSMsg);

        String tot24str = String.format("%04d", sendMoneyOFSMsg.length()) + sendMoneyOFSMsg;
        Data agentAuthData = new Data();
        String transactionRRN = RRNGenerator.getInstance("SM").getRRN();
        //agentAuthData.setAccountNumber("1236544");
        T24TXNQueue tot24 = prepareT24Transaction(transactionRRN,
                agentAuthData,
                "1452365214",
                tot24str, "12369854","1200");


        log.info("Data to be sent is: {}",tot24);

        log.info("RRN is: {}",transactionRRN);
        final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue(T24_IP);
        final String t24Port = xSwitchParameterService.fetchXSwitchParamValue(T24_PORT);

        log.info("Ip {} and Port {}",t24Ip,t24Port);
        student = t24Channel.processAcademicBridgeToT24(t24Ip, Integer.parseInt(t24Port), tot24);
        transactionService.updateT24TransactionDTO(tot24);

        System.err.printf(
                "Send Money Charges Request : Transaction %s has been queued for T24 Processing. %n",
                t24Ip);


        log.info("response code is : {}",tot24.getT24responsecode());

        if (tot24.getT24responsecode().equalsIgnoreCase("1")) {


            //return extractChargesFromResponse(validationReferenceNo, tot24);
             return student;

        }else {
           // student=null;
        }

        return student;//kelvin to do fix this bug
    }

    private T24TXNQueue prepareT24Transaction(String transactionRRN, Data agentAuthData, String configuredSendMoneySuspenseAccount, String tot24str, String tid,String mti) {
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
        tot24.setTxnmti(mti);
        tot24.setT24responsecode("1");
        return tot24;
    }

    public String bootstrapAcademicBridgePaymentOFSMsg(String debitAcc, String creditAcc, double amount, String sender,
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
                + "CHANNEL::=OTH,AB.SCHOOL.ID::="
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
       // return "0000AENQUIRY.SELECT,,TRUSER1/123456/RW0010400,BPR.ACB.GET.DET.AGB,BILL.NO:EQ="+billNumber;
    }




    public BillPaymentResponse academicBridgePayment(String billNumber){
        BillPaymentResponse student = new BillPaymentResponse();
        String sendMoneyOFSMsg = billNumber;
        String tot24str = String.format("%04d", sendMoneyOFSMsg.length()) + sendMoneyOFSMsg;
        Data agentAuthData = new Data();
        String transactionRRN = RRNGenerator.getInstance("SM").getRRN();

        T24TXNQueue tot24 = prepareT24Transaction(transactionRRN,
                agentAuthData,
                "1452365214",
                tot24str, "12369854","1300");


        log.info("Data to be sent is: {}",tot24);
        log.info("RRN is: {}",transactionRRN);

        final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue(T24_IP);
        final String t24Port = xSwitchParameterService.fetchXSwitchParamValue(T24_PORT);

        log.info("Ip {} and  port {}",t24Ip,t24Port);
        student = t24Channel.processAcademicBridgePaymentToT24(t24Ip, Integer.parseInt(t24Port), tot24);
        //transactionService.updateT24TransactionDTO(tot24);

        System.err.printf(
                "Send Money Charges Request : Transaction %s has been queued for T24 Processing. %n",
                t24Ip);


        log.info("response code is : {}",tot24.getT24responsecode());

        if (tot24.getT24responsecode().equalsIgnoreCase("1")) {

            log.info("request successful >>>>");


            //return extractChargesFromResponse(validationReferenceNo, tot24);
            return student;

        }


        log.info("request not successful >>>>");
        return student;//kelvin to do fix this bug
    }




}

