package ke.tra.com.tsync.repository;

import ke.tra.com.tsync.entities.FailedTransactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface FailedTransactionRepository extends JpaRepository<FailedTransactions, BigDecimal> {
}
