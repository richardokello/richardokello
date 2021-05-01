package co.ke.tracom.bprgateway.web.agaciro.data.nid;

import co.ke.tracom.bprgateway.web.agaciro.data.AgaciroRequest;
import lombok.Data;

@Data
public class ValidateNIDRequest extends AgaciroRequest {

  private String nid;
}
