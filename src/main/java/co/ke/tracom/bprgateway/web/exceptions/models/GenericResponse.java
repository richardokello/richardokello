package co.ke.tracom.bprgateway.web.exceptions.models;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GenericResponse {
    private String status;
    private String message;
}
