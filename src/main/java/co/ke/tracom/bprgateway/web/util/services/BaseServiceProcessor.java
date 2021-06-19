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
            if (stringResponseEntity.getStatusCode().is2xxSuccessful()) {
                AuthenticateAgentResponse authenticateAgentResponse = new ObjectMapper().readValue(stringResponseEntity.getBody(), AuthenticateAgentResponse.class);
                return Optional.ofNullable(authenticateAgentResponse);
            }
        } catch (Exception e) {
            log.error(" Failed ");
        }
        return Optional.ofNullable(null);
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
