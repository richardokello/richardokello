package co.ke.tracom.bprgatewaygen2.web.agaciro.controller;

import co.ke.tracom.bprgatewaygen2.web.agaciro.data.institutions.*;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.nid.ValidateNIDRequest;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.nid.ValidateNIDResponse;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.paymentNotification.PaymentNotificationRequest;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.paymentNotification.PaymentNotificationResponse;
import co.ke.tracom.bprgatewaygen2.web.agaciro.service.AgaciroService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agaciro")
@RequiredArgsConstructor
public class AgaciroController {

  private final AgaciroService agaciroService;

  @ApiOperation(
      value = "Returns a list of all institutions in Agaciro Contribution System",
      response = InstitutionsResponse.class,
      responseContainer = "List")
  @GetMapping("/institutions")
  public ResponseEntity<?> getInstitutions(
      @ApiParam(value = "Request object", required = true) @RequestBody
          InstitutionsRequest request) {
    InstitutionsResponse responseEntity = agaciroService.getInstitutions(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }

  @ApiOperation(
      value = "Returns details of an institution based on name",
      response = InstitutionByNameResponse.class)
  @GetMapping("/institutions/name")
  public ResponseEntity<?> getInstitutionByName(@RequestBody InstitutionByNameRequest request) {
    InstitutionByNameResponse responseEntity = agaciroService.getInstitutionByName(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }

  @ApiOperation(
      value = "Returns details of an institution based on institution's code",
      response = InstitutionByCodeResponse.class)
  @GetMapping("/institutions/code")
  public ResponseEntity<?> getInstitutionByCode(@RequestBody InstitutionByCodeRequest request) {
    InstitutionByCodeResponse responseEntity = agaciroService.getInstitutionByCode(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }

  @ApiOperation(value = "Returns registered NID from NIDA", response = ValidateNIDResponse.class)
  @GetMapping("/NID")
  public ResponseEntity<?> validateNID(@RequestBody ValidateNIDRequest request) {
    ValidateNIDResponse responseEntity = agaciroService.validateNID(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }

  @ApiOperation(
      value = "Sends a payment notification to the Agaciro System",
      response = ValidateNIDResponse.class)
  @PostMapping("/paymentNotification")
  public ResponseEntity<?> sendPaymentNotification(
      @RequestBody PaymentNotificationRequest request) {
    PaymentNotificationResponse responseEntity = agaciroService.sendPaymentNotification(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }
}
