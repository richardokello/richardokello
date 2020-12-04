package co.ke.tracom.bprgatewaygen2.web.ltss.data.paymentContribution;

import co.ke.tracom.bprgatewaygen2.web.ltss.data.nationalIDValidation.NationalIDValidationRequest;
import lombok.Data;

import java.util.Date;

@Data
public class PaymentContributionRequest {
    private NationalIDValidationRequest beneficiary;
    private String amount;
    private String description;
    private String intermediary;
    private String extReferenceNo;
    private String paymentDate;
}
