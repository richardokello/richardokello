package co.ke.tracom.bprgateway.web.depositmoney.services;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.agenttransactions.services.AgentTransactionService;
import co.ke.tracom.bprgateway.web.bankbranches.entity.BPRBranches;
import co.ke.tracom.bprgateway.web.bankbranches.service.BPRBranchService;
import co.ke.tracom.bprgateway.web.depositmoney.data.requests.DepositMoneyRequest;
import co.ke.tracom.bprgateway.web.depositmoney.data.response.DepositMoneyResult;
import co.ke.tracom.bprgateway.web.depositmoney.data.response.DepositMoneyResultData;
import co.ke.tracom.bprgateway.web.switchparameters.repository.XSwitchParameterRepository;
import co.ke.tracom.bprgateway.web.t24communication.services.T24Channel;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import co.ke.tracom.bprgateway.web.util.services.UtilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_PASSWORD;
import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_USERNAME;

@RequiredArgsConstructor
@Service
@Slf4j
public class DepositMoneyService {
    private final UtilityService utilityService;
    private final T24Channel t24Channel;
    private final TransactionService transactionService;
    private final XSwitchParameterRepository xSwitchParameterRepository;
    private final AgentTransactionService agentTransactionService;
    private final BPRBranchService bprBranchService;
    private final BaseServiceProcessor baseServiceProcessor;

    @Value("${merchant.account.validation}")
    private String agentValidation;

    public DepositMoneyResult processCustomerDepositMoneyTnx(DepositMoneyRequest depositMoneyRequest, String transactionRRN) {
        // Validate agent credentials
        Optional<AuthenticateAgentResponse> optionalAuthenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(depositMoneyRequest.getCredentials(), agentValidation);
        if (optionalAuthenticateAgentResponse.isEmpty()) {
            log.info(
                    "Agent Float Deposit:[Failed] Missing agent information %n");
            return DepositMoneyResult.builder()
                    .status("117")
                    .message("Missing agent information")
                    .data(null)
                    .build();

        } else if (optionalAuthenticateAgentResponse.get().getCode() != HttpStatus.OK.value()) {
            return DepositMoneyResult.builder()
                    .status(String.valueOf(
                            optionalAuthenticateAgentResponse.get().getCode())
                    )
                    .message(optionalAuthenticateAgentResponse.get().getMessage()).build();
        }
        AuthenticateAgentResponse authenticateAgentResponse = optionalAuthenticateAgentResponse.get();
        String agentFloatAccount = authenticateAgentResponse.getData().getAccountNumber();
        String agentMerchantId = "PCMerchant";
        String chargeCreditPLAccount = "PL64200\\HOF";
        String customerAccount = depositMoneyRequest.getAccountNumber();

        // Todo check the payment details required
        String firstDetails = depositMoneyRequest.getAccountName() + " " + depositMoneyRequest.getAccountNumber();
        firstDetails =
                firstDetails.length() > 34 ? firstDetails.substring(0, 34) : firstDetails;

        BPRBranches branch = getBranchDetailsFromAccount(agentFloatAccount);
        if (null == branch.getCompanyName()) {
            return DepositMoneyResult.builder()
                    .status("65")
                    .message("Agent branch details could not be verified. Kindly contact BPR customer care")
                    .data(null)
                    .build();
        }

        //TODO fetch the payment details (Terminal ID and Merchant ID)
        String secondDetails = agentMerchantId;  // From merchant validation request
        String thirdDetails = "CUSTOMER DEPOSIT AT AGENT";
        if (branch.getId() == null) {
            return DepositMoneyResult.builder()
                    .status("65")
                    .message("Missing agent branch details")
                    .data(null)
                    .build();
        }
        String accountBranchId = branch.getId();

        long agentbalancelong = agentTransactionService.fetchAgentAccountBalanceOnly(agentFloatAccount);

        String channel = "1510";
        log.info("Customer Deposit: Transaction %s. Agent Balance=%s Deposit amount=%d ",
                transactionRRN, agentbalancelong, depositMoneyRequest.getAmount());
        if (agentbalancelong < depositMoneyRequest.getAmount()) {
            System.out.printf(
                    "Customer Deposit: Transaction %s Failed. Agent does not have sufficient balance %n", transactionRRN);
            return DepositMoneyResult.builder()
                    .status("65")
                    .message("Insufficient agent balance")
                    .data(null)
                    .build();
        }

        String customerDepositOFS =
                "0000AFUNDS.TRANSFER,CUSTOMER.DEPOSIT/I/PROCESS//0,"
                        + MASKED_T24_USERNAME
                        + "/"
                        + MASKED_T24_PASSWORD
                        + "/"
                        + accountBranchId.trim()
                        + ",,TRANSACTION.TYPE::=ACTD,DEBIT.ACCT.NO::="
                        + agentFloatAccount.trim()
                        + ",DEBIT.AMOUNT::="
                        + (int) depositMoneyRequest.getAmount()
                        + ",CREDIT.ACCT.NO::="
                        + customerAccount.trim().trim()
                        + ",DEBIT.CURRENCY::=RWF,TCM.REF::="
                        + transactionRRN.trim()
                        + ",PAYMENT.DETAILS:1:="
                        + utilityService.sanitizePaymentDetails(firstDetails, "Customer Deposit")
                        + ",PAYMENT.DETAILS:2:="
                        + secondDetails.trim()
                        + ",PAYMENT.DETAILS:3:="
                        + thirdDetails.trim();

        String tot24str = String.format("%04d", customerDepositOFS.length()) + customerDepositOFS;

        // create a table or function to generate T24 messages
        T24TXNQueue tot24 = new T24TXNQueue();
        // base 64 encode request in db
        tot24.setRequestleg(tot24str);
        tot24.setStarttime(System.currentTimeMillis());
        tot24.setTxnchannel(channel);
        tot24.setGatewayref(transactionRRN);
        tot24.setPostedstatus("0");
        tot24.setProcode("440000");
        String tid = "PC";
        tot24.setTid(tid);
        tot24.setDebitacctno(agentFloatAccount);
        tot24.setCreditacctno(customerAccount);


        final String t24Ip = xSwitchParameterRepository.findByParamName("T24_IP").get().getParamValue();
        final String t24Port = xSwitchParameterRepository.findByParamName("T24_PORT").get().getParamValue();

        t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);

