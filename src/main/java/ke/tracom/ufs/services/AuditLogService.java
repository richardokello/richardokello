package ke.tracom.ufs.services;

import ke.tracom.ufs.entities.UfsAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuditLogService {

    public Page<UfsAuditLog> findByActivityTypeAndStatusNot(String activityType, String status, Pageable pg);
    List<UfsAuditLog> findByUserIdAndIpAndSource(String userId, String status, String activityType, String source, String ipAddress);

}
