package co.ke.tracom.bprgateway.web.depositmoney.data.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@Builder
public class DepositMoneyResult {
    private String status;
    private String message;
    private DepositMoneyResultData data;

}
