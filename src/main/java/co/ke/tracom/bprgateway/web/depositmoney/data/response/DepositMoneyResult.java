package co.ke.tracom.bprgateway.web.depositmoney.data.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepositMoneyResult {
    private String status;
    private String message;
    private DepositMoneyResultData data;

}
