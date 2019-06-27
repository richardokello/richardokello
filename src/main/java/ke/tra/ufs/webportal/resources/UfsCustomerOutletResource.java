package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.UfsCustomerOutlet;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;

@RestController
@RequestMapping(value = "/customer-outlet")
public class UfsCustomerOutletResource extends ChasisResource<UfsCustomerOutlet,Long, UfsEdittedRecord> {


    public UfsCustomerOutletResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
