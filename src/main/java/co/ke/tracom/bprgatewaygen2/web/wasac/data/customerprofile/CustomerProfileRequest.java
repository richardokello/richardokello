package co.ke.tracom.bprgatewaygen2.web.wasac.data.customerprofile;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CustomerProfileRequest {

  @ApiModelProperty(
      name = "Customer ID",
      value = "Unique customer identifier",
      required = true)
  private String customerId;
}
