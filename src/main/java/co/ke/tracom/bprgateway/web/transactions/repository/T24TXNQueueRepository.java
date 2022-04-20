package co.ke.tracom.bprgateway.web.transactions.repository;

import co.ke.tracom.bprgateway.web.transactions.entities.T24TXNQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface T24TXNQueueRepository extends JpaRepository<T24TXNQueue, Long> {
}
