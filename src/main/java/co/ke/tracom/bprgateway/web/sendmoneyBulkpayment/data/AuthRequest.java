package co.ke.tracom.bprgateway.web.sendmoneyBulkpayment.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    private String password;
    private String username;
}
