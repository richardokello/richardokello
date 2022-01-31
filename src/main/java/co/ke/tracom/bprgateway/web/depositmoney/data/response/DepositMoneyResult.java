package co.ke.tracom.bprgateway.web.depositmoney.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepositMoneyResult {
    private String status;
    private String message;
    private DepositMoneyResultData data;

}
