package co.ke.tracom.bprgatewaygen2.web.academicbridge.controller;

import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.AcademicBridgeResponse;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.paymentstatus.AcademicBridgePaymentStatusResponse;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.paymentstatus.PaymentStatusRequest;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.savepayment.SavePaymentRequest;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.studentdetails.GetStudentDetailsRequest;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.studentdetails.GetStudentDetailsResponse;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.services.AcademicBridgeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/academic-bridge/payments")
@RequiredArgsConstructor
public class AcademicBridgeController {

    private final AcademicBridgeService academicBridgeService;

    @ApiOperation(
            value="Get student bill details given the bill number",
            response = GetStudentDetailsResponse.class)
    @GetMapping(value="/students/info/{billNumber}")
    public ResponseEntity<?> getStudentDetails(@ApiParam(value = "Student bill number", required = true)
                                                   @PathVariable String billNumber) {
        GetStudentDetailsRequest request = new GetStudentDetailsRequest();
        request.setBillNumber(billNumber);
        GetStudentDetailsResponse responseEntity = academicBridgeService.fetchStudentDetailsByBillNumber(request);
        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }

    @ApiOperation(
            value="Save payment on academic bridge school's database for a given bill number",
            response = AcademicBridgeResponse.class)
    @PostMapping("/students/{billNumber}")
    public ResponseEntity<?> savePayment(@ApiParam(value = "Student bill number", required = true)
                                             @PathVariable String billNumber,
                                         @ApiParam(value = "Payment details: reference_number, paid_amount, sender_name, sender_phone_number, reason", required = true)
                                         @RequestBody SavePaymentRequest request) {
        request.setBillNumber(billNumber);
        AcademicBridgeResponse responseEntity = academicBridgeService.sendPaymentDetailsToAcademicBridge(request);
        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }

    @ApiOperation(
            value="Check if a payment was successfully saved on academic bridge given bank’s reference number.",
            response = AcademicBridgePaymentStatusResponse.class)
    @GetMapping("students/status/{referenceNumber}")
    public ResponseEntity<?> AcademicBridgePaymentStatusResponse(@ApiParam(value = "Student bill number", required = true)
                                                                     @PathVariable String referenceNumber) {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setReferenceNo(referenceNumber);
        AcademicBridgePaymentStatusResponse responseEntity = academicBridgeService.checkPaymentStatus(request);
        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }
}
