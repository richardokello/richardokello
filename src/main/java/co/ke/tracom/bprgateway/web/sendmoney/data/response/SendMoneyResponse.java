package co.ke.tracom.bprgateway.web.sendmoney.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendMoneyResponse {
    private String status;
    private String message;
    private SendMoneyResponseData data;

}
