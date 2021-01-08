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

/**
 * This class provides convenient methods for making HTTP requests to a remote API that handles XML
 * formatted requests and responses.
 */
@Component
@RequiredArgsConstructor
public class XMLHttpService {
  private final Logger logger = LoggerFactory.getLogger(XMLHttpService.class);
  private final RestTemplate restTemplate;

  /**
   * @param request an object that implements XMLRequestI interface
   * @param apiUrl The URL you are sending the request to
   * @param responseClass The class that parses the response into an appropriate format
   * @param <T>
   * @return a response object
   */
  public <T extends Object> ResponseEntity<T> post(
      XMLRequestI request, String apiUrl, Class<T> responseClass) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_XML);
    logger.info("XML REQUEST STRING: {}", request.getRequestXML());
    HttpEntity<String> entity = new HttpEntity<>(request.getRequestXML(), httpHeaders);
    ResponseEntity<T> responseEntity = restTemplate.postForEntity(apiUrl, entity, responseClass);
    logger.info(
        "XML SERVICE RESPONSE : {} {}", responseEntity.getHeaders(), responseEntity.getBody());
    return responseEntity;
  }
}
