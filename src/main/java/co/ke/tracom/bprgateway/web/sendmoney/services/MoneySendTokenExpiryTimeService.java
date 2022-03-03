package co.ke.tracom.bprgateway.web.sendmoney.services;

import co.ke.tracom.bprgateway.web.sendmoney.entity.MoneySend;
import co.ke.tracom.bprgateway.web.sendmoney.repository.MoneySendRepository;
import co.ke.tracom.bprgateway.web.sms.dto.SMSRequest;
import co.ke.tracom.bprgateway.web.sms.dto.SMSResponse;
import co.ke.tracom.bprgateway.web.sms.services.SMSService;
import co.ke.tracom.bprgateway.web.smsscheduled.entities.ScheduledSMS;
import co.ke.tracom.bprgateway.web.smsscheduled.repository.ScheduledSMSRepository;
import co.ke.tracom.bprgateway.web.switchparameters.XSwitchParameterService;
import co.ke.tracom.bprgateway.web.util.services.UtilityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static co.ke.tracom.bprgateway.web.sms.dto.SMSRequest.SMS_FUNCTION_SENDER;

@Data
@RequiredArgsConstructor
@Service
public class MoneySendTokenExpiryTimeService {
    private final MoneySendRepository repository;


    private final XSwitchParameterService xSwitchParameterService;
    private final BPRCreditCardNumberGenerator bprCreditCardNumberGenerator;
    private final DesUtil desUtil;

    private final SendMoneyService sendMoneyService;
    private final SMSService smsService;
    private final ScheduledSMSRepository scheduledSMSRepository;
    private final UtilityService utilityService;

    private final TokenDurationService tokenDurationService;

    @Value("${vision-fund.token-expiry.configuration-name}")
    private String tokenConfigurationName;


    private static final Logger log = LoggerFactory.getLogger(MoneySendTokenExpiryTimeService.class);


    @Async
    public CompletableFuture<List<MoneySend>> check() {

        //Optional<List<MoneySend>> optionalMoneySendList = repository.findBySendmoneytokenexpiretimeBeforeAndFulfilmentstatusEquals(/*new Date().getTime()*/0, 0);
        Optional<List<MoneySend>> optionalMoneySendList = repository.findByFulfilmentstatusEquals(0, Instant.now().toEpochMilli(),30);
        List<MoneySend> moneySendList = new ArrayList<>();
        optionalMoneySendList.ifPresent(moneySends -> moneySends.forEach(

                l -> {
                    String receiverMobile = l.getRecevernumber();
                    String senderMobileNo = l.getSendernumber();
                    String transactionRRN = l.getTransactionRRN();
                    double amount = Double.parseDouble(l.getAmount());

                    long sendMoneyExpiryTime = l.getSendmoneytokenexpiretime() == null ? 0 : l.getSendmoneytokenexpiretime();


                    // SendMoneyRequest request = new SendMoneyRequest();
                    // request.setAmount(amount);

                    //Generate new cno and update the record
                    String virtualBIN = xSwitchParameterService.fetchXSwitchParamValue("CARDLESSTXNBIN");
                    String CARDLESS_TXN_BIN = "123456";
                    String virtualCardBIN = virtualBIN.equals("") ? CARDLESS_TXN_BIN : virtualBIN;
                    String vCardNo = bprCreditCardNumberGenerator.generate(virtualCardBIN, 12);
                    String generatedCardNo = desUtil.encryptPlainText(vCardNo);
                        if (generatedCardNo != null) {
                            Random generator = new Random();
                            String passCode = String.format("%06d", 100000 + generator.nextInt(899999));
                            System.out.println();

                            Duration secondDurationByConfigurationName = tokenDurationService.getSecondDurationByConfigurationName(tokenConfigurationName);
                            long sendmoneytokenexpirytime2 = Instant.ofEpochMilli(sendMoneyExpiryTime).plus(secondDurationByConfigurationName).toEpochMilli();
                            SMSRequest senderMessage2 = saveSenderMessage2(amount, transactionRRN, receiverMobile, senderMobileNo, passCode, vCardNo, l.getMoneysendid(),sendmoneytokenexpirytime2);

                            try {
                                String fdiSMSAPIAuthToken = smsService.getFDISMSAPIAuthToken();
                                SMSResponse senderResponse = smsService.sendFDISMSRequest(fdiSMSAPIAuthToken, senderMessage2);
                                log.info("Sender SMS Response[" + transactionRRN + "]: " + senderResponse.toString());

                                //then save new vcard and passcode
                                l.setMstoken2(l.getMstoken());
                                l.setCno2(l.getCno());
                                l.setMstoken(passCode);
                                l.setCno(generatedCardNo);
                                l.setRecevernumber(l.getSendernumber());

                                //use the exact configuration name


                                l.setSendmoneytokenstarttime2(sendMoneyExpiryTime);
                                l.setSendmoneytokenexpiretime2(sendmoneytokenexpirytime2);
                                repository.save(l);

                                //add the updated object
                                moneySendList.add(l);
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        }
                    //});
                    System.out.println(l.toString());

                }
                //System.out::println
        ));
        return CompletableFuture.completedFuture(moneySendList);

    }

    private SMSRequest saveSenderMessage2(double amount, String transactionRRN,
                                          String recipientMobile, String senderMobileNo, String passCode, String vCardNo, Long sendMoneyId,Long sendmoneytokenstarttime) {

        String senderMessage = senderMessage2(amount, recipientMobile, passCode, vCardNo);
        SMSRequest fdismsRequest = getFDISMSRequest(senderMessage, senderMobileNo, SMS_FUNCTION_SENDER);
        // Insert Second SMS
        String SMSContent = utilityService.encryptText(senderMessage);
        ScheduledSMS scheduledSMS = new ScheduledSMS();
        scheduledSMS.setSentstatus(1);
        scheduledSMS.setAttempts(0);
        scheduledSMS.setMessage(SMSContent);
        scheduledSMS.setReceiverphone(senderMobileNo);
        scheduledSMS.setTxnref(transactionRRN);
        scheduledSMS.setSendMoneyId(sendMoneyId);
        scheduledSMS.setSendmoneytokenstarttime(sendmoneytokenstarttime);
        scheduledSMSRepository.save(scheduledSMS);
        log.error("\n00000000000000000000000000000000000000\n{}\n00000000000000000000000000000000000000\n",utilityService.decryptText(scheduledSMS.getMessage()));

        return fdismsRequest;
    }

    private SMSRequest getFDISMSRequest(String message, String smsRecipientNo, String smsFunctionReceiver) {

        System.err.println("message = " + smsFunctionReceiver + " : " + message);
        return SMSRequest.builder()
                .recipient(smsRecipientNo)
                .SMSFunction(smsFunctionReceiver)
                .message(message)
                .build();
    }

    private String senderMessage2(double amount, String recipientMobile, String passCode, String cardNo) {
        return "The recipient, "
                + recipientMobile
                + ", of the amount "
                + amount
                + " didn't withdraw the money within the stipulated time of 72 hours. "
                + "Kindly withdraw the money within 24 hours from now at the BPR agent. "
                + " Provide VCARD no: "
                + cardNo
                + " and Passcode: "
                + passCode
                + ". Thanks for banking with us.";
    }
}
