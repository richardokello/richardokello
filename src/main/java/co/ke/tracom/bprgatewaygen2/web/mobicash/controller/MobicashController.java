package co.ke.tracom.bprgatewaygen2.web.mobicash.controller;

import co.ke.tracom.bprgatewaygen2.web.agaciro.data.nid.ValidateNIDResponse;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.agent.AgentDetailsRequest;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.agent.AgentDetailsResponse;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.authentication.AuthenticationRequest;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.authentication.AuthenticationResponse;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.payment.MobicashPaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.payment.MobicashPaymentResponse;
import co.ke.tracom.bprgatewaygen2.web.mobicash.service.MobiCashService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/mobicash")
@RequiredArgsConstructor
public class MobicashController {

  private final MobiCashService mobiCashService;

  /**
   * Retrieves dynamic token to be used for subsequent requests
   *
   * @param request Authentication request object
   * @return authorization token
   */
  @ApiOperation(
      value = "Returns a dynamic token to be used for subsequent requests",
      response = AuthenticationResponse.class)
  @PostMapping("/oauth/token")
  public ResponseEntity<?> requestToken(@RequestBody AuthenticationRequest request) {
    AuthenticationResponse responseEntity = mobiCashService.authRequest(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }

  @ApiOperation(
      value = "Returns agent details",
      response = AgentDetailsResponse.class)
  @PostMapping("/agent")
  public ResponseEntity<?> getAgentDetails(@RequestBody AgentDetailsRequest request) {
    AgentDetailsResponse responseEntity = mobiCashService.getAgentDetails(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }

  @ApiOperation(
      value = "Credits a specific Mobicash account",
      response = AgentDetailsResponse.class)
  @PostMapping("/account")
  public ResponseEntity<?> creditAccount(@RequestBody MobicashPaymentRequest request) {
    MobicashPaymentResponse responseEntity = mobiCashService.sendPayment(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }
}
