package co.ke.tracom.bprgateway.web.sendmoney.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.CompletableFuture;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendMoneyResponse extends CompletableFuture<Void> {
    private String status;
    private String message;
    private SendMoneyResponseData data;

}
