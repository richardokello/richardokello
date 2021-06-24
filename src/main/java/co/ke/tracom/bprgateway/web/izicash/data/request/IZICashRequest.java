package co.ke.tracom.bprgateway.web.izicash.data.request;

import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IZICashRequest {
    private String customerPAN;
    private String passCode;
    private String mobileNo;
    private long amount;
    private MerchantAuthInfo credentials;
}
