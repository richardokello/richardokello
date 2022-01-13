package co.ke.tracom.bprgateway.web.agenttransactions.services;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.request.AgentTransactionRequest;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AgentTransactionResponse;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.bankbranches.entity.BPRBranches;
import co.ke.tracom.bprgateway.web.bankbranches.service.BPRBranchService;
import co.ke.tracom.bprgateway.web.exceptions.custom.InvalidAgentCredentialsException;
import co.ke.tracom.bprgateway.web.switchparameters.XSwitchParameterService;
import co.ke.tracom.bprgateway.web.t24communication.services.T24Channel;
import co.ke.tracom.bprgateway.web.transactionLimits.TransactionLimitManagerService;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import co.ke.tracom.bprgateway.web.util.data.MerchantcustomerInfoDeposit;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import co.ke.tracom.bprgateway.web.util.services.UtilityService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_PASSWORD;
import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_USERNAME;

@Service
@Slf4j
@RequiredArgsConstructor
public class AgentTransactionService {

    static final Long AGENT_DEPOSIT_TRANSACTION_LIMIT_ID = 6L;
    static final Long AGENT_WITHDRAWAL_TRANSACTION_LIMIT_ID = 7L;

    private final T24Channel t24Channel;
    private final TransactionService transactionService;
    private final UtilityService utilityService;
    private final BaseServiceProcessor baseServiceProcessor;
    private final BPRBranchService bprBranchService;

    private final XSwitchParameterService xSwitchParameterService;
    private final TransactionLimitManagerService limitManagerService;

