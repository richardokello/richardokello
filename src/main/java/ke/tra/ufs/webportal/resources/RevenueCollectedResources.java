package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.views.VwRevenueCollected;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

@RestController
@RequestMapping("/revenue_collected")
public class RevenueCollectedResources extends ChasisResource<VwRevenueCollected, BigDecimal, UfsEdittedRecord> {
    public RevenueCollectedResources(LoggerService loggerService, EntityManager entityManager){
        super(loggerService, entityManager);
    }
}
