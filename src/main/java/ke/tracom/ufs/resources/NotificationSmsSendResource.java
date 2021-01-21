package ke.tracom.ufs.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tracom.ufs.entities.NotificationSmsSend;
import ke.tracom.ufs.entities.UfsEdittedRecord;
import ke.tracom.ufs.entities.wrapper.SendSmsWrapper;
import ke.tracom.ufs.repositories.NotificationSmsSendRepository;
import ke.tracom.ufs.services.NotificationSendSmsService;
import ke.tracom.ufs.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("/notification-sms-send")
public class NotificationSmsSendResource extends ChasisResource<NotificationSmsSend,Long, UfsEdittedRecord> {

    private final NotificationSendSmsService notificationSendSmsService;
    private final NotificationSmsSendRepository notificationSmsSendRepository;

    public NotificationSmsSendResource(LoggerService loggerService, EntityManager entityManager,NotificationSendSmsService notificationSendSmsService,
                                       NotificationSmsSendRepository notificationSmsSendRepository) {
        super(loggerService, entityManager);
        this.notificationSendSmsService = notificationSendSmsService;
        this.notificationSmsSendRepository = notificationSmsSendRepository;
    }


    @RequestMapping(value = "/send" , method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<ResponseWrapper<NotificationSmsSend>> sendSms(@Valid @RequestBody SendSmsWrapper sendSms) {
        ResponseWrapper response = new ResponseWrapper();

       NotificationSmsSend notificationSmsSend = new NotificationSmsSend();

        notificationSmsSend.setContent(sendSms.getContent());

        if(sendSms.getTimeToSendType().equals(AppConstants.SMS_SEND_NOW)){
            notificationSmsSend.setTimeToSend(new Date());
        }else{
            notificationSmsSend.setTimeToSend(sendSms.getTimeToSend());
        }

        notificationSmsSend.setTimeToSendType(sendSms.getTimeToSendType());

        if(Objects.nonNull(sendSms.getEnteredrecepients())){
            notificationSmsSend.setRecepients(sendSms.getEnteredrecepients());
        }else {
            notificationSmsSend.setRecepients(sendSms.getSelectedrecepients());
        }

        notificationSendSmsService.save(notificationSmsSend);
        response.setCode(201);
        response.setData(notificationSmsSend);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
