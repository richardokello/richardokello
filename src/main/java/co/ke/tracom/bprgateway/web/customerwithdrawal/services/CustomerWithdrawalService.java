package co.ke.tracom.bprgateway.web.customerwithdrawal.services;

import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import co.ke.tracom.bprgateway.web.util.PaymentProcessorInterface;
import co.ke.tracom.bprgateway.web.util.services.UtilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerWithdrawalService implements PaymentProcessorInterface {
  private final UtilityService utilityService;
  private final BaseServiceProcessor baseServiceProcessor;

  @Override
  public void inquiry() {

  }

  @Override
  public void authorize() {}

  @Override
  public void checkProcessingStatus() {}
}
