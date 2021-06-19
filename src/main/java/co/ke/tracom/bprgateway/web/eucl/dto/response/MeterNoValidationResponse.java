package co.ke.tracom.bprgateway.web.eucl.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeterNoValidationResponse {
    String status;
    String message;
    MeterNoData data;
}
