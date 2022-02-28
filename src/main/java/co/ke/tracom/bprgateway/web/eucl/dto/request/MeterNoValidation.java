package co.ke.tracom.bprgateway.web.eucl.dto.request;

import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeterNoValidation {
<<<<<<< HEAD
    private String meterNo;
    private String phoneNo;
    private long amount;
=======
    private String amount;
    private String phoneNo;
    private String meterNo;
>>>>>>> b3145de46f0a14cf9880671dbd2fab347bdf95fb
    private MerchantAuthInfo credentials;
}
