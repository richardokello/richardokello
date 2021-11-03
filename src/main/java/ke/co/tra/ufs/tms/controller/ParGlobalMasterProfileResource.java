package ke.co.tra.ufs.tms.controller;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.exceptions.ExpectationFailed;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ActionWrapper;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.co.tra.ufs.tms.entities.ParGlobalMasterChildProfile;
import ke.co.tra.ufs.tms.entities.ParGlobalMasterProfile;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import ke.co.tra.ufs.tms.service.ParGlobalMasterProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/par-master-profiles")
public class ParGlobalMasterProfileResource extends ChasisResource<ParGlobalMasterProfile, BigDecimal, UfsEdittedRecord> {

    private final ParGlobalMasterProfileService parGlobalMasterProfileService;

    public ParGlobalMasterProfileResource(LoggerService loggerService, EntityManager entityManager, ParGlobalMasterProfileService parGlobalMasterProfileService) {
        super(loggerService, entityManager);
        this.parGlobalMasterProfileService = parGlobalMasterProfileService;
    }

    @Override
    public ResponseEntity<ResponseWrapper<ParGlobalMasterProfile>> create(@Valid @RequestBody ParGlobalMasterProfile parGlobalMasterProfile) {
        ResponseEntity<ResponseWrapper<ParGlobalMasterProfile>> response = super.create(parGlobalMasterProfile);
        BigDecimal id = Objects.requireNonNull(response.getBody()).getData().getId();

        // save profile children
        if (parGlobalMasterProfile.getChildProfileIds().size() > 0) {
            parGlobalMasterProfileService.saveAllChildConfigs(parGlobalMasterProfile.getChildProfileIds(), id);
        }
        return response;
    }


    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper> approveActions(@Valid @RequestBody ActionWrapper<BigDecimal> actions) throws ExpectationFailed {
        ResponseEntity<ResponseWrapper> resp = super.approveActions(actions);

        if (!resp.getStatusCode().equals(HttpStatus.OK)) {
            return resp;
        }

        for (BigDecimal id : actions.getIds()) {
            Optional<ParGlobalMasterProfile> t = parGlobalMasterProfileService.findById(id);
            if (t.isPresent()) {
                if (t.get().getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE) &&
                        t.get().getActionStatus().equalsIgnoreCase(AppConstants.STATUS_APPROVED)) {
                    try {
                        // get child profile from edited record
                        ParGlobalMasterProfile entity = supportRepo.mergeChanges(id, t.get());
                        List<BigDecimal> existingIds = new ArrayList<>();
                        List<BigDecimal> newIds = entity.getChildProfileIds();

                        parGlobalMasterProfileService.findAllChildConfigsByMasterProfile(id).forEach(item -> {
                            existingIds.add(item.getId());
                        });

                        List<BigDecimal> isPresent = new ArrayList<>();
                        List<BigDecimal> toDelete = new ArrayList<>();
                        List<BigDecimal> toCreate = new ArrayList<>();

                        if (existingIds != null) {
                            existingIds.forEach(item -> {
                                if (Objects.nonNull(newIds) && newIds.contains(item)) {
                                    isPresent.add(item);
                                } else {
                                    toDelete.add(item);
                                }
                            });
                        }

                        if (newIds != null) {

                            newIds.forEach(item -> {
                                if (!isPresent.contains(item) && !toDelete.contains(item)) {
                                    toCreate.add(item);
                                }
                            });
                        }

                        //delete the roleWorkgroups if the toDelete array is not null
                        if (!toDelete.isEmpty()) {
                            List<ParGlobalMasterChildProfile> waitingDeletion = parGlobalMasterProfileService.findAllByMasterIdAndAndChildIdIn(id, toDelete);
                            parGlobalMasterProfileService.deleteAllChildConfigs(waitingDeletion);
                        }

                        //create the roleWorkgroups if the toCreate array is not null
                        if (!toCreate.isEmpty()) {
                            parGlobalMasterProfileService.saveAllChildConfigs(toCreate, id);
                        }

                    } catch (IOException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return resp;
    }
}
