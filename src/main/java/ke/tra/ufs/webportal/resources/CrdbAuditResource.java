package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.tra.ufs.webportal.entities.CRDBBILLERS_AUDIT;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/crdb-audit")
public class CrdbAuditResource extends ChasisResource<CRDBBILLERS_AUDIT, BigDecimal, UfsEdittedRecord> {

    public CrdbAuditResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
