package co.ke.tracom.bprgateway.web.eucl.dto.response;

import co.ke.tracom.bprgateway.web.util.data.BaseResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseData extends BaseResponseData {
    String t24Reference;
    String token;
    String unitsInKW;
    String rrn;
}
