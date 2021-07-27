package co.ke.tracom.bprgateway.web.sms.services;

import co.ke.tracom.bprgateway.core.util.RRNGenerator;
import co.ke.tracom.bprgateway.web.sms.dto.SMSRequest;
import co.ke.tracom.bprgateway.web.sms.dto.SMSResponse;
import co.ke.tracom.bprgateway.web.sms.dto.fbismsapi.AuthResponse;
import co.ke.tracom.bprgateway.web.sms.dto.fbismsapi.FDISMSResponse;
import co.ke.tracom.bprgateway.web.switchparameters.XSwitchParameterService;
import co.ke.tracom.bprgateway.web.t24communication.services.T24Channel;
import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_PASSWORD;
import static co.ke.tracom.bprgateway.web.t24communication.services.T24Channel.MASKED_T24_USERNAME;

@Service
@Slf4j
@RequiredArgsConstructor
public class SMSService {
    private final XSwitchParameterService xSwitchParameterService;
    private final T24Channel t24Channel;

    @Deprecated
    public SMSResponse sendSMS(SMSRequest request) {
        final String t24Ip = xSwitchParameterService.fetchXSwitchParamValue("T24_IP");
        final String t24Port = xSwitchParameterService.fetchXSwitchParamValue("T24_PORT");

        String tot24str = "0000AENQUIRY.SELECT,," + MASKED_T24_USERNAME + "/" + MASKED_T24_PASSWORD + "/RW0010400,BPR.AGB.PROCESS.SMS,SMS.MSG:EQ=" + request.getMessage() + ",CUS.NO:EQ=" + request.getRecipient() + ",MARKER:EQ=" + request.getSMSFunction();
        tot24str = String.format("%04d", tot24str.length()) + tot24str;

        return packageRequest(t24Ip, t24Port, tot24str);
    }

    @Deprecated
    private SMSResponse packageRequest(String t24Ip, String t24Port, String tot24str) {
        T24TXNQueue t24TXNQueue = new T24TXNQueue();
        t24TXNQueue.setRequestleg(tot24str.trim());
        t24TXNQueue.setStarttime(System.currentTimeMillis());
        t24TXNQueue.setTxnchannel("PC");
        String transactionReferenceNo = RRNGenerator.getInstance("AD").getRRN();
        t24TXNQueue.setGatewayref(transactionReferenceNo);
        t24TXNQueue.setPostedstatus("0");
        t24TXNQueue.setProcode("500001");
        String tid = "PC";
        t24TXNQueue.setTid(tid);

        t24Channel.processTransactionToT24(t24Ip, Integer.parseInt(t24Port), t24TXNQueue);
        SMSResponse response = new SMSResponse();

        if (t24TXNQueue.getT24responsecode().equals("1")) {
            response.setStatus("00");
            response.setMessage(t24TXNQueue.getT24failnarration());
        } else {
            response.setStatus("01");
            response.setMessage(t24TXNQueue.getT24failnarration());
        }
        return response;
    }

    public SMSResponse processFDISMSAPI(SMSRequest request) {
        try {
            String fdiSMSAPIAuthToken = getFDISMSAPIAuthToken();
            return sendFDISMSRequest(fdiSMSAPIAuthToken, request);
        } catch (Exception e) {
            log.info("Error sending SMS: " + e.getMessage());
            return SMSResponse.builder()
                    .message("Processing failed")
                    .status("500")
                    .build();
        }
    }

    public SMSResponse sendFDISMSRequest(String fdismsapiAuthToken, SMSRequest smsRequest) throws JsonProcessingException {
        String fdi_sms_send_single_mt_url = xSwitchParameterService.fetchXSwitchParamValue("FDI_SMS_SEND_SINGLE_MT_URL");
        String fdi_sms_send_id = xSwitchParameterService.fetchXSwitchParamValue("FDI_SMS_SENDER_ID");

        Map<String, Object> request = new HashMap<>();
        request.put("msisdn", smsRequest.getRecipient());
        request.put("message", smsRequest.getMessage());
        request.put("dlr", "");
        request.put("msgRef", RRNGenerator.getInstance("SM").getRRN());
        request.put("sender_id", fdi_sms_send_id); //Actual value is BPR DG

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(fdi_sms_send_single_mt_url).build().encode();

        ObjectMapper mapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Bearer " + fdismsapiAuthToken);
        HttpEntity<?> httpEntity =
                new HttpEntity<>(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request), httpHeaders);

        System.err.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request));

        log.info(
                "SERVICE REQUEST : {} {} {}",
                uriComponents.toString(),
                httpEntity.getHeaders(),
                httpEntity.getBody());
        ResponseEntity<FDISMSResponse> responseEntity =
                restTemplate.exchange(uriComponents.toString(), HttpMethod.POST, httpEntity, FDISMSResponse.class);
        FDISMSResponse authResponse = responseEntity.getBody();
        log.info("SERVICE RESPONSE : {} {}", responseEntity.getHeaders(), authResponse);


        return SMSResponse.builder()
                .message(authResponse.getMessage())
                .status("200")
                .build();
    }

    public String getFDISMSAPIAuthToken() throws JsonProcessingException {
        String fdi_sms_api_username = xSwitchParameterService.fetchXSwitchParamValue("FDI_SMS_API_USERNAME");
        String fdi_sms_api_password = xSwitchParameterService.fetchXSwitchParamValue("FDI_SMS_API_PASSWORD");
        String fdi_sms_api_authentication_url = xSwitchParameterService.fetchXSwitchParamValue("FDI_SMS_API_AUTHENTICATION_URL");
        Map<String, Object> request = new HashMap<>();
        request.put("api_username", fdi_sms_api_username);
        request.put("api_password", fdi_sms_api_password);

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(fdi_sms_api_authentication_url).build().encode();

        ObjectMapper mapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> httpEntity =
                new HttpEntity<>(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request), httpHeaders);

        System.err.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request));

        log.info(
                "SERVICE REQUEST : {} {} {}",
                uriComponents.toString(),
                httpEntity.getHeaders(),
                httpEntity.getBody());
        ResponseEntity<AuthResponse> responseEntity =
                restTemplate.exchange(uriComponents.toString(), HttpMethod.POST, httpEntity, AuthResponse.class);
        AuthResponse authResponse = responseEntity.getBody();
        log.info("SERVICE RESPONSE : {} {}", responseEntity.getHeaders(), authResponse);

        String expiresAt = authResponse.getExpiresAt();
        System.out.println("expiresAt = " + expiresAt);
        String accessToken = authResponse.getAccessToken();
        System.out.println("accessToken = " + accessToken);

        return accessToken;

    }

}
