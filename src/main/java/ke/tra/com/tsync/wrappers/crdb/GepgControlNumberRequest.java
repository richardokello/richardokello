package ke.tra.com.tsync.wrappers.crdb;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)

public class GepgControlNumberRequest  {

        @JsonProperty("paymentReference")
        String paymentReference;
        @JsonProperty("code")
        String code;
        @JsonProperty("partnerID")
        String partnerID;
        @JsonProperty("sessionToken")
        String sessionToken;
        @JsonProperty("checksum")
        String checksum;
        @JsonProperty("requestID")
        String requestID;



}
