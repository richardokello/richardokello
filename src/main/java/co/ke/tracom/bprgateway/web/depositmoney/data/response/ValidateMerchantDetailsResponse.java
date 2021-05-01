package co.ke.tracom.bprgateway.web.depositmoney.data.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"message", "code", "data"})
public class ValidateMerchantDetailsResponse {
  @JsonProperty("message")
  private String message;

  @JsonProperty("code")
  private String code;

  @JsonProperty("data")
  private Data data;

  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  @JsonProperty("message")
  public void setMessage(String message) {
    this.message = message;
  }

  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  @JsonProperty("code")
  public void setCode(String code) {
    this.code = code;
  }

  @JsonProperty("data")
  public Data getData() {
    return data;
  }

  @JsonProperty("data")
  public void setData(Data data) {
    this.data = data;
  }
}
