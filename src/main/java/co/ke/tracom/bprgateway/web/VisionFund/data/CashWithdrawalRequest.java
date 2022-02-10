package co.ke.tracom.bprgateway.web.VisionFund.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CashWithdrawalRequest {
    private String accountNumber;
    private String currencyCode = "RWF";//*
    private String referenceNumber;//*
    private String mobileNumber;
    private String tranDesc;
    private Long amount;
    private String nationalID;
    private String token;
    private String accountName;//*
}
