package co.ke.tracom.bprgateway.web.irembo.dto.request;

import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IremboBillPaymentRequest {
    private MerchantAuthInfo credentials;
    private String billNo;
    private String billCode;
    private String description;
    private String amount;
    private String currencyCode;
    private String RRAAccountNo;
    private String customerName;
    private String expiryDate;
    private String transactionType;
    private String paymentStatus;
    private String mobileNo;
}
