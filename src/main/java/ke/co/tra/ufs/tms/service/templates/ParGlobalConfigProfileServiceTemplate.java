package ke.co.tra.ufs.tms.service.templates;

import ke.axle.chassis.utils.AppConstants;
import ke.co.tra.ufs.tms.entities.ParGlobalConfig;
import ke.co.tra.ufs.tms.entities.ParGlobalConfigProfile;
import ke.co.tra.ufs.tms.entities.wrappers.GlobalProfileRequest;
import ke.co.tra.ufs.tms.repository.ParGlobalConfigProfileRepository;
import ke.co.tra.ufs.tms.repository.ParGlobalConfigRepository;
import ke.co.tra.ufs.tms.service.ParGlobalConfigProfileService;
import ke.co.tra.ufs.tms.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParGlobalConfigProfileServiceTemplate implements ParGlobalConfigProfileService {
    private final ParGlobalConfigProfileRepository parGlobalConfigProfileRepository;
    private final ParGlobalConfigRepository parGlobalConfigRepository;


    public ParGlobalConfigProfileServiceTemplate(ParGlobalConfigProfileRepository parGlobalConfigProfileRepository,
                                                 ParGlobalConfigRepository parGlobalConfigRepository) {
        this.parGlobalConfigProfileRepository = parGlobalConfigProfileRepository;
        this.parGlobalConfigRepository = parGlobalConfigRepository;
    }

    @Override
    public ParGlobalConfigProfile save(GlobalProfileRequest request) {

        ParGlobalConfigProfile profile = new ParGlobalConfigProfile();
        profile.setName(request.getName());
        profile.setFileTypeId(request.getFileTypeId());
        profile.setDescription(request.getDescription());

        ParGlobalConfigProfile savedProfile = parGlobalConfigProfileRepository.save(profile);

        List<ParGlobalConfig> updated = request.getConfigs().stream()
                .peek(config -> config.setProfileId(savedProfile.getId()))
                .collect(Collectors.toList());
        parGlobalConfigRepository.saveAll(updated);
        return savedProfile;
    }

    @Override
    public ParGlobalConfigProfile update(GlobalProfileRequest request) throws NotFoundException {
        Optional<ParGlobalConfigProfile> savedProfile = parGlobalConfigProfileRepository.findById(request.getId());
        if (!savedProfile.isPresent()) throw new NotFoundException("Profile with selected ID not found");

        ParGlobalConfigProfile profile = savedProfile.get();
        profile.setName(request.getName());
        profile.setFileTypeId(request.getFileTypeId());
        profile.setDescription(request.getDescription());
        profile.setAction(AppConstants.ACTIVITY_UPDATE);
        profile.setActionStatus(AppConstants.STATUS_UNAPPROVED);

        parGlobalConfigRepository.deleteAllByProfileId(profile.getId());

        List<ParGlobalConfig> updated = request.getConfigs().stream()
                .peek(config -> config.setProfileId(profile.getId()))
                .collect(Collectors.toList());
        parGlobalConfigRepository.saveAll(updated);
        return profile;
    }
}
