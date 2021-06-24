package co.ke.tracom.bprgateway.web.accountvalidation.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BPRAccountValidationResponse {
    private String message;
    private String status;
    private String accountName;
    private String rrn;
}
