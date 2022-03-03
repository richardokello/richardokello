package co.ke.tracom.bprgateway.web.VisionFund.data.custom;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CustomVerificationResponse {
    private String responseCode;
    private String responseString;
    private String txnReference;
    private Date txnDateTime;
    private String custNo;
    private String category;
    private String status;
    private String branchId;
    private String custNm;
}
