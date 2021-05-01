package co.ke.tracom.bprgateway.web.tigopesa.data.transactionStatus;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
public class TransactionStatusResponse {

  @JsonProperty("Result")
  private String Result; // errorCode

  @JsonProperty("Message")
  private String Message; // error Message

  @JsonProperty("transactionId")
  private String transactionId;

  @JsonProperty("referenceId")
  private String referenceId;

  @JsonProperty("status")
  private String status;

  @JsonProperty("voucherCode")
  private int voucherCode;

  @JsonProperty("message")
  private String message;

  @JsonGetter("Message")
  public String getMessage() {
    return this.Message;
  }

  @JsonSetter("Message")
  public void setMessage(String Message) {
    this.Message = Message;
  }

  @JsonGetter("message")
  public String get_message() {
    return this.message;
  }

  @JsonSetter("message")
  public void set_message(String message) {
    this.message = message;
  }
}
