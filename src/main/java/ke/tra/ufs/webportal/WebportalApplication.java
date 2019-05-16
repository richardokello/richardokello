package ke.tra.ufs.webportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class WebportalApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebportalApplication.class, args);
    }

}
