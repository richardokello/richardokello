package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsOrganizationHierarchy;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface UfsOrganizationHierarchyRepository extends CrudRepository<UfsOrganizationHierarchy, BigDecimal> {

    /**
     * @param isRootTenant
     * @param intrash
     * @return
     */
    public UfsOrganizationHierarchy findByIsRootTenantAndIntrash(Short isRootTenant, String intrash);

    public UfsOrganizationHierarchy findByOrganizationHierachyId(BigDecimal id);

    public Optional<UfsOrganizationHierarchy> findByLevelNo(BigDecimal levelNo);


}
