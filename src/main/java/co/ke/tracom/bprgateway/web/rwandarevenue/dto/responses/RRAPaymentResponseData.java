package co.ke.tracom.bprgateway.web.rwandarevenue.dto.responses;

import co.ke.tracom.bprgateway.web.util.data.BaseResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RRAPaymentResponseData extends BaseResponseData {
    double transactionCharges;
    String T24Reference;
    String rrn;
    String tid;
    String mid;
    String RRAReference;
    String taxPayerName;
}
