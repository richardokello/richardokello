package co.ke.tracom.bprgatewaygen2.web.wasac.data.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "response"
})
public class WasacPaymentResponse {

    private String status;

    @JsonProperty("response")
    private Response response;

    @JsonProperty("response")
    public Response getResponse() {
        return response;
    }

    @JsonProperty("response")
    public void setResponse(Response response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public WasacPaymentResponse setStatus(String status) {
        this.status = status;
        return this;
    }
}
