package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsCountries;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


@Repository
public interface UfsCountryRepository extends CrudRepository<UfsCountries, BigDecimal> {

    public UfsCountries findByIdAndIntrash(BigDecimal id, String intrash);
}
