package co.ke.tracom.bprgateway.web.mobicash.data.authentication;

import co.ke.tracom.bprgateway.web.mobicash.data.MobiCashResponse;
import lombok.Data;

@Data
public class AuthenticationResponse extends MobiCashResponse {

  private String access_token;
}