    @SneakyThrows
    public AgentTransactionResponse processAgentFloatDeposit(AgentTransactionRequest agentTransactionRequest) {
        T24TXNQueue tot24 = new T24TXNQueue();
        AgentTransactionResponse response = new AgentTransactionResponse();
        // Validate agent credentials
        AuthenticateAgentResponse authenticateAgentDepositResponse=null;


        // validate amount limits
        try {
  // Setting Agent username and password.
           // MerchantcustomerInfoDeposit authData = new MerchantcustomerInfoDeposit();
            MerchantAuthInfo authData=new MerchantAuthInfo();
            authData.setUsername(agentTransactionRequest.getCredentials().getUsername());
            authData.setPassword(agentTransactionRequest.getCredentials().getPassword());

// We auntheticate the agent password and username
         //  authenticateAgentDepositResponse = baseServiceProcessor.authenticateAgentDepositUsername(authData);
            authenticateAgentDepositResponse=baseServiceProcessor.authenticateAgentUsernamePassword(authData);
        } catch (InvalidAgentCredentialsException e){
            transactionService.saveFailedUserPasswordTransactions("Failed Logins","Agent logins",agentTransactionRequest.getCredentials().getUsername(),
                    "AgentValidation","FAILED","ipAddress");
            response.setMessage(e.getMessage());
            return response;

        }
            response.setUsername(authenticateAgentDepositResponse.getData().getUsername());
            response.setNames(authenticateAgentDepositResponse.getData().getNames());
            response.setBusinessName(authenticateAgentDepositResponse.getData().getBusinessName());
            response.setLocation(authenticateAgentDepositResponse.getData().getLocation());
            response.setTid(authenticateAgentDepositResponse.getData().getTid());
            response.setMid(authenticateAgentDepositResponse.getData().getMid());

            String transactionReferenceNo = RRNGenerator.getInstance("AD").getRRN();
            String POSAgentAccount = authenticateAgentDepositResponse.getData().getAccountNumber();

            long POSAgentFloatBalance = fetchAgentAccountBalanceOnly(POSAgentAccount);
            long depositAmount = agentTransactionRequest.getAmount();

        if (POSAgentFloatBalance < depositAmount) {

            transactionService.saveCardLessTransactionToAllTransactionTable(
                    tot24, "AGENT FLOAT DEPOSIT", "1200",
                    agentTransactionRequest.getAmount(), "117",
                    authenticateAgentDepositResponse.getData().getTid(), authenticateAgentDepositResponse.getData().getMid());
            log.info(
                    "Agent Float Deposit:[Failed] Transaction " + transactionReferenceNo + " failed. Error message: Insufficient agent balance %n");
            response.setStatus("117");
            response.setMessage("Insufficient agent balance");
            return response;
        }

        TransactionLimitManagerService.TransactionLimit limitValid = limitManagerService.isLimitValid(AGENT_DEPOSIT_TRANSACTION_LIMIT_ID, agentTransactionRequest.getAmount());

        if (!limitValid.isValid()) {

            transactionService.saveCardLessTransactionToAllTransactionTable(
                    tot24, "AGENT FLOAT DEPOSIT", "1200",
                    agentTransactionRequest.getAmount(), "061",
                    authenticateAgentDepositResponse.getData().getTid(), authenticateAgentDepositResponse.getData().getMid());
            response.setStatus("061");
            response.setMessage("Amount should be between "+ limitValid.getLower()+ " and " + limitValid.getUpper());
            return response ;
        }
// agent customer username and password.
        MerchantcustomerInfoDeposit customerAgent = MerchantcustomerInfoDeposit.builder()
                .password(null)
               .username(agentTransactionRequest.getCustomerAgentId()).build();
//      //  MerchantAuthInfo customerAgent= MerchantAuthInfo.builder().username(agentTransactionRequest.getCustomerAgentId()).build();
        AuthenticateAgentResponse customerAgentData;
        try {

            customerAgentData = baseServiceProcessor.authenticateAgentDepositUsername(customerAgent);

        } catch (InvalidAgentCredentialsException e){
            transactionService.saveFailedUserPasswordTransactions("Failed Logins PC Module transactions","Agent logins",agentTransactionRequest.getCredentials().getUsername(),
                    "AgentValidation","FAILED","ipAddress");
            response.setMessage(e.getMessage());
            return response;

        }


        String customerAgentAccountNo = customerAgentData.getData().getAccountNumber();
        String customerAgentName = customerAgentData.getData().getNames();
        System.out.printf(
                "Agent Float Deposit: Transaction %s customer agent name: %s and account no: %s. %n",
                transactionReferenceNo, customerAgentName, customerAgentAccountNo);

        String firstPaymentDetails = POSAgentAccount + " " + authenticateAgentDepositResponse.getData().getNames();
        firstPaymentDetails =
                firstPaymentDetails.length() > 34
                        ? firstPaymentDetails.substring(0, 34)
                        : firstPaymentDetails;

        BPRBranches bprBranches = bprBranchService.fetchBranchAccountsByBranchCode(POSAgentAccount);
        if (null == bprBranches.getCompanyName()) {

            transactionService.saveCardLessTransactionToAllTransactionTable(
                    tot24, "AGENT FLOAT DEPOSIT", "1200",
                    agentTransactionRequest.getAmount(), "065",
                    authenticateAgentDepositResponse.getData().getTid(), authenticateAgentDepositResponse.getData().getMid());
            log.info("Agent float deposit transaction [" + transactionReferenceNo + "] failed. Error: Agent branch details could not be verified.");

            response.setStatus("065");
            response.setMessage("Agent branch details could not be verified. Kindly contact BPR customer care");
            return response;
        }

        String accountBranchId = bprBranches.getId();
        if (accountBranchId.isEmpty()) {



            transactionService.saveCardLessTransactionToAllTransactionTable(
                    tot24, "AGENT FLOAT DEPOSIT", "1200",
                    agentTransactionRequest.getAmount(), "003",
                    authenticateAgentDepositResponse.getData().getTid(), authenticateAgentDepositResponse.getData().getMid());

            System.out.printf(
                    "Agent Float Deposit:[Failed] Transaction %s failed. Error message: Missing branch details for recipient agent. %n",
                    transactionReferenceNo);
            response.setStatus("003");
            response.setMessage("Missing agent branch Id");
            return response;
        }

        //TODO Terminal id and merchant id combination
        String secondPaymentDetails = authenticateAgentDepositResponse.getData().getAccountNumber() + " " + authenticateAgentDepositResponse.getData().getNames();
        String thirdPaymentDetails = "Agent Float Deposit";
        String agentFloatDepositOFSTemplate =
                "0000AFUNDS.TRANSFER,AGENCY.DEPOSIT/I/PROCESS/2/0,"
                        + "%s/%s/%s,"
                        + ",TRANSACTION.TYPE::=AC,"
                        + "DEBIT.ACCT.NO::=%s,"
                        + "DEBIT.AMOUNT::=%s,"
                        + "CREDIT.ACCT.NO::=%s,"
                        + "DEBIT.CURRENCY::=RWF,"
                        + "TCM.REF::=%s,"
                        + "PAYMENT.DETAILS:1:=%s,"
                        + "PAYMENT.DETAILS:2:=%s,"
                        + "PAYMENT.DETAILS:3:=%s";

        String agentFloatDepositOFS =
                String.format(
                        agentFloatDepositOFSTemplate,
                        MASKED_T24_USERNAME,
                        MASKED_T24_PASSWORD,
                        accountBranchId,
                        POSAgentAccount,
                        depositAmount,
                        customerAgentAccountNo,
                        transactionReferenceNo,
                        utilityService.sanitizePaymentDetails(firstPaymentDetails.trim(), "Agent Float Deposit"),
                        secondPaymentDetails.trim(),
                        thirdPaymentDetails.trim());
        String tot24str = String.format("%04d", agentFloatDepositOFS.length()) + agentFloatDepositOFS;

       // T24TXNQueue tot24 = new T24TXNQueue();
        tot24.setRequestleg(tot24str);
        tot24.setStarttime(System.currentTimeMillis());

        String channel = "1510";
        tot24.setTxnchannel(channel);

        tot24.setGatewayref(transactionReferenceNo);
        tot24.setPostedstatus("0");

        //TODO Put processing codes on enum
        tot24.setProcode("480000");
        String tid = authenticateAgentDepositResponse.getData().getTid();
        tot24.setTid(tid);
        tot24.setDebitacctno(POSAgentAccount);
        tot24.setCreditacctno(customerAgentAccountNo);

        final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue("T24_IP");
        final String t24Port = xSwitchParameterService.fetchXSwitchParamValue("T24_PORT");

        t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);

