package ke.tracom.ufs.services.template;

import ke.axle.chassis.utils.LoggerService;
import ke.tracom.ufs.entities.*;
import ke.tracom.ufs.repositories.UfsGeographicalRegionRepository;
import ke.tracom.ufs.repositories.UfsOrganizationHierarchyRepository;
import ke.tracom.ufs.repositories.UfsOrganizationUnitsRepository;
import ke.tracom.ufs.repositories.UfsRegionsBatchRepository;
import ke.tracom.ufs.services.OrganizationService;
import ke.tracom.ufs.services.SysConfigService;
import ke.tracom.ufs.utils.AppConstants;
import ke.tracom.ufs.utils.RegionDetails;
import ke.tracom.ufs.utils.SharedMethods;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.Validator;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;

@Transactional
@Service
public class OrganizationServiceTemplate implements OrganizationService {
    private final UfsOrganizationHierarchyRepository repository;
    private final UfsRegionsBatchRepository batchRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final Validator validator;
    private final UfsGeographicalRegionRepository geographicalRegionRepository;
    private final UfsOrganizationUnitsRepository organizationUnitsRepository;

    public OrganizationServiceTemplate(UfsOrganizationHierarchyRepository repository, UfsRegionsBatchRepository batchRepository, Validator validator, UfsGeographicalRegionRepository geographicalRegionRepository, UfsOrganizationUnitsRepository organizationUnitsRepository) {
        this.repository = repository;
        this.batchRepository = batchRepository;
        this.validator = validator;
        this.geographicalRegionRepository = geographicalRegionRepository;
        this.organizationUnitsRepository = organizationUnitsRepository;
    }

    @Override
    public UfsOrganizationHierarchy findByIsRootTenantAndIntrash() {
        return repository.findByIsRootTenantAndIntrash((short) 1, AppConstants.INTRASH_NO);
    }

    @Override
    public UfsRegionsBatch saveBatch(UfsRegionsBatch batch) {
        return batchRepository.save(batch);
    }

    @Override
    @Async
    public void processRegionsUploadCsv(UfsRegionsBatch batch, SysConfigService configService, SharedMethods sharedMethods, LoggerService loggerService, UfsUser user, UfsGeographicalRegion ufsGeographicalRegion) {
        try {
            List<RegionDetails> entities = sharedMethods.convertCsv(RegionDetails.class, ufsGeographicalRegion.getFile().getBytes());
            SaveUploads(batch, loggerService, user, ufsGeographicalRegion, entities);
        } catch (IOException ex) {
            log.error(AppConstants.AUDIT_LOG, "Processing Regions upload failed", ex);

            loggerService.log("Processing the upload failed. " + ex.getMessage(),
                    SharedMethods.getEntityName(UfsGeographicalRegion.class),
                    null,
                    user.getUserId(),
                    AppConstants.ACTIVITY_CREATE,
                    AppConstants.ACTIVITY_STATUS_FAILED,
                    null);
            batch.setProcessingStatus(AppConstants.ACTIVITY_STATUS_FAILED);
        }
        this.batchRepository.save(batch);
    }

