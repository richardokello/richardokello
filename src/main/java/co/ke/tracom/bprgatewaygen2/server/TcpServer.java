package co.ke.tracom.bprgatewaygen2.server;

import co.ke.tracom.bprgatewaygen2.server.data.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TcpServer {

    private final NetServer server;

    public TcpServer (int port) {
        Vertx vertx = Vertx.vertx();
        NetServerOptions options = new NetServerOptions().setPort(port).setHost("localhost");
        server = vertx.createNetServer(options);
    }

    public void addRequestHandler () {
        server.connectHandler(socket -> {
            socket.handler(buffer -> {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Request req = mapper.readValue(buffer.toString(), Request.class);
                    log.info("GENERIC REQUEST OBJECT: {}", req);

                    String transactionType = req.getTnxType();
                    switch (transactionType) {
                        case "water":
                            // fetch water
                            break;
                        default:
                            // default response - invalid input/undefined response -  error?
                            break;
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    log.error("TCP SERVER ERROR: {}", e.getMessage());
                }

            });
        });
    }

    public void start () {
        addRequestHandler();
        server.listen(res -> {
            if (res.succeeded()) {
                log.info("TCP server started successfully!");
            } else {
                log.error("TCP server failed to start!");
            }
        });

    }
}
