package co.ke.tracom.bprgateway.web.eucl.dto.request;

import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EUCLPaymentRequest {
    private MerchantAuthInfo credentials;
    private String amount;
    private String meterNo;
    private String phoneNo;
    private String meterLocation;
    private String source;

  public void validatedAmount()
  {
      Long amount=Long.parseLong(this.amount);
      if (this.amount.isEmpty()){
          throw new RuntimeException("amount cannot be null");
      }
      if (100>amount&& amount>20000000){
          throw new RuntimeException("amount out of limit");
      }
  }
}
