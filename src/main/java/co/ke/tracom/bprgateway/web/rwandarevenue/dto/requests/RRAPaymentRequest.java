package co.ke.tracom.bprgateway.web.rwandarevenue.dto.requests;

import co.ke.tracom.bprgateway.web.rwandarevenue.dto.responses.RRATINValidationResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RRAPaymentRequest {
    String agentId;
    RRATINValidationResponse data;
}
