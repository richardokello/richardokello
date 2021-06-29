package co.ke.tracom.bprgateway.web.transactions.repository;

import co.ke.tracom.bprgateway.web.transactions.entities.TransactionAdvices;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionAdvicesRepository extends CrudRepository<TransactionAdvices, Long> {
    Optional<TransactionAdvices> findByAdvisedAndTrialsLessThanAndTransactionTypeAndRequestTypeAndTransactionReference(
String advised, int trials,String transType,String reqType, String refereneNo
    );
}
