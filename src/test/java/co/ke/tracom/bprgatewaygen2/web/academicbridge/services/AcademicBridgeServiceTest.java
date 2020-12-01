package co.ke.tracom.bprgatewaygen2.web.academicbridge.services;

import co.ke.tracom.bprgatewaygen2.core.util.CustomObjectMapper;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.studentdetails.GetStudentDetails;
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
import org.springframework.http.ResponseEntity;
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
    public static WireMockRule wireMockRule = new WireMockRule(8900);

    @Autowired
    AcademicBridgeService academicBridgeService;

    ObjectMapper objectMapper = new CustomObjectMapper();

    @Value("${academic-bridge.base.url.test}")
    private String baseUrl;

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
            "reference_number=%s&" +
            "paid_amount=%s&" +
            "sender_name=%&" +
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
        String expectedResponseString = objectMapper.writeValueAsString(expectedResponse);
        GetStudentDetails userDetailsRequest = generateStudentDetailsRequest();

        String url = generateGetStudentDetailsUrl(userDetailsRequest);

        // Mock of Academic Bridge API
        wireMockRule.stubFor(WireMock.get(WireMock.urlPathEqualTo(url))
                .willReturn(WireMock.aResponse()
                        .withBody(expectedResponseString)
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type",
                                "application/json;charset=UTF-8")));

        // Response from remote API
        ResponseEntity<GetStudentDetailsResponse> response = academicBridgeService.fetchStudentDetailsByBillNumber(userDetailsRequest);
        assertNotNull(response);
        assertTrue(response.getBody() instanceof GetStudentDetailsResponse);

        GetStudentDetailsResponse actualResponse = generateStudentDetailsResponse();
        assertNotNull(actualResponse);

        // Both response should match
        assertGetStudentResponseDetails(expectedResponse, actualResponse);
    }

    @Test
    public void sendPaymentDetailsToAcademicBridge() {
    }

    @Test
    public void checkPaymentStatus() {
    }

    // Sample response data
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

    private GetStudentDetails generateStudentDetailsRequest () {
        GetStudentDetails details = new GetStudentDetails();
        details.setBillNumber("12345");
        return details;
    }

    private String generateGetStudentDetailsUrl(GetStudentDetails studentDetails) {
        String requestURL = String.format(getStudentDetailsURL,
                studentDetails.getBillNumber(),
                academicBridgeAPIKey,
                academicBridgeAPISecret);
        return baseUrl + requestURL;
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
}