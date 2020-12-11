package co.ke.tracom.bprgatewaygen2.web.mobicash.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MobiCashRequest {

  @ApiModelProperty(
      name = "Client code",
      value = "Bank partner Identifier code. Provided by MobiCash",
      required = true)
  private String clientCode;
  @ApiModelProperty(
      name = "Transaction Identifier",
      value = "Bank partner unique number",
      required = true)
  private String transactionIdentifier;
  @ApiModelProperty(
      name = "Transaction Identifier",
      value = "Client/Agent account number. Can be phone number or MCash ID",
      required = true)
  private String accountNumber;
  @ApiModelProperty(
      name = "Authorization",
      value = "Dynamic token",
      required = true)
  private String authorization;
}
