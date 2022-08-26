package co.ke.tracom.bprgateway.web.mobicash.service;

import co.ke.tracom.bprgateway.core.tracomchannels.json.RestHTTPService;
import co.ke.tracom.bprgateway.core.config.CustomObjectMapper;
import co.ke.tracom.bprgateway.web.exceptions.custom.ExternalHTTPRequestException;
import co.ke.tracom.bprgateway.web.mobicash.data.agent.AgentDetailsRequest;
import co.ke.tracom.bprgateway.web.mobicash.data.agent.AgentDetailsResponse;
import co.ke.tracom.bprgateway.web.mobicash.data.authentication.AuthenticationRequest;
import co.ke.tracom.bprgateway.web.mobicash.data.authentication.AuthenticationResponse;
import co.ke.tracom.bprgateway.web.mobicash.data.payment.MobicashPaymentRequest;
import co.ke.tracom.bprgateway.web.mobicash.data.payment.MobicashPaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MobiCashService {

  private final CustomObjectMapper mapper = new CustomObjectMapper();
  private static final String mobicashServiceResponse="MOBICASH SERVICE RESPONSE: {}";
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
   * param authenticationRequest
   * return AuthenticationResponse authentication details
   */
  public AuthenticationResponse authRequest(AuthenticationRequest authenticationRequest) {
    AuthenticationResponse authenticationResponse;

    try {
      String requestURL = baseURL + authRequestURL;
      ResponseEntity<String> response =
          restHTTPService.postRequest(authenticationRequest, requestURL);
      log.info(mobicashServiceResponse, response);
      authenticationResponse = mapper.readValue(response.getBody(), AuthenticationResponse.class);
      accessToken = authenticationResponse.getAccess_token();
    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      throw new ExternalHTTPRequestException("Error during authentication");
    }
    return authenticationResponse;
  }

  /**
   * Retrieves agent details using phone number or agent mcash ID
   * param agentDetailsRequest
   * @return AgentDetailsResponse agent details
   */
  public AgentDetailsResponse getAgentDetails(AgentDetailsRequest agentDetailsRequest) {
    AgentDetailsResponse agentDetailsResponse;

    try {
      agentDetailsRequest.setAuthorization(accessToken);
      String requestURL = baseURL + agentDetailsURL;
      ResponseEntity<String> response =
          restHTTPService.postRequest(agentDetailsRequest, requestURL);
      log.info(mobicashServiceResponse, response);
      agentDetailsResponse = mapper.readValue(response.getBody(), AgentDetailsResponse.class);
    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      throw new ExternalHTTPRequestException("Error getting agent details from MobiCash API");
    }

    return agentDetailsResponse;
  }

  /**
   * Credits a specific MobiCash account
   *param paymentRequest
   * @return PaymentResponse payment response object
   */
  public MobicashPaymentResponse sendPayment(MobicashPaymentRequest paymentRequest) {
    MobicashPaymentResponse paymentResponse;

    try {
      paymentRequest.setAuthorization(accessToken);
      String requestURL = baseURL + creditAccountURL;
      ResponseEntity<String> response = restHTTPService.postRequest(paymentRequest, requestURL);
      log.info(mobicashServiceResponse, response);
      paymentResponse = mapper.readValue(response.getBody(), MobicashPaymentResponse.class);
    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      throw new ExternalHTTPRequestException("Error sending payment to MobiCash API");
    }

    return paymentResponse;
  }

  private void logError(Exception ex) {
    log.error("MOBICASH SERVICE: {}", ex.getMessage());
  }
}
