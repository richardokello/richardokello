package co.ke.tracom.bprgateway.web.rwandarevenue.dto.requests;

import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RRAPaymentRequest {
    private MerchantAuthInfo credentials;
    private String taxTypeDescription;
    private String declarationDate;
    private int taxCentreNo;
    private String TIN;
    private int RRAOriginNo;
    private long declarationID;
    private String taxPayerName;
    private String requestDate;
    private String taxCentreDescription;
    private int taxTypeNo;
    private long assessNo;
    private double amountToPay;
    private String RRAReferenceNo;
}
