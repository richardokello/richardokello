package co.ke.tracom.bprgateway.web.VisionFund.data;

import co.ke.tracom.bprgateway.servers.tcpserver.dto.GenericRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountDepositRequest extends GenericRequest {
    private String accountNumber;
    private String currencyCode = "RWF";
    private String referenceNumber;
    private String mobileNumber;
    private String tranDesc;
    private Long amount;
    private String nationalID;

}
