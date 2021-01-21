package ke.tracom.ufs.services.template;

import ke.tracom.ufs.entities.NotificationEmailSend;
import ke.tracom.ufs.repositories.NotificationEmailSendRepository;
import ke.tracom.ufs.services.NotificationSendEmailService;
import org.springframework.stereotype.Service;

@Service
public class NotificationSendEmailTemplate implements NotificationSendEmailService {

    private final NotificationEmailSendRepository notificationEmailSendRepository;

    public NotificationSendEmailTemplate(NotificationEmailSendRepository notificationEmailSendRepository) {
        this.notificationEmailSendRepository = notificationEmailSendRepository;
    }

    @Override
    public void save(NotificationEmailSend notificationEmailSend) {
        this.notificationEmailSendRepository.save(notificationEmailSend);
    }
}
