package co.ke.tracom.bprgateway.web.agenttransactions.controller;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.request.AgentBalanceInquiryRequest;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.request.AgentTransactionRequest;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AgentBalanceInquiryResponse;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AgentTransactionResponse;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.agenttransactions.services.AgentTransactionService;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import co.ke.tracom.bprgateway.web.util.services.UtilityService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/agent-transactions")
@RestController
public class AgentTransactionController {
    private final AgentTransactionService agentTransactionService;
    private final BaseServiceProcessor baseServiceProcessor;
    private final UtilityService utilityService;

    @Value("${merchant.account.validation}")
    private String agentValidation;


    @ApiOperation(value = "Process agent deposit", response = AgentTransactionResponse.class)
    @PostMapping(value = "/deposit")
    public ResponseEntity<?> agentDeposit(@RequestBody AgentTransactionRequest request) {
        AgentTransactionResponse response = agentTransactionService.processAgentFloatDeposit(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Process agent withdrawal", response = AgentTransactionResponse.class)
    @PostMapping(value = "/withdrawal")
    public ResponseEntity<?> agentWithdrawal(@RequestBody AgentTransactionRequest request) {

        String transactionReferenceNo = RRNGenerator.getInstance("AW").getRRN();
        log.info("Incoming agent withdrawal request [" + transactionReferenceNo + "]");
        AgentTransactionResponse response = agentTransactionService.processAgentFloatWithdrawal(request, transactionReferenceNo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Fetch agent account balance", response = AgentBalanceInquiryResponse.class)
    @PostMapping(value = "/balance-inquiry")
    public ResponseEntity<AgentBalanceInquiryResponse> agentBalanceInquiry(@RequestBody AgentBalanceInquiryRequest request) {

        AgentBalanceInquiryResponse response = AgentBalanceInquiryResponse.builder().build();
        Optional<AuthenticateAgentResponse> optionalAuthenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials(), agentValidation);
        if (optionalAuthenticateAgentResponse.isEmpty()) {
            log.info("Agent Float Deposit:[Failed] Missing agent information.");
            response.setStatus("117");
            response.setMessage("Missing agent information");
        } else if (optionalAuthenticateAgentResponse.get().getCode() != HttpStatus.OK.value()) {
            response.setStatus(String.valueOf(
                    optionalAuthenticateAgentResponse.get().getCode())
            );
            response.setMessage(optionalAuthenticateAgentResponse.get().getMessage());
        } else {
            String accountNumber = optionalAuthenticateAgentResponse.get().getData().getAccountNumber();
            Long agentAccountBalance = agentTransactionService.fetchAgentAccountBalanceOnly(accountNumber);
            response.setStatus("00");
            response.setMessage("Balance inquiry processed successfully");
            response.setBalance(utilityService. formatDecimal(agentAccountBalance));
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
