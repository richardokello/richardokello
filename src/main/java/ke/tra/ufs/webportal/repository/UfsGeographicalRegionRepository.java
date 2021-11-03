package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsGeographicalRegion;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface UfsGeographicalRegionRepository extends CrudRepository<UfsGeographicalRegion, BigDecimal> {

    Optional<UfsGeographicalRegion> findByregionNameAndParentIdsAndIntrash(String name, BigDecimal parentIds, String intrash);

    Optional<UfsGeographicalRegion> findByregionNameAndIntrash(String name, String intrash);

    UfsGeographicalRegion findByGeographicalId(BigDecimal id);


}
