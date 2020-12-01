package co.ke.tracom.bprgatewaygen2.web.mobicash.data.authentication;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String clientCode;
    private String clientSecret;
    private String grantType;
    private String authorization; // Initially shared by MobiCash
}
