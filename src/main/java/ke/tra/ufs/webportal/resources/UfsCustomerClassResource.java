package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.UfsCustomerClass;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;


@Controller
@RequestMapping("/customer-class")
public class UfsCustomerClassResource extends ChasisResource<UfsCustomerClass, Long, UfsEdittedRecord> {

    public UfsCustomerClassResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
