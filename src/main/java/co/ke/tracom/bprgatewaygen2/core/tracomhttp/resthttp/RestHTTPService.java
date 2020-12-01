package co.ke.tracom.bprgatewaygen2.core.tracomhttp.resthttp;

import co.ke.tracom.bprgatewaygen2.core.tracomhttp.CustomHTTPCommunicationInterface;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.MobiCashRequest;
import co.ke.tracom.bprgatewaygen2.web.mobicash.data.authentication.AuthenticationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.internal.HEMLogging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

@Service
public class RestHTTPService implements CustomHTTPCommunicationInterface {
    final private Logger logger = LoggerFactory.getLogger(RestHTTPService.class);
    private static final String USER_AGENT = "Mozilla/5.0";

    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<String> postRequest(Object request, String url) throws Exception {
        //Fetch Host
        /*Pack the URL for this request*/
        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl(url)
                .build()
                .encode();

        ObjectMapper mapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        if (request instanceof MobiCashRequest) {
            httpHeaders.set("Authorization", "Bearer " + ((MobiCashRequest) request).getAuthorization());
        }

        if (request instanceof AuthenticationRequest) {
            httpHeaders.set("Authorization", "Bearer " + ((AuthenticationRequest) request).getAuthorization());
        }

        HttpEntity<?> httpEntity = new HttpEntity<>(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request), httpHeaders);

        System.err.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request));
        logger.info("SERVICE REQUEST : {} {} {}", uriComponents.toString(), httpEntity.getHeaders(), httpEntity.getBody());
        ResponseEntity<String> responseEntity = restTemplate.exchange(uriComponents.toString(), HttpMethod.POST, httpEntity, String.class);
        logger.info("SERVICE RESPONSE : {} {}", responseEntity.getHeaders(), responseEntity.getBody());
        return responseEntity;
    }

    public String sendGetRequest(String url) throws Exception {

        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();

        // optional default is GET
        httpClient.setRequestMethod("GET");

        //add request header
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = httpClient.getResponseCode();
        logger.info("SERVICE REQUEST : {} ", url);
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {


            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            //print result
            System.out.println(response.toString());

        }
        return response.toString();


    }

    public <T extends Object> ResponseEntity<T> get(String apiUrl, Object request, Class<T> responseClazz) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<T> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, responseClazz);
        return responseEntity;
    }

}
