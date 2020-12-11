package co.ke.tracom.bprgatewaygen2.web.ltss.data.newSubscriber;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NewSubscriberRequest {

  @ApiModelProperty(name = "Identification",
      value = "Subscriber's national ID number (16)",
      required = true)
  private String identification;
  @ApiModelProperty(name = "Phone number ",
      required = true)
  private String phone;
  private String occupation;
  private int frequency;
  private long amount;
}
