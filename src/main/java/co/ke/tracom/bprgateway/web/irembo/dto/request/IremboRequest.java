package co.ke.tracom.bprgateway.web.irembo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IremboRequest {
    private String billNumber;
    private String amount;

}
