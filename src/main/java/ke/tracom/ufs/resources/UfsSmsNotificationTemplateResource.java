package ke.tracom.ufs.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tracom.ufs.entities.UfsEdittedRecord;
import ke.tracom.ufs.entities.UfsSmsNotificationTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;

@RestController
@RequestMapping("/notification-sms-template")
public class UfsSmsNotificationTemplateResource extends ChasisResource<UfsSmsNotificationTemplate,Long, UfsEdittedRecord> {


    public UfsSmsNotificationTemplateResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
