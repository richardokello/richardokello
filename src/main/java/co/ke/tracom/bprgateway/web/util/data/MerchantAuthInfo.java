package co.ke.tracom.bprgateway.web.util.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MerchantAuthInfo {
    private String username;
    private String password;
}
