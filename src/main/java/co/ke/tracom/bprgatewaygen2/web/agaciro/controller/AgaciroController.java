package co.ke.tracom.bprgatewaygen2.web.agaciro.controller;

import co.ke.tracom.bprgatewaygen2.web.agaciro.data.institutions.InstitutionByCodeRequest;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.institutions.InstitutionByCodeResponse;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.institutions.InstitutionByNameRequest;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.institutions.InstitutionByNameResponse;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.institutions.InstitutionsRequest;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.institutions.InstitutionsResponse;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.nid.ValidateNIDRequest;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.nid.ValidateNIDResponse;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.paymentNotification.PaymentNotificationRequest;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.paymentNotification.PaymentNotificationResponse;
import co.ke.tracom.bprgatewaygen2.web.agaciro.service.AgaciroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agaciro")
@RequiredArgsConstructor
public class AgaciroController {

  private final AgaciroService agaciroService;

  @GetMapping("/institutions")
  public ResponseEntity<?> getInstitutions(@RequestBody InstitutionsRequest request) {
    InstitutionsResponse responseEntity = agaciroService.getInstitutions(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }

  @GetMapping("/institutions/name")
  public ResponseEntity<?> getInstitutionByName(@RequestBody InstitutionByNameRequest request) {
    InstitutionByNameResponse responseEntity = agaciroService.getInstitutionByName(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }

  @GetMapping("/institutions/code")
  public ResponseEntity<?> getInstitutionByCode(@RequestBody InstitutionByCodeRequest request) {
    InstitutionByCodeResponse responseEntity = agaciroService.getInstitutionByCode(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }

  @GetMapping("/NID")
  public ResponseEntity<?> validateNID(@RequestBody ValidateNIDRequest request) {
    ValidateNIDResponse responseEntity = agaciroService.validateNID(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }

  @PostMapping("/payment")
  public ResponseEntity<?> sendPaymentNotification(
      @RequestBody PaymentNotificationRequest request) {
    PaymentNotificationResponse responseEntity = agaciroService.sendPaymentNotification(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }

}

