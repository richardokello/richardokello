package co.ke.tracom.bprgatewaygen2.core.tracomhttp.xmlHttp;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class XMLHttpService {
    final private Logger logger = LoggerFactory.getLogger(XMLHttpService.class);
    private final RestTemplate restTemplate;

    public <T extends Object> ResponseEntity<T> post(XMLRequestI request, String apiUrl, Class<T> responseClass)  {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_XML);
        logger.info("REQUEST XML: {}", request.getRequestXML());
        HttpEntity<String> entity = new HttpEntity<>(request.getRequestXML(), httpHeaders);
        ResponseEntity<T> responseEntity = restTemplate.postForEntity(apiUrl, entity, responseClass);
        logger.info("XML SERVICE RESPONSE : {} {}", responseEntity.getHeaders(), responseEntity.getBody());
        return responseEntity;
    }
}
