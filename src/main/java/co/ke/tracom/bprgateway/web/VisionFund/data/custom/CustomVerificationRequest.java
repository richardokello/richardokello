package co.ke.tracom.bprgateway.web.VisionFund.data.custom;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class CustomVerificationRequest {
    private String accountNumber;
    private String referenceNumber;
    private String mobileNumber;
    private String tranDesc;
}
