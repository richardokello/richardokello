package co.ke.tracom.bprgatewaygen2.server;

import co.ke.tracom.bprgatewaygen2.server.data.billMenu.BillMenuRequest;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.services.AcademicBridgeService;
import co.ke.tracom.bprgatewaygen2.web.billMenus.data.BillMenuResponse;
import co.ke.tracom.bprgatewaygen2.web.billMenus.service.BillMenusService;
import co.ke.tracom.bprgatewaygen2.web.config.CustomObjectMapper;
import co.ke.tracom.bprgatewaygen2.web.exceptions.custom.UnprocessableEntityException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BillRequestHandler {

  public static void menu(String requestString, BillMenusService billMenusService, NetSocket socket)
      throws JsonProcessingException, UnprocessableEntityException {
    CustomObjectMapper mapper = new CustomObjectMapper();
    /* Parse request string into bill menu request object */
    BillMenuRequest billMenuRequest = mapper.readValue(requestString, BillMenuRequest.class);
    log.info("BILL MENU REQUEST DATA: {}", requestString);
    String tnxType = billMenuRequest.getTxnType();

    if (tnxType != null && tnxType.equals("fetch-menu")) {
      BillMenuResponse billMenuResponse = billMenusService.getAllMenus();
      Buffer outBuffer = Buffer.buffer();
      outBuffer.appendString(mapper.writeValueAsString(billMenuResponse));
      socket.write(outBuffer);
    } else {
      log.error("TCP SERVER - BAD REQUEST DATA FOR FETCHING MENU: {} ", requestString);
      throw new UnprocessableEntityException("Entity cannot be processed");
    }
  }

  public static void academicBridge(
      String requestString, AcademicBridgeService academicBridgeService, NetSocket socket)
      throws JsonProcessingException {}
}
