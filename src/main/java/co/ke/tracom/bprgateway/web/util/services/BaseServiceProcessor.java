package co.ke.tracom.bprgateway.web.util.services;

import co.ke.tracom.bprgateway.core.tracomchannels.json.RestHTTPService;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.exceptions.custom.InterServiceCommunicationException;
import co.ke.tracom.bprgateway.web.exceptions.custom.InvalidAgentCredentialsException;
import co.ke.tracom.bprgateway.web.transactionLimits.entity.TransactionLimitManager;
import co.ke.tracom.bprgateway.web.transactionLimits.repository.TransactionLimitManagerRepository;
import co.ke.tracom.bprgateway.web.transactions.services.TransactionService;
import co.ke.tracom.bprgateway.web.util.ResponseCodes;
import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import co.ke.tracom.bprgateway.web.util.data.MerchantcustomerInfoDeposit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class BaseServiceProcessor {
    private final TransactionService transactionService;


 @Value("${merchant.account.validation}")
// @Value("http://localhost:8787/authenticate/password-validation")
    private String agentValidationUrl;


 @Value("http://192.168.24.30:8787/authenticate/agent-deposit")
  //@Value("http://localhost:8787/authenticate/agent-deposit")
    private String agentDepositValidationUrl;

    private final TransactionLimitManagerRepository transactionLimitManagerRepository;
    public final RestHTTPService restHTTPService;

    public AuthenticateAgentResponse authenticateAgentUsernamePassword(MerchantAuthInfo merchantAuthInfo) throws InvalidAgentCredentialsException, InterServiceCommunicationException {
        ResponseEntity<String> stringResponseEntity = null;
        try {
            stringResponseEntity = restHTTPService.postRequest(merchantAuthInfo, agentValidationUrl);
            log.info("Response status from URL[" + agentValidationUrl + "]" + stringResponseEntity.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new InterServiceCommunicationException("Inter-service communication error. Please try again!");
          }

        // Objects.requireNonNull(stringResponseEntity, "Inter-service communication error. Please try again------>that!");

        String body = stringResponseEntity.getBody();
        AuthenticateAgentResponse authenticateAgentResponse = null;
        try {
            authenticateAgentResponse = new ObjectMapper().readValue(body, AuthenticateAgentResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new InvalidAgentCredentialsException("Agent credentials validation failed.");
        }

        if (authenticateAgentResponse.getCode() == 200 && stringResponseEntity.getStatusCode().value() == 200) {
            return authenticateAgentResponse;
        } else {
            System.out.println("authenticateAgentResponse = " + authenticateAgentResponse);
            throw new InvalidAgentCredentialsException("Agent credentials validation failed");

        }
    }


    public AuthenticateAgentResponse authenticateAgentDepositUsername(MerchantcustomerInfoDeposit merchantAuthInfoAGentDepo) throws InvalidAgentCredentialsException, InterServiceCommunicationException {
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restHTTPService.postRequest(merchantAuthInfoAGentDepo, agentDepositValidationUrl);
            log.info("Response status from URL[" + agentDepositValidationUrl + " ] and the status is {}" + responseEntity.getBody());
        } catch (Exception j) {
            j.printStackTrace();
            throw new InterServiceCommunicationException("Inter-service communication =========error service validation failed. Please try again!");

        }
        //Objects.requireNonNull(responseEntity,"Inter-service communication error. Please try to use use non null again!");
        String agentDepositbody = responseEntity.getBody();
        AuthenticateAgentResponse authenticateAgentDepositResponse = null;
        try {
            authenticateAgentDepositResponse = new ObjectMapper().readValue(agentDepositbody, AuthenticateAgentResponse.class);
        } catch (Exception j) {
            throw new InvalidAgentCredentialsException("Agent deposit username validation failed");
        }
        if (authenticateAgentDepositResponse.getCode() == 200 && responseEntity.getStatusCode().value() == 200) {
            return authenticateAgentDepositResponse;
        } else {
            throw new InvalidAgentCredentialsException("Agent deposit username validation failed");
        }
    }


    @Data
    @AllArgsConstructor
    static class AuthResponse<T> {
        private String message;
        private Integer code;
        private T data;
        private long timestamp;
    }

    public Map<String, String> validateTransactionLimits(String mti, String processingCode, String amount) {
        Map<String, String> results = new HashMap<>();
        Optional<TransactionLimitManager> optionalTransactionLimit =
                transactionLimitManagerRepository.findByISOMsgMTIAndProcessingCode(mti, processingCode);
        if (optionalTransactionLimit.isPresent()) {
            TransactionLimitManager transactionLimitManager = optionalTransactionLimit.get();
            validateTransactionAmount(results, amount, transactionLimitManager);
        } else {
            results.put("status", ResponseCodes.GENERAL_RESPONSE.getResponseCode());
        }
        return results;
    }

    private void validateTransactionAmount(Map<String, String> results, String amount, TransactionLimitManager transactionLimitManager) {
        long transactionValue = Long.parseLong(amount);

        if (transactionValue > transactionLimitManager.getUpperlimit()
                || transactionValue < transactionLimitManager.getLowerlimit()) {
            results.put("status", ResponseCodes.AMOUNT_OFF_LIMITS.getResponseCode());
            results.put("message", "Transaction amount above limits");
        } else {
            results.put("status", ResponseCodes.PROCESSING_SUCCESS.getResponseCode());
        }
    }
}