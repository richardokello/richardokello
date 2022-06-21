package ke.tracom.ufs.services.template;


import ke.tracom.ufs.entities.UfsAuditLog;
import ke.tracom.ufs.repositories.UfsAuditLogRepository;
import ke.tracom.ufs.services.AuditLogService;
import ke.tracom.ufs.utils.AppConstants;
import ke.tracom.ufs.wrappers.LogExtras;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class AuditLogServiceTemplate implements AuditLogService {

    private final UfsAuditLogRepository auditLogRepository;
    private final LogExtras logExtras;

    public AuditLogServiceTemplate(UfsAuditLogRepository auditLogRepository, LogExtras logExtras) {
        this.auditLogRepository = auditLogRepository;
        this.logExtras = logExtras;
    }

    @Override
    @Transactional
    public Page<UfsAuditLog> findByActivityTypeAndStatusNot(String activityType, String status, Pageable pg) {
        return auditLogRepository.findByActivityTypeAndStatusNot(activityType, status, pg);
    }

    @Override
    public List<UfsAuditLog> findByUserIdAndIpAndSource(String userId, String status, String activityType, String source, String ipAddress) {
        return auditLogRepository.findByUserIdAndIpAndSource(userId, status, activityType, source, ipAddress);

    }

    @Override
    public UfsAuditLog createLog(UfsAuditLog log) {
        System.out.println(">>>> Saving new log >>>>>");
        log.setIpAddress(logExtras.getIpAddress());
        log.setIntrash(AppConstants.NO);
        log.setSource(logExtras.getSource());
        log.setNotes(log.getDescription());
        log.setClientId(logExtras.getClientId());
        return auditLogRepository.save(log);
    }
}
