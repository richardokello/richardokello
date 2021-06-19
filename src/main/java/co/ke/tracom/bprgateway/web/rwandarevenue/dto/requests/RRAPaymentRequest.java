package co.ke.tracom.bprgateway.web.rwandarevenue.dto.requests;

import co.ke.tracom.bprgateway.web.rwandarevenue.dto.responses.RRATINValidationResponse;
import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RRAPaymentRequest {
    private MerchantAuthInfo credentials;
    RRATINValidationResponse data;
}
