package co.ke.tracom.bprgatewaygen2.web.academicbridge.data.savepayment;

import lombok.Data;

@Data
public class SavePaymentRequest {

  private String billNumber;
  private String referenceNo;
  private double paidAmount;
  private String senderName;
  private String senderPhoneNo;
  private String reason;

}