    @Override
    @Async
    public void processRegionsUploadXlxs(UfsRegionsBatch batch, SysConfigService configService, SharedMethods sharedMethods, LoggerService loggerService, UfsUser user, UfsGeographicalRegion ufsGeographicalRegion) {
        try {
            List<RegionDetails> entities = sharedMethods.convertXls(RegionDetails.class, sharedMethods.convert(ufsGeographicalRegion.getFile()));
            SaveUploads(batch, loggerService, user, ufsGeographicalRegion, entities);
        } catch (IOException ex) {
            log.error(AppConstants.AUDIT_LOG, "Processing Regions upload failed", ex);

            loggerService.log("Processing the upload failed. " + ex.getMessage(),
                    SharedMethods.getEntityName(UfsGeographicalRegion.class),
                    null,
                    user.getUserId(),
                    AppConstants.ACTIVITY_CREATE,
                    AppConstants.ACTIVITY_STATUS_FAILED,
                    null);
            batch.setProcessingStatus(AppConstants.ACTIVITY_STATUS_FAILED);
        } catch (InvalidFormatException ex) {
            java.util.logging.Logger.getLogger(OrganizationServiceTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.batchRepository.save(batch);
    }

    @Override
    public Optional<UfsOrganizationHierarchy> isUnitAllowed(String parent_id) {
        Optional<UfsOrganizationUnits> units = organizationUnitsRepository.findById(parent_id);
        if (!units.isPresent()) {
            return Optional.empty();
        }
        UfsOrganizationUnits org_unit = units.get();
        BigDecimal level = org_unit.getLevelId().getLevelNo();
        BigDecimal nextlevel = level.add(new BigDecimal(1));
        return repository.findByLevelNo(nextlevel);
    }

    @Override
    public Optional<UfsOrganizationHierarchy> findRootHierarchyLevel() {
        return repository.findByLevelNo(new BigDecimal(1));
    }

    @Override
    public Optional<UfsOrganizationHierarchy> findByLevelNo(BigDecimal levelNo) {
        log.debug("Fetching by Level huhuhu: " + levelNo);
        return repository.findByLevelNo(levelNo);
    }

    private void SaveUploads(UfsRegionsBatch batch, LoggerService loggerService, UfsUser user, UfsGeographicalRegion ufsGeographicalRegion, List<RegionDetails> entities) {
        for (RegionDetails entity : entities) {
            //validate entity
            DirectFieldBindingResult valid = new DirectFieldBindingResult(entity,
                    "RegionDetails");
            validator.validate(entity, valid);
            if (valid.hasErrors()) {
                loggerService.log("Creating  a region (Name:  " + entity.getRegionName() + ") failed. Encountered validation errors (Errors: " + SharedMethods.getFieldErrorsString(valid) + ")",
                        SharedMethods.getEntityName(UfsGeographicalRegion.class),
                        null,
                        user.getUserId(),
                        AppConstants.ACTIVITY_CREATE,
                        AppConstants.ACTIVITY_STATUS_FAILED,
                        null);
                continue;
            }
            Optional<UfsGeographicalRegion> region;
            if (Objects.nonNull(ufsGeographicalRegion.getParentIds())) {
                region = this.geographicalRegionRepository.findByregionNameAndParentIdsAndIntrash(entity.getRegionName(), ufsGeographicalRegion.getParentIds(), AppConstants.NO);
            } else {
                region = this.geographicalRegionRepository.findByregionNameAndIntrash(entity.getRegionName(), AppConstants.NO);
            }
            if (region.isPresent()) {
                loggerService.log("Creating  a region (Name:  " + entity.getRegionName() + ") failed. Already a region with same Name Exists",
                        SharedMethods.getEntityName(UfsGeographicalRegion.class),
                        null,
                        user.getUserId(),
                        AppConstants.ACTIVITY_CREATE,
                        AppConstants.ACTIVITY_STATUS_FAILED,
                        null);
                continue;
            }

            UfsGeographicalRegion regions;
            if (Objects.nonNull(ufsGeographicalRegion.getParentIds())) {
                regions = new UfsGeographicalRegion(entity.getRegionName(),
                        AppConstants.ACTION_STATUS_UNCONFIRMED, ufsGeographicalRegion.getParentIds(), ufsGeographicalRegion.getTenantIds());
            } else {
                regions = new UfsGeographicalRegion(entity.getRegionName(),
                        AppConstants.ACTION_STATUS_UNCONFIRMED, ufsGeographicalRegion.getTenantIds());
            }

            regions = geographicalRegionRepository.save(regions);

            loggerService.log("Creating  a region (Name:  " + entity.getRegionName() + ") successfully",
                    SharedMethods.getEntityName(UfsGeographicalRegion.class),
                    regions.getId(),
                    user.getUserId(),
                    AppConstants.ACTIVITY_CREATE,
                    AppConstants.ACTIVITY_STATUS_SUCCESS,
                    null);
        }
        batch.setProcessingStatus(AppConstants.STATUS_COMPLETED);
    }
}
