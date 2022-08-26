package co.ke.tracom.bprgateway.web.sendmoney.controller;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.exceptions.custom.InvalidAgentCredentialsException;
import co.ke.tracom.bprgateway.web.sendmoney.data.requests.ReceiveMoneyRequest;
import co.ke.tracom.bprgateway.web.sendmoney.data.requests.SendMoneyRequest;
import co.ke.tracom.bprgateway.web.sendmoney.data.response.SendMoneyResponse;
import co.ke.tracom.bprgateway.web.sendmoney.services.SendMoneyService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SendMoneyController {
    private final SendMoneyService sendMoneyService;

    @ApiOperation(value = "Send Money Transaction", response = SendMoneyResponse.class)
    @PostMapping(value = "/pc/customer/send-money")
    public ResponseEntity<SendMoneyResponse> sendMoneyTransaction(@Valid @RequestBody SendMoneyRequest request) throws InvalidAgentCredentialsException {
        String transactionRRN = RRNGenerator.getInstance("SM").getRRN();
        SendMoneyResponse response = sendMoneyService.processSendMoneyRequest(request, transactionRRN);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Receive Money Transaction", response = SendMoneyResponse.class)
    @PostMapping(value = "/pc/customer/receive-money")
    public ResponseEntity<SendMoneyResponse> receiveMoneyTransaction(@Valid @RequestBody ReceiveMoneyRequest request) {
        String transactionRRN = RRNGenerator.getInstance("SM").getRRN();
        SendMoneyResponse response = sendMoneyService.processReceiveMoneyRequest(request, transactionRRN);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
