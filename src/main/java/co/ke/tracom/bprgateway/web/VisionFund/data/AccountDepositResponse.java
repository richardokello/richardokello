package co.ke.tracom.bprgateway.web.VisionFund.data;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AccountDepositResponse {
    private String responseCode;
    private String responseString;
    private double availBalance;
    private double ledgerBalance;
    private String txnReference;
    private Date   txnDateTime;
}
