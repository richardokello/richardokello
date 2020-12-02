package co.ke.tracom.bprgatewaygen2.web.academicbridge.controller;

import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.AcademicBridgeResponse;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.paymentstatus.AcademicBridgePaymentStatusResponse;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.paymentstatus.PaymentStatusRequest;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.savepayment.SavePaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.studentdetails.GetStudentDetails;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.studentdetails.GetStudentDetailsResponse;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.services.AcademicBridgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/academic-bridge/students")
@RequiredArgsConstructor
public class AcademicBridgeController {

    private final AcademicBridgeService academicBridgeService;

    @GetMapping("/info")
    public ResponseEntity<?> getStudentDetails(@RequestBody GetStudentDetails request) {
        GetStudentDetailsResponse responseEntity = academicBridgeService.fetchStudentDetailsByBillNumber(request);
        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }

    @GetMapping("/save")
    public ResponseEntity<?> savePayment(@RequestBody SavePaymentRequest request) {
        AcademicBridgeResponse responseEntity = academicBridgeService.sendPaymentDetailsToAcademicBridge(request);
        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }

    @GetMapping("/check")
    public ResponseEntity<?> AcademicBridgePaymentStatusResponse(@RequestBody PaymentStatusRequest request) {
        AcademicBridgePaymentStatusResponse responseEntity = academicBridgeService.checkPaymentStatus(request);
        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }
}
