package ke.co.tra.ufs.tms.controller;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.co.tra.ufs.tms.entities.ParBinProfile;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

@Controller
@RequestMapping("/bin-profile")
public class ParBinProfileResource extends ChasisResource<ParBinProfile, BigDecimal, UfsEdittedRecord> {

    public ParBinProfileResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
