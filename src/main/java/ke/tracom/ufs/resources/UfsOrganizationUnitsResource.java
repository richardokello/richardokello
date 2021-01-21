package ke.tracom.ufs.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.exceptions.GeneralBadRequest;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tracom.ufs.entities.UfsEdittedRecord;
import ke.tracom.ufs.entities.UfsOrganizationHierarchy;
import ke.tracom.ufs.entities.UfsOrganizationUnits;
import ke.tracom.ufs.services.OrganizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping(value = "/organization-units")
public class UfsOrganizationUnitsResource extends ChasisResource<UfsOrganizationUnits, String, UfsEdittedRecord> {
    private final OrganizationService organizationService;

    public UfsOrganizationUnitsResource(LoggerService loggerService, EntityManager entityManager, OrganizationService organizationService, LoggerService loggerService1) {
        super(loggerService, entityManager);
        this.organizationService = organizationService;
    }

    @Override
    public ResponseEntity<ResponseWrapper<UfsOrganizationUnits>> create(@Valid @RequestBody UfsOrganizationUnits ufsOrganizationUnits) {
        if (Objects.nonNull(ufsOrganizationUnits.getParentIds())) {
            String parentIds = ufsOrganizationUnits.getParentIds();
            Optional<UfsOrganizationHierarchy> hierarchy = organizationService.isUnitAllowed(parentIds);
            if (hierarchy.isPresent()) {
                ufsOrganizationUnits.setLevelIds(hierarchy.get().getId());
            } else {
                try {
                    throw new GeneralBadRequest("No hierarchy level created for the item you are are creating");
                } catch (GeneralBadRequest generalBadRequest) {
                    generalBadRequest.printStackTrace();
                }
            }
        } else {
            Optional<UfsOrganizationHierarchy> hierarchy = organizationService.findRootHierarchyLevel();
            if (hierarchy.isPresent()) {
                ufsOrganizationUnits.setLevelIds(hierarchy.get().getId());
            } else {
                try {
                    throw new GeneralBadRequest("No hierarchy level created for the item you are are creating");
                } catch (GeneralBadRequest generalBadRequest) {
                    generalBadRequest.printStackTrace();
                }
            }
        }
        return super.create(ufsOrganizationUnits);
    }
}
