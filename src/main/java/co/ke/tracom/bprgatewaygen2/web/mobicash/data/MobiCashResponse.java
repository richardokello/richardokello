package co.ke.tracom.bprgatewaygen2.web.mobicash.data;

import lombok.Data;

@Data
public class MobiCashResponse {

  private String responseCode;
  private String statusCode;
  private String responseDescription;
}
