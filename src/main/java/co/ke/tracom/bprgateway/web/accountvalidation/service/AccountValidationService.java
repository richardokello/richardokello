package co.ke.tracom.bprgateway.web.accountvalidation.service;

import co.ke.tracom.bprgateway.core.tracomchannels.tcp.T24TCPClient;
import co.ke.tracom.bprgateway.web.accountvalidation.data.BPRAccountValidationRequest;
import co.ke.tracom.bprgateway.web.switchparameters.entities.XSwitchParameter;
import co.ke.tracom.bprgateway.web.switchparameters.repository.XSwitchParameterRepository;
import co.ke.tracom.bprgateway.web.util.services.UtilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountValidationService {
  private final T24TCPClient t24TCPClient;
  private final XSwitchParameterRepository xSwitchParameterRepository;

  private final String T24_INQUIRY_WAIT_TIME_MILLISECONDS = "T24_INQUIRY_WAIT_TIME_MILLISECONDS";
  private final String maskedUsername = "########U";
  private final String maskedPassword = "########A";

  public void processAccountValidation(BPRAccountValidationRequest request) {
    Optional<XSwitchParameter> optionalWaitTime =
        xSwitchParameterRepository.findByParamName(T24_INQUIRY_WAIT_TIME_MILLISECONDS);


      String accountValidationRequest =
              "0000AENQUIRY.SELECT,,"
                      + maskedUsername
                      + "/"
                      + maskedPassword
                      + ",ECL.ENQUIRY.DETS,ID:EQ="
                      + request.getAccountNo()
                      + ","
                      + "TRANS.TYPE.ID:EQ=CUSTDET";
      String OFSMsg = String.format("%04d", accountValidationRequest.length()) + accountValidationRequest;


    try {
      t24TCPClient.sendTransactionToT24(OFSMsg);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
