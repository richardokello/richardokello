package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.TidMidCurrency;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TidMidCurrencyRepository extends CrudRepository<TidMidCurrency, Long> {

    @Query("SELECT t from TidMidCurrency t where t.tidMidIds = ?1 AND t.currencyIds <>?2")
    List<TidMidCurrency> findAllByMidAndCurrencyIdsIsNot(Long tidMidIds,BigDecimal currencyIds);

    @Query("SELECT t from TidMidCurrency t where t.tidMidIds = ?1")
    List<TidMidCurrency> findAllByTidMidIds(Long tidMidIds);
}
