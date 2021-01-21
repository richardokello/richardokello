package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsCurrency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


@Repository
public interface UfsCurrencyRepository extends CrudRepository<UfsCurrency, BigDecimal> {

    public UfsCurrency findByIdAndIntrash(BigDecimal id,String intrash);

    UfsCurrency findByNameAndIntrash(String name,String intrash);
}
