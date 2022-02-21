package co.ke.tracom.bprgateway.servers.tcpserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicTransactionData {

    private String debitCustomer;
    private String orderingBank;
    private String abStudentName;
    private String bprSenderName;
    private String localCahrgeAmount;
    private String abSchoolId;
    private String creditValueDate;
    private String processingDate;
    private String debitAcctNo;
    private String abBillNo;
    private String creditTheirRef;
    private String dateTime;
    private String mobileNo;
    private String deliveryOutRef;
    private String abSchoolName;
    private String creditAmount;
    private String billerId;
    private String T24reference;
    private String T24responsecode;
    private String Error;

}
