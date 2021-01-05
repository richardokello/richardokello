package co.ke.tracom.bprgatewaygen2.server;

import co.ke.tracom.bprgatewaygen2.server.data.TcpRequest;
import co.ke.tracom.bprgatewaygen2.server.data.TcpResponse;
import co.ke.tracom.bprgatewaygen2.web.billMenus.data.BillMenuResponse;
import co.ke.tracom.bprgatewaygen2.web.billMenus.service.BillMenusService;
import co.ke.tracom.bprgatewaygen2.web.config.SpringContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TcpServer {

  private final NetServer server;

  public TcpServer(int port) {
    Vertx vertx = Vertx.vertx();
    NetServerOptions options = new NetServerOptions().setPort(port).setHost("localhost");
    server = vertx.createNetServer(options);
  }

  private BillMenusService getBillmenuService() {
    return SpringContext.getBean(BillMenusService.class);
  }

  public void addRequestHandler() {
    server.connectHandler(
        socket -> {
          socket.handler(
              buffer -> {
                  ObjectMapper mapper = new ObjectMapper();
                try {
                  TcpRequest req = mapper.readValue(buffer.toString(), TcpRequest.class);
                  log.info("GENERIC REQUEST OBJECT: {}", req);

                  String transactionType = req.getTxnType();
                  switch (transactionType) {
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
                    TcpResponse response = new TcpResponse();
                    response.setMessage("Bad request format. Should be json formatted");
                    response.setStatus(400);
                    Buffer outBuffer = Buffer.buffer();
                    try {
                        outBuffer.appendString(mapper.writeValueAsString(response));
                    } catch (JsonProcessingException ex) {
                        ex.printStackTrace();
                        log.error("TCP SERVER ERROR: {}", ex.getMessage());
                    }
                    socket.write(outBuffer);
                  log.error("TCP SERVER ERROR: {}", e.getMessage());
                }
              });
        });
  }

  public void start() {
    addRequestHandler();
    server.listen(
        res -> {
          if (res.succeeded()) {
            log.info("TCP server started successfully!");
          } else {
            log.error("TCP server failed to start!");
          }
        });
  }
}
