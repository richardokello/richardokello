package co.ke.tracom.bprgateway.web.agenttransactions.dto.request;

import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import co.ke.tracom.bprgateway.web.util.data.MerchantInfoDeposit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentTransactionRequest {
    private MerchantAuthInfo credentials;
    private MerchantInfoDeposit depositCredentials;
    private String customerAgentId;
   private String customerAgentPass;
    private long amount;
}
