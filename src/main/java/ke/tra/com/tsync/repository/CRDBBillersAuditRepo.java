package ke.tra.com.tsync.repository;

import ke.tra.com.tsync.entities.CRDBBILLERS_AUDIT;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface CRDBBillersAuditRepo extends JpaRepository<CRDBBILLERS_AUDIT, BigDecimal> {
}
