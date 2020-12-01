package co.ke.tracom.bprgatewaygen2.web.tigopesa.service;

import co.ke.tracom.bprgatewaygen2.core.tracomhttp.resthttp.RestHTTPService;
import co.ke.tracom.bprgatewaygen2.core.tracomhttp.xmlHttp.XMLHttpService;
import co.ke.tracom.bprgatewaygen2.web.exceptions.custom.ExternalHTTPRequestException;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.agent.AgentDetailsRequest;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.agent.AgentDetailsResponse;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.authentication.AuthenticationRequest;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.authentication.AuthenticationResponse;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.payment.PaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.payment.PaymentResponse;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.checkBalance.CheckBalanceRequest;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.checkBalance.CheckBalanceResponse;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.transactionStatus.TransactionStatusRequest;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.transactionStatus.TransactionStatusResponse;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.walletPayment.WalletPaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.walletPayment.WalletPaymentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TigopesaService {

    @Value("/TELEPIN")
    private String requestURL;

    //private final RestHTTPService restHTTPService;
    private final XMLHttpService xmlHttpService;

    /**
     * This method is used to pay bills
     *
     * @param paymentRequest
     * @return payment response details
     */
    public PaymentResponse sendPayment(PaymentRequest paymentRequest) {
        PaymentResponse paymentResponse;

        try {
            ResponseEntity<String> response = xmlHttpService.post(paymentRequest, requestURL);
            ObjectMapper mapper = new ObjectMapper();
            paymentResponse = mapper.readValue(response.getBody(), PaymentResponse.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ExternalHTTPRequestException("Error getting agent details from MobiCash API");
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
            ResponseEntity<String> response = xmlHttpService.post(checkBalanceRequest, requestURL);
            ObjectMapper mapper = new ObjectMapper();
            checkBalanceResponse = mapper.readValue(response.getBody(), CheckBalanceResponse.class);
        } catch (Exception ex) {
            ex.printStackTrace();
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
    public TransactionStatusResponse checkTransactionStatus(TransactionStatusRequest transactionStatusRequest) {
        TransactionStatusResponse transactionStatusResponse;

        try {
            ResponseEntity<String> response = xmlHttpService.post(transactionStatusRequest, requestURL);
            ObjectMapper mapper = new ObjectMapper();
            transactionStatusResponse = mapper.readValue(response.getBody(), TransactionStatusResponse.class);
        } catch (Exception ex) {
            ex.printStackTrace();
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
            ResponseEntity<String> response = xmlHttpService.post(walletPaymentRequest, requestURL);
            ObjectMapper mapper = new ObjectMapper();
            walletPaymentResponse = mapper.readValue(response.getBody(), WalletPaymentResponse.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ExternalHTTPRequestException("Error depositing money to MFS wallet using TigoPesa API");
        }

        return walletPaymentResponse;
    }

}

