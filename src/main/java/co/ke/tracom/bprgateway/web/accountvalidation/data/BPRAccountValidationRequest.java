package co.ke.tracom.bprgateway.web.accountvalidation.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BPRAccountValidationRequest {
    private String channel = "";
    private String transactionRef;
    private String accountNo;
}
