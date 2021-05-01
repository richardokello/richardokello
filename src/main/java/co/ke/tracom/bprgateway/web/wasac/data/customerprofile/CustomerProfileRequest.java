package co.ke.tracom.bprgateway.web.wasac.data.customerprofile;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerProfileRequest {

  @ApiModelProperty(name = "Customer ID", value = "Unique customer identifier", required = true)
  private String customerId;
}
