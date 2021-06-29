package co.ke.tracom.bprgateway.web.sendmoney.data.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BulkPaymentResponse {
    private String status;
    private String message;
}