        transactionService.updateT24TransactionDTO(tot24);

        System.out.printf(
                "Agent Float Deposit: Transaction %s has been queued for T24 Processing. %n",
                transactionReferenceNo);


        if (null != tot24.getT24responsecode() && tot24.getT24responsecode().equals("1")) {
            System.out.printf(
                    "Agent Float Deposit:[Success] Transaction %s processing succeeded. %n",
                    transactionReferenceNo);
            try {
                String charges =
                        tot24.getTotalchargeamt() == null ? "0" : tot24.getTotalchargeamt();
                String cleanedChargeAmount = charges.replace("RWF", "");
                String ISOFormattedAmount =
                        String.format("%012d", Integer.parseInt(cleanedChargeAmount));
                response.setTransactionCharges(Double.parseDouble(ISOFormattedAmount));

            } catch (Exception e) {
                System.out.printf(
                        "Agent Float Deposit:[Error] An error occurred processing charge amount for transaction %s. Error message %s %n",
                        transactionReferenceNo, e.getMessage());
                e.printStackTrace();
            }

            transactionService.saveCardLessTransactionToAllTransactionTable(
                    tot24, "AGENT FLOAT DEPOSIT", "1200",
                    agentTransactionRequest.getAmount(), "000",
                    authenticateAgentDepositResponse.getData().getTid(), authenticateAgentDepositResponse.getData().getMid());

            response.setT24Reference(tot24.getT24reference());
            response.setRrn(transactionReferenceNo);
            response.setStatus("00");
            response.setMessage("Transaction processed successful");
            return response;
        }

