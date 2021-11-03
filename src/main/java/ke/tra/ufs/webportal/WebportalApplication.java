package ke.tra.ufs.webportal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableEurekaClient
@EnableScheduling
public class WebportalApplication {

    @Value("${baseUrl}")
    private String baseUrl;

    public static void main(String[] args) {
        System.out.println("-----------------Starting UFS server ......");
        SpringApplication.run(WebportalApplication.class, args);
        System.out.println("**************** UFS server started ****************");
    }


    @Bean
    @Primary
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        MappingJackson2HttpMessageConverter converter
//                = new MappingJackson2HttpMessageConverter(mapper);
        return new MappingJackson2HttpMessageConverter(mapper);
    }

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("BSS-THREAD");
        executor.initialize();
        return executor;
    }

    @Primary
    @Bean
    public RemoteTokenServices tokenService() {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(baseUrl + "ufs-common-modules/api/v1/oauth/check_token");
        tokenService.setClientId("common_module_client");
        tokenService.setClientSecret("secret");
        return tokenService;
    }

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
