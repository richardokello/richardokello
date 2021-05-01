package co.ke.tracom.bprgateway.web.util.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"appVersion", "userName", "userpassword", "userworkgroup", "terminalSerialNo", "terminalpin", "userID"})
public class Field47Data {
  @JsonProperty("appVersion")
  private String appVersion;

  @JsonProperty("userName")
  private String userName;

  @JsonProperty("userpassword")
  private String userpassword;

  @JsonProperty("userworkgroup")
  private String userworkgroup;

  @JsonProperty("terminalSerialNo")
  private String terminalSerialNo;

  @JsonProperty("terminalpin")
  private String terminalpin;

  @JsonProperty("userID")
  private String userID;

  @Override
  public String toString() {
    return "Field47Data{" +
            "appVersion='" + appVersion + '\'' +
            ", userName='" + userName + '\'' +
            ", userpassword='" + userpassword + '\'' +
            ", userworkgroup='" + userworkgroup + '\'' +
            ", terminalSerialNo='" + terminalSerialNo + '\'' +
            ", terminalpin='" + terminalpin + '\'' +
            ", userID='" + userID + '\'' +
            '}';
  }
}
