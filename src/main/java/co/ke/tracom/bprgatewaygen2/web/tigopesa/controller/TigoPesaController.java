package co.ke.tracom.bprgatewaygen2.web.tigopesa.controller;

import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.checkBalance.CheckBalanceRequest;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.checkBalance.CheckBalanceResponse;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.payment.BillPaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.payment.BillPaymentResponse;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.transactionStatus.TransactionStatusRequest;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.transactionStatus.TransactionStatusResponse;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.walletPayment.WalletPaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.walletPayment.WalletPaymentResponse;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.service.TigopesaService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    name = "/api/tigopesa",
    produces = {"application/json"})
@RequiredArgsConstructor
public class TigoPesaController {

  private final TigopesaService tigopesaService;

  @ApiOperation(value = "Make a bill payment", response = BillPaymentResponse.class)
  @PostMapping("/bill")
  public ResponseEntity<?> payBill(@RequestBody BillPaymentRequest request) {
    BillPaymentResponse responseEntity = tigopesaService.payBill(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }

  @ApiOperation(
      value = "Checks balance of bill which customer has to pay",
      response = CheckBalanceResponse.class)
  @PostMapping("/balance")
  public ResponseEntity<?> checkBalance(@RequestBody CheckBalanceRequest request) {
    CheckBalanceResponse responseEntity = tigopesaService.checkBalance(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }

  @ApiOperation(value = "Checks the transaction status", response = TransactionStatusResponse.class)
  @PostMapping("/transaction/status")
  public ResponseEntity<?> checkTransactionStatus(@RequestBody TransactionStatusRequest request) {
    TransactionStatusResponse responseEntity = tigopesaService.checkTransactionStatus(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }

  @ApiOperation(value = "Deposits amount to virtual wallet", response = WalletPaymentResponse.class)
  @PostMapping("/wallet")
  public ResponseEntity<?> creditWallet(@RequestBody WalletPaymentRequest request) {
    WalletPaymentResponse responseEntity = tigopesaService.creditWallet(request);
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }
}
