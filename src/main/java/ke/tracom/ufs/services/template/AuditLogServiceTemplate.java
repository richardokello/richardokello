package ke.tracom.ufs.services.template;


import ke.tracom.ufs.entities.UfsAuditLog;
import ke.tracom.ufs.repositories.UfsAuditLogRepository;
import ke.tracom.ufs.services.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class AuditLogServiceTemplate implements AuditLogService {

    private final UfsAuditLogRepository auditLogRepository;

    public AuditLogServiceTemplate(UfsAuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    @Transactional
    public Page<UfsAuditLog> findByActivityTypeAndStatusNot(String activityType, String status,Pageable pg) {
        return auditLogRepository.findByActivityTypeAndStatusNot(activityType,status,pg);
    }
}
