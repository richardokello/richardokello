package co.ke.tracom.bprgateway.web.wasac.data.customerprofile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "postname",
  "name",
  "zone",
  "mobile",
  "email",
  "phone",
  "personnalid",
  "branch",
  "balance",
  "meterid",
  "customerid"
})
public class Response {

  @JsonProperty("postname")
  private String postname;

  @JsonProperty("name")
  private String name;

  @JsonProperty("zone")
  private String zone;

  @JsonProperty("mobile")
  private String mobile;

  @JsonProperty("email")
  private String email;

  @JsonProperty("phone")
  private String phone;

  @JsonProperty("personnalid")
  private String personnalid;

  @JsonProperty("branch")
  private String branch;

  @JsonProperty("balance")
  private String balance;

  @JsonProperty("meterid")
  private String meterid;

  @JsonProperty("customerid")
  private String customerid;

  @JsonProperty("postname")
  public String getPostname() {
    return postname;
  }

  @JsonProperty("postname")
  public void setPostname(String postname) {
    this.postname = postname;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty("zone")
  public String getZone() {
    return zone;
  }

  @JsonProperty("zone")
  public void setZone(String zone) {
    this.zone = zone;
  }

  @JsonProperty("mobile")
  public String getMobile() {
    return mobile;
  }

  @JsonProperty("mobile")
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  @JsonProperty("email")
  public void setEmail(String email) {
    this.email = email;
  }

  @JsonProperty("phone")
  public String getPhone() {
    return phone;
  }

  @JsonProperty("phone")
  public void setPhone(String phone) {
    this.phone = phone;
  }

  @JsonProperty("personnalid")
  public String getPersonnalid() {
    return personnalid;
  }

  @JsonProperty("personnalid")
  public void setPersonnalid(String personnalid) {
    this.personnalid = personnalid;
  }

  @JsonProperty("branch")
  public String getBranch() {
    return branch;
  }

  @JsonProperty("branch")
  public void setBranch(String branch) {
    this.branch = branch;
  }

  @JsonProperty("balance")
  public String getBalance() {
    return balance;
  }

  @JsonProperty("balance")
  public void setBalance(String balance) {
    this.balance = balance;
  }

  @JsonProperty("meterid")
  public String getMeterid() {
    return meterid;
  }

  @JsonProperty("meterid")
  public void setMeterid(String meterid) {
    this.meterid = meterid;
  }

  @JsonProperty("customerid")
  public String getCustomerid() {
    return customerid;
  }

  @JsonProperty("customerid")
  public void setCustomerid(String customerid) {
    this.customerid = customerid;
  }
}
