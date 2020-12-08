package co.ke.tracom.bprgatewaygen2.web.tigopesa.data.checkBalance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CheckBalanceResponse {

  @JsonProperty("Result")
  private String Result; // errorCode

  @JsonProperty("Message")
  private String Message; // error Message

  @JsonProperty("referenceId")
  private String referenceId;

  @JsonProperty("amount")
  private float amount;

}
