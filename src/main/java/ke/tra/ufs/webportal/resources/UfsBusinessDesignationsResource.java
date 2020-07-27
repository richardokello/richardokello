package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.UfsBusinessDesignations;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;


@RestController
@RequestMapping(value = "/business-designations")
public class UfsBusinessDesignationsResource  extends ChasisResource<UfsBusinessDesignations,Long, UfsEdittedRecord> {

    public UfsBusinessDesignationsResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
