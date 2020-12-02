package co.ke.tracom.bprgatewaygen2.web.agaciro.service;

import co.ke.tracom.bprgatewaygen2.core.util.CustomObjectMapper;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.paymentstatus.AcademicBridgePaymentStatusResponse;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.paymentstatus.PaymentStatusRequest;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.institutions.*;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.nid.DocumentNID;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.nid.ValidateNIDRequest;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.nid.ValidateNIDResponse;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.paymentNotification.Payment;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.paymentNotification.PaymentNotificationRequest;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.paymentNotification.PaymentNotificationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

public class AgaciroServiceTest {

    @ClassRule
    public static WireMockRule wireMockRule = new WireMockRule(8900); // start mock server at port 8900

    @Autowired
    AgaciroService agaciroService;

    ObjectMapper objectMapper = new CustomObjectMapper();

    @Value("${third-party.base.url.test}")
    private String baseUrl; // set base url to mock server's url

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

    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(agaciroService, "agaciroBaseURL", baseUrl);
        wireMockRule.resetMappings();
        wireMockRule.resetScenarios();
        wireMockRule.resetRequests();
    }

    @Test
    public void getInstitutions() throws JsonProcessingException {
        InstitutionsResponse expectedResponse = generateInstitutionsResponse();
        InstitutionsRequest institutionsRequest = generateInstitutionsRequest();
        String getInstitutionsUrl = generateGetInstitutionsUrl(institutionsRequest);
        String expectedResponseString = objectMapper.writeValueAsString(expectedResponse);

        configureWireMockForOkResponse(expectedResponseString, getInstitutionsUrl);

        // Response from remote API
        InstitutionsResponse response = agaciroService.getInstitutions(
                institutionsRequest);
        assertNotNull(response);
        assertTrue(response instanceof InstitutionsResponse);

        // Both responses should match
        assertGetInstitutionsResponse(expectedResponse, response);
    }

    private void assertGetInstitutionsResponse(InstitutionsResponse expected, InstitutionsResponse actual) {
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getInstitutions(), actual.getInstitutions());
        assertEquals(expected.getResult_code(), actual.getResult_code());
    }

    private String generateGetInstitutionsUrl(InstitutionsRequest institutionsRequest) {
        String requestURL = String.format(getInstitutionsURL,
                institutionsRequest.getUsername(),
                institutionsRequest.getPassword());
        return requestURL;
    }

    private InstitutionsRequest generateInstitutionsRequest() {
        InstitutionsRequest req = new InstitutionsRequest();
        req.setPassword("1234");
        req.setUsername("Putin");
        return req;
    }

    private InstitutionsResponse generateInstitutionsResponse() {
        InstitutionsResponse resp = new InstitutionsResponse();
        ArrayList<InstitutionEntity> institutions = new ArrayList<>();
        InstitutionEntity institution1 = new InstitutionEntity();
        institution1.setCode("0000");
        institution1.setName("CENTRAL TREASURY");
        institutions.add(institution1);
        resp.setInstitutions(institutions);
        resp.setMessage("Successful");
        resp.setResult_code("200");

        return resp;
    }

    @Test
    public void getInstitutionByName() throws JsonProcessingException {
        InstitutionByNameResponse expectedResponse = generateInstitutionByNameResponse();
        InstitutionByNameRequest institutionByNameRequest = generateInstitutionByNameRequest();
        String getInstitutionByNameUrl = generateGetInstitutionByNameUrl(institutionByNameRequest);
        String expectedResponseString = objectMapper.writeValueAsString(expectedResponse);

        configureWireMockForOkResponse(expectedResponseString, getInstitutionByNameUrl);

        // Response from remote API
        InstitutionByNameResponse response = agaciroService.getInstitutionByName(
                institutionByNameRequest);
        assertNotNull(response);
        assertTrue(response instanceof InstitutionByNameResponse);

        // Both responses should match
        assertGetInstitutionByNameResponse(expectedResponse, response);
    }

    private void assertGetInstitutionByNameResponse(InstitutionByNameResponse expected, InstitutionByNameResponse actual) {
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getInstitution(), actual.getInstitution());
        assertEquals(expected.getResult_code(), actual.getResult_code());
    }

    private String generateGetInstitutionByNameUrl(InstitutionByNameRequest institutionByNameRequest) {
        String requestURL = String.format(getInstitutionByNameURL,
                institutionByNameRequest.getUsername(),
                institutionByNameRequest.getPassword(),
                institutionByNameRequest.getInstitution_name());
        return requestURL;
    }

    private InstitutionByNameRequest generateInstitutionByNameRequest() {
        InstitutionByNameRequest req = new InstitutionByNameRequest();
        req.setPassword("1234");
        req.setUsername("Putin");
        req.setInstitution_name("ALADIN");
        return req;
    }

    private InstitutionByNameResponse generateInstitutionByNameResponse() {
        InstitutionByNameResponse resp = new InstitutionByNameResponse();
        InstitutionEntity institution = new InstitutionEntity();
        institution.setCode("0000");
        institution.setName("CENTRAL TREASURY");
        resp.setInstitution(institution);
        resp.setMessage("Successful");
        resp.setResult_code("200");
        return resp;
    }

    @Test
    public void getInstitutionByCode() throws JsonProcessingException {
        InstitutionByCodeResponse expectedResponse = generateInstitutionByCodeResponse();
        InstitutionByCodeRequest institutionByCodeRequest = generateInstitutionByCodeRequest();
        String getInstitutionByCodeUrl = generateGetInstitutionByCodeUrl(institutionByCodeRequest);
        String expectedResponseString = objectMapper.writeValueAsString(expectedResponse);

        configureWireMockForOkResponse(expectedResponseString, getInstitutionByCodeUrl);

        // Response from remote API
        InstitutionByCodeResponse response = agaciroService.getInstitutionByCode(
                institutionByCodeRequest);
        assertNotNull(response);
        assertTrue(response instanceof InstitutionByCodeResponse);

        // Both responses should match
        assertGetInstitutionByCodeResponse(expectedResponse, response);
    }

    private String generateGetInstitutionByCodeUrl(InstitutionByCodeRequest institutionByCodeRequest) {
        String requestURL = String.format(getInstitutionByCodeURL,
                institutionByCodeRequest.getUsername(),
                institutionByCodeRequest.getPassword(),
                institutionByCodeRequest.getInstitution_code());
        return requestURL;
    }

    private InstitutionByCodeRequest generateInstitutionByCodeRequest() {
        InstitutionByCodeRequest req = new InstitutionByCodeRequest();
        req.setPassword("1234");
        req.setUsername("Putin");
        req.setInstitution_code("0000");
        return req;
    }

    private InstitutionByCodeResponse generateInstitutionByCodeResponse() {
        InstitutionByCodeResponse resp = new InstitutionByCodeResponse();
        InstitutionEntity institution = new InstitutionEntity();
        institution.setCode("0000");
        institution.setName("CENTRAL TREASURY");
        resp.setInstitution(institution);
        resp.setMessage("Successful");
        resp.setResult_code("200");
        return resp;
    }

    private void assertGetInstitutionByCodeResponse(InstitutionByCodeResponse expected, InstitutionByCodeResponse actual) {
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getInstitution(), actual.getInstitution());
        assertEquals(expected.getResult_code(), actual.getResult_code());
    }

    @Test
    public void validateNID() throws JsonProcessingException {
        ValidateNIDResponse expectedResponse = generateNIDResponse();
        ValidateNIDRequest validateNIDRequest = generateValidateNIDRequest();
        String validateNIDRequestUrl = generatevalidateNIDRequestUrl(validateNIDRequest);
        String expectedResponseString = objectMapper.writeValueAsString(expectedResponse);

        configureWireMockForOkResponse(expectedResponseString, validateNIDRequestUrl);

        // Response from remote API
        ValidateNIDResponse response = agaciroService.validateNID(
                validateNIDRequest);
        assertNotNull(response);
        assertTrue(response instanceof ValidateNIDResponse);

        // Both responses should match
        assertValidateNIDResponse(expectedResponse, response);
    }

    private String generatevalidateNIDRequestUrl(ValidateNIDRequest validateNIDRequest) {
        String requestURL = String.format(validateNIDURL,
                validateNIDRequest.getUsername(),
                validateNIDRequest.getPassword(),
                validateNIDRequest.getNid());
        return requestURL;
    }

    private void assertValidateNIDResponse(ValidateNIDResponse expected, ValidateNIDResponse actual) {
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getDocument(), actual.getDocument());
        assertEquals(expected.getResult_code(), actual.getResult_code());
    }

    private ValidateNIDRequest generateValidateNIDRequest() {
        ValidateNIDRequest req = new ValidateNIDRequest();
        req.setNid("1234-abcd");
        req.setPassword("12345");
        req.setUsername("putin");
        return req;
    }

    private ValidateNIDResponse generateNIDResponse() {
        ValidateNIDResponse resp = new ValidateNIDResponse();
        DocumentNID d = new DocumentNID();
        d.setNid("1234-abcd");
        resp.setDocument(d);
        resp.setMessage("Successful");
        resp.setMessage("0");
        return resp;
    }

    @Test
    public void sendPaymentNotification() throws JsonProcessingException {
        PaymentNotificationResponse expectedResponse = generatePaymentNotificationResponse();
        PaymentNotificationRequest paymentNotificationRequest = generatePaymentNotificationRequest();
        String notificationRequestUrl = paymentNotificationURL;
        String expectedResponseString = objectMapper.writeValueAsString(expectedResponse);

        configureWireMockForOkPostResponse(expectedResponseString, notificationRequestUrl);

        // Response from remote API
        PaymentNotificationResponse response = agaciroService.sendPaymentNotification(
                paymentNotificationRequest);
        assertNotNull(response);
        assertTrue(response instanceof PaymentNotificationResponse);

        // Both responses should match
        assertPaymentNotificationResponse(expectedResponse, response);
    }

    private void assertPaymentNotificationResponse(PaymentNotificationResponse expected, PaymentNotificationResponse actual) {
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getPayment(), actual.getPayment());
        assertEquals(expected.getResult_code(), actual.getResult_code());
    }

    private PaymentNotificationRequest generatePaymentNotificationRequest() {
        PaymentNotificationRequest req = new PaymentNotificationRequest();
        req.setAmount(300);
        req.setContributor_type("company");
        req.setUsername("putin");
        req.setPassword("12345");
        req.setCredited_account_number("98765");
        req.setDesignation("John Doe");
        req.setEmployee(true);
        req.setInstitution_code("200");
        req.setMovement_number("888");
        req.setNid("3077654");
        req.setOperation_nature(new Date());
        req.setPhone_number("0112345");
        req.setTransaction_date("Dec 2020");
        req.setReason("Sample reason");
        req.setPassport_number("778899");

        return req;
    }

    private PaymentNotificationResponse generatePaymentNotificationResponse() {
        PaymentNotificationResponse resp = new PaymentNotificationResponse();
        resp.setMessage("Successful");
        resp.setResult_code("0");
        Payment p = new Payment();
        p.setPayment("7000");
        resp.setPayment(p);
        return resp;
    }

    public void configureWireMockForOkResponse(String expectedResponse, String url) {
        wireMockRule.stubFor(WireMock.get(WireMock.urlPathEqualTo(url))
                .willReturn(WireMock.aResponse()
                        .withBody(expectedResponse)
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type",
                                "application/json;charset=UTF-8")));
    }

    public void configureWireMockForOkPostResponse(String expectedResponse, String url) {
        wireMockRule.stubFor(WireMock.post(WireMock.urlPathEqualTo(url))
                .willReturn(WireMock.aResponse()
                        .withBody(expectedResponse)
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type",
                                "application/json;charset=UTF-8")));
    }

}