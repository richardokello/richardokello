package co.ke.tracom.bprgatewaygen2.web.agaciro.data.institutions;

import co.ke.tracom.bprgatewaygen2.web.agaciro.data.AgaciroRequest;
import lombok.Data;

@Data
public class InstitutionByCodeRequest extends AgaciroRequest {

  private String institutionCode;
}
