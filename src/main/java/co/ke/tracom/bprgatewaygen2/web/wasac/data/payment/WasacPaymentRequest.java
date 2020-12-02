package co.ke.tracom.bprgatewaygen2.web.wasac.data.payment;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "username",
        "password",
        "customerid",
        "reference",
        "event",
        "account",
        "operationdate",
        "valuedate",
        "amountcredit",
        "description",
        "terminal"
})
public class WasacPaymentRequest {

    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("customerid")
    private String customerid;
    @JsonProperty("reference")
    private String reference;
    @JsonProperty("event")
    private String event;
    @JsonProperty("account")
    private String account;
    @JsonProperty("operationdate")
    private String operationdate;
    @JsonProperty("valuedate")
    private String valuedate;
    @JsonProperty("amountcredit")
    private String amountcredit;
    @JsonProperty("description")
    private String description;
    @JsonProperty("terminal")
    private String terminal;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("customerid")
    public String getCustomerid() {
        return customerid;
    }

    @JsonProperty("customerid")
    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    @JsonProperty("reference")
    public String getReference() {
        return reference;
    }

    @JsonProperty("reference")
    public void setReference(String reference) {
        this.reference = reference;
    }

    @JsonProperty("event")
    public String getEvent() {
        return event;
    }

    @JsonProperty("event")
    public void setEvent(String event) {
        this.event = event;
    }

    @JsonProperty("account")
    public String getAccount() {
        return account;
    }

    @JsonProperty("account")
    public void setAccount(String account) {
        this.account = account;
    }

    @JsonProperty("operationdate")
    public String getOperationdate() {
        return operationdate;
    }

    @JsonProperty("operationdate")
    public void setOperationdate(String operationdate) {
        this.operationdate = operationdate;
    }

    @JsonProperty("valuedate")
    public String getValuedate() {
        return valuedate;
    }

    @JsonProperty("valuedate")
    public void setValuedate(String valuedate) {
        this.valuedate = valuedate;
    }

    @JsonProperty("amountcredit")
    public String getAmountcredit() {
        return amountcredit;
    }

    @JsonProperty("amountcredit")
    public void setAmountcredit(String amountcredit) {
        this.amountcredit = amountcredit;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("terminal")
    public String getTerminal() {
        return terminal;
    }

    @JsonProperty("terminal")
    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
