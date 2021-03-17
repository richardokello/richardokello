package co.ke.tracom.bprgatewaygen2.web.agaciro.service;

import co.ke.tracom.bprgatewaygen2.core.tracomhttp.resthttp.RestHTTPService;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.institutions.*;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.nid.ValidateNIDRequest;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.nid.ValidateNIDResponse;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.paymentNotification.PaymentNotificationRequest;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.paymentNotification.PaymentNotificationResponse;
import co.ke.tracom.bprgatewaygen2.web.config.CustomObjectMapper;
import co.ke.tracom.bprgatewaygen2.web.exceptions.custom.ExternalHTTPRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AgaciroService {

  private final CustomObjectMapper mapper = new CustomObjectMapper();

  private final RestHTTPService restHTTPService;

  // Todo: update base URL for Agaciro service
  @Value("")
  private String agaciroBaseURL;

  @Value("/api/bank_payments/getInstitutions?username=%s&password=%s")
  private String getInstitutionsURL;

  @Value("/api/bank_payments/validateNID?username=%s&password=%s&nid=%s")
  private String validateNIDURL;

  @Value("/api/bank_payments/getByInstitutionName?username=%s&password=%s&institution_name=%s")
  private String getInstitutionByNameURL;

  @Value("/api/bank_payments/getByInstitutionCode?username=%s&password=%s&institution_code=%s")
  private String getInstitutionByCodeURL;

  @Value("/api/bank_payments/PaymentNotification")
  private String paymentNotificationURL;

  /**
   * Returns a list of all registered institutions in Agaciro Contribution System
   *
   * @param getInstitutionsRequest
   * @return A list of all registered institutions
   */
  public InstitutionsResponse getInstitutions(InstitutionsRequest getInstitutionsRequest) {
    InstitutionsResponse institutions = new InstitutionsResponse();

    try {
      String requestURL =
          String.format(
              getInstitutionsURL,
              getInstitutionsRequest.getUsername(),
              getInstitutionsRequest.getPassword());
      String results = restHTTPService.sendGetRequest(agaciroBaseURL + requestURL);
      log.info("AGACIRO SERVICE RESPONSE FROM REMOTE API: {}", results);
      institutions = mapper.readValue(results, InstitutionsResponse.class);
    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      throw new ExternalHTTPRequestException("Error fetching institutions from Agaciro API");
    }
    return institutions;
  }

  /**
   * Returns details of an institution based on name
   *
   * @param getInstitutionByNameRequest
   * @return details of institution that matches name
   */
  public InstitutionByNameResponse getInstitutionByName(
      InstitutionByNameRequest getInstitutionByNameRequest) {
    InstitutionByNameResponse institution = null;

    try {
      String requestURL =
          String.format(
              getInstitutionByNameURL,
              getInstitutionByNameRequest.getUsername(),
              getInstitutionByNameRequest.getPassword(),
              getInstitutionByNameRequest.getInstitutionName());
      String results = restHTTPService.sendGetRequest(agaciroBaseURL + requestURL);
      log.info("AGACIRO SERVICE RESPONSE: {}", results);
      institution = mapper.readValue(results, InstitutionByNameResponse.class);
    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      throw new ExternalHTTPRequestException(
          "Error fetching institutions by name from Agaciro API");
    }
    return institution;
  }

  /**
   * Returns details of an institution based on institution code
   *
   * @param getInstitutionByCodeRequest
   * @return
   */
  public InstitutionByCodeResponse getInstitutionByCode(
      InstitutionByCodeRequest getInstitutionByCodeRequest) {
    InstitutionByCodeResponse institution = null;

    try {
      String requestURL =
          String.format(
              getInstitutionByCodeURL,
              getInstitutionByCodeRequest.getUsername(),
              getInstitutionByCodeRequest.getPassword(),
              getInstitutionByCodeRequest.getInstitutionCode());
      String results = restHTTPService.sendGetRequest(requestURL);
      log.info("AGACIRO SERVICE RESPONSE: {}", results);
      institution = mapper.readValue(results, InstitutionByCodeResponse.class);
    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      throw new ExternalHTTPRequestException("Error fetching institution by code from Agaciro API");
    }

    return institution;
  }

  /**
   * Returns registered NID from NIDA
   *
   * @param validateNIDRequest username, password and Rwandan National ID
   * @return response showing whether given NID is valid
   */
  public ValidateNIDResponse validateNID(ValidateNIDRequest validateNIDRequest) {
    ValidateNIDResponse response = null;

    try {
      String requestURL =
          String.format(
              validateNIDURL,
              validateNIDRequest.getUsername(),
              validateNIDRequest.getPassword(),
              validateNIDRequest.getNid());
      String results = restHTTPService.sendGetRequest(agaciroBaseURL + requestURL);
      log.info("AGACIRO SERVICE RESPONSE: {}", results);
      response = mapper.readValue(results, ValidateNIDResponse.class);
    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      throw new ExternalHTTPRequestException("Error validating NID from Agaciro API");
    }

    return response;
  }

  /**
   * @param getPaymentNotificationRequest
   * @return
   */
  public PaymentNotificationResponse sendPaymentNotification(
      PaymentNotificationRequest getPaymentNotificationRequest) {
    PaymentNotificationResponse paymentNotification;

    try {
      ResponseEntity<String> response =
          restHTTPService.postRequest(
              getPaymentNotificationRequest, agaciroBaseURL + paymentNotificationURL);
      log.info("AGACIRO SERVICE RESPONSE: {}", response);
      paymentNotification = mapper.readValue(response.getBody(), PaymentNotificationResponse.class);
    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      throw new ExternalHTTPRequestException("Error getting payment notification from Agaciro API");
    }

    return paymentNotification;
  }

  private void logError(Exception ex) {
    log.error("AGACIRO SERVICE: {}", ex.getMessage());
  }
}
