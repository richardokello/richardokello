package co.ke.tracom.bprgateway.web.izicash.data.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class IZICashResponse {
    private String status;
    private String message;
    private IZICashResponseData data;
}
