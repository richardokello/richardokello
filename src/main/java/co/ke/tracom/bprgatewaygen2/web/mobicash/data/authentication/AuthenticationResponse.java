package co.ke.tracom.bprgatewaygen2.web.mobicash.data.authentication;

import co.ke.tracom.bprgatewaygen2.web.mobicash.data.MobiCashResponse;
import lombok.Data;

@Data
public class AuthenticationResponse extends MobiCashResponse {
    private String access_token;
}
