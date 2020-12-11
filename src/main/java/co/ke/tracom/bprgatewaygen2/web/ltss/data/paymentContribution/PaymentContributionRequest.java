package co.ke.tracom.bprgatewaygen2.web.ltss.data.paymentContribution;

import co.ke.tracom.bprgatewaygen2.web.ltss.data.nationalIDValidation.NationalIDValidationRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PaymentContributionRequest {
  @ApiModelProperty(name = "Beneficiary",
      value = "Payment beneficiary",
      example = "{ 'identification': '12233344555644', 'name': 'RWAHUNGU Eric'",
      required = true)
  private NationalIDValidationRequest beneficiary;
  private String amount;
  @ApiModelProperty(name = "Description",
      value = "Reason for payment ",
      required = true)
  private String description;
  @ApiModelProperty(name = "Intermediary",
      value = "Payment Service Provider’s name (60)",
      required = true)
  private String intermediary;

  @ApiModelProperty(
      name = "extReferenceNo",
      value = "The Payment Service Provider’s system Reference number for the transaction",
      required = true)
  private String extReferenceNo;

  private String paymentDate;
}
