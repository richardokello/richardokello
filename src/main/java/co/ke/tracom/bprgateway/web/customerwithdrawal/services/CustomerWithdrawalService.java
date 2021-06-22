package co.ke.tracom.bprgateway.web.customerwithdrawal.services;

import co.ke.tracom.bprgateway.web.customerwithdrawal.data.requests.AccountWithdrawalRequest;
import co.ke.tracom.bprgateway.web.customerwithdrawal.data.response.WithdrawMoneyResult;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import co.ke.tracom.bprgateway.web.util.services.UtilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerWithdrawalService {
  private final UtilityService utilityService;
  private final BaseServiceProcessor baseServiceProcessor;


  public WithdrawMoneyResult processAccountWithdrawal(AccountWithdrawalRequest request, String transactionRRN) {
    return null;
  }
}
