package co.ke.tracom.bprgateway.web.rwandarevenue.controller;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.requests.RRAPaymentRequest;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.requests.RRATINValidationRequest;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.responses.RRAData;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.responses.RRAPaymentResponse;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.responses.RRAPaymentResponseData;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.responses.RRATINValidationResponse;
import co.ke.tracom.bprgateway.web.rwandarevenue.services.RRAService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/rwanda-revenue-authority")
@RestController
public class RwandaRevenueAuthorityController {

    private final RRAService rraService;

    @ApiOperation(
            value = "Return validation details for customer RRA TIN Number",
            response = RRATINValidationResponse.class)
    @PostMapping(value = "/validate-tin")
    public ResponseEntity<?> validateRRATIN(@RequestBody RRATINValidationRequest request) {
        String transactionRRN =  RRNGenerator.getInstance("RV").getRRN();
        RRATINValidationResponse response = rraService.validateCustomerTIN(request, transactionRRN);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Make payment against tax declaration amount",
            response = RRAPaymentResponse.class)
    @PostMapping(value = "/rra-payment")
    public ResponseEntity<?> RRAPaymentRequest(@RequestBody RRAPaymentRequest request) {

        RRAPaymentResponseData data = RRAPaymentResponseData.builder()
                .T24Reference(RRNGenerator.getInstance("BP").getRRN())
                .transactionCharges(34.55)
                .build();
        RRAPaymentResponse response = RRAPaymentResponse.builder()
                .status("00")
                .message("RRA Validation processed successfully")
                .data(data)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
