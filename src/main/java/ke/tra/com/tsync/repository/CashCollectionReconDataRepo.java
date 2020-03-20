package ke.tra.com.tsync.repository;

import ke.tra.com.tsync.entities.CashCollectionRecon;
import ke.tra.com.tsync.entities.TransactionTypes;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface CashCollectionReconDataRepo extends JpaRepository<CashCollectionRecon, BigDecimal>  {
    Optional<CashCollectionRecon> findFirstByPaymentNarrationLike(String paymentNarration)  throws JDBCConnectionException;
}


