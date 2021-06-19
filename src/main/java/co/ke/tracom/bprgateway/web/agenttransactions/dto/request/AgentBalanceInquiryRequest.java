package co.ke.tracom.bprgateway.web.agenttransactions.dto.request;

import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentBalanceInquiryRequest {
    MerchantAuthInfo credentials;
}
