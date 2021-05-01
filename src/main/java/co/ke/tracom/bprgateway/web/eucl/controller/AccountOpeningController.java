package co.ke.tracom.bprgateway.web.eucl.controller;

import co.ke.tracom.bprgateway.web.eucl.dto.request.AccountOpeningRequest;
import co.ke.tracom.bprgateway.web.eucl.dto.request.MeterNoValidation;
import co.ke.tracom.bprgateway.web.eucl.dto.response.AccountOpeningResponse;
import co.ke.tracom.bprgateway.web.eucl.dto.response.MeterNoData;
import co.ke.tracom.bprgateway.web.eucl.dto.response.NIDValidationResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/pc/api/eucl")
@RestController
public class AccountOpeningController {
    @ApiOperation(
            value = "Return validation details for meter number",
            response = NIDValidationResponse.class)
    @PostMapping(value = "/nid-validation")
    public ResponseEntity<?> NIDValidation(@RequestBody MeterNoValidation request) {


        MeterNoData data = MeterNoData.builder()
                .meterNo(request.getMeterNo())
                .accountName("MWISENEZA NDAYISHIMIYE ANDERSON")
                .meterLocation("KIGALI RWANDA")
                .build();
        NIDValidationResponse response = NIDValidationResponse
                .builder()
                .status("00")
                .message("Meter no validation successful")
                .data(data)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Create ",
            response = AccountOpeningResponse.class)
    @PostMapping(value = "/create-customer")
    public ResponseEntity<?> OpenCustomerAccount(@RequestBody AccountOpeningRequest request) {
        AccountOpeningResponse response = AccountOpeningResponse
                .builder()
                .status("00")
                .message("Account creation successful")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
