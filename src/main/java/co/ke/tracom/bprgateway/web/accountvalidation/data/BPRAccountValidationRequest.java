package co.ke.tracom.bprgateway.web.accountvalidation.data;

import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BPRAccountValidationRequest {
    private String channel = "1510";
    private String accountNo;
    MerchantAuthInfo credentials;
}
