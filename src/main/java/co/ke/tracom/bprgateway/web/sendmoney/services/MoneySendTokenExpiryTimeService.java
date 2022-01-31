package co.ke.tracom.bprgateway.web.sendmoney.services;

import co.ke.tracom.bprgateway.web.sendmoney.entity.MoneySend;
import co.ke.tracom.bprgateway.web.sendmoney.repository.MoneySendRepository;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

//@Service
public class MoneySendTokenExpiryTimeService {
    private final MoneySendRepository repository;

    public MoneySendTokenExpiryTimeService(MoneySendRepository repository) {
        this.repository = repository;
    }

    /*private final XSwitchParameterService xSwitchParameterService;
    private final BPRCreditCardNumberGenerator bprCreditCardNumberGenerator;
    private final DesUtil desUtil;

    private final SendMoneyService sendMoneyService;
    private final SMSService smsService;
    private final ScheduledSMSRepository scheduledSMSRepository;
    private final UtilityService utilityService;*/


    //private static final Logger log = LoggerFactory.getLogger(MoneySendTokenExpiryTimeService.class);




    @Async
    public CompletableFuture<List<MoneySend>> check() {

        //Optional<List<MoneySend>> optionalMoneySendList = repository.findBySendmoneytokenexpiretimeBeforeAndFulfilmentstatusEquals(/*new Date().getTime()*/0, 0);
        /*Optional<List<MoneySend>> optionalMoneySendList = repository.findByFulfilmentstatusEquals( 0);
        List<MoneySend> moneySendList = new ArrayList<>();
        optionalMoneySendList.ifPresent(moneySends -> moneySends.forEach(

                l -> {
                    String receiverMobile = l.getRecevernumber();
                    String senderMobileNo = l.getSendernumber();
                    String transactionRRN = l.getTransactionRRN();
                    double amount = Double.parseDouble(l.getAmount());

                    long sendMoneyExpiryTime = l.getSendmoneytokenexpiretime();


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


                            //SMSRequest recipientMessage = saveRecipientMessage(request, transactionRRN, receiverMobile, senderMobileNo, vCardNo);
                            SMSRequest senderMessage2 = saveSenderMessage2(amount, transactionRRN, receiverMobile, senderMobileNo, passCode, vCardNo);

//                            String fdiSMSAPIAuthToken = null;
//                            SMSResponse recipientResponse;
//                            SMSResponse senderResponse;

                            //SMSResponse recipientResponse = smsService.sendFDISMSRequest(fdiSMSAPIAuthToken, recipientMessage);
                            //log.info("Recipient SMS Response[" + transactionRRN + "]: " + recipientResponse.toString());
                            //SMSResponse senderResponse = smsService.sendFDISMSRequest(fdiSMSAPIAuthToken, senderMessage);
                            //log.info("Sender SMS Response[" + transactionRRN + "]: " + senderResponse.toString());

                            try {
                                String fdiSMSAPIAuthToken = smsService.getFDISMSAPIAuthToken();
                                //recipientResponse = smsService.sendFDISMSRequest(fdiSMSAPIAuthToken, recipientMessage);
                                SMSResponse senderResponse = smsService.sendFDISMSRequest(fdiSMSAPIAuthToken, senderMessage2);
                                log.info("Sender SMS Response[" + transactionRRN + "]: " + senderResponse.toString());

                                //then save new vcard and passcode
                                l.setMstoken2(passCode);
                                l.setCno2(generatedCardNo);
                                long sendmoneytokenexpirytime2 = Instant.ofEpochMilli(sendMoneyExpiryTime).plus(Duration.ofHours(24)).toEpochMilli();
                                l.setSendmoneytokenstarttime2(Instant.now().toEpochMilli());
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
        ));*/
        return CompletableFuture.completedFuture(null);

    }
/*
    private SMSRequest saveRecipientMessage(SendMoneyRequest request, String transactionRRN,
                                            String receiverMobile, String senderMobile, String vCardNo) {

        String recipientMessage = recipientMessage(request, senderMobile, vCardNo);

        SMSRequest fdismsRequest = getFDISMSRequest(recipientMessage, receiverMobile, SMS_FUNCTION_RECEIVER);

        while (recipientMessage.length() > 0) {
            ScheduledSMS scheduledSMSTransaction = new ScheduledSMS();
            scheduledSMSTransaction.setSentstatus(1);
            scheduledSMSTransaction.setAttempts(0);
            scheduledSMSTransaction.setReceiverphone(receiverMobile);
            scheduledSMSTransaction.setTxnref(transactionRRN);

            if (recipientMessage.length() <= 160) {
                scheduledSMSTransaction.setMessage(utilityService.encryptSensitiveData(recipientMessage));
                recipientMessage = "";
            } else {
                scheduledSMSTransaction.setMessage(
                        utilityService.encryptSensitiveData(recipientMessage.substring(0, 160)));
                recipientMessage = recipientMessage.substring(160);
            }
            scheduledSMSRepository.save(scheduledSMSTransaction);
        }
//        SMSRequest smsObject = getNewSMSRequest(request, senderMobileNo, receiverMobile, passCode, vCardNo, SMS_FUNCTION_RECEIVER);
        return fdismsRequest;
    }

    private SMSRequest saveSenderMessage(SendMoneyRequest request, String transactionRRN,
                                         String recipientMobile, String senderMobileNo, String passCode) {

        String senderMessage = senderMessage(request, recipientMobile, passCode);
        SMSRequest fdismsRequest = getFDISMSRequest(senderMessage, senderMobileNo, SMS_FUNCTION_SENDER);
        // Insert Second SMS
        String SMSContent = utilityService.encryptText(senderMessage);
        ScheduledSMS scheduledSMS = new ScheduledSMS();
        scheduledSMS.setSentstatus(1);
        scheduledSMS.setAttempts(0);
        scheduledSMS.setMessage(SMSContent);
        scheduledSMS.setReceiverphone(senderMobileNo);
        scheduledSMS.setTxnref(transactionRRN);
        scheduledSMSRepository.save(scheduledSMS);

        return fdismsRequest;
    }

    private SMSRequest saveSenderMessage2(double amount, String transactionRRN,
                                          String recipientMobile, String senderMobileNo, String passCode, String vCardNo) {

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
        scheduledSMSRepository.save(scheduledSMS);

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

    private String recipientMessage(SendMoneyRequest request, String sendMobile, String cardNo) {
        return "You have received RWF"
                + request.getAmount()
                + " from "
                + sendMobile
                + " to withdraw at a BPR agent. Provide VCARD no: "
                + cardNo
                + " ,Sender will share the passcode. MURAKOZE";
    }

    private String senderMessage(SendMoneyRequest request, String recipientMobile, String passCode) {
        return "You have successfully sent RWF"
                + request.getAmount()
                + " to "
                + recipientMobile
                + " via BPR Cardless transfer. Kindly share passcode with the recipient. Passcode: "
                + passCode
                + ". Thanks for banking with us.";
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
    }*/
}
