package co.ke.tracom.bprgateway.web.rwandarevenue.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RRAPaymentResponseData {
    double transactionCharges;
    String T24Reference;

}
