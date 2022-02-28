package co.ke.tracom.bprgateway.web.ltss.data.checkPayment;

import co.ke.tracom.bprgateway.web.ltss.data.nationalIDValidation.NationalIDValidationResponse;
import lombok.Data;

@Data
public class CheckPaymentResponse {
    private String status;
    private String message;
    private NationalIDValidationResponse beneficiary;
    private String amount;
    private String intermediary;
    private String extReferenceNo;
    private String refNo;
    private String paymentDate;
}