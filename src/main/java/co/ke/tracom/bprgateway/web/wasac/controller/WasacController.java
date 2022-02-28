package co.ke.tracom.bprgateway.web.wasac.controller;

import co.ke.tracom.bprgateway.servers.tcpserver.data.academicBridge.AcademicBridgeValidation;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.BillPaymentRequest;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.BillPaymentResponse;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.ValidationRequest;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.CustomerProfileRequest;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.CustomerProfileResponse;
import co.ke.tracom.bprgateway.web.wasac.data.payment.WasacPaymentResponse;
import co.ke.tracom.bprgateway.web.wasac.service.WASACService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(
    value = "/api/wasac",
    produces = {"application/json"})
@RequiredArgsConstructor
public class WasacController {

  private final WASACService wasacService;

  @ApiOperation(
      value = "Get the customer profile details from WASAC given a customer ID",
      response = CustomerProfileResponse.class)
  @GetMapping("/customer")
  public ResponseEntity<?> getCustomerProfile(@RequestBody CustomerProfileRequest request) {

    log.info("WASAC REQUEST DATA - CUSTOMER REQUEST: {}", request);
    CustomerProfileResponse responseEntity = wasacService.fetchCustomerProfile(request);
    if (responseEntity.getStatus().equals("05")){
      return new ResponseEntity<>(responseEntity, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }

  @ApiOperation(
      value = "Post payment details to WASAC. Username and password needed for authentication",
      response = WasacPaymentResponse.class)
  @PostMapping("/payment")
  public ResponseEntity<?> creditAccount(
      @ApiParam(value = "Payment details", required = true) @RequestBody
              BillPaymentRequest request) {

    log.info("WASAC REQUEST DATA - PAYMENT: {}", request);
    BillPaymentResponse responseEntity = wasacService.payWaterBill(request);
    if (responseEntity.getResponseCode().equals("05")){
      return new ResponseEntity<>(responseEntity, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }

  @ApiOperation(
          value = "Validate Water Account. Username and password needed for authentication",
          response = WasacPaymentResponse.class)
  @GetMapping("/validate")
  public ResponseEntity<?> validateWaterAccount(@RequestBody
                                                        ValidationRequest validationRequest){
    log.info("WASAC REQUEST DATA - PAYMENT: {}", validationRequest);
    AcademicBridgeValidation responseEntity = wasacService.validateWaterAccount(validationRequest, "PC");
    if (responseEntity.getResponseCode().equals("05")){
      return new ResponseEntity<>(responseEntity, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }
}
