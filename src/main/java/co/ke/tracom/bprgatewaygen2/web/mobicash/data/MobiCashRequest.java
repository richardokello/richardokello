package co.ke.tracom.bprgatewaygen2.web.mobicash.data;

import lombok.Data;

@Data
public class MobiCashRequest {
    private String clientCode;
    private String transactionIdentifier;
    private String accountNumber;
    private String authorization;
}
