package co.ke.tracom.bprgatewaygen2.web.wasac.controller;

import co.ke.tracom.bprgatewaygen2.web.mobicash.data.agent.AgentDetailsRequest;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.agent.AgentDetailsResponse;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.authentication.AuthenticationRequest;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.authentication.AuthenticationResponse;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.payment.MobicashPaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.payment.MobicashPaymentResponse;
import co.ke.tracom.bprgatewaygen2.web.mobicash.service.MobiCashService;
import co.ke.tracom.bprgatewaygen2.web.wasac.data.customerprofile.CustomerProfileRequest;
import co.ke.tracom.bprgatewaygen2.web.wasac.data.customerprofile.CustomerProfileResponse;
import co.ke.tracom.bprgatewaygen2.web.wasac.data.payment.WasacPaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.wasac.data.payment.WasacPaymentResponse;
import co.ke.tracom.bprgatewaygen2.web.wasac.service.WASACService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/wasac")
@RequiredArgsConstructor
public class WasacController {

    private final WASACService wasacService;

    @PostMapping("/customer/profile")
    public ResponseEntity<?> getCustomerProfile(@RequestBody CustomerProfileRequest request) {
        CustomerProfileResponse responseEntity = wasacService.fetchCustomerProfile(request);
        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }

    @PostMapping("/bill")
    public ResponseEntity<?> creditAccount(@RequestBody WasacPaymentRequest request) {
        WasacPaymentResponse responseEntity = wasacService.payWaterBill(request);
        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }
}

