package co.ke.tracom.bprgateway.web.tigopesa.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TigopesaRequest {

  private String username;
  private String password;

  @ApiModelProperty(
      name = "MSISDN",
      value = "The account mobile number including country code",
      required = true)
  private String msisdn;

  @ApiModelProperty(name = "MSISDN", value = "The billing number", required = true)
  private String billNumber;

  @ApiModelProperty(name = "MSISDN", value = "The billing company code", required = true)
  private String companyCode;

  @ApiModelProperty(hidden = true)
  private String requestXML;

  @ApiModelProperty(
      name = "Transaction ID",
      value = "The sales order number, a Telepin internal reference number.",
      required = true)
  private String transactionId;
}
