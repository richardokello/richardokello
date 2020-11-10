package repository;

import entities.CashCollectionPreauth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface CashCollectionPreauthRepo extends JpaRepository<CashCollectionPreauth, BigDecimal> {
    Optional<CashCollectionPreauth> findDistinctFirstByCashcollectcode(String cashCollectCode);
}
