package ke.tra.ufs.webportal.resources;


import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.views.VwAlltxnsFailed;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/failed-transactions")
public class FailedTransactionsResource extends ChasisResource<VwAlltxnsFailed, BigDecimal, UfsEdittedRecord> {

    public FailedTransactionsResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
