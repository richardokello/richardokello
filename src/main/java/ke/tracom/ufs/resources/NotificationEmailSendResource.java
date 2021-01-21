package ke.tracom.ufs.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tracom.ufs.entities.NotificationEmailSend;
import ke.tracom.ufs.entities.UfsEdittedRecord;
import ke.tracom.ufs.entities.UfsSysConfig;
import ke.tracom.ufs.entities.wrapper.SendEmailWrapper;
import ke.tracom.ufs.repositories.UfsSysConfigRepository;
import ke.tracom.ufs.services.NotificationSendEmailService;
import ke.tracom.ufs.utils.AppConstants;
import ke.tracom.ufs.utils.SharedMethods;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/notification-email-send")
public class NotificationEmailSendResource extends ChasisResource<NotificationEmailSend,Long, UfsEdittedRecord> {

    private final NotificationSendEmailService notificationSendEmailService;
    private final UfsSysConfigRepository sysConfigRepository;
    private final SharedMethods sharedMethods;

    public NotificationEmailSendResource(LoggerService loggerService, EntityManager entityManager,NotificationSendEmailService notificationSendEmailService,
                                         UfsSysConfigRepository sysConfigRepository,SharedMethods sharedMethods) {
        super(loggerService, entityManager);
        this.notificationSendEmailService = notificationSendEmailService;
        this.sysConfigRepository = sysConfigRepository;
        this.sharedMethods = sharedMethods;
    }



    @RequestMapping(value = "/send" , method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<ResponseWrapper<NotificationEmailSend>> sendEmail(@Valid SendEmailWrapper sendEmailWrapper) throws IOException {

        ResponseWrapper response = new ResponseWrapper();
        NotificationEmailSend notificationEmailSend = new NotificationEmailSend();

        notificationEmailSend.setContent(sendEmailWrapper.getContent());
        notificationEmailSend.setSignature(sendEmailWrapper.getSignature());

        if(sendEmailWrapper.getTimeToSendType().equals(AppConstants.EMAIL_SEND_NOW)){
            notificationEmailSend.setTimeToSend(new Date());
        }else{
            sendEmailWrapper.setTimeToSend(sendEmailWrapper.getTimeToSend());
        }

        notificationEmailSend.setTimeToSendType(sendEmailWrapper.getTimeToSendType());

        if(Objects.nonNull(sendEmailWrapper.getEnteredrecepients())){
            notificationEmailSend.setRecepients(sendEmailWrapper.getEnteredrecepients());
        }else {
            notificationEmailSend.setRecepients(sendEmailWrapper.getSelectedrecepients());

            if(Objects.nonNull(sendEmailWrapper.getCc())){
                notificationEmailSend.setCc(sendEmailWrapper.getCc());
            }

            if(Objects.nonNull(sendEmailWrapper.getBcc())){
                notificationEmailSend.setBcc(sendEmailWrapper.getBcc());
            }

        }

        if(Objects.nonNull(sendEmailWrapper.getAttachement())){
            String fileName = sysConfigRepository.uploadDir(AppConstants.ENTITY_GLOBAL_INTEGRATION,"fileUploadDir");
            List<String> uploadAttachements = new ArrayList<String>();

            for(MultipartFile file:sendEmailWrapper.getAttachement()){

                String fileUrl = sharedMethods.store(file, fileName);
                uploadAttachements.add(fileUrl);
            }

            notificationEmailSend.setAttachement(uploadAttachements.toString());
        }

        notificationSendEmailService.save(notificationEmailSend);
        response.setCode(201);
        response.setData(notificationEmailSend);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
