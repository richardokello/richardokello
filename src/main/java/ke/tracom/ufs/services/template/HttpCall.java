package ke.tracom.ufs.services.template;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class HttpCall<T> {

    public ResponseEntity<T> sendAPIGatewayPOSTRequest(String switchUrl, MultiValueMap<String, String> map, HttpHeaders httpHeaders, Class<T> clazz){
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, httpHeaders);


        return restTemplate.postForEntity(switchUrl, httpEntity, clazz);
    }
}
