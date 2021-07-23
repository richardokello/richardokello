package co.ke.tracom.bprgateway.sms;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.sms.dto.SMSResponse;
import co.ke.tracom.bprgateway.web.sms.dto.fbismsapi.AuthResponse;
import co.ke.tracom.bprgateway.web.sms.dto.fbismsapi.FDISMSResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SMSTest {

    @Test
    public void test_authorization() throws JsonProcessingException {
        String fdi_sms_api_username = "510D89CC-9C80-4405-870C-6CBCA7EED0A8";
        String fdi_sms_api_password = "635EA5FF-2EF9-46DC-BF3D-DC679B00F6AB";
        String fdi_sms_api_authentication_url = "https://messaging.fdibiz.com/api/v1/auth";
        Map<String, Object> request = new HashMap<>();
        request.put("api_username", fdi_sms_api_username);
        request.put("api_password", fdi_sms_api_password);

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(fdi_sms_api_authentication_url).build().encode();

        ObjectMapper mapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> httpEntity =
                new HttpEntity<>(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request), httpHeaders);

        System.err.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request));

        log.info(
                "SERVICE REQUEST : {} {} {}",
                uriComponents.toString(),
                httpEntity.getHeaders(),
                httpEntity.getBody());
        ResponseEntity<AuthResponse> responseEntity =
                restTemplate.exchange(uriComponents.toString(), HttpMethod.POST, httpEntity, AuthResponse.class);
        AuthResponse authResponse = responseEntity.getBody();
        log.info("SERVICE RESPONSE : {} {}", responseEntity.getHeaders(), authResponse);

        String expiresAt = authResponse.getExpiresAt();
        System.out.println("expiresAt = " + expiresAt);
        String accessToken = authResponse.getAccessToken();
        System.out.println("accessToken = " + accessToken);

    }


    @Test
    public void test_sending_fdi_sms() throws JsonProcessingException {
        String fdi_sms_send_single_mt_url = "https://messaging.fdibiz.com/api/v1/mt/single";
        String fdi_sms_send_id = "BPR DG";
        String fdismsapiAuthToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjcwMDU3MDYsIm5iZiI6MTYyNzAwNTcwNiwianRpIjoiMDgxMDQ4OTktYjJmMi00ZTgzLTg1ZTQtYTIzNzgxMjQ4MDBjIiwiZXhwIjoxNjI3MDI3MzA2LCJpZGVudGl0eSI6NzUsImZyZXNoIjpmYWxzZSwidHlwZSI6ImFjY2VzcyIsInVzZXJfY2xhaW1zIjp7InF1ZXVlIjoicHJpb3JpdHkiLCJiaWxsaW5nX2lkIjoxLCJhY2NvdW50X2lkIjo5MSwiYXBpX25hbWUiOiJCUFIgREcifX0.5eK2ystvvGZlYdA8GYTdvNYj1AcOaQjKkYE32IBgtJ0";

        Map<String, Object> request = new HashMap<>();
        request.put("msisdn", "0788636380");
        request.put("message", "This is a test with new BPR FDI SMS API");
        request.put("dlr", "");
        request.put("msgRef", RRNGenerator.getInstance("SM").getRRN());
        request.put("sender_id", fdi_sms_send_id); //Actual value is BPR DG

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(fdi_sms_send_single_mt_url).build().encode();

        ObjectMapper mapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Bearer " + fdismsapiAuthToken);
        HttpEntity<?> httpEntity =
                new HttpEntity<>(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request), httpHeaders);

        System.err.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request));

        log.info(
                "SERVICE REQUEST : {} {} {}",
                uriComponents.toString(),
                httpEntity.getHeaders(),
                httpEntity.getBody());
        ResponseEntity<FDISMSResponse> responseEntity =
                restTemplate.exchange(uriComponents.toString(), HttpMethod.POST, httpEntity, FDISMSResponse.class);
        FDISMSResponse responseEntityBody = responseEntity.getBody();
        log.info("SERVICE RESPONSE : {} {}", responseEntity.getHeaders(), responseEntityBody);



    }
}
