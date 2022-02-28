package co.ke.tracom.bprgateway.web.agenttransactions.dto.request;

import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import co.ke.tracom.bprgateway.web.util.data.MerchantcustomerInfoDeposit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentTransactionRequest {
    private String customerAgentId;
    private long amount;
    private MerchantAuthInfo credentials;
   private MerchantcustomerInfoDeposit depositCredentials;

  private String customerAgentPass;

}
