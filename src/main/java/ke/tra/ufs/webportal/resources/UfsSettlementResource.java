package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.UfsSettlement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.math.BigInteger;

@RestController
@RequestMapping("/settlement")
public class UfsSettlementResource extends ChasisResource<UfsSettlement, BigInteger, UfsEdittedRecord> {
    public UfsSettlementResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
