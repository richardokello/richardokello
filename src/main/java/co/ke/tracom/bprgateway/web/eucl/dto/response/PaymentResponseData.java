package co.ke.tracom.bprgateway.web.eucl.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseData {
    String t24Reference;
    String token;
    String unitsInKW;
    String rrn;
}
