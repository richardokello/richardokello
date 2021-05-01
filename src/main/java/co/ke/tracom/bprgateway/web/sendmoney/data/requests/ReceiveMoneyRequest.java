package co.ke.tracom.bprgateway.web.sendmoney.data.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class  ReceiveMoneyRequest {
    private String receiverMobileNo;
    private String passCode;
    private double amount;
    private String agentId;
}
