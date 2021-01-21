package ke.tracom.ufs.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tracom.ufs.entities.UfsAuthentication;
import ke.tracom.ufs.entities.UfsEdittedRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;


@RestController
@RequestMapping("/authentication")
public class UfsAuthenticationResource extends ChasisResource<UfsAuthentication,Long, UfsEdittedRecord> {

    public UfsAuthenticationResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
