package co.ke.tracom.bprgateway.web.accountvalidation.controller;

import co.ke.tracom.bprgateway.web.accountvalidation.data.BPRAccountValidationRequest;
import co.ke.tracom.bprgateway.web.accountvalidation.service.AccountValidationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class BPRAccountValidationController {
  private final AccountValidationService accountValidationService;

  @ApiOperation(value = "Validate any BPR Accounts i.e. agent account no or customer account no")
  @PostMapping(value = "/bank-account-validation")
  public ResponseEntity accountValidation(@RequestBody BPRAccountValidationRequest request) {
    Map<String, Object> results = new HashMap<>();
    results.put("responseCode", "00");
    results.put("message", "Transaction Processed Successfully");
    results.put("accountName", "Mukakarake Dancille");
    return ResponseEntity.ok(results);
  }

}
