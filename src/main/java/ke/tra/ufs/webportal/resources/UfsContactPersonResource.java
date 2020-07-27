package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.UfsContactPerson;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;


@RestController
@RequestMapping(value = "/contact-person")
public class UfsContactPersonResource extends ChasisResource<UfsContactPerson,Long, UfsEdittedRecord> {

    public UfsContactPersonResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
