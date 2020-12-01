package co.ke.tracom.bprgatewaygen2.web.wasac.service;

import co.ke.tracom.bprgatewaygen2.core.tracomhttp.resthttp.RestHTTPService;
import co.ke.tracom.bprgatewaygen2.core.util.AppConstants;
import co.ke.tracom.bprgatewaygen2.web.wasac.data.customerprofile.CustomerProfileRequest;
import co.ke.tracom.bprgatewaygen2.web.wasac.data.customerprofile.CustomerProfileResponse;
import co.ke.tracom.bprgatewaygen2.web.wasac.data.payment.PaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.wasac.data.payment.PaymentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public class WASACService {
    @Value("${wasac.customer-profile.request-base-url}")
    private String WASACBaseURL;
    @Value("${wasac.customer-profile.request-suffix-url}")
    private String WASACSuffixURL;
    @Value("${wasac.payment.advise-url}")
    private String WASACSPaymentAdviseURL;

    private final RestHTTPService restHTTPService;

    /**
     * Fetch customer data given Customer ID from remote API.
     * URL: https://dev.api.wasac.rw/<customerid>/profile
     * <p>
     * postname: client postname
     * name: client name
     * zone: zone(location)
     * mobile: mobile phone number
     * email: clients email
     * phone: clients' fixed phone
     * personnalid: National ID
     * branch: WASAC branch
     * balance: Due balance
     * meterid: Water Meter Number
     * customerid: Unique customer identifier
     *
     * @param profileRequest
     */
    public CustomerProfileResponse fetchCustomerProfile(CustomerProfileRequest profileRequest) {
        CustomerProfileResponse profileResponse = new CustomerProfileResponse().setStatus(AppConstants.EXCEPTION_OCCURRED_ON_EXTERNAL_HTTP_REQUEST.value()  );
        try {
            String requestURL = WASACBaseURL + profileRequest.getCustomerId() + WASACSuffixURL;
            String results = restHTTPService.sendGetRequest(requestURL);

            ObjectMapper mapper = new ObjectMapper();
            CustomerProfileResponse customerProfileResult = mapper.readValue(results, CustomerProfileResponse.class);
            customerProfileResult.setStatus(AppConstants.TRANSACTION_SUCCESS_STANDARD.value());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return profileResponse;
    }

    public PaymentResponse payWaterBill(PaymentRequest request) {
        PaymentResponse paymentResponse = new PaymentResponse().setStatus(AppConstants.EXCEPTION_OCCURRED_ON_EXTERNAL_HTTP_REQUEST.value());
        try {
            ResponseEntity<String> response= restHTTPService.postRequest(request, WASACSPaymentAdviseURL);

            ObjectMapper mapper = new ObjectMapper();
            PaymentResponse paymentAdviseResponse = mapper.readValue(response.getBody(), PaymentResponse.class);
            paymentAdviseResponse.setStatus(AppConstants.TRANSACTION_SUCCESS_STANDARD.value());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentResponse;
    }
}
