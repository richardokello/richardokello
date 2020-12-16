package co.ke.tracom.bprgatewaygen2.web.billMenus.controller;

import co.ke.tracom.bprgatewaygen2.web.billMenus.data.BillMenuResponse;
import co.ke.tracom.bprgatewaygen2.web.billMenus.service.BillMenusService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/menus")
public class BillMenuController {

  private final BillMenusService billMenusService;

  /**
   * Return all menu items currently available.
   */
  @GetMapping
  public ResponseEntity<?> getBillMenus() {

    BillMenuResponse billMenuResponse = billMenusService.getAllMenus();

    return new ResponseEntity<>(billMenuResponse, HttpStatus.OK);
  }


}
