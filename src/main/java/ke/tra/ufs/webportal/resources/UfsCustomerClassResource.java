package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsCustomerClass;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.UfsTieredCommissionAmount;
import ke.tra.ufs.webportal.repository.UfsTieredCommissionAmountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.persistence.EntityManager;
import javax.validation.Valid;


@Controller
@RequestMapping("/customer-class")
public class UfsCustomerClassResource extends ChasisResource<UfsCustomerClass, Long, UfsEdittedRecord> {



    public UfsCustomerClassResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }


}
