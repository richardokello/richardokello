package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.UfsBankRegion;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;


@RestController
@RequestMapping(value = "/bank-region")
public class BankRegionResource extends ChasisResource<UfsBankRegion, Long, UfsEdittedRecord> {

    public BankRegionResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}

