package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.UfsCurrency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


@Repository
public interface UfsCurrencyRepository extends CrudRepository<UfsCurrency, BigDecimal> {

    public UfsCurrency findByCurrencyId(BigDecimal id);
}
