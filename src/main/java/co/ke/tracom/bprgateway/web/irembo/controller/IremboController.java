package co.ke.tracom.bprgateway.web.irembo.controller;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.irembo.dto.request.BillNumberValidationRequest;
import co.ke.tracom.bprgateway.web.irembo.dto.request.IremboBillPaymentRequest;
import co.ke.tracom.bprgateway.web.irembo.dto.response.IremboBillNoValidationResponse;
import co.ke.tracom.bprgateway.web.irembo.dto.response.IremboPaymentResponse;
import co.ke.tracom.bprgateway.web.irembo.dto.response.IremboPaymentResponseData;
import co.ke.tracom.bprgateway.web.irembo.dto.response.IremboValidationData;
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
    public ResponseEntity<?> MeterNoValidation(@RequestBody BillNumberValidationRequest request) {

            String transactionRefNo = RRNGenerator.getInstance("IV").getRRN();
//   IremboBillNoValidationResponse response =         iremboService.validateIremboBillNo(request, transactionRefNo);

        IremboValidationData data = IremboValidationData.builder()
                .billNo(request.getCustomerBillNo())
                .customerName("MWISENEZA NDAYISHIMIYE ANDERSON")
                .mobileNo("0453718285")
                .RRAAccountNo("100001766")
                .amount("3000000")
                .currencyCode("RWF")
                .createdAt("01/03/2021 03:03:26")
                .expiryDate("07/04/2021 09:31:07")
                .transactionType("RRA")
                .build();

        IremboBillNoValidationResponse response = IremboBillNoValidationResponse
                .builder()
                .status("00")
                .message("Meter no validation successful")
                .data(data)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Irembo bill payment",
            response = IremboPaymentResponse.class)
    @PostMapping(value = "/bill-payment")
    public ResponseEntity<?> iremboBillPayment(@RequestBody IremboBillPaymentRequest request) {

        IremboPaymentResponseData data = IremboPaymentResponseData.builder()
                .t24Reference(RRNGenerator.getInstance("BP").getRRN()).build();


        IremboPaymentResponse response = IremboPaymentResponse
                .builder()
                .status("00")
                .message("Account creation successful")
                .data(data)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
