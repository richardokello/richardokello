package co.ke.tracom.bprgatewaygen2.core.tracomhttp.xmlHttp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class XMLHttpService {

    @Autowired
    RestTemplate restTemplate;

    public <T extends Object> ResponseEntity<T> post(Object request, String apiUrl)  {

        return null;
    }
}
