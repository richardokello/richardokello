package co.ke.tracom.bprgateway.web.accountopening.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountOpeningResponse {
    String status;
    String message;
}
