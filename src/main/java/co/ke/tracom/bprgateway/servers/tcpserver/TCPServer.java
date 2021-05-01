package co.ke.tracom.bprgateway.servers.tcpserver;

import co.ke.tracom.bprgateway.servers.tcpserver.dto.GenericRequest;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.TcpResponse;
import co.ke.tracom.bprgateway.web.billMenus.service.BillMenusService;
import co.ke.tracom.bprgateway.core.config.CustomObjectMapper;
import co.ke.tracom.bprgateway.web.exceptions.custom.UnprocessableEntityException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * This server handles TCP requests for any given bill and dispatches them to an appropriate handler
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TCPServer {
  private NetServer server;

  private final BillMenusService billMenuService;
  private final BillRequestHandler billRequestHandler;

  @Value("${tcp.server.port}")
  private String port;

  @PostConstruct
  public void initializeTCPServer() {

    Vertx vertx = Vertx.vertx();
    NetServerOptions options = new NetServerOptions().setPort(Integer.parseInt(port));
    server = vertx.createNetServer(options);
    start();
  }

  public void addRequestHandlers() {
    System.err.println("TCP Server adding connection handler...");
    server.connectHandler(
        socket -> {
          socket.handler(
              buffer -> {
                CustomObjectMapper mapper = new CustomObjectMapper();
                try {
                  String requestPayload = buffer.toString().trim();
                  GenericRequest genericRequest =
                      mapper.readValue(requestPayload, GenericRequest.class);
                  log.info("GENERIC REQUEST OBJECT: {}", genericRequest);
                  String transactionType = genericRequest.getTnxType();
                  log.info("Transaction Type: {}", transactionType);
                  switch (transactionType) {
                    case "fetch-menu":
                      billRequestHandler.menu(requestPayload, billMenuService, socket);
                      break;
                    case "validation":
                      billRequestHandler.validation(requestPayload, socket);
                      break;
                    case "bill-payment":
                      billRequestHandler.billPayment(requestPayload, socket);
                      break;
                    case "bill-status":
                      billRequestHandler.billStatus(requestPayload, socket);
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

    System.err.println("TCP Server connection handler added successfully...");
  }

  /** Sets up request handlers and starts the TCP server */
  private void start() {
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
