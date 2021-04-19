package ke.co.tra.ufs.tms.controller;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.utils.SharedMethods;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.co.tra.ufs.tms.entities.ParGlobalConfigProfile;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import ke.co.tra.ufs.tms.entities.wrappers.GlobalConfigFileRequest;
import ke.co.tra.ufs.tms.entities.wrappers.GlobalProfileRequest;
import ke.co.tra.ufs.tms.repository.ParGlobalConfigProfileRepository;
import ke.co.tra.ufs.tms.service.ParFileConfigService;
import ke.co.tra.ufs.tms.service.ParGlobalConfigProfileService;
import ke.co.tra.ufs.tms.utils.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;

@RequestMapping("global-config-profiles")
@RestController
public class ParGlobalConfigProfileResource extends ChasisResource<ParGlobalConfigProfile, BigDecimal, UfsEdittedRecord> {

    private final ParGlobalConfigProfileService parGlobalConfigProfileService;
    private final ParFileConfigService parFileConfigService;
    private final LoggerService loggerService;
    private final ParGlobalConfigProfileRepository parGlobalConfigProfileRepository;

    public ParGlobalConfigProfileResource(LoggerService loggerService, EntityManager entityManager, ParGlobalConfigProfileService parGlobalConfigProfileService, ParFileConfigService parFileConfigService, ParGlobalConfigProfileRepository parGlobalConfigProfileRepository) {
        super(loggerService, entityManager);
        this.parGlobalConfigProfileService = parGlobalConfigProfileService;
        this.parFileConfigService = parFileConfigService;
        this.loggerService = loggerService;
        this.parGlobalConfigProfileRepository = parGlobalConfigProfileRepository;
    }

    @Override
    public ResponseEntity<ResponseWrapper<ParGlobalConfigProfile>> create(@Valid @RequestBody ParGlobalConfigProfile parGlobalConfigProfile) {
        ResponseWrapper<ParGlobalConfigProfile> wrapper = new ResponseWrapper<>();
        wrapper.setCode(HttpStatus.NOT_ACCEPTABLE.value());
        wrapper.setMessage("Process not allowed");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(wrapper);
    }


    @Transactional
    @PostMapping("/config")
    public ResponseEntity<ResponseWrapper<ParGlobalConfigProfile>> createProfileWithConfigs(@Valid @RequestBody GlobalProfileRequest request) {
        ParGlobalConfigProfile profile = parGlobalConfigProfileService.save(request);
        loggerService.log("Created Config Profile successfully ",
                ParGlobalConfigProfile.class.getSimpleName(), SharedMethods.getEntityIdValue(profile),
                AppConstants.ACTIVITY_CREATE, AppConstants.STATUS_COMPLETED, "");
        ResponseWrapper<ParGlobalConfigProfile> wrapper = new ResponseWrapper<>();
        wrapper.setData(profile);
        return ResponseEntity.status(HttpStatus.CREATED).body(wrapper);
    }

    @Transactional
    @PutMapping("/config")
    public ResponseEntity<ResponseWrapper<ParGlobalConfigProfile>> updateProfileWithConfigs(@Valid @RequestBody GlobalProfileRequest request) throws NotFoundException {
        Optional<ParGlobalConfigProfile> savedProfile = parGlobalConfigProfileRepository.findById(request.getId());
        if (!savedProfile.isPresent()) {
            throw new NotFoundException("Profile with selected ID not found");
        }
        if (savedProfile.get().getActionStatus().equals(AppConstants.STATUS_UNAPPROVED)) {
            throw new NotFoundException("Record has unapproved action");
        }

        ParGlobalConfigProfile profile = parGlobalConfigProfileService.update(request);
        loggerService.log("Updated Config Profile successfully ",
                ParGlobalConfigProfile.class.getSimpleName(), SharedMethods.getEntityIdValue(profile),
                AppConstants.ACTIVITY_UPDATE, AppConstants.STATUS_COMPLETED, "");
        ResponseWrapper<ParGlobalConfigProfile> wrapper = new ResponseWrapper<>();
        wrapper.setData(profile);
        return ResponseEntity.status(HttpStatus.CREATED).body(wrapper);
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/generate")
    public ResponseEntity<ResponseWrapper> generateMenuFile(@Valid @RequestBody GlobalConfigFileRequest fileRequest) {
        ResponseWrapper<Object> wrapper = new ResponseWrapper<>();
        // todo remove endpoint
        //parFileConfigService.generateGlobalConfigFileAsync(fileRequest, "/home/kenn/");
        return ResponseEntity.ok(wrapper);
    }
}
