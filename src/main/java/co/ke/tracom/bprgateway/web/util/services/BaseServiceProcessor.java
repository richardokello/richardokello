package co.ke.tracom.bprgateway.web.util.services;

import co.ke.tracom.bprgateway.web.sendmoney.data.response.ValidateMerchantDetailsResponse;
import co.ke.tracom.bprgateway.web.transactionLimits.entity.TransactionLimitManager;
import co.ke.tracom.bprgateway.web.transactionLimits.repository.TransactionLimitManagerRepository;
import co.ke.tracom.bprgateway.web.util.BaseServiceInterface;
import co.ke.tracom.bprgateway.web.util.ResponseCodes;
import co.ke.tracom.bprgateway.web.util.data.Field47Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BaseServiceProcessor implements BaseServiceInterface {
  private final TransactionLimitManagerRepository transactionLimitManagerRepository;

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
