package co.ke.tracom.bprgateway.web.customerwithdrawal.controller;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.academicbridge.data.studentdetails.GetStudentDetailsResponse;
import co.ke.tracom.bprgateway.web.customerwithdrawal.data.requests.AccountWithdrawalRequest;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CustomerWithdrawalController {

  @ApiOperation(value = "Withdrawal Transaction", response = GetStudentDetailsResponse.class)
  @PostMapping(value = "/pc/customer/account-withdrawal")
  public ResponseEntity<?> customerWithdrawalFromAccount(@RequestBody AccountWithdrawalRequest sendMoney) {

    Map<String, Object> results = new HashMap<>();
    results.put("responseCode", "00");
    results.put("message", "Transaction Processed Successfully");
    results.put("t24Reference", RRNGenerator.getInstance("PC").getRRN());
    results.put("charges", 500.51);

    return ResponseEntity.ok(results);
  }
}
