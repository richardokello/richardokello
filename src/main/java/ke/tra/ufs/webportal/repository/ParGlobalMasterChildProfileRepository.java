package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.ParGlobalMasterChildProfile;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ParGlobalMasterChildProfileRepository extends CrudRepository<ParGlobalMasterChildProfile, BigDecimal> {
    List<ParGlobalMasterChildProfile> findAllByMasterProfileId(BigDecimal id);

    List<ParGlobalMasterChildProfile> findAllByMasterProfileIdAndIdIn(BigDecimal id, List<BigDecimal> ids);
}
