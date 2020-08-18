package ke.tra.com.tsync.repository;

import ke.tra.com.tsync.entities.UfsCurrency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface UfsCurrencyRepository extends CrudRepository<UfsCurrency, BigDecimal> {
}
