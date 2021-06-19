package co.ke.tracom.bprgateway.web.bulkpayment.controller;

import co.ke.tracom.bprgateway.web.bulkpayment.dto.BulkPaymentResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequestMapping("/pc/api/bulk")
@RestController
public class BulkPaymentController {

    @ApiOperation(
            value = "Upload batch file with send money request",
            response = BulkPaymentResponse.class)
    @PostMapping(value = "/payment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> purchaseElectricity(
            @RequestParam String merchantId,
            @RequestParam String password,
            @RequestParam MultipartFile file
    ) {
        log.info(file.getOriginalFilename());
        log.info(password + " " + merchantId);
        BulkPaymentResponse response = BulkPaymentResponse.builder()
                .status("00")
                .message("Bulk payment file uploaded successfully").build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
