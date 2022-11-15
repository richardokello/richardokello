package co.ke.tracom.bprgateway.web.sendmoneyBulkpayment.contoller;

import co.ke.tracom.bprgateway.web.sendmoney.data.response.SendMoneyResponse;
import co.ke.tracom.bprgateway.web.sendmoneyBulkpayment.data.SendMoneydata;
import co.ke.tracom.bprgateway.web.sendmoneyBulkpayment.service.CSVHelper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@NoArgsConstructor
@RestController
@RequestMapping("/pc/api")
public class BulkSendMoneyContoller {
//    @Autowired
   // private  BulkSendMoneyService sendMoneyService;
    @Autowired
    private CSVHelper csvService;
    @PostMapping("/upload/{username}")
    public ResponseEntity<List<SendMoneydata>> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable(value = "username")String username) {
        String message = "";

        if (CSVHelper.hasCSVFormat(file)) {
            try {

                CompletableFuture<List<SendMoneyResponse>> response= csvService.saveBulkSendMoney(file,username);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return new ResponseEntity<List<SendMoneydata>>((List<SendMoneydata>) response, HttpStatus.OK);
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
             //   return (ResponseEntity<List<SendMoneydata>>) ResponseEntity.status(HttpStatus.EXPECTATION_FAILED);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        message = "Please upload a csv file!";
        return (ResponseEntity<List<SendMoneydata>>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
    }
}
