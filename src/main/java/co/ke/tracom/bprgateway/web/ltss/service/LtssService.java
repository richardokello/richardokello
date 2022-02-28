package co.ke.tracom.bprgateway.web.ltss.service;

import co.ke.tracom.bprgateway.core.tracomchannels.json.RestHTTPService;
import co.ke.tracom.bprgateway.core.config.CustomObjectMapper;
import co.ke.tracom.bprgateway.web.exceptions.custom.ExternalHTTPRequestException;
import co.ke.tracom.bprgateway.web.ltss.data.checkPayment.CheckPaymentRequest;
import co.ke.tracom.bprgateway.web.ltss.data.checkPayment.CheckPaymentResponse;
import co.ke.tracom.bprgateway.web.ltss.data.exception.StatusCode;
import co.ke.tracom.bprgateway.web.ltss.data.nationalIDValidation.NationalIDValidationRequest;
import co.ke.tracom.bprgateway.web.ltss.data.nationalIDValidation.NationalIDValidationResponse;
import co.ke.tracom.bprgateway.web.ltss.data.newSubscriber.NewSubscriberRequest;
import co.ke.tracom.bprgateway.web.ltss.data.newSubscriber.NewSubscriberResponse;
import co.ke.tracom.bprgateway.web.ltss.data.paymentContribution.PaymentContributionRequest;
import co.ke.tracom.bprgateway.web.ltss.data.paymentContribution.PaymentContributionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
@RequiredArgsConstructor
public class LtssService {

  private final CustomObjectMapper mapper = new CustomObjectMapper();

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
      ResponseEntity<String> response = restHttpService.postLTSSRequest(validationRequest, requestURL);
      log.info("LTSS SERVICE RESPONSE: {}", response);
      validationResponse = mapper.readValue(response.getBody(), NationalIDValidationResponse.class);
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
      ResponseEntity<String> response =
          restHttpService.postLTSSRequest(paymentContributionRequest, requestURL);
      log.info("LTSS SERVICE RESPONSE: {}", response);
      paymentContributionResponse =
          mapper.readValue(response.getBody(), PaymentContributionResponse.class);
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
   *     documentation so an explicit response object to parse the response has not been used
   */
  public CheckPaymentResponse checkPaymentByRefNo(CheckPaymentRequest checkPaymentRequest) {
    ResponseEntity<String> response;
    CheckPaymentResponse checkPaymentResponse = new CheckPaymentResponse();
    try {
      String requestURL = ltssBaseUrl + checkPaymentByRefNoUrl;
      response = restHttpService.postLTSSRequest(checkPaymentRequest, requestURL);
      checkPaymentResponse = mapper.readValue(response.getBody(), CheckPaymentResponse.class);
      log.info("LTSS SERVICE RESPONSE: {}", response);
    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);

      String s = ex.getMessage().split(":\\p{Space}")[1].strip();
      int endIndex = s.length()-1;
      String message = s.substring(1, endIndex);
      StatusCode statusCode = new StatusCode();
      try {
        statusCode = mapper.readValue(message,StatusCode.class);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
      checkPaymentResponse.setMessage(statusCode.getMessage());
      checkPaymentResponse.setStatus(statusCode.getStatus());

//      throw new ExternalHTTPRequestException(
//          "Error checking payment by reference number from LTSS API");
    }
    return checkPaymentResponse;
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
      ResponseEntity<String> response =
          restHttpService.postLTSSRequest(newSubscriberRequest, requestURL);
      log.info("LTSS SERVICE RESPONSE: {}", newSubscriberResponse);
      newSubscriberResponse = mapper.readValue(response.getBody(), NewSubscriberResponse.class);

    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      System.err.println("((((("+ex+")))))");

      String s = ex.getMessage().split(":\\p{Space}")[1].strip();
      int endIndex = s.length()-1;
      String message = s.substring(1, endIndex);
      StatusCode statusCode = new StatusCode();
      try {
        statusCode = mapper.readValue(message,StatusCode.class);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
      newSubscriberResponse.setMessage(statusCode.getMessage());
      newSubscriberResponse.setStatus(statusCode.getStatus());
      //throw new ExternalHTTPRequestException("Error registering a new subscriber in  LTSS Service");
    }
    return newSubscriberResponse;
  }

  private void logError(Exception ex) {
    log.error("LTSS SERVICE: {}", ex.getMessage());
  }
}
