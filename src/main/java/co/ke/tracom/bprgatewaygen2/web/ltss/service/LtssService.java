package co.ke.tracom.bprgatewaygen2.web.ltss.service;

import co.ke.tracom.bprgatewaygen2.core.tracomhttp.resthttp.RestHTTPService;
import co.ke.tracom.bprgatewaygen2.web.exceptions.custom.ExternalHTTPRequestException;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.checkPayment.CheckPaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.nationalIDValidation.NationalIDValidationRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.nationalIDValidation.NationalIDValidationResponse;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.newSubscriber.NewSubscriberRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.newSubscriber.NewSubscriberResponse;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.paymentContribution.PaymentContributionRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.paymentContribution.PaymentContributionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LtssService {

  private final RestHTTPService restHttpService;

  @Value("${ltss.base.url}")
  private String ltssBaseUrl;

  @Value("${ltss.nationalId.validation.url}")
  private String nationalIdValidationUrl;

  @Value("${ltss.payment.contribution.url}")
  private String paymentContributionUrl;

  @Value("${ltss.payment.check.url}")
  private String checkPaymentByRefNoUrl;

  @Value("${ltss.register.subscriber.url}")
  private String registerNewSubscriberURL;

  /**
   * Validates National ID
   *
   * @param validationRequest validation request object
   * @return validation response
   */
  public NationalIDValidationResponse validateNationalID(
      NationalIDValidationRequest validationRequest) {
    NationalIDValidationResponse validationResponse;

    try {
      String requestURL = ltssBaseUrl + nationalIdValidationUrl;
      ResponseEntity<String> response = restHttpService.postRequest(validationRequest, requestURL);
      ObjectMapper mapper = new ObjectMapper();
      validationResponse = mapper.readValue(response.getBody(), NationalIDValidationResponse.class);
      log.info("LTSS SERVICE RESPONSE: {}", validationResponse);
    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      throw new ExternalHTTPRequestException("Error validation National ID from LTSS API");
    }
    return validationResponse;
  }

  /**
   * Sends a payment contribution
   *
   * @param paymentContributionRequest payment request object
   * @return payment contribution response
   */
  public PaymentContributionResponse sendPaymentContribution(
      PaymentContributionRequest paymentContributionRequest) {
    PaymentContributionResponse paymentContributionResponse;

    try {
      String requestURL = ltssBaseUrl + paymentContributionUrl;
      ResponseEntity<String> response = restHttpService
          .postRequest(paymentContributionRequest, requestURL);
      ObjectMapper mapper = new ObjectMapper();
      paymentContributionResponse = mapper
          .readValue(response.getBody(), PaymentContributionResponse.class);
      log.info("LTSS SERVICE RESPONSE: {}", paymentContributionResponse);
    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      throw new ExternalHTTPRequestException("Error sending payment contribution to LTSS API");
    }
    return paymentContributionResponse;
  }

  /**
   * checks payment by reference number
   *
   * @param checkPaymentRequest check payment request object
   * @return response entity from remote API; Structure of response is not specified in LTSS service
   * documentation so an explicit response object to reflect this is not declared
   */
  public ResponseEntity<?> checkPaymentByRefNo(CheckPaymentRequest checkPaymentRequest) {
    ResponseEntity<String> response;
    try {
      String requestURL = ltssBaseUrl + checkPaymentByRefNoUrl;
      response = restHttpService.postRequest(checkPaymentRequest, requestURL);
      log.info("LTSS SERVICE RESPONSE: {}", response);
    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      throw new ExternalHTTPRequestException(
          "Error checking payment by reference number from LTSS API");
    }
    return response;
  }

  /**
   * Registers a new subscriber
   *
   * @param newSubscriberRequest new subscriber request object
   * @return new subscriber response
   */
  public NewSubscriberResponse registerNewSubscriber(NewSubscriberRequest newSubscriberRequest) {
    NewSubscriberResponse newSubscriberResponse = new NewSubscriberResponse();

    try {
      String requestURL = ltssBaseUrl + registerNewSubscriberURL;
      ResponseEntity<String> response = restHttpService
          .postRequest(newSubscriberRequest, requestURL);
      ObjectMapper mapper = new ObjectMapper();
      newSubscriberResponse = mapper.readValue(response.getBody(), NewSubscriberResponse.class);
      log.info("LTSS SERVICE RESPONSE: {}", newSubscriberResponse);

    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      throw new ExternalHTTPRequestException("Error registering a new subscriber in  LTSS Service");
    }
    return newSubscriberResponse;
  }

  private void logError (Exception ex) {
    log.error("LTSS SERVICE: {}", ex.getMessage());
  }
}








































