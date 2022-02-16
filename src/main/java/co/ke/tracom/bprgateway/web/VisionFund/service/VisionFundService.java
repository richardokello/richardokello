package co.ke.tracom.bprgateway.web.VisionFund.service;

import co.ke.tracom.bprgateway.core.util.AppConstants;
import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.VisionFund.data.*;
import co.ke.tracom.bprgateway.web.VisionFund.data.custom.CustomVerificationRequest;
import co.ke.tracom.bprgateway.web.VisionFund.data.custom.CustomVerificationResponse;
import co.ke.tracom.bprgateway.web.VisionFund.entity.VisionFund;
import co.ke.tracom.bprgateway.web.VisionFund.repository.VisionFundRepository;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.Data;
import co.ke.tracom.bprgateway.web.switchparameters.XSwitchParameterService;
import co.ke.tracom.bprgateway.web.t24communication.services.T24Channel;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import co.ke.tracom.bprgateway.web.util.services.UtilityService;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.CustomerProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class VisionFundService {
    private final XSwitchParameterService xSwitchParameterService;
    private final UtilityService utilityService;
    private final T24Channel t24Channel;
    private final TransactionService transactionService;
    private final VisionFundRepository visionFundRepository;
    private final BaseServiceProcessor baseServiceProcessor;

    @SneakyThrows
    public BalanceEnquiryResponse getBalance(BalanceEnquiryRequest enquiryRequest) {
        AuthenticateAgentResponse authenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(
                new MerchantAuthInfo(enquiryRequest.getCredentials().getUsername(), enquiryRequest.getCredentials().getPassword()));

        if (authenticateAgentResponse.getCode() != 200) {
            return BalanceEnquiryResponse.builder()
                    .responseCode("05")
                    .responseString("Invalid credentials")
                    .build();
        }
        Data agentAuthData = authenticateAgentResponse.getData();

        String agentFloatBalanceOFS =
                String.format(
                        "0000AENQUIRY.SELECT,,%s/%s,BPR.ENQ.VFR.GET.DET,ID:EQ=%s",
                        getT24UserName(), getT24Password(), enquiryRequest.getAccountNumber());
        String tot24str = String.format("%04d", agentFloatBalanceOFS.length()) + agentFloatBalanceOFS;

        T24TXNQueue t24TXNQueue = new T24TXNQueue();
        t24TXNQueue.setRequestleg(tot24str);
        t24TXNQueue.setStarttime(System.currentTimeMillis());

        t24TXNQueue.setTxnchannel("PC");

        String rrn = RRNGenerator.getInstance("VF").getRRN();
        t24TXNQueue.setGatewayref(rrn);
        t24TXNQueue.setPostedstatus("0");
        t24TXNQueue.setProcode("500000");

        String tid = "PC";
        t24TXNQueue.setTid(tid);

        processT24Transactions(t24TXNQueue);



        if ((t24TXNQueue.getResponseleg() != null)) {

            if (t24TXNQueue.getBaladvise() == null) {

                transactionService.saveCardLessTransactionToAllTransactionTable(t24TXNQueue, enquiryRequest.getTnxType(), "0200", 0,
                        AppConstants.EXCEPTION_OCCURRED_ON_EXTERNAL_HTTP_REQUEST.value(), agentAuthData.getTid(), agentAuthData.getMid());
                return BalanceEnquiryResponse.builder()
                        .responseCode(AppConstants.EXCEPTION_OCCURRED_ON_EXTERNAL_HTTP_REQUEST.value())
                        .responseString("Transaction failed! "+t24TXNQueue.getT24failnarration())
                        .build();
            } else {
                try {
                    transactionService.saveCardLessTransactionToAllTransactionTable(t24TXNQueue, enquiryRequest.getTnxType(), "0200", 0,
                            AppConstants.TRANSACTION_SUCCESS_STANDARD.value(), agentAuthData.getTid(), agentAuthData.getMid());
                    return BalanceEnquiryResponse.builder()
                            .responseCode(AppConstants.TRANSACTION_SUCCESS_STANDARD.value())
                            .txnDateTime(DateFormat.getDateInstance().parse(t24TXNQueue.getDatetime()))
                            .availBalance(Double.parseDouble(t24TXNQueue.getBaladvise()))
                            .responseString(AppConstants.TRANSACTION_SUCCESS_STANDARD.getReasonPhrase())
                            .txnReference(t24TXNQueue.getGatewayref())
                            .build();
                } catch (ParseException e) {
                    log.error("<<<Vision Fund Balance Enquiry>>>\n{}",e.getMessage());
                }

            }
        }
        return null;
    }

    @SneakyThrows
    public AccountDepositResponse makeDeposit(AccountDepositRequest depositRequest) {
        AuthenticateAgentResponse authenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(
                new MerchantAuthInfo(depositRequest.getCredentials().getUsername(), depositRequest.getCredentials().getPassword()));

        if (authenticateAgentResponse.getCode() != 200) {
            return AccountDepositResponse.builder()
                    .responseCode("05")
                    .responseString("Invalid credentials")
                    .build();
        }
        Data agentAuthData = authenticateAgentResponse.getData();

        String bareOfs ="FUNDS.TRANSFER," +
                "BPR.VFR.DEP/I/PROCESS/1/0," +
                "%s/%s/%s,," +
                "DEBIT.ACCT.NO::=404466672910121,n" +
                "DEBIT.AMOUNT::=1100," +
                "DEBIT.CURRENCY::=RWF," +
                "TCM.REF::=003030164525," +
                "PAYMENT.DETAILS:3:= %s," +
                "PAYMENT.DETAILS:4:=PO400010 000000RW0010007,\n" +
                "BPR.ID.NUMBER::=%s," +
                "MOBILE.NO::=%s," +
                "DEBIT.CURRENCY::=%s," +
                "DEBIT.AMOUNT::=%d," +
                "BPR.ID.DOC.NO::%s";
        String ofs=String.format(bareOfs,
                getT24UserName(),getT24Password(),getDefaultBranch(),depositRequest.getTranDesc(),depositRequest.getAccountNumber(),
                depositRequest.getMobileNumber(),depositRequest.getCurrencyCode(),depositRequest.getAmount(),depositRequest.getNationalID());

        String formattedOFS = String.format("%04d", ofs.length()) + ofs;

        // create a table or function to generate T24 messages
        T24TXNQueue tot24 = new T24TXNQueue();
        tot24.setRequestleg(formattedOFS);
        tot24.setStarttime(System.currentTimeMillis());
        tot24.setTxnchannel("PC");
        String t24ref = RRNGenerator.getInstance("VF").getRRN();
        tot24.setGatewayref(t24ref);
        tot24.setPostedstatus("0");
        tot24.setProcode("460001");

        /*processT24Transactions(tot24);

        //Save the transaction
        //todo table to be created
        //VisionFund visionFund = new VisionFund();
        //visionFund.setAccountName(tot24.getAccountname());
        //visionFund.setAccountNumber(depositRequest.getAccountNumber());
        //visionFund.setAmount(depositRequest.getAmount());
        //visionFund.setMobileNumber(depositRequest.getMobileNumber());
        //visionFund.setTranDesc(depositRequest.getTranDesc());
        //visionFund.setCurrencyCode(depositRequest.getCurrencyCode());
        //visionFund.setNationalID(depositRequest.getNationalID());
        //visionFund.setReferenceNumber(tot24.getGatewayref());
        //visionFundRepository.save(visionFund);

        if (tot24.getT24responsecode() == null || tot24.getT24responsecode().equals("3")){

            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, depositRequest.getTnxType(), "0200", depositRequest.getAmount(),
                    AppConstants.EXCEPTION_OCCURRED_ON_EXTERNAL_HTTP_REQUEST.value(), agentAuthData.getTid(), agentAuthData.getMid());
            return AccountDepositResponse.builder()
                    .responseCode("05")
                    .responseString("Transaction failed! "+tot24.getT24failnarration())
                    .build();
        }else{
            try{
                transactionService.saveCardLessTransactionToAllTransactionTable(tot24, depositRequest.getTnxType(), "0200", depositRequest.getAmount(),
                        AppConstants.TRANSACTION_SUCCESS_STANDARD.value(), agentAuthData.getTid(), agentAuthData.getMid());
                return AccountDepositResponse.builder()
                        .responseCode(AppConstants.TRANSACTION_SUCCESS_STANDARD.value())
                        .responseString(AppConstants.TRANSACTION_SUCCESS_STANDARD.getReasonPhrase())
                        .availBalance(Double.parseDouble(tot24.getBaladvise()))
                        .txnReference(tot24.getGatewayref())
                        .txnDateTime(DateFormat.getInstance().parse(tot24.getDatetime()))
                        .build();
            }catch (Exception e){

                log.error("<<<Vision Fund Withdrawal Transaction>>>\n{}",e.getMessage());
            }
        }
        */
        String sampleOFS ="FT191893VRLD/TRCOM191890255539685.01/1,TRANSACTION.TYPE:1:1=ACVF,DEBIT.ACCT.NO:1:1=404466672910121,CURRENCY.MKT.DR:1:1=1,DEBIT.CURRENCY:1:1=RWF,DEBIT.VALUE.DATE:1:1=20190708,CREDIT.ACCT.NO:1:1=425251529710158,CURRENCY.MKT.CR:1:1=1,CREDIT.CURRENCY:1:1=RWF,CREDIT.AMOUNT:1:1=1100,CREDIT.VALUE.DATE:1:1=20190708,PROCESSING.DATE:1:1=20190708,PAYMENT.DETAILS:1:1=VFR DEPOSIT FOR ,PAYMENT.DETAILS:2:1= BY ,PAYMENT.DETAILS:3:1= test 123,PAYMENT.DETAILS:4:1=PO400010 000000RW0010007,CHARGE.COM.DISPLAY:1:1=NO,COMMISSION.CODE:1:1=CREDIT LESS CHARGES,COMMISSION.TYPE:1:1=ABRDEPCOM,COMMISSION.AMT:1:1=RWF500,CHARGE.CODE:1:1=WAIVE,PROFIT.CENTRE.CUST:1:1=4666729,RETURN.TO.DEPT:1:1=NO,FED.FUNDS:1:1=NO,POSITION.TYPE:1:1=TR,BPR.ID.NUMBER:1:1=102036384750101,BPR.ID.DOC.NO:1:1=1198080005389094,BAL.AFT.TXN:1:1=940550.81,TRAN.REF:1:1=1001250.81,MOBILE.NO:1:1=250788894696,TCM.REF:1:1=003030164525,AMOUNT.DEBITED:1:1=RWF1100,AMOUNT.CREDITED:1:1=RWF600,TOTAL.CHARGE.AMT:1:1=RWF500,CREDIT.COMP.CODE:1:1=RW0010425,DEBIT.COMP.CODE:1:1=RW0010404,LOC.AMT.DEBITED:1:1=1100,LOC.AMT.CREDITED:1:1=600,LOCAL.CHARGE.AMT:1:1=500,LOC.POS.CHGS.AMT:1:1=500,CUST.GROUP.LEVEL:1:1=99,DEBIT.CUSTOMER:1:1=4666729,CREDIT.CUSTOMER:1:1=2515297,DR.ADVICE.REQD.Y.N:1:1=N,CR.ADVICE.REQD.Y.N:1:1=N,CHARGED.CUSTOMER:1:1=2515297,TOT.REC.COMM:1:1=0,TOT.REC.COMM.LCL:1:1=0,TOT.REC.CHG:1:1=0,TOT.REC.CHG.LCL:1:1=0,RATE.FIXING:1:1=NO,TOT.REC.CHG.CRCCY:1:1=0,TOT.SND.CHG.CRCCY:1:1=500,AUTH.DATE:1:1=20190708,ROUND.TYPE:1:1=NATURAL,STMT.NOS:1:1=193880255539686.00,STMT.NOS:2:1=1-2,STMT.NOS:3:1=1,STMT.NOS:4:1=RW0010425,STMT.NOS:5:1=193880255539686.01,STMT.NOS:6:1=1-2,STMT.NOS:7:1=RW0010400,STMT.NOS:8:1=193880255539686.02,STMT.NOS:9:1=1-2,CURR.NO:1:1=1,INPUTTER:1:1=2555_INPUTTER__OFS_TRCOMOFS,DATE.TIME:1:1=2101291102,AUTHORISER:1:1=2555_INPUTTER_OFS_TRCOMOFS,CO.CODE:1:1=RW0010404,DEPT.CODE:1:1=400";
        String[] split = sampleOFS.split(",");
        return AccountDepositResponse.builder()
                .responseCode(AppConstants.TRANSACTION_SUCCESS_STANDARD.value())
                .responseString(AppConstants.TRANSACTION_SUCCESS_STANDARD.getReasonPhrase())
                .availBalance(Double.parseDouble(split[27].split("=")[1]))
                .txnReference((split[0].split("/"))[0])
                .txnDateTime(new Date())
                .build();


        //return null;
    }

    @SneakyThrows
    public CashWithdrawalResponse doWithdraw(CashWithdrawalRequest withdrawalRequest) {
        AuthenticateAgentResponse authenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(
                new MerchantAuthInfo(withdrawalRequest.getCredentials().getUsername(), withdrawalRequest.getCredentials().getPassword()));

        if (authenticateAgentResponse.getCode() != 200) {
            return CashWithdrawalResponse.builder()
                    .responseCode("05")
                    .responseString("Invalid credentials")
                    .build();
        }
        Data agentAuthData = authenticateAgentResponse.getData();

        String ofs = String.format("FUNDS.TRANSFER," +
                        "BPR.VFR.WDR/I/PROCESS/1/0,%s/%s/%s,," +
                        "DEBIT.CURRENCY::=RWF,DEBIT.AMOUNT::=1100," +
                        "CREDIT.ACCT.NO::=464430463110128,TCM.REF::=003030164525,PAYMENT.DETAILS:1:=\"VFR WITHDRAWAL FOR\":AC.NAME," +
                        "PAYMENT.DETAILS:2:=\"BY\":AC.NAME,PAYMENT.DETAILS:3:=test123,PAYMENT.DETAILS:4:=PO400010 000000RW0010007," +
                        "BPR.ID.NUMBER::=%s," +
                        "MOBILE.NO::=%s," +
                        "DEBIT.CURRENCY::=%s," +
                        "DEBIT.AMOUNT::=%s," +
                        "BPR.ID.DOC.NO::=%s," +
                        "EUCL.CUS.NO::=%s," +
                        "AC.NAME::=%s",
                getT24UserName(), getT24Password(), getDefaultBranch(), withdrawalRequest.getAccountNumber(),
                withdrawalRequest.getMobileNumber(), withdrawalRequest.getCurrencyCode(), withdrawalRequest.getAmount(),
                withdrawalRequest.getNationalID(), withdrawalRequest.getToken(), withdrawalRequest.getAccountName());

        String formattedOFS = String.format("%04d", ofs.length()) + ofs;

        // create a table or function to generate T24 messages
        T24TXNQueue tot24 = new T24TXNQueue();
        tot24.setRequestleg(formattedOFS);
        tot24.setStarttime(System.currentTimeMillis());
        tot24.setTxnchannel("PC");
        String t24ref = RRNGenerator.getInstance("VF").getRRN();
        tot24.setGatewayref(t24ref);
        tot24.setPostedstatus("0");
        tot24.setProcode("460001");
        /*tot24.setLegalid(withdrawalRequest.getNationalID());
        tot24.setPhone(withdrawalRequest.getMobileNumber());
        tot24.setTokenNo(withdrawalRequest.getToken());*/

        /*processT24Transactions(tot24);

        //Save the transaction
        //VisionFund visionFund = new VisionFund();
        //visionFund.setAccountName(tot24.getAccountname());
        //visionFund.setAccountNumber(withdrawalRequest.getAccountNumber());
        //visionFund.setAmount(withdrawalRequest.getAmount());
        //visionFund.setMobileNumber(withdrawalRequest.getMobileNumber());
        //visionFund.setTranDesc(withdrawalRequest.getTranDesc());
        //visionFund.setCurrencyCode(withdrawalRequest.getCurrencyCode());
        //visionFund.setNationalID(withdrawalRequest.getNationalID());
        //visionFund.setReferenceNumber(tot24.getGatewayref());
        //visionFund.setToken(withdrawalRequest.getToken());
        //visionFundRepository.save(visionFund);

        if (tot24.getT24responsecode() == null || tot24.getT24responsecode().equals("3")){

            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, withdrawalRequest.getTnxType(), "0200", withdrawalRequest.getAmount(),
                    AppConstants.EXCEPTION_OCCURRED_ON_EXTERNAL_HTTP_REQUEST.value(), agentAuthData.getTid(), agentAuthData.getMid());
            return CashWithdrawalResponse.builder()
                    .responseCode(AppConstants.EXCEPTION_OCCURRED_ON_EXTERNAL_HTTP_REQUEST.value())
                    .responseString("Transaction failed! "+tot24.getT24failnarration())
                    .build();
        }else{
            try{

                transactionService.saveCardLessTransactionToAllTransactionTable(tot24, withdrawalRequest.getTnxType(), "0200", withdrawalRequest.getAmount(),
                        AppConstants.TRANSACTION_SUCCESS_STANDARD.value(), agentAuthData.getTid(), agentAuthData.getMid());

                return CashWithdrawalResponse.builder()
                        .responseCode(AppConstants.TRANSACTION_SUCCESS_STANDARD.value())
                        .responseString(AppConstants.TRANSACTION_SUCCESS_STANDARD.getReasonPhrase())
                        .availBalance(Double.parseDouble(tot24.getBaladvise()))
                        .txnReference(tot24.getGatewayref())
                        .txnDateTime(DateFormat.getInstance().parse(tot24.getDatetime()))
                        .build();
            }catch (Exception e){

                log.error("<<<Vision Fund Withdrawal Transaction>>>\n{}",e.getMessage());
            }
        }
        */
        //for simulation purposes
        String ofsSampleResponse = "FT19189M51JV/TRCOM191890684045803.01/1,TRANSACTION.TYPE:1:1=ACVW,DEBIT.ACCT.NO:1:1=560239437610116,CURRENCY.MKT.DR:1:1=1,DEBIT.CURRENCY:1:1=RWF,DEBIT.AMOUNT:1:1=60000,DEBIT.VALUE.DATE:1:1=20190708,CREDIT.ACCT.NO:1:1=464430463110128,CURRENCY.MKT.CR:1:1=1,CREDIT.CURRENCY:1:1=RWF,CREDIT.VALUE.DATE:1:1=20190708,PROCESSING.DATE:1:1=20190708,PAYMENT.DETAILS:1:1=VFR WITHDRAWAL FOR CHARLES,PAYMENT.DETAILS:2:1= BY ,PAYMENT.DETAILS:3:1= test 123,PAYMENT.DETAILS:4:1=PO400010 000000RW0010007,CHARGES.ACCT.NO:1:1=560239437610116,CHARGE.COM.DISPLAY:1:1=NO,COMMISSION.CODE:1:1=DEBIT PLUS CHARGES,CHARGE.CODE:1:1=DEBIT PLUS CHARGES,PROFIT.CENTRE.CUST:1:1=2394376,RETURN.TO.DEPT:1:1=NO,FED.FUNDS:1:1=NO,POSITION.TYPE:1:1=TR,BPR.ID.NUMBER:1:1=102036384750101,BPR.ID.DOC.NO:1:1=1198080005389094,AC.NAME:1:1=CHARLES,ETAX.NARRATIVE:1:1=941600.81,TRAN.REF:1:1=17312320,INVOICE.DETAILS:1:1=2021-01-29T12:56:20+0200,MOBILE.NO:1:1=250788894696,TCM.REF:1:1=003030164525,AB.SCHL.NAME:1:1=941600.81,EUCL.CUS.NO:1:1=731618,AMOUNT.DEBITED:1:1=RWF60000,AMOUNT.CREDITED:1:1=RWF60000,CREDIT.COMP.CODE:1:1=RW0010464,DEBIT.COMP.CODE:1:1=RW0010560,LOC.AMT.DEBITED:1:1=60000,LOC.AMT.CREDITED:1:1=60000,CUST.GROUP.LEVEL:1:1=99,DEBIT.CUSTOMER:1:1=2394376,CREDIT.CUSTOMER:1:1=4304631,DR.ADVICE.REQD.Y.N:1:1=N,CR.ADVICE.REQD.Y.N:1:1=N,CHARGED.CUSTOMER:1:1=4304631,TOT.REC.COMM:1:1=0,TOT.REC.COMM.LCL:1:1=0,TOT.REC.CHG:1:1=0,TOT.REC.CHG.LCL:1:1=0,RATE.FIXING:1:1=NO,TOT.REC.CHG.CRCCY:1:1=0,TOT.SND.CHG.CRCCY:1:1=0,AUTH.DATE:1:1=20190708,ROUND.TYPE:1:1=NATURAL,STMT.NOS:1:1=193880684045805.00,STMT.NOS:2:1=1-2,STMT.NOS:3:1=RW0010464,STMT.NOS:4:1=193880684045805.01,STMT.NOS:5:1=1-2,STMT.NOS:6:1=RW0010560,STMT.NOS:7:1=193880684045805.02,STMT.NOS:8:1=1-2,STMT.NOS:9:1=RW0010400,STMT.NOS:10:1=193880684045805.03,STMT.NOS:11:1=1-4,CURR.NO:1:1=1,INPUTTER:1:1=6840_INPUTTER__OFS_TRCOMOFS,DATE.TIME:1:1=2101291243,AUTHORISER:1:1=6840_INPUTTER_OFS_TRCOMOFS,CO.CODE:1:1=RW0010404,DEPT.CODE:1:1=400";
        String[] strings = ofsSampleResponse.split(",");
        return CashWithdrawalResponse.builder().
                responseCode(AppConstants.TRANSACTION_SUCCESS_STANDARD.value()).
                responseString(AppConstants.TRANSACTION_SUCCESS_STANDARD.getReasonPhrase()).
                availBalance(Double.parseDouble(strings[27].split("=")[1])).
                txnReference((strings[0].split("/"))[0]).
                txnDateTime(new Date()).
                build();
        //return null;
    }

    @SneakyThrows
    public CustomVerificationResponse verifyCustomer(CustomVerificationRequest verificationRequest) {
        AuthenticateAgentResponse authenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(
                new MerchantAuthInfo(verificationRequest.getCredentials().getUsername(), verificationRequest.getCredentials().getPassword()));

        if (authenticateAgentResponse.getCode() != 200) {
           return CustomVerificationResponse.builder()
                    .responseCode("05")
                    .responseString("Invalid credentials")
                    .build();
        }
        Data agentAuthData = authenticateAgentResponse.getData();

        String ofsFormat = "0000AENQUIRY.SELECT,,%s/%s/%s,BPR.VFR.GET.DAT,ACCT.NO:EQ=%s,MOBILE.NO:EQ=%s";

        String format = String.format(ofsFormat, getT24UserName(), getT24Password(), getDefaultBranch(), verificationRequest.getAccountNumber(), verificationRequest.getMobileNumber());
        String t24OSF = String.format("%04d", format.length()) + format;

        // create a table or function to generate T24 messages
        T24TXNQueue tot24 = new T24TXNQueue();
        tot24.setRequestleg(t24OSF);
        tot24.setStarttime(System.currentTimeMillis());
        tot24.setTxnchannel("PC");
        String t24ref = RRNGenerator.getInstance("VF").getRRN();
        tot24.setGatewayref(t24ref);
        tot24.setPostedstatus("0");
        tot24.setProcode("500000");

        processT24Transactions(tot24);
        //Save the transaction
        //VisionFund visionFund = new VisionFund();
        //visionFund.setAccountName(tot24.getAccountname());
        //visionFund.setAccountNumber(verificationRequest.getAccountNumber());
        //visionFund.setMobileNumber(verificationRequest.getMobileNumber());
        //visionFund.setReferenceNumber(tot24.getGatewayref());
        //visionFundRepository.save(visionFund);

        if (tot24.getT24responsecode() == null || tot24.getT24responsecode().equals("3")){

            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, verificationRequest.getTnxType(), "0200", 0,
                    AppConstants.EXCEPTION_OCCURRED_ON_EXTERNAL_HTTP_REQUEST.value(), agentAuthData.getTid(), agentAuthData.getMid());
            return CustomVerificationResponse.builder()
                    .responseCode("05")
                    .responseString("Transaction failed! "+tot24.getT24failnarration())
                    .build();
        }else{
            try{
                transactionService.saveCardLessTransactionToAllTransactionTable(tot24, verificationRequest.getTnxType(), "0200", 0,
                        AppConstants.TRANSACTION_SUCCESS_STANDARD.value(), agentAuthData.getTid(), agentAuthData.getMid());
                return CustomVerificationResponse.builder()
                        .responseCode(AppConstants.TRANSACTION_SUCCESS_STANDARD.value())
                        .responseString(AppConstants.TRANSACTION_SUCCESS_STANDARD.getReasonPhrase())
                        .txnReference(tot24.getGatewayref())
                        .txnDateTime(DateFormat.getInstance().parse(tot24.getDatetime()))
                        .custNo(tot24.getCustid())
                        .custNm(tot24.getCustomerName())
                        .build();
            }catch (Exception e){

                log.error("<<<Vision Fund Verification Transaction>>>\n{}",e.getMessage());
            }
        }


        String ofsResponse = ",Y.CUS.ID::CUSTOMER.ID/Y.CUS.NAME::CUSTOMER.NAME/Y.BRANCH.ID::BRANCH.ID,\"0000363847\"    \"CHARLES RUMONGI\"       \"KIGALI         \"\n";

        var newString = (ofsResponse.split(",")[2]).replaceAll("\\p{Space}{2,}+", "");
        var responseFields = newString.split("\"\"");

        var response = CustomVerificationResponse.builder().
                custNo(responseFields[0].replaceAll("[\",\n,\\,{\\\"}]", "")).
                custNm(responseFields[1].replaceAll("[\",\n,\\,{\\\"}]", "")).
                responseString("Customer data fetched successfully").
                responseCode("00").
                branchId(responseFields[2].replaceAll("[\",\n,\\,{\\\"}]", ""))
                .build();
        return response;
    }

    private void processT24Transactions(T24TXNQueue tot24) {
        t24Channel.processTransactionToT24(getT24Ip(), Integer.parseInt(getT24Port()), tot24);

        System.out.println(">>>>> processing transaction >>>>> ");

        transactionService.updateT24TransactionDTO(tot24);
    }

    private String getT24UserName() {
        String t24USER = xSwitchParameterService.fetchXSwitchParamValue("T24USER");

        if (t24USER.isEmpty()) {
            log.info("****************************** Missing T24 User in the database. ******************************");
            return "TRUSER1";
        }
        return utilityService.decryptSensitiveData(t24USER);
    }

    private String getT24Password() {
        String t24PASS = xSwitchParameterService.fetchXSwitchParamValue("T24PASS");

        if (t24PASS.isEmpty()) {
            log.info("****************************** Missing T24 Pass in the database. ******************************");
            return "123456";
        }
        return utilityService.decryptSensitiveData(t24PASS);
    }

    private String getDefaultBranch() {
        String default_eucl_branch = xSwitchParameterService.fetchXSwitchParamValue("DEFAULT_EUCL_BRANCH");
        if (default_eucl_branch.isEmpty()) {
            log.info("****************************** Missing T24 Pass in the database. ******************************");
            return "RW0010593";
        }
        return default_eucl_branch;
    }

    private String getT24Ip() {
        return xSwitchParameterService.fetchXSwitchParamValue("T24_IP");
    }

    private String getT24Port() {
        return xSwitchParameterService.fetchXSwitchParamValue("T24_PORT");
    }
}
