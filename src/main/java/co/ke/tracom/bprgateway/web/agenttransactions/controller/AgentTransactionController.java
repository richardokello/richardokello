package co.ke.tracom.bprgateway.web.agenttransactions.controller;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.request.AgentTransactionRequest;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AgentTransactionResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/agent-transactions")
@RestController
public class AgentTransactionController {
  @ApiOperation(value = "Process agent deposit", response = AgentTransactionResponse.class)
  @PostMapping(value = "/deposit")
  public ResponseEntity<?> agentDeposit(@RequestBody AgentTransactionRequest request) {

    AgentTransactionResponse response =
        AgentTransactionResponse.builder()
            .status("00")
            .message("Deposit processed successfully")
            .T24Reference(RRNGenerator.getInstance("BP").getRRN())
            .transactionCharges(34.55)
            .build();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @ApiOperation(value = "Process agent withdrawal", response = AgentTransactionResponse.class)
  @PostMapping(value = "/withdrawal")
  public ResponseEntity<?> agentWithdrawal(@RequestBody AgentTransactionRequest request) {

    AgentTransactionResponse response =
        AgentTransactionResponse.builder()
            .status("00")
            .message("Withdrawal processed successfully")
            .T24Reference(RRNGenerator.getInstance("BP").getRRN())
            .transactionCharges(34.55)
            .build();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
