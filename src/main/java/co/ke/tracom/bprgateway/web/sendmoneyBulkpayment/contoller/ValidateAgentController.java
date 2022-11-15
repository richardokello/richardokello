package co.ke.tracom.bprgateway.web.sendmoneyBulkpayment.contoller;


import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.sendmoneyBulkpayment.data.AuthRequest;
import co.ke.tracom.bprgateway.web.sendmoneyBulkpayment.service.BulkPaymentAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
@RestController("/api/validate")
public class ValidateAgentController {
    @Autowired
    private BulkPaymentAuth bulkPaymentAuth;
 @PostMapping("bulkauthentication")
    public ResponseEntity<AuthenticateAgentResponse>validateAgent(@Valid @RequestBody AuthRequest authInfo){
        try {
            AuthenticateAgentResponse authResponse = bulkPaymentAuth.authenticateAgent(authInfo);
            if (authResponse.getCode()==200){
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
            }else {
                return new ResponseEntity<>(authResponse, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
