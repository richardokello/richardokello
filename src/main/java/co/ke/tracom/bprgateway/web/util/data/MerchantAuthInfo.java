package co.ke.tracom.bprgateway.web.util.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MerchantAuthInfo {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
}
