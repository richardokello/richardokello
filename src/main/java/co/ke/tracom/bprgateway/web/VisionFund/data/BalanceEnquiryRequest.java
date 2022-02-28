package co.ke.tracom.bprgateway.web.VisionFund.data;

import co.ke.tracom.bprgateway.servers.tcpserver.dto.GenericRequest;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BalanceEnquiryRequest extends GenericRequest {
    private String accountNumber;
    private String currencyCode = "RWF";
    private String mobileNumber;
    private  String referenceNumber;
}
