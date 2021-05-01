package co.ke.tracom.bprgateway.web.agenttransactions.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentTransactionRequest {
    String agentId;
    String transactionType;
    double amount;
}
