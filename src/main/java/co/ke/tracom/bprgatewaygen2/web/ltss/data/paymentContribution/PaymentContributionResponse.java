package co.ke.tracom.bprgatewaygen2.web.ltss.data.paymentContribution;

import co.ke.tracom.bprgatewaygen2.web.ltss.data.nationalIDValidation.NationalIDValidationResponse;
import java.util.Date;
import lombok.Data;

@Data
public class PaymentContributionResponse {

  private NationalIDValidationResponse beneficiary;
  private String amount;
  private String description;
  private String intermediary;
  private String extReferenceNo;
  private String refNo;
  private Date paymentDate;
}
