package ke.tra.com.tsync.wrappers.crdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;


@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostGePGControlNumberPaymentRequest {

    @JsonProperty("code")
    public String code;

    @JsonProperty("sessionToken")
    private String sessionToken;

    @JsonProperty("partnerID")
    private String partnerID;

    @JsonProperty("checksum")
    private String checksum;

    @JsonProperty("requestID")
    private String requestID;

    @JsonProperty("paymentReference")
    private String paymentReference;

    @JsonProperty("callbackurl")
    private String callbackurl;

    @JsonProperty("paymentType")
    private String paymentType;

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
}
