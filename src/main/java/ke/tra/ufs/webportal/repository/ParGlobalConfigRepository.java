package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.ParGlobalConfig;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ParGlobalConfigRepository extends CrudRepository<ParGlobalConfig, BigDecimal> {
    List<ParGlobalConfig> findAllByProfileId(BigDecimal profileId);
    List<ParGlobalConfig> deleteAllByProfileId(BigDecimal profileId);
}
