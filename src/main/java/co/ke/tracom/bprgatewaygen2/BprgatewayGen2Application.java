package co.ke.tracom.bprgatewaygen2;

import co.ke.tracom.bprgatewaygen2.server.TcpServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BprgatewayGen2Application {

  public static void main(String[] args) {
    SpringApplication.run(BprgatewayGen2Application.class, args);
    new TcpServer(5050).start();
  }
}
