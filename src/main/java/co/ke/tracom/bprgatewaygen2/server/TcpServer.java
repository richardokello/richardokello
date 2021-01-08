package co.ke.tracom.bprgatewaygen2.server;

import co.ke.tracom.bprgatewaygen2.server.data.TcpRequest;
import co.ke.tracom.bprgatewaygen2.server.data.TcpResponse;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.services.AcademicBridgeService;
import co.ke.tracom.bprgatewaygen2.web.billMenus.service.BillMenusService;
import co.ke.tracom.bprgatewaygen2.web.config.CustomObjectMapper;
import co.ke.tracom.bprgatewaygen2.web.config.SpringContext;
import co.ke.tracom.bprgatewaygen2.web.exceptions.custom.UnprocessableEntityException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import lombok.extern.slf4j.Slf4j;

/**
 * This server handles TCP requests for any given bill and dispatches them to an appropriate handler
 */
@Slf4j
public class TcpServer {

  private final NetServer server;
  private final int port;

  public TcpServer(int port) {
    this.port = port;
    Vertx vertx = Vertx.vertx();
    NetServerOptions options = new NetServerOptions().setPort(port).setHost("localhost");
    server = vertx.createNetServer(options);
  }

  /**
   * Retrieves BillMenuService bean from spring context
   *
   * @return BillMenuService bean
   */
  private BillMenusService getBillmenuService() {
    return SpringContext.getBean(BillMenusService.class);
  }

  /**
   * Retrieves AcademicBridgeService bean from spring context
   *
   * @return AcademicBridgeService bean
   */
  private AcademicBridgeService getAcademicBridgeService() {
    return SpringContext.getBean(AcademicBridgeService.class);
  }

  /** */
  public void addRequestHandlers() {
    server.connectHandler(
        socket -> {
          socket.handler(
              buffer -> {
                CustomObjectMapper mapper = new CustomObjectMapper();
                try {
                  String requestString = buffer.toString();
                  TcpRequest req = mapper.readValue(requestString, TcpRequest.class);
                  log.info("GENERIC REQUEST OBJECT: {}", req);
                  String billType = req.getBill();

                  switch (billType) {
                    case "menu":
                      BillRequestHandler.menu(requestString, getBillmenuService(), socket);
                      break;
                    case "academic-bridge":
                      BillRequestHandler.academicBridge(
                          requestString, getAcademicBridgeService(), socket);
                      break;
                    default:
                      throw new UnprocessableEntityException("Entity cannot be processed");
                  }
                } catch (JsonProcessingException
                    | UnprocessableEntityException
                    | NullPointerException e) {
                  e.printStackTrace();
                  TcpResponse response = new TcpResponse();
                  response.setMessage("Bad request: " + e.getMessage());
                  response.setStatus(400);
                  Buffer outBuffer = Buffer.buffer();
                  try {
                    outBuffer.appendString(mapper.writeValueAsString(response));
                    socket.write(outBuffer);
                  } catch (JsonProcessingException ex) {
                    ex.printStackTrace();
                    log.error("TCP SERVER ERROR: {}", ex.getMessage());
                    socket.write(ex.getMessage());
                  }
                } finally {
                  socket.close();
                }
              });
        });
  }

  /** Sets up request handlers and starts the TCP server */
  public void start() {
    addRequestHandlers();
    server.listen(
        res -> {
          if (res.succeeded()) {
            log.info("TCP SERVER STARTED SUCCESSFULLY ON PORT: {}!", port);
          } else {
            log.error("TCP server failed to start!");
          }
        });
  }
}
