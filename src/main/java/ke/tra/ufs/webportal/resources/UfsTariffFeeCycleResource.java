package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.UfsFeeCycle;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.math.BigInteger;

@RestController
@RequestMapping("/fee_cycle")
public class UfsTariffFeeCycleResource  extends ChasisResource<UfsFeeCycle, BigInteger, UfsEdittedRecord> {
    public UfsTariffFeeCycleResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
