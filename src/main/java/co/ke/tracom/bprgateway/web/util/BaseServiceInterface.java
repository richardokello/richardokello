package co.ke.tracom.bprgateway.web.util;

import co.ke.tracom.bprgateway.web.sendmoney.data.response.ValidateMerchantDetailsResponse;
import co.ke.tracom.bprgateway.web.util.data.Field47Data;

import java.util.Map;

public interface BaseServiceInterface {
  ValidateMerchantDetailsResponse login(Field47Data loginRequest);

  ValidateMerchantDetailsResponse validateMerchantData(Field47Data loginRequest);

  Map<String,String> validateTransactionLimits(String mti, String processingCode, String amount);
}
