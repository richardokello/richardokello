package co.ke.tracom.bprgateway.web.academicbridge.services;
import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.BillPaymentResponse;
import co.ke.tracom.bprgateway.web.academicbridge.data.studentdetails.GetStudentDetailsResponse;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.Data;
import co.ke.tracom.bprgateway.web.switchparameters.XSwitchParameterService;
import co.ke.tracom.bprgateway.web.t24communication.services.T24Channel;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.util.TransactionISO8583ProcessingCode;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.CustomerProfileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public CustomerProfileResponse validateStudentId(String billNumber){
        System.out.println("bill number "+billNumber);
        CustomerProfileResponse student = new CustomerProfileResponse();
        String sendMoneyOFSMsg = "0000AENQUIRY.SELECT,,INPUTT/123123/RW0010400,BPR.ACB.GET.DET.AGB,BILL.NO:EQ=1001190067-1";
       // String sendMoneyOFSMsg = "0000AFUNDS.TRANSFER,BPR.ACB.PAY.AGB/I/PROCESS,INPUTT/123123/RW0010461,,TRANSACTION.TYPE::=ACAB,DEBIT.ACCT.NO::=593412948060277,DEBIT.CURRENCY::=RWF,ORDERING.BANK::=BNK,CREDIT.ACCT.NO::=408430683210261,CREDIT.CURRENCY::=RWF,CREDIT.AMOUNT::=2000,BPR.SENDER.NAME::=TINASHE TEST,MOBILE.NO::=0789379839,AB.SCHOOL.ID::=1614240687,AB.SCHL.NAME::=DEMO SCHOOL,AB.STU.NAME::=GABRIEL IMANIKUZWE,AB.BILL.NO::=1001190067-1";
       // String sendMoneyOFSMsg = bootstrapAcademicBridgeGetDetailsOFSMsg(billNumber);
        System.out.println("OFS : "+sendMoneyOFSMsg);
       // String sendMoneyOFSMsg = billNumber;
        String tot24str = String.format("%04d", sendMoneyOFSMsg.length()) + sendMoneyOFSMsg;
        Data agentAuthData = new Data();
        String transactionRRN = RRNGenerator.getInstance("SM").getRRN();
        //agentAuthData.setAccountNumber("1236544");
        T24TXNQueue tot24 = prepareT24Transaction(transactionRRN,
                agentAuthData,
                "1452365214",
                tot24str, "12369854","1200");

        System.out.println("Data to be sent is: "+tot24);
        System.out.println("RRN is : "+transactionRRN);
        final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue(T24_IP);
        final String t24Port = xSwitchParameterService.fetchXSwitchParamValue(T24_PORT);
        /*final String t24Ip = "41.215.130.247";
        final String t24Port = "7002";*/
        System.out.println("IP an Port : "+t24Ip + "  "+t24Port);
        student = t24Channel.processAcademicBridgeToT24(t24Ip, Integer.parseInt(t24Port), tot24);
        //transactionService.updateT24TransactionDTO(tot24);

        System.err.printf(
                "Send Money Charges Request : Transaction %s has been queued for T24 Processing. %n",
                t24Ip);

        System.out.println("response code is : "+tot24.getT24responsecode());

        if (tot24.getT24responsecode().equalsIgnoreCase("1")) {


            //return extractChargesFromResponse(validationReferenceNo, tot24);
            return student;

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


    public GetStudentDetailsResponse extractBillValidationMessage(){
        GetStudentDetailsResponse student = new GetStudentDetailsResponse();

        String respone = "0303,Y.SCHOOL.ID::SCHOOL ID/Y.SCHOOL.NAME::SCHOOL NAME/Y.STUDENT.NAME::STUDENT NAME/Y.STU.REG.NO::STUDENT REGNO/Y.PAY.TYPE::PAY TYPE/Y.SCHOOL.AC::SCHOOL ACCOUNT,\"45             \"\t\"Demo school              \"\t\"Gabriel  Imanikuzwe                          \"\t\"1001190067    \"\t\"          \"\t\"40810263810194      \"\n";
        System.out.println(respone);

        String [] res = respone.split(",");
        System.out.println(res[0]);

        respone = res[1];
        System.out.println("After substring "+respone);

        String [] data = res[2].substring(1,res[2].length()-1).split("\"");

        System.out.println(res[2]);


        student.setStudent_name(data[5]);//
        student.setStudent_reg_number(data[7]);//
        student.setSchool_account_number(data[11]);//
        student.setSchool_name(data[3]);
        student.setSchool_ide(Integer.parseInt(data[1].trim()));

         return student;
    }

    public void extractbridgePaymentResponse(){
        String response = "FT19193YB12T//1,TRANSACTION.TYPE:1:1=ACAB,DEBIT.ACCT.NO:1:1=593412948060277,CURRENCY.MKT.DR:1:1=1,DEBIT.CURRENCY:1:1=RWF,DEBIT.VALUE.DATE:1:1=20190712,CREDIT.THEIR.REF:1:1=4f5ed910-80a6-458c-8abe-6a29bc3cc7cf1618507223,CREDIT.ACCT.NO:1:1=408430683210261,CURRENCY.MKT.CR:1:1=1,CREDIT.CURRENCY:1:1=USD,CREDIT.AMOUNT:1:1=2000.00,CREDIT.VALUE.DATE:1:1=20190712,TREASURY.RATE:1:1=911.25,PROCESSING.DATE:1:1=20190712,ORDERING.BANK:1:1=BNK,PAYMENT.DETAILS:1:1=ACADEMIC BRIDGE TEST,CHARGES.ACCT.NO:1:1=593412948060277,CHARGE.COM.DISPLAY:1:1=NO,COMMISSION.CODE:1:1=DEBIT PLUS CHARGES,COMMISSION.TYPE:1:1=ACBOURFEE,COMMISSION.TYPE:2:1=ACBTHRFEE,COMMISSION.AMT:1:1=RWF150,COMMISSION.AMT:2:1=RWF150,CHARGE.CODE:1:1=DEBIT PLUS CHARGES,BASE.CURRENCY:1:1=USD,PROFIT.CENTRE.DEPT:1:1=593,RETURN.TO.DEPT:1:1=NO,FED.FUNDS:1:1=NO,POSITION.TYPE:1:1=TR,BPR.SENDER.NAME:1:1=TINASHE TEST,MOBILE.NO:1:1=789379839,CHANNEL:1:1=OTH,BILLER.ID:1:1=ACBRIDGEBP,AB.SCHOOL.ID:1:1=1614240687,AB.SCHL.NAME:1:1=DEMO SCHOOL,AB.STU.NAME:1:1=GABRIEL IMANIKUZWE,AB.BILL.NO:1:1=1001190067-1,AMOUNT.DEBITED:1:1=RWF1822500,AMOUNT.CREDITED:1:1=USD2000.00,TOTAL.CHARGE.AMT:1:1=RWF300,CUSTOMER.RATE:1:1=911.25,DELIVERY.OUTREF:1:1=D20210415058726944600-900.50.1      CHARGE ADVICE,DELIVERY.OUTREF:2:1=D20210415058726944601-900.2.1       DEBIT ADVICE,DELIVERY.OUTREF:3:1=D20210415058726944602-910.2.1       CREDIT ADVICE,CREDIT.COMP.CODE:1:1=RW0010408,DEBIT.COMP.CODE:1:1=RW0010593,LOC.AMT.DEBITED:1:1=1822500,LOC.AMT.CREDITED:1:1=1808630,LOCAL.CHARGE.AMT:1:1=300,LOC.POS.CHGS.AMT:1:1=300,MKTG.EXCH.PROFIT:1:1=13870,CUST.GROUP.LEVEL:1:1=99,DEBIT.CUSTOMER:1:1=4129480,CREDIT.CUSTOMER:1:1=4306832,DR.ADVICE.REQD.Y.N:1:1=Y,CR.ADVICE.REQD.Y.N:1:1=Y,CHARGED.CUSTOMER:1:1=4306832,TOT.REC.COMM:1:1=0,TOT.REC.COMM.LCL:1:1=0,TOT.REC.CHG:1:1=0,TOT.REC.CHG.LCL:1:1=0,RATE.FIXING:1:1=NO,TOT.REC.CHG.CRCCY:1:1=0,TOT.SND.CHG.CRCCY:1:1=0.33,AUTH.DATE:1:1=20190712,ROUND.TYPE:1:1=NATURAL,STMT.NOS:1:1=194640587269442.00,STMT.NOS:2:1=1-4,STMT.NOS:3:1=1-2,STMT.NOS:4:1=RW0010408,STMT.NOS:5:1=194640587269442.01,STMT.NOS:6:1=1-2,STMT.NOS:7:1=RW0010593,STMT.NOS:8:1=194640587269442.02,STMT.NOS:9:1=1-4,STMT.NOS:10:1=RW0010400,STMT.NOS:11:1=194640587269442.03,STMT.NOS:12:1=1-6,OVERRIDE:1:1=RATE.REQ}RATE REQUIRED - EXCEEDS LIMIT,CURR.NO:1:1=1,INPUTTER:1:1=5872_INPUTTER__OFS_BPRMB1,DATE.TIME:1:1=2104151917,AUTHORISER:1:1=5872_INPUTTER_OFS_BPRMB1,CO.CODE:1:1=RW0010461,DEPT.CODE:1:1=400";

        System.out.println(response);

        String [] res = response.split("//");
        System.out.println(res[0]);
        System.out.println("############################################################");
        System.out.println("T24 reference : "+res[1]);
        System.out.println("***********************************************************");
        System.out.println("############################################################");
        String [] data = res[1].replace(":1:1","").replace(".","_").split(",");
        // String data = res[1].replace(":2:1","");
        System.out.println("Transaction status : "+data[0]);
        System.out.println(data[1]);
        System.out.println(data[2]);
        System.out.println("***********************************************************");
        System.out.println("############################################################");
        if(data[0].equalsIgnoreCase("1") ){
            // System.out.println(data.getClass());
            HashMap<String, String> t24data = new HashMap<>();
            int ii = 0;
            for (String column : data) {
                if (ii > 0) {
                   /*// String[] splitdata = data[1].split("=");
                    String key = data[0].replaceAll("\\.", "");
                    t24data.put(key, data[1]);*/
                    System.out.println(column);
                    t24data.put(column.split("=")[0],column.split("=")[1]);

                }
                ii++;
            }

            System.out.println("############################################################");
            System.out.println(t24data.get("TRANSACTION_TYPE"));
            System.out.println(t24data.get("DEBIT_CURRENCY"));
        }
        //  System.out.println(data[0]);
        System.out.println("***********************************************************");
    }

    public BillPaymentResponse academicBridgePayment(String billNumber){
        BillPaymentResponse student = new BillPaymentResponse();
        //String sendMoneyOFSMsg = "0000AENQUIRY.SELECT,,INPUTT/123123/RW0010400,BPR.ACB.GET.DET.AGB,BILL.NO:EQ=1001190067-1";
        //String sendMoneyOFSMsg = bootstrapAcademicBridgeGetDetailsOFSMsg(billNumber);
        String sendMoneyOFSMsg = billNumber;
        String tot24str = String.format("%04d", sendMoneyOFSMsg.length()) + sendMoneyOFSMsg;
        Data agentAuthData = new Data();
        String transactionRRN = RRNGenerator.getInstance("SM").getRRN();
        //agentAuthData.setAccountNumber("1236544");
        T24TXNQueue tot24 = prepareT24Transaction(transactionRRN,
                agentAuthData,
                "1452365214",
                tot24str, "12369854","1300");

        System.out.println("Data to be sent is: "+tot24);
        System.out.println("RRN is : "+transactionRRN);
       /* final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue(T24_IP);
        final String t24Port = xSwitchParameterService.fetchXSwitchParamValue(T24_PORT);*/
        final String t24Ip = "41.215.130.247";
        final String t24Port = "7002";
        System.out.println("IP an Port : "+t24Ip + "  "+t24Port);
        student = t24Channel.processAcademicBridgePaymentToT24(t24Ip, Integer.parseInt(t24Port), tot24);
        //transactionService.updateT24TransactionDTO(tot24);

        System.err.printf(
                "Send Money Charges Request : Transaction %s has been queued for T24 Processing. %n",
                t24Ip);

        System.out.println("response code is : "+tot24.getT24responsecode());

        if (tot24.getT24responsecode().equalsIgnoreCase("1")) {
            System.out.println("request successful >>>>");


            //return extractChargesFromResponse(validationReferenceNo, tot24);
            return student;

        }

        System.out.println("request not successful >>>>");
        return student;//kelvin to do fix this bug
    }

    public void extractResponse(){
        String response = "FT19193YB12T//1,TRANSACTION.TYPE:1:1=ACAB,DEBIT.ACCT.NO:1:1=593412948060277,CURRENCY.MKT.DR:1:1=1,DEBIT.CURRENCY:1:1=RWF,DEBIT.VALUE.DATE:1:1=20190712,CREDIT.THEIR.REF:1:1=4f5ed910-80a6-458c-8abe-6a29bc3cc7cf1618507223,CREDIT.ACCT.NO:1:1=408430683210261,CURRENCY.MKT.CR:1:1=1,CREDIT.CURRENCY:1:1=USD,CREDIT.AMOUNT:1:1=2000.00,CREDIT.VALUE.DATE:1:1=20190712,TREASURY.RATE:1:1=911.25,PROCESSING.DATE:1:1=20190712,ORDERING.BANK:1:1=BNK,PAYMENT.DETAILS:1:1=ACADEMIC BRIDGE TEST,CHARGES.ACCT.NO:1:1=593412948060277,CHARGE.COM.DISPLAY:1:1=NO,COMMISSION.CODE:1:1=DEBIT PLUS CHARGES,COMMISSION.TYPE:1:1=ACBOURFEE,COMMISSION.TYPE:2:1=ACBTHRFEE,COMMISSION.AMT:1:1=RWF150,COMMISSION.AMT:2:1=RWF150,CHARGE.CODE:1:1=DEBIT PLUS CHARGES,BASE.CURRENCY:1:1=USD,PROFIT.CENTRE.DEPT:1:1=593,RETURN.TO.DEPT:1:1=NO,FED.FUNDS:1:1=NO,POSITION.TYPE:1:1=TR,BPR.SENDER.NAME:1:1=TINASHE TEST,MOBILE.NO:1:1=789379839,CHANNEL:1:1=OTH,BILLER.ID:1:1=ACBRIDGEBP,AB.SCHOOL.ID:1:1=1614240687,AB.SCHL.NAME:1:1=DEMO SCHOOL,AB.STU.NAME:1:1=GABRIEL IMANIKUZWE,AB.BILL.NO:1:1=1001190067-1,AMOUNT.DEBITED:1:1=RWF1822500,AMOUNT.CREDITED:1:1=USD2000.00,TOTAL.CHARGE.AMT:1:1=RWF300,CUSTOMER.RATE:1:1=911.25,DELIVERY.OUTREF:1:1=D20210415058726944600-900.50.1      CHARGE ADVICE,DELIVERY.OUTREF:2:1=D20210415058726944601-900.2.1       DEBIT ADVICE,DELIVERY.OUTREF:3:1=D20210415058726944602-910.2.1       CREDIT ADVICE,CREDIT.COMP.CODE:1:1=RW0010408,DEBIT.COMP.CODE:1:1=RW0010593,LOC.AMT.DEBITED:1:1=1822500,LOC.AMT.CREDITED:1:1=1808630,LOCAL.CHARGE.AMT:1:1=300,LOC.POS.CHGS.AMT:1:1=300,MKTG.EXCH.PROFIT:1:1=13870,CUST.GROUP.LEVEL:1:1=99,DEBIT.CUSTOMER:1:1=4129480,CREDIT.CUSTOMER:1:1=4306832,DR.ADVICE.REQD.Y.N:1:1=Y,CR.ADVICE.REQD.Y.N:1:1=Y,CHARGED.CUSTOMER:1:1=4306832,TOT.REC.COMM:1:1=0,TOT.REC.COMM.LCL:1:1=0,TOT.REC.CHG:1:1=0,TOT.REC.CHG.LCL:1:1=0,RATE.FIXING:1:1=NO,TOT.REC.CHG.CRCCY:1:1=0,TOT.SND.CHG.CRCCY:1:1=0.33,AUTH.DATE:1:1=20190712,ROUND.TYPE:1:1=NATURAL,STMT.NOS:1:1=194640587269442.00,STMT.NOS:2:1=1-4,STMT.NOS:3:1=1-2,STMT.NOS:4:1=RW0010408,STMT.NOS:5:1=194640587269442.01,STMT.NOS:6:1=1-2,STMT.NOS:7:1=RW0010593,STMT.NOS:8:1=194640587269442.02,STMT.NOS:9:1=1-4,STMT.NOS:10:1=RW0010400,STMT.NOS:11:1=194640587269442.03,STMT.NOS:12:1=1-6,OVERRIDE:1:1=RATE.REQ}RATE REQUIRED - EXCEEDS LIMIT,CURR.NO:1:1=1,INPUTTER:1:1=5872_INPUTTER__OFS_BPRMB1,DATE.TIME:1:1=2104151917,AUTHORISER:1:1=5872_INPUTTER_OFS_BPRMB1,CO.CODE:1:1=RW0010461,DEPT.CODE:1:1=400";

        System.out.println(response);

        String [] res = response.split("//");
        System.out.println(res[0]);
        System.out.println("############################################################");
        System.out.println("T24 reference : "+res[1]);
        System.out.println("***********************************************************");
        System.out.println("############################################################");
        String [] data = res[1].replace(":1:1","").replace(".","_").split(",");
        // String data = res[1].replace(":2:1","");
        System.out.println("Transaction status : "+data[0]);
        System.out.println(data[1]);
        System.out.println(data[2]);
        System.out.println("***********************************************************");
        System.out.println("############################################################");
        if(data[0].equalsIgnoreCase("1") ){
            // System.out.println(data.getClass());
            HashMap<String, String> t24data = new HashMap<>();
            int ii = 0;
            for (String column : data) {
                if (ii > 0) {
                   /*// String[] splitdata = data[1].split("=");
                    String key = data[0].replaceAll("\\.", "");
                    t24data.put(key, data[1]);*/
                    System.out.println(column);
                    t24data.put(column.split("=")[0],column.split("=")[1]);

                }
                ii++;
            }

            System.out.println("############################################################");
            System.out.println(t24data.get("TRANSACTION_TYPE"));
            System.out.println(t24data.get("DEBIT_CURRENCY"));
        }
        //  System.out.println(data[0]);
        System.out.println("***********************************************************");
    }

    private Map extractPaymentData(List data){
        HashMap<String,String> paymentData = new HashMap<>();

        return  paymentData;
    }



}

