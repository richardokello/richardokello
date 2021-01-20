package ke.co.tra.ufs.tms.controller;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.co.tra.ufs.tms.entities.ParDeviceSelectedOptions;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

@RestController
@RequestMapping("/par-selected-device-options")
public class ParDeviceSelectedOptionsResource extends ChasisResource<ParDeviceSelectedOptions, BigDecimal, UfsEdittedRecord> {
    public ParDeviceSelectedOptionsResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
