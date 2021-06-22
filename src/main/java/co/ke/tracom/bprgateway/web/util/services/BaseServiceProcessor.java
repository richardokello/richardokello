package co.ke.tracom.bprgateway.web.util.services;

import co.ke.tracom.bprgateway.core.tracomchannels.json.RestHTTPService;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.sendmoney.data.response.ValidateMerchantDetailsResponse;
import co.ke.tracom.bprgateway.web.transactionLimits.entity.TransactionLimitManager;
import co.ke.tracom.bprgateway.web.transactionLimits.repository.TransactionLimitManagerRepository;
import co.ke.tracom.bprgateway.web.util.BaseServiceInterface;
import co.ke.tracom.bprgateway.web.util.ResponseCodes;
import co.ke.tracom.bprgateway.web.util.data.Field47Data;
import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class BaseServiceProcessor implements BaseServiceInterface {
    private final TransactionLimitManagerRepository transactionLimitManagerRepository;
    private final RestHTTPService restHTTPService;

    public Optional<AuthenticateAgentResponse> authenticateAgentUsernamePassword(MerchantAuthInfo merchantAuthInfo, String url) {
        try {
            ResponseEntity<String> stringResponseEntity = restHTTPService.postRequest(merchantAuthInfo, url);
            log.info("Response status from URL[" + url + "]" + stringResponseEntity.getStatusCode());

            String body = stringResponseEntity.getBody();
            if (stringResponseEntity.getStatusCode().is2xxSuccessful()) {
                AuthenticateAgentResponse authenticateAgentResponse = new ObjectMapper().readValue(body, AuthenticateAgentResponse.class);
                return Optional.of(authenticateAgentResponse);
            } else {
                AuthenticateAgentResponse response = new AuthenticateAgentResponse();
                response.setCode(stringResponseEntity.getStatusCode().value());
                response.setMessage("Agent validation failed.");
                return Optional.of(response);
            }
        } catch (Exception e) {
            log.error(" Failed " + e.getMessage());
            AuthenticateAgentResponse response = new AuthenticateAgentResponse();
            response.setCode(HttpStatus.BAD_GATEWAY.value());
            response.setMessage("Unable to fetch agent validation information. Please try again!");
            return Optional.of(response);
        }
    }

    @Override
    public ValidateMerchantDetailsResponse login(Field47Data loginRequest) {

        return null;
    }

    @Override
    public ValidateMerchantDetailsResponse validateMerchantData(Field47Data loginRequest) {
        return null;
    }

    @Override
    public Map<String, String> validateTransactionLimits(
            String mti, String processingCode, String amount) {
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

    private void validateTransactionAmount(
            Map<String, String> results, String amount, TransactionLimitManager transactionLimitManager) {
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
