package co.ke.tracom.bprgateway.web.util.services;

import co.ke.tracom.bprgateway.core.tracomchannels.json.RestHTTPService;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.exceptions.custom.InterServiceCommunicationException;
import co.ke.tracom.bprgateway.web.exceptions.custom.InvalidAgentCredentialsException;
import co.ke.tracom.bprgateway.web.transactionLimits.entity.TransactionLimitManager;
import co.ke.tracom.bprgateway.web.transactionLimits.repository.TransactionLimitManagerRepository;
import co.ke.tracom.bprgateway.web.util.ResponseCodes;
import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
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


    @Value("${merchant.account.validation}")
    private String agentValidationUrl;

    private final TransactionLimitManagerRepository transactionLimitManagerRepository;
    private final RestHTTPService restHTTPService;

    public AuthenticateAgentResponse authenticateAgentUsernamePassword(MerchantAuthInfo merchantAuthInfo) {
        try {
            ResponseEntity<String> stringResponseEntity = restHTTPService.postRequest(merchantAuthInfo, agentValidationUrl);
            log.info("Response status from URL[" + agentValidationUrl + "]" + stringResponseEntity.getStatusCode());

            String body = stringResponseEntity.getBody();
            if (stringResponseEntity.getStatusCode().is2xxSuccessful()) {
                return new ObjectMapper().readValue(body, AuthenticateAgentResponse.class);
            } else {
                throw new InvalidAgentCredentialsException("Agent credentials validation failed");
            }
        } catch (Exception e) {
            throw new InterServiceCommunicationException("Inter-service communication error. Please try again!");
        }
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
