package co.ke.tracom.bprgateway.web.VisionFund.data;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BalanceEnquiryRequest {
    private String accountNumber;
    private String currencyCode = "RWF";
    private String mobileNumber;
    private  String referenceNumber;
}
