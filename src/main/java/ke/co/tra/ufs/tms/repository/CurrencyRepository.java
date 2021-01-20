package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import ke.co.tra.ufs.tms.entities.UfsCurrency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Owori Juma
 */
public interface CurrencyRepository extends CrudRepository<UfsCurrency, BigDecimal> {

    /**
     *
     * @param actionStatus
     * @param needle
     * @param intrash
     * @param pg
     * @return
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.actionStatus LIKE ?1%"
            + " AND (u.name LIKE %?2% OR u.code LIKE %?2%) AND lower(u.intrash) = lower(?3)")
    Page<UfsCurrency> findAll(String actionStatus, String needle, String intrash, Pageable pg);

    /**
     *
     * @param code
     * @return
     */
    public UfsCurrency findByCode(String code);

    /**
     *
     * @param intrash
     * @param pg
     * @return
     */
    public Page<UfsCurrency> findByintrash(String intrash, Pageable pg);

}
