package co.ke.tracom.bprgateway.web.sendmoney.controller;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.academicbridge.data.studentdetails.GetStudentDetailsResponse;
import co.ke.tracom.bprgateway.web.sendmoney.data.requests.ReceiveMoneyRequest;
import co.ke.tracom.bprgateway.web.sendmoney.data.requests.SendMoneyRequest;
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
public class SendMoneyController {

  @ApiOperation(value = "Send Money Transaction", response = GetStudentDetailsResponse.class)
  @PostMapping(value = "/pc/customer/send-money")
  public ResponseEntity<?> sendMoneyTransaction(@RequestBody SendMoneyRequest sendMoney) {

    Map<String, Object> results = new HashMap<>();
    results.put("responseCode", "00");
    results.put("message", "Transaction Processed Successfully");
    results.put("t24Reference", RRNGenerator.getInstance("PC").getRRN());
    results.put("charges", 500.51);

    return ResponseEntity.ok(results);
  }
  @ApiOperation(value = "Receive Money Transaction", response = GetStudentDetailsResponse.class)
  @PostMapping(value = "/pc/customer/receive-money")
  public ResponseEntity<?> receiveMoneyTransaction(@RequestBody ReceiveMoneyRequest receive) {

    Map<String, Object> results = new HashMap<>();
    results.put("responseCode", "00");
    results.put("message", "Transaction Processed Successfully");
    results.put("t24Reference", RRNGenerator.getInstance("PC").getRRN());
    results.put("charges", 30.51);

    return ResponseEntity.ok(results);
  }
}
