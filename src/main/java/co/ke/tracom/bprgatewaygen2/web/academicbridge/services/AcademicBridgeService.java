package co.ke.tracom.bprgatewaygen2.web.academicbridge.services;

import co.ke.tracom.bprgatewaygen2.core.tracomhttp.resthttp.RestHTTPService;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.AcademicBridgeResponse;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.paymentstatus.AcademicBridgePaymentStatusResponse;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.paymentstatus.PaymentStatusRequest;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.savepayment.SavePaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.studentdetails.GetStudentDetailsRequest;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.studentdetails.GetStudentDetailsResponse;
import co.ke.tracom.bprgatewaygen2.web.exceptions.custom.ExternalHTTPRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AcademicBridgeService {

  private final RestHTTPService restHTTPService;

  @Value("${academic-bridge.api-key}")
  private String academicBridgeAPIKey;

  @Value("${academic-bridge.api-secret}")
  private String academicBridgeAPISecret;

  /* Provide bill number as first substitution */
  @Value("${academic-bridge.get-student-details-url}%s?API_KEY=%s&API_SECRET=%s")
  private String getStudentDetailsURL;

  @Value("${academic-bridge.base.url}")
  private String baseUrl;

  /* Provide bill number as first substitution */
  @Value(
      "${academic-bridge.save-payment-url}%s?"
          + "API_KEY=%s&"
          + "API_SECRET=%s&"
          + "reference_number=%s&"
          + "paid_amount=%s&"
          + "sender_name=%s&"
          + "sender_phone_number=%s&"
          + "reason=%s")
  private String savePaymentURL;

  /* Provide reference number as first substitution */
  @Value("${academic-bridge.check-payment-status-url}%s?API_KEY=%s&API_SECRET=%s")
  private String checkPaymentStatusURL;

  /**
   * To get bill details given the bill number.
   *
   * @param request Student bill number
   * @return student Information
   */
  public GetStudentDetailsResponse fetchStudentDetailsByBillNumber(
      GetStudentDetailsRequest request) {
    GetStudentDetailsResponse response;
    try {
      String requestURL =
          String.format(
              getStudentDetailsURL,
              request.getBillNumber(),
              academicBridgeAPIKey,
              academicBridgeAPISecret);
      String results = restHTTPService.sendGetRequest(baseUrl + requestURL);
      ObjectMapper mapper = new ObjectMapper();
      response = mapper.readValue(results, GetStudentDetailsResponse.class);
      log.info("ACADEMIC BRIDGE RESPONSE: {}", response);
    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      throw new ExternalHTTPRequestException(
          "Error fetching student details from Academic Bridge API");
    }
    return response;
  }

  /**
   * Save payment on academic bridge school’s database.
   *
   * @param savePaymentRequest save payment details
   * @return payment results
   */
  public AcademicBridgeResponse sendPaymentDetailsToAcademicBridge(
      SavePaymentRequest savePaymentRequest) {
    AcademicBridgeResponse response;

    try {
      String requestURL =
          String.format(
              savePaymentURL,
              savePaymentRequest.getBillNumber(),
              academicBridgeAPIKey,
              academicBridgeAPISecret,
              savePaymentRequest.getReferenceNo(),
              savePaymentRequest.getPaidAmount(),
              savePaymentRequest.getSenderName(),
              savePaymentRequest.getSenderPhoneNo(),
              savePaymentRequest.getReason());
      String results = restHTTPService.sendGetRequest(baseUrl + requestURL);
      ObjectMapper mapper = new ObjectMapper();
      response = mapper.readValue(results, AcademicBridgeResponse.class);
      log.info("ACADEMIC BRIDGE RESPONSE: {}", response);
    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      throw new ExternalHTTPRequestException("Error sending payment to Academic Bridge API");
    }

    return response;
  }

  /**
   * Check if the payment was successfully saved on academic bridge given bank’s reference number.
   *
   * @param request Transaction reference no
   * @return Transaction posting
   */
  public AcademicBridgePaymentStatusResponse checkPaymentStatus(PaymentStatusRequest request) {
    AcademicBridgePaymentStatusResponse response;
    try {
      String requestURL =
          String.format(
              checkPaymentStatusURL,
              request.getReferenceNo(),
              academicBridgeAPIKey,
              academicBridgeAPISecret);
      String results = restHTTPService.sendGetRequest(baseUrl + requestURL);
      ObjectMapper mapper = new ObjectMapper();
      response = mapper.readValue(results, AcademicBridgePaymentStatusResponse.class);
      log.info("ACADEMIC BRIDGE RESPONSE: {}", response);
    } catch (Exception ex) {
      ex.printStackTrace();
      logError(ex);
      throw new ExternalHTTPRequestException(
          "Error checking payment status from Academic Bridge API");
    }
    return response;
  }

  private void logError(Exception ex) {
    log.error("ACADEMIC BRIDGE: {}", ex.getMessage());
  }
}
