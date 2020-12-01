package co.ke.tracom.bprgatewaygen2.web.academicbridge.data.studentdetails;

import lombok.Data;

@Data
public class GetStudentDetailsResponse {
    private boolean success;
    private int error_code;
    private String error_msg;
    private int school_ide;
    private String school_name;
    private String school_account_number;
    private String school_account_name;
    private String student_name;
    private String student_reg_number;
    private String bill_number;
    private String type_of_payment;
}
