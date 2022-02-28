package co.ke.tracom.bprgateway.web.ltss.data.paymentContribution;

import co.ke.tracom.bprgateway.web.ltss.data.nationalIDValidation.NationalIDValidationResponse;
import lombok.Data;

import java.util.Date;

@Data
public class PaymentContributionResponse {

  private NationalIDValidationResponse beneficiary;
  private String amount;
  private String description;
  private String intermediary;
  private String extReferenceNo;
  private String refNo;
  private String paymentDate;
}
