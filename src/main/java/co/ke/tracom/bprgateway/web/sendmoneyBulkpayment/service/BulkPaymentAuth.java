package co.ke.tracom.bprgateway.web.sendmoneyBulkpayment.service;

import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.Data;
import co.ke.tracom.bprgateway.web.exceptions.custom.InvalidAgentCredentialsException;
import co.ke.tracom.bprgateway.web.sendmoneyBulkpayment.data.AuthRequest;
import co.ke.tracom.bprgateway.web.util.data.MerchantAuthInfo;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class BulkPaymentAuth {
    @Autowired
    private BaseServiceProcessor baseServiceProcessor;

     public AuthenticateAgentResponse authenticateAgent(AuthRequest request) {
        AuthenticateAgentResponse authenticateAgentResponse=null;
        MerchantAuthInfo authInfo = new MerchantAuthInfo();
        authInfo.setUsername(request.getUsername());
        authInfo.setPassword(request.getPassword());

        try {
            authenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(authInfo);
        } catch (InvalidAgentCredentialsException e) {
            authenticateAgentResponse.setCode(117);
            authenticateAgentResponse.setMessage(e.getMessage());
            authenticateAgentResponse.setData(null);


            return authenticateAgentResponse;

        }
        return authenticateAgentResponse;
    }
    public Data getResponse(){
        AuthRequest request=new AuthRequest();
         return authenticateAgent(request).getData();
    }



}
