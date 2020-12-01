package co.ke.tracom.bprgatewaygen2.web.tigopesa.data;

import lombok.Data;

@Data
public class TigopesaResponse {
    private String errorCode;
    private String errorMessage;
    private String transactionId;
    private String referenceId;
    private String status;
    private String message;
    private int voucherCode;
}
