package co.ke.tracom.bprgateway.web.agaciro.data.paymentNotification;

import co.ke.tracom.bprgateway.web.agaciro.data.AgaciroRequest;
import lombok.Data;

import java.util.Date;

@Data
public class PaymentNotificationRequest extends AgaciroRequest {

  private String contributorType;
  private float amount;
  private String creditedAccountNumber;
  private String operationCode;
  private Date operationNature;
  private String transactionDate;
  private String designation;
  private String reason;
  private String movementNumber;
  private String institutionCode;
  private boolean employee;
  private String nid;
  private String phoneNumber;
  private String passportNumber;
}
