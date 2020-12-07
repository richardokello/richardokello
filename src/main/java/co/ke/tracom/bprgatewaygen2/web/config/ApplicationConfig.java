package co.ke.tracom.bprgatewaygen2.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {
    /**
     * Create rest template bean to be used application wide
     * @return rest template instance
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
