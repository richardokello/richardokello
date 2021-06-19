package co.ke.tracom.bprgateway.web.wasac.service;

import co.ke.tracom.bprgateway.core.tracomchannels.json.RestHTTPService;
import co.ke.tracom.bprgateway.core.util.AppConstants;
import co.ke.tracom.bprgateway.servers.tcpserver.data.academicBridge.AcademicBridgeValidation;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.TransactionData;
import co.ke.tracom.bprgateway.core.config.CustomObjectMapper;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.CustomerProfileRequest;
import co.ke.tracom.bprgateway.web.wasac.data.customerprofile.CustomerProfileResponse;
import co.ke.tracom.bprgateway.web.wasac.data.payment.WasacPaymentRequest;
import co.ke.tracom.bprgateway.web.wasac.data.payment.WasacPaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WASACService {

    private final CustomObjectMapper mapper = new CustomObjectMapper();

    private final RestHTTPService restHTTPService;

    @Value("${wasac.customer-profile.request-base-url}")
    private String WASACBaseURL;

    @Value("${wasac.customer-profile.request-suffix-url}")
    private String WASACProfileURL;

    @Value("${wasac.payment.advise-url}")
    private String WASACSPaymentAdviseURL;

    /**
     * Fetch customer data given Customer ID from remote API. URL:
     * https://dev.api.wasac.rw/<customerid>/profile
     *
     * <p>postname: client postname name: client name zone: zone(location) mobile: mobile phone number
     * email: clients email phone: clients' fixed phone personnalid: National ID branch: WASAC branch
     * balance: Due balance meterid: Water Meter Number customerid: Unique customer identifier
     *
     * @param profileRequest
     */
    public CustomerProfileResponse fetchCustomerProfile(CustomerProfileRequest profileRequest) {
        CustomerProfileResponse profileResponse =
                CustomerProfileResponse.builder()
                        .status(AppConstants.EXCEPTION_OCCURRED_ON_EXTERNAL_HTTP_REQUEST.value()).build();
        try {
            String requestURL = WASACBaseURL + profileRequest.getCustomerId() + WASACProfileURL;
            String results = restHTTPService.sendGetOKHTTPRequest(requestURL);
            log.info("WASAC SERVICE RESPONSE: {}", results);
            profileResponse =
                    mapper.readValue(results, CustomerProfileResponse.class);
            profileResponse.setStatus(AppConstants.TRANSACTION_SUCCESS_STANDARD.value());

            profileResponse.setMessage("Customer profile fetched successfully");
        } catch (Exception e) {
            e.printStackTrace();
            logError(e);
        }
        return profileResponse;
    }

    public void fetchProfile() {


        List<TransactionData> data = new ArrayList<>();
        data.add(TransactionData.builder().name("school name").value("Tracom Academy").build());
        data.add(TransactionData.builder().name("school Account No").value("115627393").build());
        data.add(TransactionData.builder().name("school account name").value("Tracom Academy").build());
        data.add(TransactionData.builder().name("Student Reg").value("COM/0510/10").build());
        data.add(TransactionData.builder().name("Type Of Payment").value("Tuition").build());

        AcademicBridgeValidation response =
                AcademicBridgeValidation.builder()
                        .responseCode("00")
                        .responseMessage("Transaction processed successfully")
                        .data(data)
                        .build();
    }

    public WasacPaymentResponse payWaterBill(WasacPaymentRequest request) {
        WasacPaymentResponse paymentResponse =
                new WasacPaymentResponse()
                        .setStatus(AppConstants.EXCEPTION_OCCURRED_ON_EXTERNAL_HTTP_REQUEST.value());
        try {
            ResponseEntity<String> response =
                    restHTTPService.postRequest(request, WASACSPaymentAdviseURL);
            log.info("WASAC SERVICE RESPONSE: {}", response);
            WasacPaymentResponse paymentAdviseResponse =
                    mapper.readValue(response.getBody(), WasacPaymentResponse.class);
            paymentAdviseResponse.setStatus(AppConstants.TRANSACTION_SUCCESS_STANDARD.value());
        } catch (Exception e) {
            logError(e);
            e.printStackTrace();
        }
        return paymentResponse;
    }


    private void logError(Exception ex) {
        log.error("WASAC SERVICE: {}", ex.getMessage());
    }
}
