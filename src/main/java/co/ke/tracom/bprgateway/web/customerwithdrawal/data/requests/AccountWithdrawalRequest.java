package co.ke.tracom.bprgateway.web.customerwithdrawal.data.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountWithdrawalRequest {
    private String panReceived;
    private String passCodeReceived;

}
