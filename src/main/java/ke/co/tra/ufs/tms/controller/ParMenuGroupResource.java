package ke.co.tra.ufs.tms.controller;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.co.tra.ufs.tms.entities.ParMenuGroup;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

@Controller
@RequestMapping("/menu-groups")
public class ParMenuGroupResource extends ChasisResource<ParMenuGroup, BigDecimal, UfsEdittedRecord> {
    public ParMenuGroupResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
