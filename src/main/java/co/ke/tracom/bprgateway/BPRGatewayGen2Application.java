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
@EnableScheduling
@EnableAsync
>>>>>>> f1e5e363255f8fa74569c927bdee807353ef8b0c
public class BPRGatewayGen2Application {
    public static void main(String[] args) {
        SpringApplication.run(BPRGatewayGen2Application.class, args);
    }
}
