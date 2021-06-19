package co.ke.tracom.bprgateway.web.accountvalidation.service;

import co.ke.tracom.bprgateway.core.tracomchannels.tcp.T24MessageProcessor;
import co.ke.tracom.bprgateway.core.tracomchannels.tcp.T24TCPClient;
import co.ke.tracom.bprgateway.core.tracomchannels.tcp.dto.BankAccountValidationResponse;
import co.ke.tracom.bprgateway.core.tracomchannels.tcp.dto.EUCLMeterValidationResponse;
import co.ke.tracom.bprgateway.web.accountvalidation.data.BPRAccountValidationRequest;
import co.ke.tracom.bprgateway.web.accountvalidation.data.BPRAccountValidationResponse;
import co.ke.tracom.bprgateway.web.switchparameters.entities.XSwitchParameter;
import co.ke.tracom.bprgateway.web.switchparameters.repository.XSwitchParameterRepository;
import co.ke.tracom.bprgateway.web.util.services.UtilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccountValidationService {
    private final T24TCPClient t24TCPClient;
    private final T24MessageProcessor t24MessageProcessor;

    private final String maskedUsername = "########U";
    private final String maskedPassword = "########A";

    public BPRAccountValidationResponse processBankAccountValidation(BPRAccountValidationRequest request) {

        String accountValidationRequest =
                "0000AENQUIRY.SELECT,," + maskedUsername + "/" + maskedPassword +
                        ",ECL.ENQUIRY.DETS,ID:EQ=" + request.getAccountNo() + ",TRANS.TYPE.ID:EQ=CUSTDET";
        String OFSMsg = String.format("%04d", accountValidationRequest.length()) + accountValidationRequest;

        try {
            String responseOFSMessage = t24TCPClient.sendTransactionToT24(OFSMsg);
            log.info("T24 Account Validation Response " + responseOFSMessage);
            BankAccountValidationResponse bankAccountValidationResponse = t24MessageProcessor.parseT24ResponseForBankAccountValidation(responseOFSMessage);

            return BPRAccountValidationResponse.builder()
                    .status(String.valueOf(bankAccountValidationResponse.getStatus()))
                    .message("Account validation successful")
                    .accountName(bankAccountValidationResponse.getAccountName())
                    .build();

        } catch (NoSuchFieldException | IOException e) {
            e.printStackTrace();
            return BPRAccountValidationResponse.builder()
                    .status(String.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR))
                    .message(e.getMessage()).build();
        }
    }
}
