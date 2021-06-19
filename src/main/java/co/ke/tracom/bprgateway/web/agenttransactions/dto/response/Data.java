package co.ke.tracom.bprgateway.web.agenttransactions.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "username",
        "names",
        "workgroup",
        "tid",
        "tidStatus",
        "accountNumber",
        "mid"
})
public class Data {

    @JsonProperty("username")
    private String username;
    @JsonProperty("names")
    private String names;
    @JsonProperty("workgroup")
    private Object workgroup;
    @JsonProperty("tid")
    private Object tid;
    @JsonProperty("tidStatus")
    private Boolean tidStatus;
    @JsonProperty("accountNumber")
    private String accountNumber;
    @JsonProperty("mid")
    private Object mid;

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
    public Object getWorkgroup() {
        return workgroup;
    }

    @JsonProperty("workgroup")
    public void setWorkgroup(Object workgroup) {
        this.workgroup = workgroup;
    }

    @JsonProperty("tid")
    public Object getTid() {
        return tid;
    }

    @JsonProperty("tid")
    public void setTid(Object tid) {
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
    public Object getMid() {
        return mid;
    }

    @JsonProperty("mid")
    public void setMid(Object mid) {
        this.mid = mid;
    }

}