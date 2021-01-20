package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParGlobalMasterChildProfile;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ParGlobalMasterChildProfileRepository extends CrudRepository<ParGlobalMasterChildProfile, BigDecimal> {
    List<ParGlobalMasterChildProfile> findAllByMasterProfileId(BigDecimal id);

    List<ParGlobalMasterChildProfile> findAllByMasterProfileIdAndIdIn(BigDecimal id, List<BigDecimal> ids);
}
