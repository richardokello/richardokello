package co.ke.tracom.bprgateway.web.accountvalidation.service;

import co.ke.tracom.bprgateway.core.tracomchannels.tcp.T24MessageProcessor;
import co.ke.tracom.bprgateway.core.tracomchannels.tcp.T24TCPClient;
import co.ke.tracom.bprgateway.core.tracomchannels.tcp.dto.BankAccountValidationResponse;
import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.accountvalidation.data.BPRAccountValidationRequest;
import co.ke.tracom.bprgateway.web.accountvalidation.data.BPRAccountValidationResponse;
import co.ke.tracom.bprgateway.web.agenttransactions.dto.response.AuthenticateAgentResponse;
import co.ke.tracom.bprgateway.web.util.services.BaseServiceProcessor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_PASSWORD;
import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_USERNAME;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccountValidationService {
    private final T24TCPClient t24TCPClient;
    private final T24MessageProcessor t24MessageProcessor;
    private final BaseServiceProcessor baseServiceProcessor;

    @SneakyThrows
    public BPRAccountValidationResponse processBankAccountValidation(BPRAccountValidationRequest request, String rrn) {
        AuthenticateAgentResponse optionalAuthenticateAgentResponse = baseServiceProcessor.authenticateAgentUsernamePassword(request.getCredentials());

        String accountValidationRequest =
                "0000AENQUIRY.SELECT,," + MASKED_T24_USERNAME + "/" + MASKED_T24_PASSWORD +
                        ",ECL.ENQUIRY.DETS,ID:EQ=" + request.getAccountNo() + ",TRANS.TYPE.ID:EQ=CUSTDET";
        String OFSMsg = String.format("%04d", accountValidationRequest.length()) + accountValidationRequest;

        try {
            String responseOFSMessage = t24TCPClient.sendTransactionToT24(OFSMsg);
            log.info("T24 Account Validation Response OFS" + responseOFSMessage);
            BankAccountValidationResponse bankAccountValidationResponse = t24MessageProcessor.parseT24ResponseForBankAccountValidation(responseOFSMessage);

            return BPRAccountValidationResponse.builder()
                    .status(
                            bankAccountValidationResponse.getStatus() == HttpStatus.SC_OK ? "00" : String.valueOf(bankAccountValidationResponse.getStatus())
                    )
                    .message("Account validation successful")
                    .accountName(bankAccountValidationResponse.getAccountName())
                    .rrn(RRNGenerator.getInstance("AV").getRRN())
                    .build();

        } catch (NoSuchFieldException | IOException e) {
            e.printStackTrace();
            return BPRAccountValidationResponse.builder()
                    .status(String.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR))
                    .message(e.getMessage()).build();
        }
    }
}
