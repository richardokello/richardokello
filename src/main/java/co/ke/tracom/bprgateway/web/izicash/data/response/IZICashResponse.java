package co.ke.tracom.bprgateway.web.izicash.data.response;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class IZICashResponse {
    private String status;
    private String message;
    private IZICashResponseData data;
}
