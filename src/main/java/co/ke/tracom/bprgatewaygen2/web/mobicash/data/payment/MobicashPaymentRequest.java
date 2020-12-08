package co.ke.tracom.bprgatewaygen2.web.mobicash.data.payment;

import co.ke.tracom.bprgatewaygen2.web.mobicash.data.MobiCashRequest;
import java.util.Date;
import lombok.Data;

@Data
public class MobicashPaymentRequest extends MobiCashRequest {

  private Date transactionDateTime;
  private String bankReference;
  private long amount;
  private long actualBalance;
  private Date operationDateTime;
  private int transactionType;
  private int transactionTypeIdentifier;
  private String currency;
  private String branchCode;
  private String branchDesignation;
  private String operationDescription;
}
