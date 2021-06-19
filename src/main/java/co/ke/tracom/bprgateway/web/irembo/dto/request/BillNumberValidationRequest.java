package co.ke.tracom.bprgateway.web.irembo.dto.request;

import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillNumberValidationRequest {
    private MerchantAuthInfo credentials;
    private String customerBillNo;
    private String customerName;
    private String amount;
    private String purpose;
}
