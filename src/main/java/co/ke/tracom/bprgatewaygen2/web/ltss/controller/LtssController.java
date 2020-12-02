package co.ke.tracom.bprgatewaygen2.web.ltss.controller;

import co.ke.tracom.bprgatewaygen2.web.agaciro.data.institutions.*;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.nid.ValidateNIDRequest;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.nid.ValidateNIDResponse;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.paymentNotification.PaymentNotificationRequest;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.paymentNotification.PaymentNotificationResponse;
import co.ke.tracom.bprgatewaygen2.web.agaciro.service.AgaciroService;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.NewSubscriber.NewSubscriberRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.NewSubscriber.NewSubscriberResponse;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.checkPayment.CheckPaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.nationalIDValidation.NationalIDValidationRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.nationalIDValidation.NationalIDValidationResponse;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.paymentContribution.PaymentContributionRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.paymentContribution.PaymentContributionResponse;
import co.ke.tracom.bprgatewaygen2.web.ltss.service.LtssService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ltss")
@RequiredArgsConstructor
public class LtssController {

    private final LtssService ltssService;

    @PostMapping("/subscriber/validate")
    public ResponseEntity<?> validateNationalID(@RequestBody NationalIDValidationRequest request) {
        NationalIDValidationResponse responseEntity = ltssService.validateNationalID(request);
        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }

    @PostMapping("/payment/contribution")
    public ResponseEntity<?> sendPayment(@RequestBody PaymentContributionRequest request) {
        PaymentContributionResponse responseEntity = ltssService.sendPaymentContribution(request);
        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }

    @PostMapping("/payment/check")
    public ResponseEntity<?> checkPayment(@RequestBody CheckPaymentRequest request) {
        ResponseEntity<?> responseEntity = ltssService.checkPaymentByRefNo(request);
        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }

    @PostMapping("/subscriber/register")
    public ResponseEntity<?> registerSubscriber(@RequestBody NewSubscriberRequest request) {
        NewSubscriberResponse responseEntity = ltssService.registerNewSubscriber(request);
        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }

}


