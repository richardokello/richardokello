package co.ke.tracom.bprgateway.web.irembo.dto.response;

import co.ke.tracom.bprgateway.web.util.data.BaseResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IremboPaymentResponseData extends BaseResponseData {
    String t24Reference;
    String charges;
}
