package ke.tracom.ufs.services;

import ke.axle.chassis.utils.LoggerService;
import ke.tracom.ufs.entities.UfsGeographicalRegion;
import ke.tracom.ufs.entities.UfsOrganizationHierarchy;
import ke.tracom.ufs.entities.UfsRegionsBatch;
import ke.tracom.ufs.entities.UfsUser;
import ke.tracom.ufs.utils.SharedMethods;

import java.math.BigDecimal;
import java.util.Optional;

public interface OrganizationService {
    public UfsOrganizationHierarchy findByIsRootTenantAndIntrash();

    public UfsRegionsBatch saveBatch(UfsRegionsBatch batch);

    public void processRegionsUploadCsv(UfsRegionsBatch batch, SysConfigService configService, SharedMethods sharedMethods, LoggerService loggerService, UfsUser user, UfsGeographicalRegion ufsGeographicalRegion);

    public void processRegionsUploadXlxs(UfsRegionsBatch batch, SysConfigService configService, SharedMethods sharedMethods, LoggerService loggerService, UfsUser user, UfsGeographicalRegion ufsGeographicalRegion);

    public Optional<UfsOrganizationHierarchy> isUnitAllowed(String parent_id);

    public Optional<UfsOrganizationHierarchy> findRootHierarchyLevel();

    public Optional<UfsOrganizationHierarchy> findByLevelNo(BigDecimal levelNo);
}
