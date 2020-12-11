package co.ke.tracom.bprgatewaygen2.web.ltss.data.checkPayment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CheckPaymentRequest {

  @ApiModelProperty(
      name = "External reference number",
      value = "The Payment Service Provider’s system Reference number for the transaction (40)",
      required = true)
  private String extReferenceNo;
}
