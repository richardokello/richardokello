package ke.tra.com.tsync.repository;

import ke.tra.com.tsync.entities.UfsPosAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface UfsAuditLogRepository extends JpaRepository<UfsPosAuditLog, BigDecimal> {
}
