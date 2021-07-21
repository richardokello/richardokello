package co.ke.tracom.bprgateway.web.izicash.data.request;

import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IZICashRequest {
    private String secretCode;
    private String pinCode;
    private String mobileNo;
    private long amount;
    private MerchantAuthInfo credentials;
}
