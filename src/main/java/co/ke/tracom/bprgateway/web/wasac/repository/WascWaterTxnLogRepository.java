package co.ke.tracom.bprgateway.web.wasac.repository;

import co.ke.tracom.bprgateway.web.wasac.data.WascWaterTxnLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WascWaterTxnLogRepository extends CrudRepository<WascWaterTxnLog, Long> {
}
