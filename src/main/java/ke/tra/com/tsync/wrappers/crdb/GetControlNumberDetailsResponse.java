package ke.tra.com.tsync.wrappers.crdb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;


@Data
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)

public class GetControlNumberDetailsResponse{

    @JsonProperty("requestID")
    private String requestID;
    @JsonProperty("owner")
    private String owner;
    @JsonProperty("customerEmail")
    private String customerEmail;
    @JsonProperty("customerMobile")
    private String customerMobile;
    @JsonProperty("serviceName")
    private String serviceName;
    @JsonProperty("paymentGfsCode")
    private String paymentGfsCode;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("paymentDesc")
    private String paymentDesc;
    @JsonProperty("paymentExpiry")
    private String paymentExpiry;
    @JsonProperty("paymentOption")
    private String paymentOption;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("paymentReference")
    private String paymentReference;
    @JsonProperty("partnerID")
    private String partnerID;
    @JsonProperty("message")
    public String message;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public String getToPOSStr(){

return        requestID()
        + "#"+this.owner
        + "#"+this.customerEmail
        + "#"+this.customerMobile
        + "#"+this.serviceName
        + "#"+this.paymentGfsCode
        + "#"+this.currency
        + "#"+this.paymentDesc
        + "#"+this.paymentExpiry
        + "#"+this.paymentOption
        + "#"+this.amount
        + "#"+this.paymentReference
        + "#"+this.partnerID;

    }


}
