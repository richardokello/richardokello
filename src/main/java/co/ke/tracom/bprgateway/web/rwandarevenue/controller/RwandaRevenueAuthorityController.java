package co.ke.tracom.bprgateway.web.rwandarevenue.controller;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.requests.RRAPaymentRequest;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.requests.RRATINValidationRequest;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.responses.RRAData;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.responses.RRAPaymentResponse;
import co.ke.tracom.bprgateway.web.rwandarevenue.dto.responses.RRATINValidationResponse;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/api/rwanda-revenue-authority")
@RestController
public class RwandaRevenueAuthorityController {

    @ApiOperation(
            value = "Return validation details for customer RRA TIN Number",
            response = RRATINValidationResponse.class)
    @PostMapping(value = "/validate-tin")
    public ResponseEntity<?> validateRRATIN(@RequestBody RRATINValidationRequest request) {


        RRAData rraData = RRAData.builder().taxTypeDescription("OUT SOURCING FEE")
                .declarationDate("08/01/2021 08:36:58")
                .taxCentreNo(22)
                .TIN("102092176")
                .RRAOriginNo(6)
                .declarationID(152617674)
                .taxPayerName("NGALI HOLDINGS LTD")
                .requestDate("09/01/2021 03:31:25")
                .taxCentreDescription("KICUKIRO TC")
                .taxTypeNo(191)
                .assessNo(38322021)
                .amountToPay(6000)
                .RRAReferenceNo("1032368191")
                .build();

        RRATINValidationResponse response =
                RRATINValidationResponse.builder()
                        .status("00")
                        .message("RRA Validation processed successfully")
                        .data(rraData).build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Make payment against tax declaration amount",
            response = RRAPaymentResponse.class)
    @PostMapping(value = "/rra-payment")
    public ResponseEntity<?> RRAPaymentRequest(@RequestBody RRAPaymentRequest request) {

        RRAPaymentResponse response =
                RRAPaymentResponse.builder()
                        .status("00")
                        .message("RRA Validation processed successfully")
                        .T24Reference(RRNGenerator.getInstance("BP").getRRN())
                        .transactionCharges(34.55)
                        .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
