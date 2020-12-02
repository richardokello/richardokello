package co.ke.tracom.bprgatewaygen2.web.mobicash.controller;

import co.ke.tracom.bprgatewaygen2.web.ltss.data.NewSubscriber.NewSubscriberRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.NewSubscriber.NewSubscriberResponse;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.checkPayment.CheckPaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.nationalIDValidation.NationalIDValidationRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.nationalIDValidation.NationalIDValidationResponse;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.paymentContribution.PaymentContributionRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.paymentContribution.PaymentContributionResponse;
import co.ke.tracom.bprgatewaygen2.web.ltss.service.LtssService;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.agent.AgentDetailsRequest;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.agent.AgentDetailsResponse;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.authentication.AuthenticationRequest;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.authentication.AuthenticationResponse;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.payment.MobicashPaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.payment.MobicashPaymentResponse;
import co.ke.tracom.bprgatewaygen2.web.mobicash.service.MobiCashService;
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
     * @param request Authentication request object
     * @return authorization token
     */
    @PostMapping("/oauth/token")
    public ResponseEntity<?> requestToken(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse responseEntity = mobiCashService.authRequest(request);
        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }

    @PostMapping("/agent")
    public ResponseEntity<?> getAgentDetails(@RequestBody AgentDetailsRequest request) {
        AgentDetailsResponse responseEntity = mobiCashService.getAgentDetails(request);
        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }

    @PostMapping("/account")
    public ResponseEntity<?> creditAccount(@RequestBody MobicashPaymentRequest request) {
        MobicashPaymentResponse responseEntity = mobiCashService.sendPayment(request);
        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }
}
