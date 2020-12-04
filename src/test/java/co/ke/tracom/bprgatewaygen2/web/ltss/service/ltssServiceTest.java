package co.ke.tracom.bprgatewaygen2.web.ltss.service;

import co.ke.tracom.bprgatewaygen2.core.util.CustomObjectMapper;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.institutions.InstitutionsRequest;
import co.ke.tracom.bprgatewaygen2.web.agaciro.data.institutions.InstitutionsResponse;
import co.ke.tracom.bprgatewaygen2.web.agaciro.service.AgaciroService;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.checkPayment.CheckPaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.nationalIDValidation.NationalIDValidationRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.nationalIDValidation.NationalIDValidationResponse;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.newSubscriber.NewSubscriberRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.newSubscriber.NewSubscriberResponse;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.paymentContribution.PaymentContributionRequest;
import co.ke.tracom.bprgatewaygen2.web.ltss.data.paymentContribution.PaymentContributionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
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
import org.springframework.test.util.ReflectionTestUtils;

import static co.ke.tracom.bprgatewaygen2.web.TestUtil.configureWireMockForOkPostResponse;
import static co.ke.tracom.bprgatewaygen2.web.TestUtil.configureWireMockForOkResponse;
import static co.ke.tracom.bprgatewaygen2.web.ltss.service.LtssServiceTestHelper.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ltssServiceTest {

    @ClassRule
    public static WireMockRule wireMockRule = new WireMockRule(8900); // start mock server at port 8900

    @Autowired
    LtssService ltssService;

    ObjectMapper objectMapper = new CustomObjectMapper();

    @Value("${third-party.base.url.test}")
    private String mockBaseUrl; // set base url to mock server's url

    @Value("${ltss.nationalId.validation.Url}")
    private String nationalIdValidationUrl;

    @Value("${ltss.payment.contribution.url}")
    private String paymentContributionUrl;

    @Value("${ltss.payment.check.url}")
    private String checkPaymentByRefNoUrl;

    @Value("${ltss.register.subscriber.url}")
    private String registerNewSubscriberURL;

    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(ltssService, "ltssBaseUrl", mockBaseUrl);
        wireMockRule.resetMappings();
        wireMockRule.resetScenarios();
        wireMockRule.resetRequests();
    }

    @Test
    public void validateNationalID() throws JsonProcessingException {
        NationalIDValidationResponse expectedResponse = generateNationalIdResponseObj();
        NationalIDValidationRequest requestObj = generateNationalIdRequestObj();
        String requestUrl = nationalIdValidationUrl;
        String expectedResponseString = objectMapper.writeValueAsString(expectedResponse);

        configureWireMockForOkPostResponse(wireMockRule, expectedResponseString, requestUrl);

        // Response from remote API
        NationalIDValidationResponse response = ltssService.validateNationalID(
                requestObj);
        assertNotNull(response);
        assertTrue(response instanceof NationalIDValidationResponse);

        // Both responses should match
        assertValidateIdResponse(expectedResponse, response);
    }

    private void assertValidateIdResponse(NationalIDValidationResponse expected, NationalIDValidationResponse actual) {
        assertEquals(expected.getIdentification(), actual.getIdentification());
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    public void sendPaymentContribution() throws JsonProcessingException {
        PaymentContributionResponse expectedResponse = generatePaymentContributionResponseObj();
        PaymentContributionRequest requestObj = generatePaymentContributionRequestObj();
        String requestUrl = paymentContributionUrl;
        String expectedResponseString = objectMapper.writeValueAsString(expectedResponse);

        configureWireMockForOkPostResponse(wireMockRule, expectedResponseString, requestUrl);

        // Response from remote API
        PaymentContributionResponse response = ltssService.sendPaymentContribution(
                requestObj);
        assertNotNull(response);
        assertTrue(response instanceof PaymentContributionResponse);

        // Both responses should match
        assertPaymentContributionResponse(expectedResponse, response);
    }

    private void assertPaymentContributionResponse(PaymentContributionResponse expected, PaymentContributionResponse actual) {
        assertEquals(expected.getAmount(), actual.getAmount());
        assertEquals(expected.getBeneficiary(), actual.getBeneficiary());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getExtReferenceNo(), actual.getExtReferenceNo());
        assertEquals(expected.getIntermediary(), actual.getIntermediary());
        assertEquals(expected.getPaymentDate(), actual.getPaymentDate());
    }

    @Test
    public void checkPaymentByRefNo() {
        CheckPaymentRequest requestObj = generateCheckPaymentRequestObj();
        String requestUrl = checkPaymentByRefNoUrl;

        wireMockRule.stubFor(WireMock.get(WireMock.urlPathEqualTo(requestUrl))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type",
                                "application/json;charset=UTF-8")));

        // Response from remote API
        ResponseEntity<?> response = ltssService.checkPaymentByRefNo(
                requestObj);
        assertNotNull(response);
        assertTrue(response instanceof ResponseEntity);
    }

    @Test
    public void registerNewSubscriber() throws JsonProcessingException {
        NewSubscriberResponse expectedResponse = generateSubscriberResponseObj();
        NewSubscriberRequest requestObj = generateSubscriberRequestObj();
        String requestUrl = registerNewSubscriberURL;
        String expectedResponseString = objectMapper.writeValueAsString(expectedResponse);

        configureWireMockForOkPostResponse(wireMockRule, expectedResponseString, requestUrl);

        // Response from remote API
        NewSubscriberResponse response = ltssService.registerNewSubscriber(
                requestObj);
        assertNotNull(response);
        assertTrue(response instanceof NewSubscriberResponse);

        // Both responses should match
        assertNewSubscriberResponse(expectedResponse, response);
    }

    private void assertNewSubscriberResponse(NewSubscriberResponse expected, NewSubscriberResponse actual) {
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getAmount(), actual.getAmount());
        assertEquals(expected.getFrequency(), actual.getFrequency());
        assertEquals(expected.getIdentification(), actual.getIdentification());
        assertEquals(expected.getOccupation(), actual.getOccupation());
        assertEquals(expected.getPhone(), actual.getPhone());
        assertEquals(expected.getStatus(), actual.getStatus());
    }
}