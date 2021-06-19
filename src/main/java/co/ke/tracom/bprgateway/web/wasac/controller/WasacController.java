package co.ke.tracom.bprgateway.web.wasac.controller;

import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.CustomerProfileRequest;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.CustomerProfileResponse;
import co.ke.tracom.bprgateway.web.wasac.data.payment.WasacPaymentRequest;
import co.ke.tracom.bprgateway.web.wasac.data.payment.WasacPaymentResponse;
import co.ke.tracom.bprgateway.web.wasac.service.WASACService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping(
    name = "/api/wasac",
    produces = {"application/json"})
@RequiredArgsConstructor
public class WasacController {

  private final WASACService wasacService;

  @ApiOperation(
      value = "Get the customer profile details from WASAC given a customer ID",
      response = CustomerProfileResponse.class)
  @GetMapping("/customer/{customerId}")
  public ResponseEntity<?> getCustomerProfile(
          @RequestBody CustomerProfileRequest request) {

    log.info("WASAC REQUEST DATA - CUSTOMER REQUEST: {}", request);
    CustomerProfileResponse responseEntity = wasacService.fetchCustomerProfile(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }

  @ApiOperation(
      value = "Post payment details to WASAC. Username and password needed for authentication",
      response = WasacPaymentResponse.class)
  @PostMapping("/payment")
  public ResponseEntity<?> creditAccount(
      @ApiParam(value = "Payment details", required = true) @RequestBody
          WasacPaymentRequest request) {

    log.info("WASAC REQUEST DATA - PAYMENT: {}", request);
    WasacPaymentResponse responseEntity = wasacService.payWaterBill(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }
}
