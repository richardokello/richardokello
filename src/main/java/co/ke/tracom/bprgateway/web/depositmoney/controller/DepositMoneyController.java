package co.ke.tracom.bprgateway.web.depositmoney.controller;

import co.ke.tracom.bprgateway.web.depositmoney.data.requests.DepositMoneyRequest;
import co.ke.tracom.bprgateway.web.depositmoney.data.response.DepositMoneyResult;
import co.ke.tracom.bprgateway.web.depositmoney.services.DepositMoneyService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
public class DepositMoneyController {

    private final DepositMoneyService depositMoneyService;

    @ApiOperation(value = "Deposit Money Transaction", response = DepositMoneyResult.class)
    @PostMapping(value = "/pc/customer/deposit-money")
    public ResponseEntity<?> depositMoneyTransaction(@Validated @RequestBody DepositMoneyRequest depositMoneyRequest) {
        DepositMoneyResult response =  depositMoneyService.processCustomerDepositMoneyTnx(depositMoneyRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
