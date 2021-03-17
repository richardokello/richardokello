package co.ke.tracom.bprgatewaygen2.web.academicbridge.services;

import co.ke.tracom.bprgatewaygen2.web.config.CustomObjectMapper;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.AcademicBridgeResponse;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.paymentstatus.AcademicBridgePaymentStatusResponse;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.paymentstatus.PaymentStatusRequest;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.savepayment.SavePaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.studentdetails.GetStudentDetailsRequest;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.studentdetails.GetStudentDetailsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AcademicBridgeServiceTest {

    @ClassRule
    public static WireMockRule wireMockRule = new WireMockRule(8900); // start mock server at port 8900

    @Autowired
    AcademicBridgeService academicBridgeService;

    ObjectMapper objectMapper = new CustomObjectMapper();

    @Value("${academic-bridge.base.url.test}")
    private String baseUrl; // set base url to mock server's url

    @Value("${academic-bridge.api-key}")
    private String academicBridgeAPIKey;

    @Value("${academic-bridge.api-secret}")
    private String academicBridgeAPISecret;

    // ACADEMIC BRIDGE API ENDPOINTS
    @Value("${academic-bridge.get-student-details-url}%s?API_KEY=%s&API_SECRET=%s")
    private String getStudentDetailsURL;

    @Value("${academic-bridge.save-payment-url}%s?" +
            "API_KEY=%s&" +
            "API_SECRET=%s&" +
            "reference_number=%s" +
            "paid_amount=%s&" +
            "sender_name=%s&" +
            "sender_phone_number=%s&" +
            "reason=%s"
    )
    private String savePaymentURL;

    @Value("${academic-bridge.check-payment-status-url}%s")
    private String checkPaymentStatusURL;

    @Before
    public void init() {
        ReflectionTestUtils.setField(academicBridgeService, "baseUrl", baseUrl);
        wireMockRule.resetMappings();
        wireMockRule.resetScenarios();
        wireMockRule.resetRequests();
    }

    @Test
    public void fetchStudentDetailsByBillNumber() throws JsonProcessingException {
        GetStudentDetailsResponse expectedResponse = generateStudentDetailsResponse();
        GetStudentDetailsRequest userDetailsRequest = generateStudentDetailsRequest();
        String studentDetailsUrl = generateGetStudentDetailsUrl(userDetailsRequest);
        String expectedResponseString = objectMapper.writeValueAsString(expectedResponse);

        configureWireMockForOkResponse(expectedResponseString, studentDetailsUrl);

        // Response from remote API
        GetStudentDetailsResponse response = academicBridgeService.fetchStudentDetailsByBillNumber(userDetailsRequest);
        assertNotNull(response);
        assertTrue(response instanceof GetStudentDetailsResponse);

        GetStudentDetailsResponse actualResponse = generateStudentDetailsResponse();
        assertNotNull(actualResponse);

        // Both responses should match
        assertGetStudentResponseDetails(expectedResponse, actualResponse);
    }

    @Test
    public void sendPaymentDetailsToAcademicBridge() throws JsonProcessingException {
        AcademicBridgeResponse expectedResponse = generateSendPaymentResponse();
        SavePaymentRequest savePaymentRequest = generateSavePaymentRequestObj();
        String savePaymentUrl = generateSavePaymentRequestUrl(savePaymentRequest);
        String expectedResponseString = objectMapper.writeValueAsString(expectedResponse);

        configureWireMockForOkResponse(expectedResponseString, savePaymentUrl);

        // Response from remote API
        AcademicBridgeResponse response = academicBridgeService.sendPaymentDetailsToAcademicBridge(savePaymentRequest);
        assertNotNull(response);
        assertTrue(response instanceof AcademicBridgeResponse);

        // Both responses should match
        assertSendPaymentResponse(expectedResponse, response);
    }

    @Test
    public void checkPaymentStatus() throws JsonProcessingException {
        AcademicBridgePaymentStatusResponse expectedResponse = generatePaymentStatusResponse();
        PaymentStatusRequest paymentStatusRequest = generatePaymentStatusRequest();
        String savePaymentUrl = generatePaymentStatusRequestUrl(paymentStatusRequest);
        String expectedResponseString = objectMapper.writeValueAsString(expectedResponse);

        configureWireMockForOkResponse(expectedResponseString, savePaymentUrl);

        // Response from remote API
        AcademicBridgePaymentStatusResponse response = academicBridgeService.checkPaymentStatus(
                paymentStatusRequest);
        assertNotNull(response);
        assertTrue(response instanceof AcademicBridgePaymentStatusResponse);

        // Both responses should match
        assertPaymentStatusResponse(expectedResponse, response);
    }

    private void assertPaymentStatusResponse(AcademicBridgePaymentStatusResponse expected, AcademicBridgePaymentStatusResponse actual) {
        assertEquals(expected.getError_code(), actual.getError_code());
        assertEquals(expected.getError_msg(), actual.getError_msg());
        assertEquals(expected.getReference_number(), actual.getReference_number());
        assertEquals(expected.getAb_reference(), actual.getAb_reference());
    }

    private String generatePaymentStatusRequestUrl(PaymentStatusRequest request) {
        String requestURL = String.format(checkPaymentStatusURL,
                request.getReferenceNo(),
                academicBridgeAPIKey,
                academicBridgeAPISecret);
        return requestURL;
    }

    private PaymentStatusRequest generatePaymentStatusRequest() {
        PaymentStatusRequest req = new PaymentStatusRequest();
        req.setReferenceNo("1234-abcd");
        return req;
    }

    private AcademicBridgePaymentStatusResponse generatePaymentStatusResponse() {
        AcademicBridgePaymentStatusResponse resp = new AcademicBridgePaymentStatusResponse();
        resp.setAb_reference("1234-abcd");
        resp.setError_code(0);
        resp.setError_msg("No Error, Payment found");
        resp.setSuccess(true);

        return resp;
    }

    // Student details Helper methods
    private GetStudentDetailsResponse generateStudentDetailsResponse() {
        GetStudentDetailsResponse details = new GetStudentDetailsResponse();
        details.setBill_number("12345");
        details.setError_code(0); // No error
        details.setError_msg("Student Not Found");
        details.setSchool_account_name("Dev Account");
        details.setSchool_account_number("9999999");
        details.setSchool_ide(100);
        details.setSchool_name("Tracom Dev School");
        details.setStudent_name("John Doe");
        details.setStudent_reg_number("9000");
        details.setSuccess(true);
        details.setType_of_payment("Fees");

        return details;
    }

    private GetStudentDetailsRequest generateStudentDetailsRequest () {
        GetStudentDetailsRequest details = new GetStudentDetailsRequest();
        details.setBillNumber("12345");
        return details;
    }

    // Generates relative url for getting student details
    private String generateGetStudentDetailsUrl(GetStudentDetailsRequest studentDetails) {
        String requestURL = String.format(getStudentDetailsURL,
                studentDetails.getBillNumber(),
                academicBridgeAPIKey,
                academicBridgeAPISecret);
        return requestURL;
    }

    private void assertGetStudentResponseDetails (GetStudentDetailsResponse actual, GetStudentDetailsResponse expected) {
        assertEquals(actual.getError_code(), expected.getError_code());
        assertEquals(actual.getError_msg(), expected.getError_msg());
        assertEquals(actual.getSchool_ide(), expected.getSchool_ide());
        assertEquals(actual.getType_of_payment(), expected.getType_of_payment());
        assertEquals(actual.getStudent_reg_number(), expected.getStudent_reg_number());
        assertEquals(actual.getStudent_name(), expected.getStudent_name());
        assertEquals(actual.getBill_number(), expected.getBill_number());
        assertEquals(actual.getSchool_account_name(), expected.getSchool_account_name());
        assertEquals(actual.getSchool_account_number(), expected.getSchool_account_number());
        assertEquals(actual.getSchool_name(), expected.getSchool_name());
        // TODO: add success field
    }

    public void configureWireMockForOkResponse(String expectedResponse, String url) {
        // Mock of Academic Bridge API
        wireMockRule.stubFor(WireMock.get(WireMock.urlPathEqualTo(url))
                .willReturn(WireMock.aResponse()
                        .withBody(expectedResponse)
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type",
                                "application/json;charset=UTF-8")));
    }

    // Send payment helper methods
    private void assertSendPaymentResponse(AcademicBridgeResponse expected, AcademicBridgeResponse actual) {
        assertEquals(actual.getError_code(), expected.getError_code());
        assertEquals(actual.getError_msg(), expected.getError_msg());
        assertEquals(actual.getReference_number(), expected.getReference_number());
        assertEquals(actual.isSuccess(), expected.isSuccess());
    }

    private String generateSavePaymentRequestUrl(SavePaymentRequest savePaymentRequest) {
        System.out.println("======================> " + savePaymentURL);
        String requestURL = String.format(savePaymentURL,
                savePaymentRequest.getBillNumber(),
                academicBridgeAPIKey,
                academicBridgeAPISecret,
                savePaymentRequest.getReferenceNo(),
                "500",
                savePaymentRequest.getSenderName(),
                savePaymentRequest.getSenderPhoneNo(),
                savePaymentRequest.getReason());
        return requestURL;
    }

    private SavePaymentRequest generateSavePaymentRequestObj() {
        SavePaymentRequest req = new SavePaymentRequest();
        req.setBillNumber("12345");
        req.setPaidAmount(500);
        req.setReason("Bill");
        req.setReferenceNo("1234-abcd");
        req.setSenderName("John-Doe");
        req.setSenderPhoneNo("112233");

        return req;
    }

    private AcademicBridgeResponse generateSendPaymentResponse() {
        AcademicBridgeResponse response = new AcademicBridgeResponse();
        response.setError_code(0);
        response.setError_msg("No error, payment is saved");
        response.setReference_number("123ABC");
        response.setSuccess(true);

        return response;
    }

}