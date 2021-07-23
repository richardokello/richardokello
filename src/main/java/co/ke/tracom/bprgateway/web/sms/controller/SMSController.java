package co.ke.tracom.bprgateway.web.sms.controller;

import co.ke.tracom.bprgateway.web.sms.dto.SMSRequest;
import co.ke.tracom.bprgateway.web.sms.dto.SMSResponse;
import co.ke.tracom.bprgateway.web.sms.services.SMSService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SMSController {
    private final SMSService smsService;

    @ApiOperation(value = "Send SMS", response = SMSResponse.class)
    @PostMapping(value = "/util/send-sms")
    public ResponseEntity<?> sendSMSRequest(@Valid @RequestBody SMSRequest request) {
        return new ResponseEntity<>(smsService.processFDISMSAPI(request), HttpStatus.OK);
    }
}
