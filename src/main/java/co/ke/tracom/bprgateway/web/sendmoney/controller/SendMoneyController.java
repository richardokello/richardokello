package co.ke.tracom.bprgateway.web.sendmoney.controller;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.academicbridge.data.studentdetails.GetStudentDetailsResponse;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.responses.RRAPaymentResponse;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.responses.RRAPaymentResponseData;
import co.ke.tracom.bprgateway.web.sendmoney.data.requests.ReceiveMoneyRequest;
import co.ke.tracom.bprgateway.web.sendmoney.data.requests.SendMoneyRequest;
import co.ke.tracom.bprgateway.web.sendmoney.data.response.SendMoneyResponse;
import co.ke.tracom.bprgateway.web.sendmoney.data.response.SendMoneyResponseData;
import co.ke.tracom.bprgateway.web.sendmoney.services.SendMoneyService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SendMoneyController {

  private final SendMoneyService sendMoneyService;

  @ApiOperation(value = "Send Money Transaction", response = SendMoneyResponse.class)
  @PostMapping(value = "/pc/customer/send-money")
  public ResponseEntity<?> sendMoneyTransaction(@Valid @RequestBody SendMoneyRequest request) {
    String transactionRRN = RRNGenerator.getInstance("SM").getRRN();
    SendMoneyResponse response = sendMoneyService.processSendMoneyRequest(request,  transactionRRN);

//    SendMoneyResponseData data = SendMoneyResponseData.builder()
//            .T24Reference(RRNGenerator.getInstance("PC").getRRN())
//            .charges(1500.55)
//            .build();
//



    return new ResponseEntity<>(response, HttpStatus.OK);
  }




  @ApiOperation(value = "Receive Money Transaction", response = SendMoneyResponse.class)
  @PostMapping(value = "/pc/customer/receive-money")
  public ResponseEntity<?> receiveMoneyTransaction(@Valid @RequestBody ReceiveMoneyRequest receive) {
    SendMoneyResponseData data = SendMoneyResponseData.builder()
            .T24Reference(RRNGenerator.getInstance("PC").getRRN())
            .charges(2000.55)
            .build();

    SendMoneyResponse response = SendMoneyResponse.builder()
            .status("00")
            .message("Transaction processed successfully")
            .data(data)
            .build();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
