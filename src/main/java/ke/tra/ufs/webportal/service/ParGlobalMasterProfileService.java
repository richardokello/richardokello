package ke.tra.ufs.webportal.service;


import ke.tra.ufs.webportal.entities.ParGlobalMasterChildProfile;
import ke.tra.ufs.webportal.entities.ParGlobalMasterProfile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ParGlobalMasterProfileService {
    void saveAllChildConfigs(List<BigDecimal> configProfiles, BigDecimal masterProfile);

    Optional<ParGlobalMasterProfile> findById(BigDecimal id);

    List<ParGlobalMasterChildProfile> findAllChildConfigsByMasterProfile(BigDecimal id);

    List<ParGlobalMasterChildProfile> findAllByMasterIdAndAndChildIdIn(BigDecimal id, List<BigDecimal> toDelete);

    void deleteAllChildConfigs(List<ParGlobalMasterChildProfile> childProfiles);
}
