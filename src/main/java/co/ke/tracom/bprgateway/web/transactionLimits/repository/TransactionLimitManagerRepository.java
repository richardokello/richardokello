package co.ke.tracom.bprgateway.web.transactionLimits.repository;

import co.ke.tracom.bprgateway.web.transactionLimits.entity.TransactionLimitManager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionLimitManagerRepository
    extends CrudRepository<TransactionLimitManager, Long> {
  Optional<TransactionLimitManager> findByISOMsgMTIAndProcessingCode(String ISOMTI, String ISOMsgProcessingCode);
}
