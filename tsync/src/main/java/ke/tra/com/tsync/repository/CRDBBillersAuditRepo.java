package ke.tra.com.tsync.repository;

import ke.tra.com.tsync.entities.CRDBBILLERS_AUDIT;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;


public interface CRDBBillersAuditRepo extends JpaRepository<CRDBBILLERS_AUDIT, BigDecimal> {
    List<CRDBBILLERS_AUDIT> findByHttpcodeIsNullAndRequestNameAndRequestDirectionAndIsOrginalRequestAndSwitchAgentCodeIsNotNull(
            String requestName, String requestDirection, Integer isOriginalRequest
    );
}
