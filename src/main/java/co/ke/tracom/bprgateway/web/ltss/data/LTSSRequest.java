package co.ke.tracom.bprgateway.web.ltss.data;

import co.ke.tracom.bprgateway.servers.tcpserver.dto.GenericRequest;
import co.ke.tracom.bprgateway.web.ltss.data.nationalIDValidation.NationalIDValidationRequest;
import lombok.Data;

@Data
public class LTSSRequest extends GenericRequest {

  private String identification;
  private String extRefNo;
  private String intermediary;
  private String description;
 // private Date datetime;
  private String amount;
  private String paymentDate;
  private NationalIDValidationRequest beneficiary;
}
