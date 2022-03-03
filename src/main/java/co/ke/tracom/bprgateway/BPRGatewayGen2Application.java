package co.ke.tracom.bprgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
<<<<<<< HEAD
@EnableScheduling
@EnableAsync
=======
//@EnableScheduling
>>>>>>> cc6f593a6187c080848ce59ebd5d82253d3de5d1
public class BPRGatewayGen2Application {
    public static void main(String[] args) {
        SpringApplication.run(BPRGatewayGen2Application.class, args);
    }
}
