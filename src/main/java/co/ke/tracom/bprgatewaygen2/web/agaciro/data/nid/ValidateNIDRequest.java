package co.ke.tracom.bprgatewaygen2.web.agaciro.data.nid;

import co.ke.tracom.bprgatewaygen2.web.agaciro.data.AgaciroRequest;
import lombok.Data;

@Data
public class ValidateNIDRequest extends AgaciroRequest {
    private String nid;
}
