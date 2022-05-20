package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface UfsAuditLogRepository extends CrudRepository<UfsAuditLog, BigDecimal> {

    Page<UfsAuditLog> findByActivityTypeAndStatusNot(String activityType, String status, Pageable pg);

    @Query("SELECT u FROM #{#entityName} u WHERE str(coalesce(u.userId.userId, -1)) = ?1 AND "
            + "lower(u.status) = ?2 AND lower(u.activityType) = ?3 AND  lower(u.source) = ?4 OR lower(u.ipAddress) = ?5 "
            + "AND ROWNUM <2 ORDER BY u.occurenceTime ")
    List<UfsAuditLog> findByUserIdAndIpAndSource(String userId, String status, String activityType, String source, String ipAddress);
}
