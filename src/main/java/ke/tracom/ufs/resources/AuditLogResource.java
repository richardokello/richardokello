package ke.tracom.ufs.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tracom.ufs.entities.UfsAuditLog;
import ke.tracom.ufs.entities.UfsEdittedRecord;
import ke.tracom.ufs.repositories.UfsAuditLogRepository;
import ke.tracom.ufs.services.AuditLogService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

@Controller
@RequestMapping(value = "/audit-log")
public class AuditLogResource  extends ChasisResource<UfsAuditLog, BigDecimal, UfsEdittedRecord> {

    private final AuditLogService auditLogService;

    public AuditLogResource(LoggerService loggerService, EntityManager entityManager,AuditLogService auditLogService) {
        super(loggerService, entityManager);
        this.auditLogService = auditLogService;
    }

    @GetMapping("/unsuccessful-logins")
    public ResponseEntity<ResponseWrapper<UfsAuditLog>> unsuccessfulLogins(Pageable pg){
        ResponseWrapper response = new ResponseWrapper();
        response.setMessage("Request was successful");
        response.setData(auditLogService.findByActivityTypeAndStatusNot(AppConstants.ACTIVITY_AUTHENTICATION,AppConstants.STATUS_COMPLETED,pg));
        response.setCode(200);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
