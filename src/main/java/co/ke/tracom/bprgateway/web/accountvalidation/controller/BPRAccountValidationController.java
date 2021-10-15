



package co.ke.tracom.bprgateway.web.accountvalidation.controller;

        import co.ke.tracom.bprgateway.core.util.RRNGenerator;
        import co.ke.tracom.bprgateway.web.accountvalidation.data.BPRAccountValidationRequest;
        import co.ke.tracom.bprgateway.web.accountvalidation.data.BPRAccountValidationResponse;
        import co.ke.tracom.bprgateway.web.accountvalidation.service.AccountValidationService;
        import io.swagger.annotations.ApiOperation;
        import lombok.RequiredArgsConstructor;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.PostMapping;
        import org.springframework.web.bind.annotation.RequestBody;
        import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BPRAccountValidationController {
    private final AccountValidationService accountValidationService;

    @ApiOperation(value = "Validate any BPR Accounts i.e. agent account no or customer account no", response = BPRAccountValidationResponse.class)
    @PostMapping(value = "/bank-account-validation")
    public ResponseEntity<?> accountValidation(@RequestBody BPRAccountValidationRequest request) {
        String rrn = RRNGenerator.getInstance("AV").getRRN();
        BPRAccountValidationResponse response = accountValidationService.processBankAccountValidation(request, rrn);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
