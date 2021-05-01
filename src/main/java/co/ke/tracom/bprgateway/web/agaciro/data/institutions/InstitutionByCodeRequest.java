package co.ke.tracom.bprgateway.web.agaciro.data.institutions;

import co.ke.tracom.bprgateway.web.agaciro.data.AgaciroRequest;
import lombok.Data;

@Data
public class InstitutionByCodeRequest extends AgaciroRequest {

  private String institutionCode;
}
