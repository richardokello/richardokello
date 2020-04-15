package ke.tra.com.tsync.wrappers.crdb;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GepgControlNumberRequest  {
        @JsonProperty("paymentReference")
        private String paymentReference;
        @JsonProperty("code")
        private String code;
        @JsonProperty("partnerID")
        private String partnerID;
        @JsonProperty("sessionToken")
        private String sessionToken;
        @JsonProperty("checksum")
        private String checksum;
        @JsonProperty("requestID")
        private String requestID;
}