        else {
            transactionService.saveCardLessTransactionToAllTransactionTable(
                    tot24, "AGENT FLOAT DEPOSIT", "1200",
                    agentTransactionRequest.getAmount(), ""+response.getStatus()+"",
                    authenticateAgentDepositResponse.getData().getTid(), authenticateAgentDepositResponse.getData().getMid());
            System.out.printf(
                    "Agent Float Deposit:[Error] Transaction %s processing failed. Error message: Transaction failed at T24 %n",
                    transactionReferenceNo);

            response.setStatus(response.getStatus());
            response.setTransactionCharges(0.0);
            response.setT24Reference(response.getT24Reference());
            response.setMessage((tot24.getT24failnarration() == null || tot24.getT24failnarration().isEmpty())
                    ? "TRANSACTION FAILED. SYSTEM FAILURE"
                    : tot24.getT24failnarration());
        }



        return response;

    }

    public Long fetchAgentAccountBalanceOnly(String agentAccount) {
        String agentFloatBalanceOFS =
                String.format(
                        "0000AENQUIRY.SELECT,,%s/%s,ECL.ENQUIRY.DETS,ID:EQ=%s,TRANS.TYPE.ID:EQ=CUSTDET",
                        MASKED_T24_USERNAME, MASKED_T24_PASSWORD, agentAccount);
        String tot24str = String.format("%04d", agentFloatBalanceOFS.length()) + agentFloatBalanceOFS;

        T24TXNQueue T24Transaction = new T24TXNQueue();
        T24Transaction.setRequestleg(tot24str);
        T24Transaction.setStarttime(System.currentTimeMillis());

        T24Transaction.setTxnchannel("PC");

        String transactionReferenceNo = RRNGenerator.getInstance("AD").getRRN();
        T24Transaction.setGatewayref(transactionReferenceNo);
        T24Transaction.setPostedstatus("0");
        T24Transaction.setProcode("500000");

        String tid = "PC";
        T24Transaction.setTid(tid);

        final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue("T24_IP");
        final String t24Port = xSwitchParameterService.fetchXSwitchParamValue("T24_PORT");
        t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), T24Transaction);
        transactionService.updateT24TransactionDTO(T24Transaction);

        if ((T24Transaction.getResponseleg() != null)) {

            if (T24Transaction.getBaladvise()==null) {
                return 0L;
            } else {
                try {
                    System.out.printf(
                            "Agent Balance [Success]: Transaction %s processing completed. Agent balance %d %n",
                            transactionReferenceNo, Long.parseLong(T24Transaction.getBaladvise()));
                    return Long.parseLong(T24Transaction.getBaladvise());
                }catch (NumberFormatException n){
                   // n.printStackTrace();
                }

            }
        }
        return 0L;
    }

    @SneakyThrows
    public AgentTransactionResponse processAgentFloatWithdrawal(AgentTransactionRequest request, String transactionReferenceNo) {
        AgentTransactionResponse response = AgentTransactionResponse.builder().build();
        T24TXNQueue tot24 = new T24TXNQueue();
                AuthenticateAgentResponse authenticateAgentResponse = null;


        try{
            authenticateAgentResponse= baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials());
        }
        catch (InvalidAgentCredentialsException e)
        {
            transactionService.saveFailedUserPasswordTransactions("Failed Logins PC module transactions","Agent logins",request.getCredentials().getUsername(),
                    "AgentValidation","FAILED","ipAddress");
        }

        response.setUsername(authenticateAgentResponse.getData().getUsername());
        response.setNames(authenticateAgentResponse.getData().getNames());
        response.setBusinessName(authenticateAgentResponse.getData().getBusinessName());
        response.setLocation(authenticateAgentResponse.getData().getLocation());
        response.setTid(authenticateAgentResponse.getData().getTid());
        response.setMid(authenticateAgentResponse.getData().getMid());

        MerchantAuthInfo customerAgent = MerchantAuthInfo.builder()
                .password(request.getCustomerAgentPass())
                .username(request.getCustomerAgentId()).build();

        AuthenticateAgentResponse customerAgentData = baseServiceProcessor.authenticateAgentUsernamePassword(customerAgent);

        String recipientAgentAccountNo = customerAgentData.getData().getAccountNumber();
        String recipientAgentName = customerAgentData.getData().getNames();
        System.out.printf(
                "Agent Float Withdrawal: Transaction %s recipient agent name: %s and account no: %s. %n",
                transactionReferenceNo, recipientAgentName, recipientAgentAccountNo);

        long POSAgentAccountBalance = fetchAgentAccountBalanceOnly(authenticateAgentResponse.getData().getAccountNumber());


        if (POSAgentAccountBalance < request.getAmount()) {

            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "AGENT FLOAT WITHDRAWAL", "1200",
                    request.getAmount(), "117",
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());
            System.out.printf(
                    "Agent Float Withdrawal:[Failed] Transaction %s failed. Error message: Insufficient balance for POS Agent. %n",
                    transactionReferenceNo);
            response.setStatus("117");
            response.setMessage("Insufficient float balance");
            return response;
        }



        TransactionLimitManagerService.TransactionLimit limitValid = limitManagerService.isLimitValid(AGENT_WITHDRAWAL_TRANSACTION_LIMIT_ID, request.getAmount());
        if (!limitValid.isValid()) {
            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "AGENT FLOAT WITHDRAWAL", "1200",
                    request.getAmount(), "061",
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());
            response.setStatus("061");
            response.setMessage("Amount should be between"+ limitValid.getLower()+ " and " + limitValid.getUpper());
            return response;
        }

        BPRBranches recipientAgentBankBranch =
                bprBranchService.fetchBranchAccountsByBranchCode(recipientAgentAccountNo);
        if (null == recipientAgentBankBranch.getCompanyName()) {

            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "AGENT FLOAT WITHDRAWAL", "1200",
                    request.getAmount(), "003",
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());
            System.out.printf(
                    "Agent Float Withdrawal:[Failed] Transaction %s failed. Error message: Missing branch details for recipient agent. %n",
                    transactionReferenceNo);
            response.setStatus("003");
            response.setMessage("Missing branch name for recipient agent");
            return response;
        }
        String recipientBranchName = recipientAgentBankBranch.getCompanyName();
        String firstPaymentDetails = recipientAgentAccountNo + " " + recipientBranchName;
        firstPaymentDetails =
                firstPaymentDetails.length() > 34
                        ? firstPaymentDetails.substring(0, 34)
                        : firstPaymentDetails;

        String recipientAccountBranchID = recipientAgentBankBranch.getId();
        if (recipientAccountBranchID.isEmpty()) {
            System.out.printf(
                    "Agent Float Withdrawal:[Failed] Transaction %s failed. Error message: Missing account branch Id details for recipient agent. %n",
                    transactionReferenceNo);
            response.setStatus("003");
            response.setMessage("Missing account branch Id for recipient agent");
            return response;
        }
        // TODO Replace with merchant and terminal ID
        String secondPaymentDetails = authenticateAgentResponse.getData().getUsername() + authenticateAgentResponse.getData().getAccountNumber();
        String thirdPaymentDetails = "Agent Float WDL";
        String OFSTemplate =
                "0000AFUNDS.TRANSFER,AGENCY.DEPOSIT/I/PROCESS/2/0,%s/%s/%s,,"
                        + "TRANSACTION.TYPE::=AC,"
                        + "DEBIT.ACCT.NO::=%s,"
                        + "DEBIT.AMOUNT::=%s,"
                        + "CREDIT.ACCT.NO::=%s,"
                        + "DEBIT.CURRENCY::=RWF,"
                        + "TCM.REF::=%s,"
                        + "DEBIT.THEIR.REF::='AgentFloatWDL',"
                        + "CREDIT.THEIR.REF::='AgentFloatWDL',"
                        + "PAYMENT.DETAILS:1:=%s,"
                        + "PAYMENT.DETAILS:2:=%s,"
                        + "PAYMENT.DETAILS:3:=%s";

        String agentFloatWithdrawalOFS =
                String.format(
                        OFSTemplate,
                        MASKED_T24_USERNAME,
                        MASKED_T24_PASSWORD,
                        recipientAccountBranchID,
                        recipientAgentAccountNo,
                        String.valueOf(request.getAmount()),
                        authenticateAgentResponse.getData().getAccountNumber(),
                        transactionReferenceNo,
                        utilityService.sanitizePaymentDetails(firstPaymentDetails.trim(), "Agent Float Withdrawal"),
                        secondPaymentDetails.trim(),
                        thirdPaymentDetails.trim());

        String tot24str = String.format("%04d", agentFloatWithdrawalOFS.length()) + agentFloatWithdrawalOFS;
        //T24TXNQueue tot24 = new T24TXNQueue();
        tot24.setRequestleg(tot24str);
        tot24.setStarttime(System.currentTimeMillis());

        //TODO check the channel information
        String channel = "1510";
        tot24.setTxnchannel(channel);

        tot24.setGatewayref(transactionReferenceNo);
        tot24.setPostedstatus("0");
        // TODO put processing codes in enum
        tot24.setProcode("490000");
        //TODO what to use for PC
        tot24.setTid(authenticateAgentResponse.getData().getUsername());
        tot24.setDebitacctno(recipientAgentAccountNo);
        tot24.setCreditacctno(authenticateAgentResponse.getData().getAccountNumber());

        final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue("T24_IP");
        final String t24Port = xSwitchParameterService.fetchXSwitchParamValue("T24_PORT");
        t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);
        transactionService.updateT24TransactionDTO(tot24);

        System.out.printf(
                "Agent Float Withdrawal: Transaction %s queued for T24 processing. %n",
                transactionReferenceNo);

        if (null != tot24.getT24responsecode() && tot24.getT24responsecode().equals("1")) {

            tot24.setT24reference(tot24.getT24reference());
            System.out.printf(
                    "Agent Float withdrawal:[Success] Transaction %s processing succeeded. %n",
                    transactionReferenceNo);
            try {
                String charges =
                        tot24.getTotalchargeamt() == null ? "0" : tot24.getTotalchargeamt();
                String cleanedChargeAmount = charges.replace("RWF", "");
                String ISOFormattedAmount =
                        String.format("%012d", Integer.parseInt(cleanedChargeAmount));
                response.setTransactionCharges(Double.parseDouble(ISOFormattedAmount));

            } catch (Exception e) {
                System.out.printf(
                        "Agent Float withdrawal:[Error] An error occurred processing charge amount for transaction %s. Error message %s %n",
                        transactionReferenceNo, e.getMessage());
                e.printStackTrace();
            }

            response.setT24Reference(tot24.getT24reference());
            response.setRrn(transactionReferenceNo);
            response.setStatus("00");
            response.setMessage("Transaction processed successful");

            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "AGENT FLOAT WITHDRAWAL", "1200",
                    request.getAmount(), "000",
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());
        } else {
            System.out.printf(
                    "Agent Float withdrawal:[Error] Transaction %s processing failed. Error message: Transaction failed at T24 %n",
                    transactionReferenceNo);

            response.setStatus("908");
            response.setTransactionCharges(0.0);
            response.setT24Reference("");
            response.setMessage((tot24.getT24failnarration() == null || tot24.getT24failnarration().isEmpty())
                    ? "TRANSACTION FAILED. SYSTEM FAILURE"
                    : tot24.getT24failnarration());

            transactionService.saveCardLessTransactionToAllTransactionTable(tot24, "AGENT FLOAT WITHDRAWAL", "1200",
                    request.getAmount(), "908",
                    authenticateAgentResponse.getData().getTid(), authenticateAgentResponse.getData().getMid());
        }
        transactionService.updateT24TransactionDTO(tot24);

        return response;
    }
}
