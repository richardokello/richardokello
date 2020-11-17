package repository;

import entities.CashCollectionRecon;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface CashCollectionReconDataRepo extends JpaRepository<CashCollectionRecon, BigDecimal>  {
    Optional<CashCollectionRecon> findFirstByPaymentNarrationLike(String paymentNarration)  throws JDBCConnectionException;
}


