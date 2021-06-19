package co.ke.tracom.bprgateway.web.customerwithdrawal.services;

import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import co.ke.tracom.bprgateway.web.util.services.UtilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerWithdrawalService {
  private final UtilityService utilityService;
  private final BaseServiceProcessor baseServiceProcessor;

}
