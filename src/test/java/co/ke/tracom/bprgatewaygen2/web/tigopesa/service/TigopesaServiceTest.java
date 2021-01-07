package co.ke.tracom.bprgatewaygen2.web.tigopesa.service;

import co.ke.tracom.bprgatewaygen2.web.config.CustomObjectMapper;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.checkBalance.CheckBalanceRequest;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.checkBalance.CheckBalanceResponse;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.payment.BillPaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.payment.BillPaymentResponse;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.transactionStatus.TransactionStatusRequest;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.transactionStatus.TransactionStatusResponse;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.walletPayment.WalletPaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.walletPayment.WalletPaymentResponse;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TigopesaServiceTest {

    @ClassRule
    public static WireMockRule wireMockRule = new WireMockRule(8900); // start mock server at port 8900

    @Autowired
    TigopesaService tigopesaService;

    ObjectMapper objectMapper = new CustomObjectMapper();

    @Value("/TELEPIN")
    private String requestURL;

    @Value("${base.url.test}")
    private String baseUrl;

    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(tigopesaService, "baseURL", baseUrl);
        wireMockRule.resetMappings();
        wireMockRule.resetScenarios();
        wireMockRule.resetRequests();
    }

    @Test
    public void payBill() {
        BillPaymentResponse expectedResponseObj = generatePayBillResponseObj();
        String xmlResponseString = generatePayBillResponseString();
        BillPaymentRequest billPaymentRequest = generateBillPaymentRequestObj();
        String reqURL = requestURL;

        configureWireMockForOkResponse(xmlResponseString, reqURL);

        // Response from remote API
        BillPaymentResponse response = tigopesaService.payBill(billPaymentRequest);
        assertNotNull(response);
        assertTrue(response instanceof BillPaymentResponse);

        // Both responses should match
        assertBillPaymentResponse(expectedResponseObj, response);
    }

    private void assertBillPaymentResponse(BillPaymentResponse expected, BillPaymentResponse actual) {
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getResult(), actual.getResult());
        assertEquals(expected.get_message(), actual.get_message());
        assertEquals(expected.getReferenceId(), actual.getReferenceId());
        assertEquals(expected.getTransactionId(), actual.getTransactionId());
        assertEquals(expected.getVoucherCode(), actual.getVoucherCode());
        assertEquals(expected.getStatus(), actual.getStatus());
    }

    private BillPaymentRequest generateBillPaymentRequestObj() {
        BillPaymentRequest req = new BillPaymentRequest();
        req.setUsername("XXXX");
        req.setPassword("xxxx");
        req.setMsisdn("255712230324");
        req.setAmount(2000);
        req.setTransactionId("1191");
        req.setBillNumber("41602900026");
        req.setCompanyCode("888999");

        return req;
    }

    private String generatePayBillResponseString () {
        String xml =  "<TCSReply>\n" +
                "<Result>0</Result>\n" +
                "<Message>Bill Payment successful. Reference</Message>\n" +
                "<transactionId>1191</transactionId>\n" +
                "<referenceId>000174213641</referenceId >\n" +
                "<status>PST</status>\n" +
                "<voucherCode>123</voucherCode>\n" +
                "<message>Dear Customer ,payment success</message>\n" +
                "</TCSReply>";

        return xml;
    }

    private BillPaymentResponse generatePayBillResponseObj () {
        BillPaymentResponse resp = new BillPaymentResponse();
        resp.setResult("0");
        resp.setMessage("Bill Payment successful. Reference");
        resp.setTransactionId("1191");
        resp.setReferenceId("000174213641");
        resp.setStatus("PST");
        resp.setVoucherCode(123);
        resp.set_message("Dear Customer ,payment success");

        return resp;
    }

    @Test
    public void checkBalance() {
        CheckBalanceResponse expectedResponseObj = generateCheckBalResponseObj();
        String xmlResponseString = generateCheckBalResponseString();
        CheckBalanceRequest checkBalanceRequest = generateCheckBalRequestObj();
        String reqURL = requestURL;

        configureWireMockForOkResponse(xmlResponseString, reqURL);

        // Response from remote API
        CheckBalanceResponse response = tigopesaService.checkBalance(checkBalanceRequest);
        assertNotNull(response);
        assertTrue(response instanceof CheckBalanceResponse);

        // Both responses should match
        assertCheckBalResponse(expectedResponseObj, response);
    }

    private void assertCheckBalResponse(CheckBalanceResponse expected, CheckBalanceResponse actual) {
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getAmount(), actual.getAmount(), 0.001);
        assertEquals(expected.getReferenceId(), expected.getReferenceId());
        assertEquals(expected.getResult(), actual.getResult());
    }

    private CheckBalanceRequest generateCheckBalRequestObj() {
        CheckBalanceRequest checkBalanceRequest = new CheckBalanceRequest();
        checkBalanceRequest.setUsername("XXXX");
        checkBalanceRequest.setPassword("xxxx");
        checkBalanceRequest.setMsisdn("255712230324");
        checkBalanceRequest.setBillNumber("123456789");
        checkBalanceRequest.setCompanyCode("123");

        return checkBalanceRequest;
    }

    private String generateCheckBalResponseString() {
        return "<TCSReply>\n" +
                "<Result>0</Result>\n" +
                "<Message>Balance Retrieved Successfully. </Message>\n" +
                "<amount>20000.0</amount>\n" +
                "<referenceId>PST</referenceId>\n" +
                "</TCSReply>";
    }

    private CheckBalanceResponse generateCheckBalResponseObj() {
        CheckBalanceResponse checkBalanceResponse = new CheckBalanceResponse();
        checkBalanceResponse.setAmount(20000);
        checkBalanceResponse.setMessage("Balance Retrieved Successfully. ");
        checkBalanceResponse.setReferenceId("PST");
        checkBalanceResponse.setResult("0");

        return checkBalanceResponse;
    }

    @Test
    public void checkTransactionStatus() {
        TransactionStatusResponse expectedResponseObj = generateTxStatusResponseObj();
        String xmlResponseString = generateTxStatusResponseString();
        TransactionStatusRequest transactionStatusRequest = generateTxStatusRequestObj();
        String reqURL = requestURL;

        configureWireMockForOkResponse(xmlResponseString, reqURL);

        // Response from remote API
        TransactionStatusResponse response = tigopesaService.checkTransactionStatus(transactionStatusRequest);
        assertNotNull(response);
        assertTrue(response instanceof TransactionStatusResponse);

        // Both responses should match
        assertTxStatusResponse(expectedResponseObj, response);
    }

    private void assertTxStatusResponse(TransactionStatusResponse expected, TransactionStatusResponse actual) {
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.get_message(), actual.get_message());
        assertEquals(expected.getReferenceId(), actual.getReferenceId());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getResult(), actual.getResult());
        assertEquals(expected.getTransactionId(), actual.getTransactionId());
        assertEquals(expected.getVoucherCode(), actual.getVoucherCode());
    }

    private TransactionStatusRequest generateTxStatusRequestObj() {
        TransactionStatusRequest req = new TransactionStatusRequest();
        req.setUsername("XXXX");
        req.setPassword("xxxx");
        req.setMsisdn("255712230324");
        req.setTransactionId("1194");
        req.setBillNumber("123456789");
        req.setCompanyCode("123");

        return req;
    }

    private TransactionStatusResponse generateTxStatusResponseObj() {
        TransactionStatusResponse resp = new TransactionStatusResponse();
        resp.setResult("0");
        resp.setMessage("Bill Payment successful. Reference");
        resp.setTransactionId("1191");
        resp.setReferenceId("000174213641");
        resp.setStatus("PST");
        resp.setVoucherCode(123);
        resp.set_message("Dear Customer ,payment success");
        return resp;
    }

    private String generateTxStatusResponseString() {
        return "<TCSReply><Result>0</Result>\n" +
                "<Message>Bill Payment successful. Reference</Message>\n" +
                "<transactionId>1191</transactionId>\n" +
                "<referenceId>000174213641</referenceId >\n" +
                "<status>PST</status>\n" +
                "<voucherCode>123</voucherCode>\n" +
                "<message>Dear Customer ,payment success</message>\n" +
                "</TCSReply>";
    }

    @Test
    public void creditWallet() {
        WalletPaymentResponse expectedResponseObj = generateCreditWalletResponseObj();
        String xmlResponseString = generateCreditWalletResponseString();
        WalletPaymentRequest walletPaymentRequest = generateCreditWalletRequestObj();
        String reqURL = requestURL;

        configureWireMockForOkResponse(xmlResponseString, reqURL);

        // Response from remote API
        WalletPaymentResponse response = tigopesaService.creditWallet(walletPaymentRequest);
        assertNotNull(response);
        assertTrue(response instanceof WalletPaymentResponse);

        // Both responses should match
        assertTxCreditWalletResponse(expectedResponseObj, response);
    }

    private void assertTxCreditWalletResponse(WalletPaymentResponse expected, WalletPaymentResponse actual) {
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.get_message(), actual.get_message());
        assertEquals(expected.getReferenceId(), actual.getReferenceId());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getResult(), actual.getResult());
        assertEquals(expected.getTransactionId(), actual.getTransactionId());
        assertEquals(expected.getVoucherCode(), actual.getVoucherCode());
    }

    private WalletPaymentRequest generateCreditWalletRequestObj() {
        WalletPaymentRequest req = new WalletPaymentRequest();
        req.setUsername("XXXX");
        req.setPassword("xxxx");
        req.setMsisdn("255712230324");
        req.setTransactionId("1194");
        req.setBillNumber("123456789");
        req.setCompanyCode("123");

        return req;
    }

    private WalletPaymentResponse generateCreditWalletResponseObj() {
        WalletPaymentResponse resp  =  new WalletPaymentResponse();
        resp.setResult("0");
        resp.setMessage("Bill Payment successful. Reference");
        resp.setTransactionId("1191");
        resp.setStatus("PST");
        resp.setVoucherCode(123);
        resp.set_message("Dear Customer ,payment success");
        resp.setReferenceId("000174213641");
        return resp;
    }

    private String generateCreditWalletResponseString() {
        return "<TCSReply><Result>0</Result>\n" +
                "<Message>Bill Payment successful. Reference</Message>\n" +
                "<transactionId>1191</transactionId>\n" +
                "<referenceId>000174213641</referenceId >\n" +
                "<status>PST</status>\n" +
                "<voucherCode>123</voucherCode>\n" +
                "<message>Dear Customer ,payment success</message>\n" +
                "</TCSReply>";
    }

    public void configureWireMockForOkResponse(String expectedResponse, String url) {
        // Mock of Academic Bridge API
        wireMockRule.stubFor(WireMock.post(WireMock.urlPathEqualTo(url))
                .willReturn(WireMock.aResponse()
                        .withBody(expectedResponse)
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type",
                                "application/xml;charset=UTF-8")));
    }
}