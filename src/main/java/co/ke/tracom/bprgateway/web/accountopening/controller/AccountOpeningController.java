package co.ke.tracom.bprgateway.web.accountopening.controller;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.accountopening.dto.request.AccountOpeningRequest;
import co.ke.tracom.bprgateway.web.accountopening.dto.request.NIDValidationRequest;
import co.ke.tracom.bprgateway.web.accountopening.dto.response.AccountOpeningResponse;
import co.ke.tracom.bprgateway.web.accountopening.dto.response.NIDValidationResponse;
import co.ke.tracom.bprgateway.web.accountopening.service.NIDValidationService;
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
@RequestMapping("/pc/api/account-opening")
@RestController
public class AccountOpeningController {
    private final NIDValidationService nidValidationService;

    @ApiOperation(
            value = "Return validation details for national ID",
            response = NIDValidationResponse.class)
    @PostMapping(value = "/nid-validation")
    public ResponseEntity<NIDValidationResponse> NIDValidation(@RequestBody NIDValidationRequest request) {

        String referenceNo = RRNGenerator.getInstance("NV").getRRN();
        NIDValidationResponse response = nidValidationService.validateNationalID(request, referenceNo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Create ",
            response = AccountOpeningResponse.class)
    @PostMapping(value = "/create-customer")
    public ResponseEntity<AccountOpeningResponse> OpenCustomerAccount(@RequestBody AccountOpeningRequest request) {
        AccountOpeningResponse response = AccountOpeningResponse
                .builder()
                .status("01")
                .message("Account opening functionality not complete")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
