package co.ke.tracom.bprgatewaygen2.web.ltss.service;

import co.ke.tracom.bprgatewaygen2.core.tracomhttp.resthttp.RestHTTPService;

import co.ke.tracom.bprgatewaygen2.web.agaciro.data.institutions.*;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.nid.ValidateNIDRequest;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.nid.ValidateNIDResponse;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.paymentNotification.PaymentNotificationRequest;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.paymentNotification.PaymentNotificationResponse;
import co.ke.tracom.bprgatewaygen2.web.exceptions.custom.ExternalHTTPRequestException;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.NewSubscriber.NewSubscriberRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.NewSubscriber.NewSubscriberResponse;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.checkPayment.CheckPaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.nationalIDValidation.NationalIDValidationRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.nationalIDValidation.NationalIDValidationResponse;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.paymentContribution.PaymentContributionRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.paymentContribution.PaymentContributionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LtssService {

    @Value("http://10.10.90.40")
    private String ltssBaseURL;

    @Value("/ltss-integration-service/pservice/ltssservice/validateSubscriber/")
    private String nationalIDValidationURL;

    @Value("/ltss-integration-service/pservice/ltssservice/sendContribution/")
    private String paymentContributionURL;

    @Value("/ltss-integration-service/pservice/ltssservice/checkPayment/")
    private String checkPaymentByRefNoURL;

    @Value("/ltss-integration-service/pservice/ltssservice/register/")
    private String registerNewSubscriberURL;




    private final RestHTTPService restHTTPService;

    /**
     * Validates National ID
     *
     * @param validationRequest
     * @return validation response
     */
    public NationalIDValidationResponse validateNationalID(NationalIDValidationRequest validationRequest) {
        NationalIDValidationResponse validationResponse = new NationalIDValidationResponse();

        try {
            String requestURL = ltssBaseURL + nationalIDValidationURL;
            ResponseEntity<String> response = restHTTPService.postRequest(validationRequest, requestURL);
            ObjectMapper mapper = new ObjectMapper();
            validationResponse = mapper.readValue(response.getBody(), NationalIDValidationResponse.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ExternalHTTPRequestException("Error validation National ID from LTSS API");
        }
        return validationResponse;
    }

    /**
     * Sends a payment contribution
     *
     * @param paymentContributionRequest
     * @return payment contribution response
     */
    public PaymentContributionResponse sendPaymentContribution(PaymentContributionRequest paymentContributionRequest) {
        PaymentContributionResponse paymentContributionResponse = new PaymentContributionResponse();

        try {
            String requestURL = ltssBaseURL + paymentContributionURL;
            ResponseEntity<String> response = restHTTPService.postRequest(paymentContributionRequest, requestURL);
            ObjectMapper mapper = new ObjectMapper();
            paymentContributionResponse = mapper.readValue(response.getBody(), PaymentContributionResponse.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ExternalHTTPRequestException("Error sending payment contribution to LTSS API");
        }
        return paymentContributionResponse;
    }

    public ResponseEntity<?> checkPaymentByRefNo(CheckPaymentRequest checkPaymentRequest) {
        ResponseEntity<String> response;
        try {
            String requestURL = ltssBaseURL + checkPaymentByRefNoURL;
            response = restHTTPService.postRequest(checkPaymentRequest, requestURL);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ExternalHTTPRequestException("Error sending payment contribution to LTSS API");
        }
        return response;
    }

    /**
     * Registers a new subscriber
     *
     * @param newSubscriberRequest
     * @return
     */
    public NewSubscriberResponse registerNewSubscriber(NewSubscriberRequest newSubscriberRequest) {
        NewSubscriberResponse newSubscriberResponse = new NewSubscriberResponse();

        try {
            String requestURL = ltssBaseURL + registerNewSubscriberURL;
            ResponseEntity<String> response = restHTTPService.postRequest(newSubscriberRequest, requestURL);
            ObjectMapper mapper = new ObjectMapper();
            newSubscriberResponse = mapper.readValue(response.getBody(), NewSubscriberResponse.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ExternalHTTPRequestException("Error registering a new subscriber to  LTSS service");
        }
        return newSubscriberResponse;
    }
}








































