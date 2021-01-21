package ke.tracom.ufs.services.template;

import ke.tracom.ufs.services.NotifyService;
import ke.tracom.ufs.utils.CommunicationService;
import ke.tracom.ufs.utils.EmailBody;
import ke.tracom.ufs.utils.enums.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 *
 * @author eli.muraya
 */
@Service
public class NotifyServiceTemplate implements NotifyService {

    @Autowired
    CommunicationService commService;

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
        commService.sendEmail(email);

    }

}
