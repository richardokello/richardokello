package co.ke.tracom.bprgateway.web.ltss.data.paymentContribution;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LTSSPaymentResponse {
    String status;
    String message;
    PaymentContributionResponse data;
}
