package co.ke.tracom.bprgateway.web.eucl.dto.request;

import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeterNoValidation {
    private String meterNo;
    private String phoneNo;
    private String amount;
    private MerchantAuthInfo credentials;
}
