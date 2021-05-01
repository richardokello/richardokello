package co.ke.tracom.bprgateway.web.accountopening.controller;

import co.ke.tracom.bprgateway.web.accountopening.dto.request.AccountOpeningRequest;
import co.ke.tracom.bprgateway.web.accountopening.dto.request.NIDValidationRequest;
import co.ke.tracom.bprgateway.web.accountopening.dto.response.AccountOpeningResponse;
import co.ke.tracom.bprgateway.web.accountopening.dto.response.NIDData;
import co.ke.tracom.bprgateway.web.accountopening.dto.response.NIDValidationResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/pc/api/account-opening")
@RestController
public class AccountOpeningController {
    @ApiOperation(
            value = "Return validation details for national ID",
            response = NIDValidationResponse.class)
    @PostMapping(value = "/nid-validation")
    public ResponseEntity<?> NIDValidation(@RequestBody NIDValidationRequest request) {


        NIDData data = NIDData.builder()
                .nationalId(request.getNationalID())
                .firstName("MWISENEZA")
                .surname("NDAYISHIMIYE ANDERSON")
                .build();
        NIDValidationResponse response = NIDValidationResponse
                .builder()
                .status("00")
                .message("Account validation successful")
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
