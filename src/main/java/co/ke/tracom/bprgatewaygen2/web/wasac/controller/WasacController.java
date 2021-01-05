package co.ke.tracom.bprgatewaygen2.web.wasac.controller;

import co.ke.tracom.bprgatewaygen2.web.wasac.data.customerprofile.CustomerProfileRequest;
import co.ke.tracom.bprgatewaygen2.web.wasac.data.customerprofile.CustomerProfileResponse;
import co.ke.tracom.bprgatewaygen2.web.wasac.data.payment.WasacPaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.wasac.data.payment.WasacPaymentResponse;
import co.ke.tracom.bprgatewaygen2.web.wasac.service.WASACService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
      @ApiParam(value = "Customer ID", required = true) @PathVariable String customerId) {
    CustomerProfileRequest request = new CustomerProfileRequest();
    request.setCustomerId(customerId);
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
    WasacPaymentResponse responseEntity = wasacService.payWaterBill(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }
}
