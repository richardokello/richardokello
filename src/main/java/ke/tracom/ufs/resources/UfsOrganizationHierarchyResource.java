package ke.tracom.ufs.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tracom.ufs.entities.UfsEdittedRecord;
import ke.tracom.ufs.entities.UfsOrganizationHierarchy;
import ke.tracom.ufs.repositories.UfsOrganizationHierarchyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping(value = "/organization-hierarchy")
public class UfsOrganizationHierarchyResource extends ChasisResource<UfsOrganizationHierarchy, BigDecimal, UfsEdittedRecord> {

    private final UfsOrganizationHierarchyRepository orgHierarchyRepository;

    public UfsOrganizationHierarchyResource(LoggerService loggerService, EntityManager entityManager,
                                            UfsOrganizationHierarchyRepository orgHierarchyRepository) {
        super(loggerService, entityManager);
        this.orgHierarchyRepository = orgHierarchyRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsOrganizationHierarchy>> create(@Valid @RequestBody UfsOrganizationHierarchy ufsOrganizationHierarchy) {
        BigDecimal level = nextLevel(new BigDecimal(1));
        ufsOrganizationHierarchy.setLevelNo(level);
        return super.create(ufsOrganizationHierarchy);
    }

    private BigDecimal nextLevel(BigDecimal start) {
        Optional<UfsOrganizationHierarchy> hierarchy = orgHierarchyRepository.findByLevelNo(start);
        if (hierarchy.isPresent()) {
            return nextLevel(start.add(new BigDecimal(1)));
        }
        return start;
    }
}
