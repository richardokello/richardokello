package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface UfsAuditLogRepository extends CrudRepository<UfsAuditLog, BigDecimal> {

    Page<UfsAuditLog> findByActivityTypeAndStatusNot(String activityType, String status, Pageable pg);
}
