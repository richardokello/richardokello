package co.ke.tracom.bprgateway.web.transactions.repository;

import co.ke.tracom.bprgateway.web.transactions.entities.AllTransactions;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllTransactionsRepository extends CrudRepository<AllTransactions, Long> {
}
