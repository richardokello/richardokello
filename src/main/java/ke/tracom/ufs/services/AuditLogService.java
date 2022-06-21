package ke.tracom.ufs.services;

import ke.tracom.ufs.entities.UfsAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuditLogService {

    public Page<UfsAuditLog> findByActivityTypeAndStatusNot(String activityType, String status, Pageable pg);
    List<UfsAuditLog> findByUserIdAndIpAndSource(String userId, String status, String activityType, String source, String ipAddress);

    // TODO - should be removed to ensure that audit logs are saved from logger service
    // this is method has been introduced until a better solution is implemented for creating audit log on logout - there is a race condition when one
    // logouts and token is cleared and logger service has to check the same token
    UfsAuditLog createLog(UfsAuditLog log);

}
