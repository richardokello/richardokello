package co.ke.tracom.bprgateway.web.depositmoney.data.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"username", "names", "workgroup", "tid", "tidStatus", "accountNumber", "mid"})
public class MerchantDetailsData {

  @JsonProperty("username")
  private String username;

  @JsonProperty("names")
  private String names;

  @JsonProperty("workgroup")
  private List<String> workgroup = null;

  @JsonProperty("tid")
  private String tid;

  @JsonProperty("tidStatus")
  private Boolean tidStatus;

  @JsonProperty("accountNumber")
  private String accountNumber;

  @JsonProperty("mid")
  private String mid;

  @JsonProperty("username")
  public String getUsername() {
    return username;
  }

  @JsonProperty("username")
  public void setUsername(String username) {
    this.username = username;
  }

  @JsonProperty("names")
  public String getNames() {
    return names;
  }

  @JsonProperty("names")
  public void setNames(String names) {
    this.names = names;
  }

  @JsonProperty("workgroup")
  public List<String> getWorkgroup() {
    return workgroup;
  }

  @JsonProperty("workgroup")
  public void setWorkgroup(List<String> workgroup) {
    this.workgroup = workgroup;
  }

  @JsonProperty("tid")
  public String getTid() {
    return tid;
  }

  @JsonProperty("tid")
  public void setTid(String tid) {
    this.tid = tid;
  }

  @JsonProperty("tidStatus")
  public Boolean getTidStatus() {
    return tidStatus;
  }

  @JsonProperty("tidStatus")
  public void setTidStatus(Boolean tidStatus) {
    this.tidStatus = tidStatus;
  }

  @JsonProperty("accountNumber")
  public String getAccountNumber() {
    return accountNumber;
  }

  @JsonProperty("accountNumber")
  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  @JsonProperty("mid")
  public String getMid() {
    return mid;
  }

  @JsonProperty("mid")
  public void setMid(String mid) {
    this.mid = mid;
  }
}
