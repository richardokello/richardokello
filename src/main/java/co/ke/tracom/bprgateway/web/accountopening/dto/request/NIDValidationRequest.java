package co.ke.tracom.bprgateway.web.accountopening.dto.request;

import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NIDValidationRequest {
    String nationalID;
    String IDType="0";
    MerchantAuthInfo credentials;

}
