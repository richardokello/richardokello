package co.ke.tracom.bprgateway.web.eucl.dto.request;

import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeterNoValidation {
    private String amount;
//    private String phoneNo;
    private String meterNo;
    private String source;
    private MerchantAuthInfo credentials;
}
