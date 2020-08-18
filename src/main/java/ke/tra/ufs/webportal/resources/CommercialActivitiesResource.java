package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.UfsCommercialActivities;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;


@RestController
@RequestMapping(value = "/commercial-activities")
public class CommercialActivitiesResource extends ChasisResource<UfsCommercialActivities,Long, UfsEdittedRecord> {

    public CommercialActivitiesResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
