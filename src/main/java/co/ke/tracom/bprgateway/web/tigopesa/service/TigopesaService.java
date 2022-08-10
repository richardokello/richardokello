package co.ke.tracom.bprgateway.web.tigopesa.service;

import co.ke.tracom.bprgateway.core.tracomchannels.xml.XMLHttpService;
import co.ke.tracom.bprgateway.web.exceptions.custom.ExternalHTTPRequestException;
import co.ke.tracom.bprgateway.web.tigopesa.data.checkBalance.CheckBalanceRequest;
import co.ke.tracom.bprgateway.web.tigopesa.data.checkBalance.CheckBalanceResponse;
import co.ke.tracom.bprgateway.web.tigopesa.data.payment.BillPaymentRequest;
import co.ke.tracom.bprgateway.web.tigopesa.data.payment.BillPaymentResponse;
import co.ke.tracom.bprgateway.web.tigopesa.data.transactionStatus.TransactionStatusRequest;
import co.ke.tracom.bprgateway.web.tigopesa.data.transactionStatus.TransactionStatusResponse;
import co.ke.tracom.bprgateway.web.tigopesa.data.walletPayment.WalletPaymentRequest;
import co.ke.tracom.bprgateway.web.tigopesa.data.walletPayment.WalletPaymentResponse;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TigopesaService {
private final static String tigopesaServiceResponse="TIGOPESA SERVICE RESPONSE: {}";
  private final XMLHttpService xmlHttpService;

  // Todo: set base url once provided (not available in the docs)
  @Value("")
  private String baseURL;

  @Value("/TELEPIN")
  private String requestURL;

  /**
   * This method is used to send a pay bill request.
   *
   * @param paymentRequest request object with payment details
   * @return payment response details
   */
  public BillPaymentResponse payBill(BillPaymentRequest paymentRequest) {
    BillPaymentResponse paymentResponse;

    try {
      ResponseEntity<String> response =
          xmlHttpService.post(paymentRequest, baseURL + requestURL, String.class);
      log.info(tigopesaServiceResponse, response);
      XmlMapper mapper = new XmlMapper();
      paymentResponse = mapper.readValue(response.getBody(), BillPaymentResponse.class);
    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      throw new ExternalHTTPRequestException("Error getting agent details from TigoPesa API");
    }
    return paymentResponse;
  }

  /**
   * Retrieves balance of the bill which customer has to pay
   *
   * @param checkBalanceRequest
   * @return customer balance details
   */
  public CheckBalanceResponse checkBalance(CheckBalanceRequest checkBalanceRequest) {
    CheckBalanceResponse checkBalanceResponse;

    try {
      ResponseEntity<String> response =
          xmlHttpService.post(checkBalanceRequest, baseURL + requestURL, String.class);
      log.info(tigopesaServiceResponse, response);
      XmlMapper mapper = new XmlMapper();
      checkBalanceResponse = mapper.readValue(response.getBody(), CheckBalanceResponse.class);
    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      throw new ExternalHTTPRequestException("Error paying bill using TigoPesa API");
    }

    return checkBalanceResponse;
  }

  /**
   * Get the transaction status
   *
   * @param transactionStatusRequest
   * @return transaction status response details
   */
  public TransactionStatusResponse checkTransactionStatus(
      TransactionStatusRequest transactionStatusRequest) {
    TransactionStatusResponse transactionStatusResponse;

    try {
      ResponseEntity<String> response =
          xmlHttpService.post(transactionStatusRequest, baseURL + requestURL, String.class);
      log.info(tigopesaServiceResponse, response);
      XmlMapper mapper = new XmlMapper();
      transactionStatusResponse =
          mapper.readValue(response.getBody(), TransactionStatusResponse.class);
    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      throw new ExternalHTTPRequestException("Error getting transaction status using TigoPesa API");
    }

    return transactionStatusResponse;
  }

  /**
   * Deposit money to MFS wallet
   *
   * @param walletPaymentRequest
   * @return wallet payment response details
   */
  public WalletPaymentResponse creditWallet(WalletPaymentRequest walletPaymentRequest) {
    WalletPaymentResponse walletPaymentResponse;

    try {
      ResponseEntity<String> response =
          xmlHttpService.post(walletPaymentRequest, baseURL + requestURL, String.class);
      XmlMapper mapper = new XmlMapper();
      walletPaymentResponse = mapper.readValue(response.getBody(), WalletPaymentResponse.class);
      log.info(tigopesaServiceResponse, walletPaymentResponse);
    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      throw new ExternalHTTPRequestException(
          "Error depositing money to MFS wallet using TigoPesa API");
    }

    return walletPaymentResponse;
  }

  private void logError(Exception ex) {
    log.error("TIGOPESA SERVICE: {}", ex.getMessage());
  }
}
