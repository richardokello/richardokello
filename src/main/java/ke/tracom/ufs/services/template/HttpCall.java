package ke.tracom.ufs.services.template;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpCall<T> {

    private final RestTemplate restTemplate;

    public HttpCall(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<T> sendAPIGatewayPOSTRequest(String switchUrl, MultiValueMap<String, String> map, HttpHeaders httpHeaders, Class<T> clazz){

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, httpHeaders);


        return restTemplate.postForEntity(switchUrl, httpEntity, clazz);
    }
}
