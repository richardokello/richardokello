package co.ke.tracom.bprgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
public class BPRGatewayGen2Application {
    public static void main(String[] args) {
        SpringApplication.run(BPRGatewayGen2Application.class, args);
    }
}
