package co.ke.tracom.bprgateway.web.irembo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IremboBillNoValidationResponse {
    private String status;
    private String message;
    private IremboValidationData data;
}
