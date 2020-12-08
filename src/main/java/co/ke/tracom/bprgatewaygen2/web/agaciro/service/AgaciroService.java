package co.ke.tracom.bprgatewaygen2.web.agaciro.service;

import co.ke.tracom.bprgatewaygen2.core.tracomhttp.resthttp.RestHTTPService;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.institutions.InstitutionByCodeRequest;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.institutions.InstitutionByCodeResponse;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.institutions.InstitutionByNameRequest;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.institutions.InstitutionByNameResponse;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.institutions.InstitutionsRequest;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.institutions.InstitutionsResponse;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.nid.ValidateNIDRequest;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.nid.ValidateNIDResponse;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.paymentNotification.PaymentNotificationRequest;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.paymentNotification.PaymentNotificationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgaciroService {

  private final RestHTTPService restHTTPService;
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
      String requestURL = String.format(getInstitutionsURL,
          getInstitutionsRequest.getUsername(),
          getInstitutionsRequest.getPassword());
      String results = restHTTPService.sendGetRequest(agaciroBaseURL + requestURL);
      ObjectMapper mapper = new ObjectMapper();
      institutions = mapper.readValue(results, InstitutionsResponse.class);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return institutions;
  }

  /**
   * Returns details of an institution based on name
   *
   * @param getInstitutionByNameRequest
   * @return
   */
  public InstitutionByNameResponse getInstitutionByName(
      InstitutionByNameRequest getInstitutionByNameRequest) {
    InstitutionByNameResponse institution = null;

    try {
      String requestURL = String.format(getInstitutionByNameURL,
          getInstitutionByNameRequest.getUsername(),
          getInstitutionByNameRequest.getPassword(),
          getInstitutionByNameRequest.getInstitution_name());
      String results = restHTTPService.sendGetRequest(agaciroBaseURL + requestURL);
      ObjectMapper mapper = new ObjectMapper();
      institution = mapper.readValue(results, InstitutionByNameResponse.class);
    } catch (Exception ex) {
      ex.printStackTrace();
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
      String requestURL = String.format(getInstitutionByCodeURL,
          getInstitutionByCodeRequest.getUsername(),
          getInstitutionByCodeRequest.getPassword(),
          getInstitutionByCodeRequest.getInstitution_code());
      String results = restHTTPService.sendGetRequest(requestURL);
      ObjectMapper mapper = new ObjectMapper();
      institution = mapper.readValue(results, InstitutionByCodeResponse.class);
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return institution;
  }

  /**
   * Returns registered NID from NIDA
   *
   * @param validateNIDRequest username, password and Rwandan National ID
   * @return ValidateNIDResponse
   */
  public ValidateNIDResponse validateNID(ValidateNIDRequest validateNIDRequest) {
    ValidateNIDResponse response = null;

    try {
      String requestURL = String.format(validateNIDURL,
          validateNIDRequest.getUsername(),
          validateNIDRequest.getPassword(),
          validateNIDRequest.getNid());
      String results = restHTTPService.sendGetRequest(agaciroBaseURL + requestURL);
      ObjectMapper mapper = new ObjectMapper();
      response = mapper.readValue(results, ValidateNIDResponse.class);
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return response;
  }

  /**
   * @param getPaymentNotificationRequest
   * @return
   */
  public PaymentNotificationResponse sendPaymentNotification(
      PaymentNotificationRequest getPaymentNotificationRequest) {
    PaymentNotificationResponse paymentNotification = null;

    try {
      ResponseEntity<String> response = restHTTPService
          .postRequest(getPaymentNotificationRequest, agaciroBaseURL + paymentNotificationURL);
      ObjectMapper mapper = new ObjectMapper();
      paymentNotification = mapper.readValue(response.getBody(), PaymentNotificationResponse.class);
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return paymentNotification;
  }
}

