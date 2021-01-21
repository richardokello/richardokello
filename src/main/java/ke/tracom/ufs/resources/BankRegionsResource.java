package ke.tracom.ufs.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tracom.ufs.entities.UfsBankRegion;
import ke.tracom.ufs.entities.UfsEdittedRecord;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

@Controller
@RequestMapping(value = "/bank-region")
public class BankRegionsResource extends ChasisResource<UfsBankRegion, BigDecimal, UfsEdittedRecord> {
    public BankRegionsResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
