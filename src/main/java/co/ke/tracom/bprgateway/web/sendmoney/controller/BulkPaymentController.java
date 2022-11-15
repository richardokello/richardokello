//package co.ke.tracom.bprgateway.web.sendmoney.controller;
//
//import co.ke.tracom.bprgateway.web.sendmoney.data.response.BulkPaymentResponse;
//import co.ke.tracom.bprgateway.web.sendmoneyBulkpayment.data.SendMoneydata;
//import co.ke.tracom.bprgateway.web.sendmoneyBulkpayment.service.BulkSendMoneyService;
//import co.ke.tracom.bprgateway.web.sendmoneyBulkpayment.service.CSVHelper;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestPart;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//
////@Slf4j
////@RequestMapping("/pc/api/bulk")
////@RestController
////public class BulkPaymentController {
////@Autowired
////    private BulkSendMoneyService sendMoneyService;
////@Autowired
////    private CSVHelper csvService;
////
////    public BulkPaymentController(BulkSendMoneyService sendMoneyService) {
////        this.sendMoneyService = sendMoneyService;
////    }
////
////    @ApiOperation(
////            value = "Upload batch file with send money request",
////            response = BulkPaymentResponse.class)
////    @PostMapping(value = "/bulkPayment", consumes ={ MediaType.ALL_VALUE})
////    public ResponseEntity <List<SendMoneydata>>sendMoney(@RequestPart("file") File file) throws IOException {
////       List<SendMoneydata> request=sendMoneyService.parseCSV(file);
////
////      return new ResponseEntity<>(request,HttpStatus.OK);
////    }
//
//
//
//
//
////    public ResponseEntity<BulkPaymentResponse> purchaseElectricity(
////            @RequestParam String merchantId,
////            @RequestParam String password,
////            @RequestParam MultipartFile file
////    ) {
////        log.info(file.getOriginalFilename());
////        log.info(password + " " + merchantId);
////        BulkPaymentResponse response = BulkPaymentResponse.builder()
////                .status("00")
////                .message("Bulk payment file uploaded successfully").build();
////        return new ResponseEntity<>(response, HttpStatus.OK);
////    }
//}
