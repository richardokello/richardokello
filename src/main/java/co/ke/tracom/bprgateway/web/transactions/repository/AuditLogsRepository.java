package co.ke.tracom.bprgateway.web.transactions.repository;

import co.ke.tracom.bprgateway.web.transactions.entities.BprMlkAuditLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
@Repository
public interface AuditLogsRepository extends JpaRepository<BprMlkAuditLogs, BigDecimal> {
    @Query(value = "SELECT LOG_ID from MLK_AUDIT_LOG ORDER BY LOG_ID DESC FETCH FIRST 1 ROW ONLY", nativeQuery = true)
    String findLast();
}
