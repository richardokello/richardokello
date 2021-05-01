package co.ke.tracom.bprgateway.web.mobicash.data;

import lombok.Data;

@Data
public class MobiCashResponse {

  private String responseCode;
  private String statusCode;
  private String responseDescription;
}
