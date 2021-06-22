package co.ke.tracom.bprgateway.web.irembo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IremboValidationData {
    private String customerName;
    private String mobileNo;
    private String billNo;
    private String RRAAccountNo;
    private String amount;
    private String currencyCode;
    private String createdAt;
    private String expiryDate;
    private String transactionType;
    private String paymentStatus;

}
