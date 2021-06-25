package co.ke.tracom.bprgateway.web.agenttransactions.dto.response;

import co.ke.tracom.bprgateway.web.util.data.BaseResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgentTransactionResponse extends BaseResponseData {
    private String status;
    private String message;
    private double transactionCharges;
    private String T24Reference;
    private String rrn;
}
