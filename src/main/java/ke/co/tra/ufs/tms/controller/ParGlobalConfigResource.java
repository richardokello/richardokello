package ke.co.tra.ufs.tms.controller;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.co.tra.ufs.tms.entities.ParGlobalConfig;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

@RestController
@RequestMapping("global-configs")
public class ParGlobalConfigResource extends ChasisResource<ParGlobalConfig, BigDecimal, UfsEdittedRecord> {
    public ParGlobalConfigResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
