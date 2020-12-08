package co.ke.tracom.bprgatewaygen2.web.mobicash.service;

import co.ke.tracom.bprgatewaygen2.core.tracomhttp.resthttp.RestHTTPService;
import co.ke.tracom.bprgatewaygen2.web.exceptions.custom.ExternalHTTPRequestException;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.agent.AgentDetailsRequest;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.agent.AgentDetailsResponse;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.authentication.AuthenticationRequest;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.authentication.AuthenticationResponse;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.payment.MobicashPaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.payment.MobicashPaymentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MobiCashService {

  private final RestHTTPService restHTTPService;
  @Value("http://server:port")
  private String baseURL;
  @Value("/server:port/mcash/oauth/token")
  private String authRequestURL;
  @Value("/mcash/services/rest/1.2.1/bank")
  private String agentDetailsURL;
  @Value("/mcash/services/rest/1.2.1/bank/trustAccount")
  private String creditAccountURL;
  private String accessToken;

  /**
   * Obtains and sets access authentication token from remote API
   *
   * @param authenticationRequest
   * @return AuthenticationResponse authentication details
   */
  public AuthenticationResponse authRequest(AuthenticationRequest authenticationRequest) {
    AuthenticationResponse authenticationResponse = new AuthenticationResponse();

    try {
      String requestURL = baseURL + authRequestURL;
      ResponseEntity<String> response = restHTTPService
          .postRequest(authenticationRequest, requestURL);
      ObjectMapper mapper = new ObjectMapper();
      authenticationResponse = mapper.readValue(response.getBody(), AuthenticationResponse.class);
      accessToken = authenticationResponse.getAccess_token();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return authenticationResponse;
  }

  /**
   * Retrieves agent details using phone number or agent mcash ID
   *
   * @param agentDetailsRequest
   * @return AgentDetailsResponse agent details
   */
  public AgentDetailsResponse getAgentDetails(AgentDetailsRequest agentDetailsRequest) {
    AgentDetailsResponse agentDetailsResponse;

    try {
      agentDetailsRequest.setAccountNumber(accessToken);
      String requestURL = baseURL + agentDetailsURL;
      ResponseEntity<String> response = restHTTPService
          .postRequest(agentDetailsRequest, requestURL);
      ObjectMapper mapper = new ObjectMapper();
      agentDetailsResponse = mapper.readValue(response.getBody(), AgentDetailsResponse.class);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new ExternalHTTPRequestException("Error getting agent details from MobiCash API");
    }

    return agentDetailsResponse;
  }

  /**
   * Credits a specific MobiCash account
   *
   * @param paymentRequest
   * @return PaymentResponse payment response object
   */
  public MobicashPaymentResponse sendPayment(MobicashPaymentRequest paymentRequest) {
    MobicashPaymentResponse paymentResponse;

    try {
      paymentRequest.setAuthorization(accessToken);
      String requestURL = baseURL + creditAccountURL;
      ResponseEntity<String> response = restHTTPService.postRequest(paymentRequest, requestURL);
      ObjectMapper mapper = new ObjectMapper();
      paymentResponse = mapper.readValue(response.getBody(), MobicashPaymentResponse.class);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new ExternalHTTPRequestException("Error sending payment MobiCash API");
    }

    return paymentResponse;
  }
}
