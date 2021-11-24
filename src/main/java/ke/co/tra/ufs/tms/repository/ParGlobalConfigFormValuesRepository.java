package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParGlobalConfigFormValues;
import ke.co.tra.ufs.tms.entities.ParMenuItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

public interface ParGlobalConfigFormValuesRepository extends CrudRepository<ParGlobalConfigFormValues, BigDecimal> {
    Optional<ParGlobalConfigFormValues> findDistinctByTypeId(BigDecimal decimal);

    @Query("SELECT u FROM #{#entityName} u WHERE u.actionStatus like ?1% "
            + "AND STR(COALESCE(u.typeId, -1)) LIKE ?2% "
            + "AND u.dateCreated BETWEEN ?3 AND ?4 "
            + "AND (u.name LIKE %?5% OR u.description LIKE %?5% ) AND lower(u.intrash) = lower(?6) ")
    Page<ParGlobalConfigFormValues> findAllConfigFormsByConfigType(String actionStatus, String customerTypeId, Date from, Date to, String toLowerCase, String no, Pageable pg);
}
