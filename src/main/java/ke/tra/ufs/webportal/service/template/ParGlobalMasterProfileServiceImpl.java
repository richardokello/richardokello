package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.ParGlobalMasterChildProfile;
import ke.tra.ufs.webportal.entities.ParGlobalMasterProfile;
import ke.tra.ufs.webportal.repository.ParGlobalMasterChildProfileRepository;
import ke.tra.ufs.webportal.repository.ParGlobalMasterProfileRepository;
import ke.tra.ufs.webportal.service.ParGlobalMasterProfileService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParGlobalMasterProfileServiceImpl implements ParGlobalMasterProfileService {

    private final ParGlobalMasterChildProfileRepository parGlobalMasterChildProfileRepository;
    private final ParGlobalMasterProfileRepository parGlobalMasterProfileRepository;

    public ParGlobalMasterProfileServiceImpl(ParGlobalMasterChildProfileRepository parGlobalMasterChildProfileRepository, ParGlobalMasterProfileRepository parGlobalMasterProfileRepository) {
        this.parGlobalMasterChildProfileRepository = parGlobalMasterChildProfileRepository;
        this.parGlobalMasterProfileRepository = parGlobalMasterProfileRepository;
    }

    @Override
    public void saveAllChildConfigs(List<BigDecimal> configProfiles, final BigDecimal masterProfile) {
        List<ParGlobalMasterChildProfile> profiles = configProfiles.stream()
                .map(configId -> {
                    ParGlobalMasterChildProfile childProfile = new ParGlobalMasterChildProfile();
                    childProfile.setMasterProfileId(masterProfile);
                    childProfile.setConfigProfileId(configId);
                    return childProfile;
                })
                .collect(Collectors.toList());

        parGlobalMasterChildProfileRepository.saveAll(profiles);
    }

    @Override
    public Optional<ParGlobalMasterProfile> findById(BigDecimal id) {
        return parGlobalMasterProfileRepository.findById(id);
    }

    @Override
    public List<ParGlobalMasterChildProfile> findAllChildConfigsByMasterProfile(BigDecimal id) {
        return parGlobalMasterChildProfileRepository.findAllByMasterProfileId(id);
    }

    @Override
    public List<ParGlobalMasterChildProfile> findAllByMasterIdAndAndChildIdIn(BigDecimal id, List<BigDecimal> toDelete) {
        return parGlobalMasterChildProfileRepository.findAllByMasterProfileIdAndIdIn(id, toDelete);
    }

    @Override
    public void deleteAllChildConfigs(List<ParGlobalMasterChildProfile> childProfiles) {
        parGlobalMasterChildProfileRepository.deleteAll(childProfiles);
    }
}
