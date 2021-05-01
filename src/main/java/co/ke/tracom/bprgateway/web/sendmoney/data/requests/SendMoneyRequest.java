package co.ke.tracom.bprgateway.web.sendmoney.data.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMoneyRequest {
    private String senderMobileNo;
    private String senderNationalID;
    private String senderNationalIDType;
    private String recipientMobileNo;
    private double amount;

}
