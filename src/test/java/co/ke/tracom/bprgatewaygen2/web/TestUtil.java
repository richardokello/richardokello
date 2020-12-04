package co.ke.tracom.bprgatewaygen2.web;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.springframework.http.HttpStatus;

public class TestUtil {

    public static void configureWireMockForOkPostResponse(WireMockRule wireMockRule, String expectedResponse, String url) {
        wireMockRule.stubFor(WireMock.post(WireMock.urlPathEqualTo(url))
                .willReturn(WireMock.aResponse()
                        .withBody(expectedResponse)
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type",
                                "application/json;charset=UTF-8")));
    }

    public static void configureWireMockForOkResponse(WireMockRule wireMockRule, String expectedResponse, String url) {
        wireMockRule.stubFor(WireMock.get(WireMock.urlPathEqualTo(url))
                .willReturn(WireMock.aResponse()
                        .withBody(expectedResponse)
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type",
                                "application/json;charset=UTF-8")));
    }
}
