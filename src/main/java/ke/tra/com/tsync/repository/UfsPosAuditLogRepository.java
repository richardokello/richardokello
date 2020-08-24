package ke.tra.com.tsync.repository;

import ke.tra.com.tsync.entities.UfsPosAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface UfsPosAuditLogRepository extends JpaRepository<UfsPosAuditLog, BigDecimal> {
}
