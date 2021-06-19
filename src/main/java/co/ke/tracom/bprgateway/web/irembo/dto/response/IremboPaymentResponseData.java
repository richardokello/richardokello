package co.ke.tracom.bprgateway.web.irembo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IremboPaymentResponseData {
    String t24Reference;
}
