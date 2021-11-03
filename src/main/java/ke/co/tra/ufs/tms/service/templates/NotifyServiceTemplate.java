package ke.co.tra.ufs.tms.service.templates;


import ke.co.tra.ufs.tms.service.NotifyService;
import ke.co.tra.ufs.tms.utils.EmailBody;
import ke.co.tra.ufs.tms.utils.enums.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * @author eli.muraya
 */
@Service
public class NotifyServiceTemplate implements NotifyService {

    @Value("${baseUrl}")
    public String baseUrl;

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Override
    public void sendEmail(String emailAddress, String title, String message) {
        System.out.println("SENDING EMAIL=====================================>>>>>>");
        EmailBody email = new EmailBody();
        email.setSubject(title);
        email.setMessage(message);
        email.setSendTo(emailAddress);
        email.setMessageType(MessageType.EMAIL);

        log.info("Sending email notification to remote client (Email Address: {}, Title: {}, Message: {})", emailAddress, title, message);
        // TODO Auto-generated method stub
        //           call send email code  here
        RestTemplate template = new RestTemplate();
        HttpEntity<EmailBody> request = new HttpEntity<>(email);


        System.out.println("Sending email..." + email.getMessage().toString());

        try {

            template.exchange(baseUrl + "ufs-communication-service/communication/send-email", HttpMethod.POST, request, EmailBody.class
            );
        } catch (HttpClientErrorException e) {
            System.out.println("Communication service is unreachable ...");
        }
//        if sending email fails, queue and try again later


    }

    @Override
    public void sendSms(String phone, String message) {
        System.out.println("SENDING SMS=====================================>>>>>>");
        EmailBody email = new EmailBody();
        email.setMessage(message);
        email.setSendTo(phone);
        email.setMessageType(MessageType.SMS);
        RestTemplate template = new RestTemplate();
        HttpEntity<EmailBody> request = new HttpEntity<>(email);
        System.out.println("Sending SMS..." + email.getMessage().toString());
        try {
            template.exchange(baseUrl + "ufs-communication-service/communication/smpp/send-sms", HttpMethod.POST, request, EmailBody.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Communication service is unreachable ...");
        }
    }

}
