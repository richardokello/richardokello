package co.ke.tracom.bprgatewaygen2.web.mobicash.data.payment;

import co.ke.tracom.bprgatewaygen2.web.mobicash.data.MobiCashRequest;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

@Data
public class MobicashPaymentRequest extends MobiCashRequest {

  private Date transactionDateTime;
  @ApiModelProperty(
      name = "Bank Reference",
      value = "Bank slip identifier",
      required = true)
  private String bankReference;
  private long amount;
  private long actualBalance;
  private Date operationDateTime;
  @ApiModelProperty(
      name = "Transaction type",
      value = "This is the transaction type either deposit or withdraw Eg:1 or 2",
      required = true)
  private int transactionType;
  private int transactionTypeIdentifier;
  private String currency;
  private String branchCode;
  private String branchDesignation;
  @ApiModelProperty(
      name = "Operation Description",
      value = "The description of the transaction",
      required = true)
  private String operationDescription;
}
