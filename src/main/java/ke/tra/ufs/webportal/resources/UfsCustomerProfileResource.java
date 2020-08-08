package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.UfsCustomerProfile;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.math.BigInteger;

@RestController
@RequestMapping("/customer-profile")
public class UfsCustomerProfileResource extends ChasisResource<UfsCustomerProfile, BigInteger, UfsEdittedRecord> {
    public UfsCustomerProfileResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
