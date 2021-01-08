package co.ke.tracom.bprgatewaygen2.web.ltss.controller;

import co.ke.tracom.bprgatewaygen2.web.ltss.data.checkPayment.CheckPaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.nationalIDValidation.NationalIDValidationRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.nationalIDValidation.NationalIDValidationResponse;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.newSubscriber.NewSubscriberRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.newSubscriber.NewSubscriberResponse;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.paymentContribution.PaymentContributionRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.paymentContribution.PaymentContributionResponse;
import co.ke.tracom.bprgatewaygen2.web.ltss.service.LtssService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(
    name = "/api/ltss",
    produces = {"application/json"})
@RequiredArgsConstructor
public class LtssController {

  private final LtssService ltssService;

  @ApiOperation(value = "Validates National ID", response = NationalIDValidationResponse.class)
  @PostMapping("/subscriber/validate")
  public ResponseEntity<?> validateNationalID(@RequestBody NationalIDValidationRequest request) {
    log.info("LTSS REQUEST DATA - VALIDATE NATIONAL ID: {}", request);
    NationalIDValidationResponse responseEntity = ltssService.validateNationalID(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }

  @ApiOperation(
      value = "Sends a payment contribution",
      response = PaymentContributionResponse.class)
  @PostMapping("/payment/contribution")
  public ResponseEntity<?> sendPayment(@RequestBody PaymentContributionRequest request) {
    log.info("LTSS REQUEST DATA - SEND PAYMENT CONTRIBUTION: {}", request);
    PaymentContributionResponse responseEntity = ltssService.sendPaymentContribution(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }

  @ApiOperation(value = "Checks payment by reference number")
  @PostMapping("/payment/check")
  public ResponseEntity<?> checkPayment(@RequestBody CheckPaymentRequest request) {
    log.info("LTSS REQUEST DATA - CHECK PAYMENT: {}", request);
    ResponseEntity<?> responseEntity = ltssService.checkPaymentByRefNo(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }

  @ApiOperation(value = "Registers a new subscriber", response = NewSubscriberResponse.class)
  @PostMapping("/subscriber/register")
  public ResponseEntity<?> registerSubscriber(@RequestBody NewSubscriberRequest request) {
    log.info("LTSS REQUEST DATA - REGISTER SUBSCRIBER: {}", request);
    NewSubscriberResponse responseEntity = ltssService.registerNewSubscriber(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }
}
