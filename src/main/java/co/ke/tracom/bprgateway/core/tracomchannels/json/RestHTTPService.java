package co.ke.tracom.bprgateway.core.tracomchannels.json;

import co.ke.tracom.bprgateway.core.tracomchannels.CustomHTTPCommunicationInterface;
import co.ke.tracom.bprgateway.web.mobicash.data.MobiCashRequest;
import co.ke.tracom.bprgateway.web.mobicash.data.authentication.AuthenticationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/** This class provides convenient methods for making HTTP requests some given URL */
@Service
@RequiredArgsConstructor
public class RestHTTPService implements CustomHTTPCommunicationInterface {
  private static final String USER_AGENT = "Mozilla/5.0";
  private final Logger logger = LoggerFactory.getLogger(RestHTTPService.class);

  public ResponseEntity<String> postRequest(Object request, String url) throws Exception {
    // Fetch Host
    /* Pack the URL for this request */
    UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).build().encode();

    ObjectMapper mapper = new ObjectMapper();
    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);

    if (request instanceof MobiCashRequest) {
      httpHeaders.set("Authorization", "Bearer " + ((MobiCashRequest) request).getAuthorization());
    }

    if (request instanceof AuthenticationRequest) {
      httpHeaders.set(
          "Authorization", "Bearer " + ((AuthenticationRequest) request).getAuthorization());
    }

    HttpEntity<?> httpEntity =
        new HttpEntity<>(
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request), httpHeaders);

    System.err.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request));
    logger.info(
        "SERVICE REQUEST : {} {} {}",
        uriComponents.toString(),
        httpEntity.getHeaders(),
        httpEntity.getBody());
    ResponseEntity<String> responseEntity =
        restTemplate.exchange(uriComponents.toString(), HttpMethod.POST, httpEntity, String.class);
    logger.info("SERVICE RESPONSE : {} {}", responseEntity.getHeaders(), responseEntity.getBody());
    return responseEntity;
  }

  public String sendGetOKHTTPRequest(String url) throws Exception {
    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder()
            .url("https://dev.api.wasac.rw/customer/202213074/profile")
            .get()
            .addHeader("cache-control", "no-cache")
            .build();

    Response response = client.newCall(request).execute();
    return response.body().string();
  }
  public String sendGetRequest(String url) throws Exception {

    HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();

    // optional default is GET
    httpClient.setRequestMethod("GET");

    // add request header
    httpClient.setRequestProperty("User-Agent", USER_AGENT);

    logger.info("SERVICE REQUEST : {} ", url);
    StringBuilder response = new StringBuilder();
    try (BufferedReader in =
        new BufferedReader(new InputStreamReader(httpClient.getInputStream()))) {

      String line;

      while ((line = in.readLine()) != null) {
        response.append(line);
      }

      // print result
      System.out.println(response.toString());
    }
    return response.toString();
  }
}
