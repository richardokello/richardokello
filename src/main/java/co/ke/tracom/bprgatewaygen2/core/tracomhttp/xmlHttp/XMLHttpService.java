package co.ke.tracom.bprgatewaygen2.core.tracomhttp.xmlHttp;


import co.ke.tracom.bprgatewaygen2.web.tigopesa.data.XMLRequestI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class XMLHttpService {

    private final RestTemplate restTemplate;

    public <T extends Object> ResponseEntity<T> post(XMLRequestI request, String apiUrl, Class<T> responseClass)  {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<String> entity = new HttpEntity<>(request.getRequestXML(), httpHeaders);
        ResponseEntity<T> responseEntity = restTemplate.postForEntity(apiUrl, entity, responseClass);
        return responseEntity;
    }
}
