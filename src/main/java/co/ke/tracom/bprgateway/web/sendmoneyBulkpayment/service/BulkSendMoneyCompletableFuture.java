package co.ke.tracom.bprgateway.web.sendmoneyBulkpayment.service;

import co.ke.tracom.bprgateway.web.agenttransactions.services.AgentTransactionService;
import co.ke.tracom.bprgateway.web.bankbranches.service.BPRBranchService;
import co.ke.tracom.bprgateway.web.sendmoney.repository.MoneySendRepository;
import co.ke.tracom.bprgateway.web.sendmoney.services.BPRCreditCardNumberGenerator;
import co.ke.tracom.bprgateway.web.sendmoney.services.DesUtil;
import co.ke.tracom.bprgateway.web.sendmoney.services.SendMoneyService;
import co.ke.tracom.bprgateway.web.sms.services.SMSService;
import co.ke.tracom.bprgateway.web.smsscheduled.repository.ScheduledSMSRepository;
import co.ke.tracom.bprgateway.web.switchparameters.XSwitchParameterService;
import co.ke.tracom.bprgateway.web.t24communication.services.T24Channel;
import co.ke.tracom.bprgateway.web.transactionLimits.TransactionLimitManagerService;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import co.ke.tracom.bprgateway.web.util.services.UtilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class BulkSendMoneyCompletableFuture {

    public final String CURRENCY_ISO_FORMAT = "%012d";
    private final String SEND_MONEY_TRANSACTION_LOG_LABEL = "Send money transaction [";
    private final String SEND_MONEY_SUSPENSE_ACC = "SENDMONEYSUSPENSE";
    private final String SEND_MONEY_LABEL = "SEND MONEY";

    private final Long SENDMONEY_TRANSACTION_LIMIT_ID = 1L;
    private final Long RECEIVEMONEY_TRANSACTION_LIMIT_ID = 2L;
    private final AgentTransactionService agentTransactionService;
    private final BaseServiceProcessor baseServiceProcessor;
    private final BPRBranchService branchService;
    private final UtilityService utilityService;
    private final T24Channel t24Channel;
    private final TransactionService transactionService;
    private final BPRCreditCardNumberGenerator bprCreditCardNumberGenerator;
    private final DesUtil desUtil;
    private final SMSService smsService;

    private final XSwitchParameterService xSwitchParameterService;
    private final MoneySendRepository moneySendRepository;
    private final ScheduledSMSRepository scheduledSMSRepository;
    private final TransactionLimitManagerService limitManagerService;
    @Autowired
    private SendMoneyService sendMoneyService;
    private long messageId=0;
}
