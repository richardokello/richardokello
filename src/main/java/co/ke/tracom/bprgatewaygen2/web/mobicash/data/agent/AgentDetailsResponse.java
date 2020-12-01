package co.ke.tracom.bprgatewaygen2.web.mobicash.data.agent;

import co.ke.tracom.bprgatewaygen2.web.mobicash.data.MobiCashResponse;
import lombok.Data;

@Data
public class AgentDetailsResponse extends MobiCashResponse {
    private String MobiCashAccountNumber;
    private String MobiCashAccountName;
    private String MobiCashMobilePhone;
}
