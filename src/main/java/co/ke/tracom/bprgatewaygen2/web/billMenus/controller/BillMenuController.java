package co.ke.tracom.bprgatewaygen2.web.billMenus.controller;

import co.ke.tracom.bprgatewaygen2.web.billMenus.data.BillMenuResponse;
import co.ke.tracom.bprgatewaygen2.web.billMenus.service.BillMenusService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Returns all bill menu items as a json string */
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(value = "/api/menus")
public class BillMenuController {

  private final BillMenusService billMenusService;

  @ApiOperation(
      value = "Return all currently available menu items ",
      response = BillMenuResponse.class)
  @GetMapping
  public ResponseEntity<?> getBillMenus() {

    BillMenuResponse billMenuResponse = billMenusService.getAllMenus();

    log.error("BILL MENU SERVICE Request: {}", billMenuResponse);

    return new ResponseEntity<>(billMenuResponse, HttpStatus.OK);
  }
}
