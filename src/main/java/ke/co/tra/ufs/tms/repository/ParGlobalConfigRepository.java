package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParGlobalConfig;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ParGlobalConfigRepository extends CrudRepository<ParGlobalConfig, BigDecimal> {
    List<ParGlobalConfig> findAllByProfileId(BigDecimal profileId);
    List<ParGlobalConfig> deleteAllByProfileId(BigDecimal profileId);
}
