package co.ke.tracom.bprgateway.web.irembo.dto.response;

import co.ke.tracom.bprgateway.web.eucl.dto.response.PaymentResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IremboPaymentResponse {
    String status;
    String message;
    IremboPaymentResponseData data;
}
