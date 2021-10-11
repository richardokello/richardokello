package co.ke.tracom.bprgateway.web.depositmoney.data.requests;

import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositMoneyRequest {
    private String accountNumber;
    private String accountName;
    private double amount;
    private String narration;
    private MerchantAuthInfo credentials;

}
