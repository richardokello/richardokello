package co.ke.tracom.bprgateway.web.sendmoneyBulkpayment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMoneydata {
    private String senderMobileNo;
    private String senderNationalID;
    private String senderNationalIDType;
    private String recipientMobileNo;
    private double amount;
    private int count;
}
