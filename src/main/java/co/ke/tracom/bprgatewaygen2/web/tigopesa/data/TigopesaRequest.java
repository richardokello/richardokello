package co.ke.tracom.bprgatewaygen2.web.tigopesa.data;

import lombok.Data;

@Data
public class TigopesaRequest {
    private String username;
    private String password;
    private String msisdn;
    private String billNumber;
    private String companyCode;
}