        //TODO check this
        transactionService.updateT24TransactionDTO(tot24);

        String T24RawResponseCode =
                tot24.getT24responsecode() == null ? "NA" : tot24.getT24responsecode();
        if (T24RawResponseCode.equals("1")) {
            try {
                String charges =
                        null == tot24.getTotalchargeamt() || tot24.getTotalchargeamt().isEmpty()
                                ? "0"
                                : tot24.getTotalchargeamt();
                String formattedCharge = charges.replace("RWF", "");

                String ISOFormattedAmount = String.format("%012d", Integer.parseInt(formattedCharge));
                System.out.printf(
                        "Customer deposit transaction charges = %s for transaction [%s]",
                        ISOFormattedAmount, transactionRRN);


                String chargesAccountNumber =
                        tot24.getChargesacctno() == null ? "" : tot24.getChargesacctno();
                String profitCentre =
                        tot24.getProfitcentrecust() == null ? "" : tot24.getProfitcentrecust();

                accountBranchId = tot24.getCocode();
                if (!chargesAccountNumber.isEmpty()) {
                    sweepChargesToPL(
                            tid,
                            profitCentre,
                            accountBranchId,
                            chargeCreditPLAccount,
                            chargesAccountNumber,
                            formattedCharge,
                            transactionRRN,
                            tot24.getT24reference(),
                            "440000");
                }

                tot24.setT24reference(tot24.getT24reference());
                //TODO fix saving transaction to database
                transactionService.saveCardLessTransactionToAllTransactionTable(
                        tot24, "AGENT DEPOSIT TO CUSTOMER");

                DepositMoneyResultData data = DepositMoneyResultData.builder()
                        .t24Reference(tot24.getT24reference())
                        .rrn(transactionRRN)
                        .charges(utilityService.formatDecimal(
                                Float.parseFloat(ISOFormattedAmount)
                        ))
                        .build();

                return DepositMoneyResult.builder()
                        .status("00")
                        .message("Transaction processed successfully")
                        .data(data).build();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.printf(
                    "Customer Deposit: [Failed] Transaction %s failed. T24 Response message %s  %n",
                    transactionRRN, tot24.getT24failnarration());
            return DepositMoneyResult.builder()
                    .status("098")
                    .message((tot24.getT24failnarration() == null || tot24.getT24failnarration().isEmpty())
                            ? "TRANSACTION FAILED. SYSTEM FAILURE"
                            : "Transaction failed. " + tot24.getT24failnarration())
                    .data(null)
                    .build();
        }
        return DepositMoneyResult.builder()
                .status("98")
                .message("Transaction failed processing")
                .data(null).build();

    }

