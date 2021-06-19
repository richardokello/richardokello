package co.ke.tracom.bprgateway.web.customerwithdrawal.data.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WithdrawalMoneyResultData {
       private String t24Reference;
       private String charges;
}
