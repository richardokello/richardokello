package co.ke.tracom.bprgateway.web.VisionFund.controller;

import co.ke.tracom.bprgateway.web.VisionFund.data.*;
import co.ke.tracom.bprgateway.web.VisionFund.data.custom.CustomVerificationRequest;
import co.ke.tracom.bprgateway.web.VisionFund.data.custom.CustomVerificationResponse;
import co.ke.tracom.bprgateway.web.VisionFund.service.VisionFundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/vision_fund")
public class VisionFundController {
    private final VisionFundService fundService;

    @PostMapping(value = "deposit")
    public ResponseEntity<?> makeDeposit(@RequestBody AccountDepositRequest depositRequest){
        AccountDepositResponse depositResponse = fundService.makeDeposit(depositRequest);
        return new ResponseEntity<>(depositResponse, HttpStatus.OK);
    }

    @PostMapping(value = "withdraw")
    public ResponseEntity<?> fundsWithdrawal(@RequestBody CashWithdrawalRequest withdrawalRequest){
        CashWithdrawalResponse withdrawalResponse = fundService.doWithdraw(withdrawalRequest);
        return new ResponseEntity<>(withdrawalResponse, HttpStatus.OK);
    }

    @GetMapping(value = "verify")
    public ResponseEntity<?> accountVerification(@RequestBody CustomVerificationRequest verificationRequest){
        CustomVerificationResponse verificationResponse = fundService.verifyCustomer(verificationRequest);
        return new ResponseEntity<>(verificationResponse, HttpStatus.OK);
    }

    @GetMapping(value = "balance")
    public ResponseEntity<?> accountBalance(@RequestBody BalanceEnquiryRequest enquiryRequest){
        BalanceEnquiryResponse enquiryResponse = fundService.getBalance(enquiryRequest);
        return new ResponseEntity<>(enquiryResponse, HttpStatus.OK);
    }

}
