package co.ke.tracom.bprgateway.web.izicash.data.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor
public class IZICashResponse {
    private String status;
    private String message;
    private IZICashResponseData data;
}
