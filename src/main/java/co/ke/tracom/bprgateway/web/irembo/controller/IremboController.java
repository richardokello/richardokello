package co.ke.tracom.bprgateway.web.irembo.controller;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.irembo.dto.request.BillNumberValidationRequest;
import co.ke.tracom.bprgateway.web.irembo.dto.request.IremboBillPaymentRequest;
import co.ke.tracom.bprgateway.web.irembo.dto.response.IremboBillNoValidationResponse;
import co.ke.tracom.bprgateway.web.irembo.dto.response.IremboPaymentResponse;
import co.ke.tracom.bprgateway.web.irembo.service.IremboService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/pc/api/irembo")
@RestController
public class IremboController {

    private final IremboService iremboService;

    @ApiOperation(
            value = "Return validation details for bill number",
            response = IremboBillNoValidationResponse.class)
    @PostMapping(value = "/bill-validation")
    public ResponseEntity<IremboBillNoValidationResponse> MeterNoValidation(@RequestBody BillNumberValidationRequest request) {
        String transactionRefNo = RRNGenerator.getInstance("IV").getRRN();
        IremboBillNoValidationResponse response = iremboService.validateIremboBillNo(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Irembo bill payment",
            response = IremboPaymentResponse.class)
    @PostMapping(value = "/bill-payment")
    public ResponseEntity<IremboPaymentResponse> iremboBillPayment(@RequestBody IremboBillPaymentRequest request) {
        String transactionRefNo = RRNGenerator.getInstance("IV").getRRN();
        IremboPaymentResponse response = iremboService.processPayment(request, transactionRefNo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
