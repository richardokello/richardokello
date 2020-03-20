package ke.tra.com.tsync.repository;

import ke.tra.com.tsync.entities.CashCollectionPreauth;
import ke.tra.com.tsync.entities.CashCollectionRecon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface CashCollectionPreauthRepo extends JpaRepository<CashCollectionPreauth, BigDecimal> {
    Optional<CashCollectionPreauth> findDistinctFirstByCashcollectcode(String cashCollectCode);
}
