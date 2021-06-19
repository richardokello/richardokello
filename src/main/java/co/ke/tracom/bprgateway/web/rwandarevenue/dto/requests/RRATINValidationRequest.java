package co.ke.tracom.bprgateway.web.rwandarevenue.dto.requests;

import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RRATINValidationRequest {
    private MerchantAuthInfo credentials;
    private String RRATIN;
}
