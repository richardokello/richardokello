package ke.tracom.ufs.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tracom.ufs.entities.UfsEdittedRecord;
import ke.tracom.ufs.entities.UfsEmailNotificationTempl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;

@RestController
@RequestMapping("/notification-email-template")
public class UfsEmailNotificationTemplResource extends ChasisResource<UfsEmailNotificationTempl,Long, UfsEdittedRecord> {

    public UfsEmailNotificationTemplResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
