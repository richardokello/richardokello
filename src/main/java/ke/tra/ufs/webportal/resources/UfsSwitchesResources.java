package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.UfsSwitches;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;


@RestController
@RequestMapping("/switch")
public class UfsSwitchesResources extends ChasisResource<UfsSwitches,Long, UfsEdittedRecord> {

    public UfsSwitchesResources(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
