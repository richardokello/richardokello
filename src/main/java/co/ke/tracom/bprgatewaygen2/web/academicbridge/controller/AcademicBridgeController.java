package co.ke.tracom.bprgatewaygen2.web.academicbridge.controller;

import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.studentdetails.GetStudentDetails;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.data.studentdetails.GetStudentDetailsResponse;
import co.ke.tracom.bprgatewaygen2.web.academicbridge.services.AcademicBridgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AcademicBridgeController {

    @Autowired
    AcademicBridgeService academicBridgeService;

    @GetMapping("/academic-bridge")
    public ResponseEntity<GetStudentDetailsResponse> getStudentDetails(@RequestBody GetStudentDetails request) {
        ResponseEntity<GetStudentDetailsResponse> responseEntity = academicBridgeService.fetchStudentDetailsByBillNumber(request);
        return responseEntity;
    }
}
