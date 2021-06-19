package co.ke.tracom.bprgateway.web.customerwithdrawal.controller;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.academicbridge.data.studentdetails.GetStudentDetailsResponse;
import co.ke.tracom.bprgateway.web.customerwithdrawal.data.requests.AccountWithdrawalRequest;
import co.ke.tracom.bprgateway.web.customerwithdrawal.data.response.WithdrawMoneyResult;
import co.ke.tracom.bprgateway.web.customerwithdrawal.data.response.WithdrawalMoneyResultData;
import co.ke.tracom.bprgateway.web.depositmoney.data.response.DepositMoneyResult;
import co.ke.tracom.bprgateway.web.depositmoney.data.response.DepositMoneyResultData;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CustomerWithdrawalController {

    @ApiOperation(value = "Withdrawal Transaction", response = WithdrawMoneyResult.class)
    @PostMapping(value = "/pc/customer/account-withdrawal")
    public ResponseEntity<?> customerWithdrawalFromAccount(@RequestBody AccountWithdrawalRequest sendMoney) {

        WithdrawalMoneyResultData data = WithdrawalMoneyResultData.builder()
                .t24Reference(RRNGenerator.getInstance("PC").getRRN())
                .charges("500.51").build();

        WithdrawMoneyResult response = WithdrawMoneyResult.builder()
                .status("00")
                .message("Transaction processed successfully")
                .data(data).build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
