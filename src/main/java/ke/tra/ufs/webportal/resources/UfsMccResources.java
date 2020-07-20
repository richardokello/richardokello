package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.UfsMcc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/mcc")
public class UfsMccResources extends ChasisResource<UfsMcc, BigDecimal, UfsEdittedRecord> {
    public UfsMccResources(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
