package co.ke.tracom.bprgateway.web.eucl.controller;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.eucl.dto.request.EUCLPaymentRequest;
import co.ke.tracom.bprgateway.web.eucl.dto.request.MeterNoValidation;
import co.ke.tracom.bprgateway.web.eucl.dto.response.EUCLPaymentResponse;
import co.ke.tracom.bprgateway.web.eucl.dto.response.MeterNoData;
import co.ke.tracom.bprgateway.web.eucl.dto.response.MeterNoValidationResponse;
import co.ke.tracom.bprgateway.web.eucl.service.EUCLService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/pc/api/eucl")
@RestController
public class EUCLController {
    private final EUCLService euclService;

    @ApiOperation(
            value = "Return validation details for meter number",
            response = MeterNoValidationResponse.class)
    @PostMapping(value = "/meter-validation")
    public ResponseEntity<?> MeterNoValidation(@Valid @RequestBody MeterNoValidation request) {
        String requestRef = RRNGenerator.getInstance("EV").getRRN();
        MeterNoValidationResponse response = euclService.validateEUCLMeterNo(request, requestRef);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Purchase electricity ",
            response = EUCLPaymentResponse.class)
    @PostMapping(value = "/payment")
    public ResponseEntity<?> purchaseElectricity(@Valid @RequestBody EUCLPaymentRequest request) {
        String requestRef = RRNGenerator.getInstance("EV").getRRN();
        EUCLPaymentResponse response = euclService.purchaseEUCLTokens(request, requestRef);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
