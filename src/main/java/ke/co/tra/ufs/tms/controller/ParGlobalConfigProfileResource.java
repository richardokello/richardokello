package ke.co.tra.ufs.tms.controller;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.co.tra.ufs.tms.entities.ParGlobalConfigProfile;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import ke.co.tra.ufs.tms.entities.wrappers.GlobalConfigFileRequest;
import ke.co.tra.ufs.tms.entities.wrappers.GlobalProfileRequest;
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

@RequestMapping("global-config-profiles")
@RestController
public class ParGlobalConfigProfileResource extends ChasisResource<ParGlobalConfigProfile, BigDecimal, UfsEdittedRecord> {

    private final ParGlobalConfigProfileService parGlobalConfigProfileService;
    private final ParFileConfigService parFileConfigService;

    public ParGlobalConfigProfileResource(LoggerService loggerService, EntityManager entityManager, ParGlobalConfigProfileService parGlobalConfigProfileService, ParFileConfigService parFileConfigService) {
        super(loggerService, entityManager);
        this.parGlobalConfigProfileService = parGlobalConfigProfileService;
        this.parFileConfigService = parFileConfigService;
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
        ResponseWrapper<ParGlobalConfigProfile> wrapper = new ResponseWrapper<>();
        wrapper.setData(profile);
        return ResponseEntity.status(HttpStatus.CREATED).body(wrapper);
    }

    @Transactional
    @PutMapping("/config")
    public ResponseEntity<ResponseWrapper<ParGlobalConfigProfile>> updateProfileWithConfigs(@Valid @RequestBody GlobalProfileRequest request) throws NotFoundException {
        ParGlobalConfigProfile profile = parGlobalConfigProfileService.update(request);
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
