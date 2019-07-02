package ke.tra.ufs.webportal.resources;


import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import ke.tra.ufs.webportal.entities.UfsCustomerTransfer;

@RestController
@RequestMapping(value = "/customer-transfer")
public class UfsCustomerTransferResource extends ChasisResource<UfsCustomerTransfer, Long, UfsEdittedRecord> {

    public UfsCustomerTransferResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
