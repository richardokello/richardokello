package co.ke.tracom.bprgateway.web.billMenus.controller;

import co.ke.tracom.bprgateway.web.billMenus.data.BillMenuResponse;
import co.ke.tracom.bprgateway.web.billMenus.service.BillMenusService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * Returns all bill menu items as a json string
 */
@RestController
@AllArgsConstructor
@Slf4j
public class BillMenuController {

    private final BillMenusService billMenusService;

    @ApiOperation(
            value = "Return all currently available menu items ",
            response = BillMenuResponse.class)
    @GetMapping({"api/menus", "api/menus/{language}"})
    public ResponseEntity<BillMenuResponse> getBillMenus(@PathVariable(required = false) String language) {

        if (language != null && !language.isEmpty() && language.equalsIgnoreCase("RW")) {
            BillMenuResponse billMenuResponse = billMenusService.fetchKinyarwandaMenus();
            log.error("BILL MENU SERVICE Request: {}", billMenuResponse);

            return new ResponseEntity<>(billMenuResponse, HttpStatus.OK);
        }

        BillMenuResponse billMenuResponse = billMenusService.fetchEnglishMenus();
        log.error("BILL MENU SERVICE Request: {}", billMenuResponse);
        return new ResponseEntity<>(billMenuResponse, HttpStatus.OK);

    }
}
