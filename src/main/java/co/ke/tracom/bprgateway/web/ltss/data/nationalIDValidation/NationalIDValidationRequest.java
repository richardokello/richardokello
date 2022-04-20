package co.ke.tracom.bprgateway.web.ltss.data.nationalIDValidation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NationalIDValidationRequest {

  @ApiModelProperty(
      name = "Identification",
      value = "Subscriber's national ID number (16)",
      required = true)
  private String identification;
  private String name;
}
