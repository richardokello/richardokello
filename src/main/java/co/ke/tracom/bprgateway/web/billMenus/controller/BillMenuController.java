package co.ke.tracom.bprgateway.web.billMenus.controller;

import co.ke.tracom.bprgateway.core.config.CustomObjectMapper;
import co.ke.tracom.bprgateway.servers.tcpserver.BillRequestHandler;
import co.ke.tracom.bprgateway.servers.tcpserver.data.academicBridge.AcademicBridgeValidation;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.BillPaymentRequest;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.BillPaymentResponse;
import co.ke.tracom.bprgateway.servers.tcpserver.dto.ValidationRequest;
import co.ke.tracom.bprgateway.web.billMenus.data.BillMenuResponse;
import co.ke.tracom.bprgateway.web.billMenus.service.BillMenusService;
import co.ke.tracom.bprgateway.web.exceptions.custom.InvalidAgentCredentialsException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Returns all bill menu items as a json string
 */
@RestController
@AllArgsConstructor
@Slf4j
public class BillMenuController {

    private final BillMenusService billMenusService;
    private final BillRequestHandler billRequestHandler;

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

    @ApiOperation(value = "biller validation", response = AcademicBridgeValidation.class)
    @PostMapping(value = "api/validations")
    public ResponseEntity<AcademicBridgeValidation>dynamicValidation(@RequestBody ValidationRequest validate) throws JsonProcessingException {
        //NetSocket socket=null;
//        CustomObjectMapper objectMapper = new CustomObjectMapper();
//        String str =  objectMapper.writeValueAsString(validate);

        AcademicBridgeValidation response=
                billRequestHandler.validation(validate, null);
        if(response.getResponseCode()==null)
        {response.setResponseMessage("No response from the server");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        if(!response.getResponseCode().equals("00")) {
            response.setResponseMessage("Validation failed");
            response.setResponseCode("401");

            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }// return new ResponseEntity<>(response , HttpStatus.OK);
        return ResponseEntity.ok().body(response);

    }

    @ApiOperation(value = "biller payment", response = BillPaymentRequest.class)
    @PostMapping(value = "api/billpayment")
    public ResponseEntity<?>dynamicPayment( @RequestBody Object paybills) throws JsonProcessingException, InvalidAgentCredentialsException {
        //NetSocket socket=null;
        CustomObjectMapper objectMapper = new CustomObjectMapper();
        String str=  objectMapper.writeValueAsString(paybills);
        BillPaymentResponse response;
        response = billRequestHandler.billPayment(str, null);

        if(response==null||!response.getResponseCode().equals("00"))
        {response.setResponseMessage("No response from the server");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        return ResponseEntity.ok().body(response);
    }
}
