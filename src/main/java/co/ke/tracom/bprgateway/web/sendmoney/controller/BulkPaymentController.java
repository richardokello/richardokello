package co.ke.tracom.bprgateway.web.sendmoney.controller;

import co.ke.tracom.bprgateway.web.sendmoney.data.requests.SendMoneyRequest;
import co.ke.tracom.bprgateway.web.sendmoney.data.response.BulkPaymentResponse;
import co.ke.tracom.bprgateway.web.sendmoney.services.BulkSendMoneyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequestMapping("/pc/api/bulk")
@RestController
public class BulkPaymentController {

    private BulkSendMoneyService sendMoneyService;

    public BulkPaymentController(BulkSendMoneyService sendMoneyService) {
        this.sendMoneyService = sendMoneyService;
    }

    @ApiOperation(
            value = "Upload batch file with send money request",
            response = BulkPaymentResponse.class)
    @PostMapping(value = "/bulkPayment", consumes ={MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SendMoneyRequest>sendMoney(@RequestPart("sendMoneyRequest")String sendMoneyRequest,
                                                        @RequestPart("file") List<MultipartFile> file) throws JsonProcessingException {
        SendMoneyRequest request=sendMoneyService.getSendMoneyJson(sendMoneyRequest,file);

      return new ResponseEntity<>(request,HttpStatus.OK);
    }
//    public ResponseEntity<BulkPaymentResponse> purchaseElectricity(
//            @RequestParam String merchantId,
//            @RequestParam String password,
//            @RequestParam MultipartFile file
//    ) {
//        log.info(file.getOriginalFilename());
//        log.info(password + " " + merchantId);
//        BulkPaymentResponse response = BulkPaymentResponse.builder()
//                .status("00")
//                .message("Bulk payment file uploaded successfully").build();
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
}
