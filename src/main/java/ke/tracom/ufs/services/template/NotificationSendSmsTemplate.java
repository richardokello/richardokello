package ke.tracom.ufs.services.template;

import ke.tracom.ufs.entities.NotificationSmsSend;
import ke.tracom.ufs.repositories.NotificationSmsSendRepository;
import ke.tracom.ufs.services.NotificationSendSmsService;
import org.springframework.stereotype.Service;

@Service
public class NotificationSendSmsTemplate implements NotificationSendSmsService {

    private final NotificationSmsSendRepository notificationSmsSendRepository;

    public NotificationSendSmsTemplate(NotificationSmsSendRepository notificationSmsSendRepository) {
        this.notificationSmsSendRepository = notificationSmsSendRepository;
    }

    @Override
    public void save(NotificationSmsSend notificationSmsSend) {
       this.notificationSmsSendRepository.save(notificationSmsSend);
    }
}
