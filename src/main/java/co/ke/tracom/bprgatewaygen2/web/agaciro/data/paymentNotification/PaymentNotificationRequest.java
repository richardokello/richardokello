package co.ke.tracom.bprgatewaygen2.web.agaciro.data.paymentNotification;

import co.ke.tracom.bprgatewaygen2.web.agaciro.data.AgaciroRequest;
import lombok.Data;

import java.util.Date;

@Data
public class PaymentNotificationRequest extends AgaciroRequest {
    private String contributor_type;
    private float amount;
    private String credited_account_number;
    private String operation_code;
    private Date operation_nature;
    private String transaction_date;
    private String designation;
    private String reason;
    private String movement_number;
    private String institution_code;
    private boolean employee;
    private String nid;
    private String phone_number;
    private String passport_number;
}
