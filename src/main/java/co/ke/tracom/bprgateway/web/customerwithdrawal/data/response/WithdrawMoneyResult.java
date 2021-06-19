package co.ke.tracom.bprgateway.web.customerwithdrawal.data.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WithdrawMoneyResult {
    private String status;
    private String message;
    private WithdrawalMoneyResultData data;

}
