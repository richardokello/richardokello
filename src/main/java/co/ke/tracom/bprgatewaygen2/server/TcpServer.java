package co.ke.tracom.bprgatewaygen2.server;

import co.ke.tracom.bprgatewaygen2.server.data.Request;
import co.ke.tracom.bprgatewaygen2.web.billMenus.data.BillMenuResponse;
import co.ke.tracom.bprgatewaygen2.web.billMenus.service.BillMenusService;
import co.ke.tracom.bprgatewaygen2.web.config.SpringContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.InjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
public class TcpServer {

    private final NetServer server;

    public TcpServer(int port) {
        Vertx vertx = Vertx.vertx();
        NetServerOptions options = new NetServerOptions().setPort(port).setHost("localhost");
        server = vertx.createNetServer(options);
    }

    private BillMenusService getBillmenuService () {
        return SpringContext.getBean(BillMenusService.class);
    }


    public void addRequestHandler () {
        server.connectHandler(socket -> {
            socket.handler(buffer -> {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Request req = mapper.readValue(buffer.toString(), Request.class);
                    log.info("GENERIC REQUEST OBJECT: {}", req);

                    String transactionType = req.getTxnType();
                    switch (transactionType) {
                        case "water":
                            // fetch water
                            break;
                        case "fetch-menu":
                            BillMenuResponse billMenuResponse = getBillmenuService().getAllMenus();
                            Buffer outBuffer = Buffer.buffer();
                            outBuffer.appendString(mapper.writeValueAsString(billMenuResponse));
                            socket.write(outBuffer);
                            break;
                        default:
                            // default response - invalid input/undefined response -  error?
                            break;
                    }
                } catch (JsonProcessingException e) {
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
