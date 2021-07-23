package co.ke.tracom.bprgateway.web.sms.dto.fbismsapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "success",
        "message",
        "cost",
        "msgRef",
        "gatewayRef"
})
@ToString
public class FDISMSResponse {

    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("message")
    private String message;
    @JsonProperty("cost")
    private String cost;
    @JsonProperty("msgRef")
    private String msgRef;
    @JsonProperty("gatewayRef")
    private String gatewayRef;

    @JsonProperty("success")
    public Boolean getSuccess() {
        return success;
    }

    @JsonProperty("success")
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("cost")
    public String getCost() {
        return cost;
    }

    @JsonProperty("cost")
    public void setCost(String cost) {
        this.cost = cost;
    }

    @JsonProperty("msgRef")
    public String getMsgRef() {
        return msgRef;
    }

    @JsonProperty("msgRef")
    public void setMsgRef(String msgRef) {
        this.msgRef = msgRef;
    }

    @JsonProperty("gatewayRef")
    public String getGatewayRef() {
        return gatewayRef;
    }

    @JsonProperty("gatewayRef")
    public void setGatewayRef(String gatewayRef) {
        this.gatewayRef = gatewayRef;
    }

}
