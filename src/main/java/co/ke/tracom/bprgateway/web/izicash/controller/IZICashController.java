package co.ke.tracom.bprgateway.web.izicash.controller;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.depositmoney.data.requests.DepositMoneyRequest;
import co.ke.tracom.bprgateway.web.depositmoney.data.response.DepositMoneyResult;
import co.ke.tracom.bprgateway.web.izicash.data.request.IZICashRequest;
import co.ke.tracom.bprgateway.web.izicash.data.response.IZICashResponse;
import co.ke.tracom.bprgateway.web.izicash.service.IZICashService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class IZICashController {

    private final IZICashService iziCashService;

    @ApiOperation(value = "IZI Cash Transaction", response = IZICashRequest.class)
    @PostMapping(value = "/pc/izicash-withdrawal")
    public ResponseEntity<?> depositMoneyTransaction(@Validated @RequestBody IZICashRequest request) {
        String transactionRRN = RRNGenerator.getInstance("CD").getRRN();
        log.info("IZICash Request: "+ transactionRRN+" Message Body: "+ request.toString());
        IZICashResponse response =  iziCashService.processWithdrawMoneyTnx(request, transactionRRN);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
