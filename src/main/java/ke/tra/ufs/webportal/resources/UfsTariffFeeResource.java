package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.UfsTariffFee;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.math.BigInteger;

@RestController
@RequestMapping("/fees")
public class UfsTariffFeeResource extends ChasisResource<UfsTariffFee, BigInteger, UfsEdittedRecord> {
    public UfsTariffFeeResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
