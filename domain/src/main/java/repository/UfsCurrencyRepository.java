package repository;

import entities.UfsCurrency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface UfsCurrencyRepository extends CrudRepository<UfsCurrency, BigDecimal> {
}
