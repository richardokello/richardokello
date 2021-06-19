package co.ke.tracom.bprgateway.web.eucl.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EUCLPaymentResponse {
    String status;
    String message;
    PaymentResponseData data;
}
