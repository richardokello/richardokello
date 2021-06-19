package co.ke.tracom.bprgateway.web.rwandarevenue.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RRAPaymentResponse {
    private String status;
    private String message;
    private RRAPaymentResponseData data;

}
