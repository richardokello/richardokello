package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.UfsBusinessType;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;


@RestController
@RequestMapping(value = "/business-types")
public class UfsBusinessTypeResource extends ChasisResource<UfsBusinessType,Long, UfsEdittedRecord> {

    public UfsBusinessTypeResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
