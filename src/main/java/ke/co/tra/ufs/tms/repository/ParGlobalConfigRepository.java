package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParGlobalConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ParGlobalConfigRepository extends CrudRepository<ParGlobalConfig, BigDecimal> {
    List<ParGlobalConfig> findAllByProfileId(BigDecimal profileId);
    List<ParGlobalConfig> deleteAllByProfileId(BigDecimal profileId);


    @Query("SELECT u FROM #{#entityName} u WHERE u.actionStatus like ?1% "
            + "AND STR(COALESCE(u.profileId, -1)) LIKE ?2% "
            + "AND u.dateCreated BETWEEN ?3 AND ?4 "
            + "AND (u.paramName LIKE %?5% OR u.paramValue LIKE %?5%  OR u.description LIKE %?5% ) AND lower(u.intrash) = lower(?6) ")
    Page<ParGlobalConfig> findAllConfigsByProfile(String actionStatus, String profile, Date from, Date to, String needle, String intrash, Pageable pg);
}
