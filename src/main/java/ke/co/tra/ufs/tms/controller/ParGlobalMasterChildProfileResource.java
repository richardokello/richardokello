package ke.co.tra.ufs.tms.controller;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.co.tra.ufs.tms.entities.ParGlobalMasterChildProfile;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.math.BigDecimal;


@RestController
@RequestMapping("/par-master-child-profiles")
public class ParGlobalMasterChildProfileResource extends ChasisResource<ParGlobalMasterChildProfile, BigDecimal, UfsEdittedRecord> {
    public ParGlobalMasterChildProfileResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
