package ke.tra.com.tsync.repository;

import ke.tra.com.tsync.entities.UfsAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface UfsAuditLogRepository extends JpaRepository<UfsAuditLog, BigDecimal> {
}