    public void sweepChargesToPL(String tid, String profitCentre, String accountBranchId, String chargeDebit,
                                 String chargeCreditPL, String chargeAmount, String depositTnxRRN, String T24reference,
                                 String proCode) {

        String depositChargeRRN = RRNGenerator.getInstance("DC").getRRN();
        String channel = "Gateway";

        String depositChargeOFS =
                "0000AFUNDS.TRANSFER,"
                        + "BPR.PL.SWEEP/I/PROCESS//0,"
                        + ""
                        + MASKED_T24_USERNAME
                        + "/"
                        + MASKED_T24_PASSWORD
                        + "/"
                        + accountBranchId
                        + ",,"
                        + "TRANSACTION.TYPE::=ACTD,"
                        + "DEBIT.ACCT.NO::="
                        + chargeDebit
                        + ","
                        + "DEBIT.AMOUNT::="
                        + chargeAmount
                        + ","
                        + "CREDIT.ACCT.NO::="
                        + chargeCreditPL
                        + ","
                        + "DEBIT.CURRENCY::=RWF,"
                        + "PROFIT.CENTRE.CUST::="
                        + profitCentre
                        + ","
                        + "ORDERING.CUST::='9999999',"
                        + "TCM.REF::="
                        + depositChargeRRN
                        + ","
                        + "DEBIT.THEIR.REF::='"
                        + T24reference
                        + "',"
                        + "CREDIT.THEIR.REF::='"
                        + T24reference
                        + "'";

        String tot24str = String.format("%04d", depositChargeOFS.length()) + depositChargeOFS;

        // create a table or function to generate T24 messages
        T24TXNQueue tot24 = new T24TXNQueue();
        tot24.setRequestleg(tot24str);
        tot24.setStarttime(System.currentTimeMillis());
        tot24.setTxnchannel(channel);
        tot24.setGatewayref(depositChargeRRN);
        tot24.setPostedstatus("0");
        tot24.setProcode(proCode);
        tot24.setTid(tid);

        final String t24Ip = xSwitchParameterRepository.findByParamName("T24_IP").get().getParamValue();
        final String t24Port = xSwitchParameterRepository.findByParamName("T24_PORT").get().getParamValue();
        t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), tot24);
        //TODO check this
        transactionService.updateT24TransactionDTO(tot24);

        String t24ref = tot24.getT24reference() == null ? "NA" : tot24.getT24reference();
        String t24ProcessingCode =
                tot24.getT24responsecode() == null ? "NA" : tot24.getT24responsecode();
        if (!t24ProcessingCode.equals("1")) {
            log.info("Customer deposit charge transaction failed for [" + depositTnxRRN + "] Deposit charge RRN [" + depositChargeRRN + "] Reason: " + tot24.getT24failnarration());
        } else {
            log.info("Customer deposit charges for transaction [" + depositChargeRRN + "] processed successfully. T24 Reference No [" + t24ref + "]");
        }
    }

    private BPRBranches getBranchDetailsFromAccount(String account) {
        return bprBranchService.fetchBranchAccountsByBranchCode(account);
    }

}
