package ke.tra.com.tsync.wrappers.crdb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;


@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
public class PostGePGControlNumberPaymentRequest {

    public String code;

    @JsonProperty("sessionToken")
    public String sessionToken;

    @JsonProperty("partnerID")
    public String partnerID;

    public String checksum;

    @JsonProperty("requestID")
    public String requestID;

    @JsonProperty("paymentReference")
    public String paymentReference;

    public String callbackurl;

    @JsonProperty("paymentType")
    public String paymentType;

    public String owner;

    @JsonProperty("customerEmail")
    public String customerEmail;

    @JsonProperty("customerMobile")
    public String customerMobile;

    @JsonProperty("serviceName")
    public String serviceName;

    @JsonProperty("paymentGfsCode")
    public String paymentGfsCode;

    public String currency;

    @JsonProperty("paymentDesc")
    public String paymentDesc;

    @JsonProperty("paymentExpiry")
    public String paymentExpiry;

    @JsonProperty("paymentOption")
    public String paymentOption;

    public String amount;


}
