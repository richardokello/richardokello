package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.AccountOpenningDetails;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.math.BigDecimal;


@RestController
@RequestMapping(value = "/account-opening-details")
public class AccountOpenningDetailsResources extends ChasisResource<AccountOpenningDetails, BigDecimal, UfsEdittedRecord> {

    public AccountOpenningDetailsResources(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}


