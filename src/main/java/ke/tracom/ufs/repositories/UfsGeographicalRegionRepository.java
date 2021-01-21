package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsGeographicalRegion;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface UfsGeographicalRegionRepository extends CrudRepository<UfsGeographicalRegion, BigDecimal> {

    Optional<UfsGeographicalRegion> findByregionNameAndParentIdsAndIntrash(String name, BigDecimal parentIds, String intrash);

    Optional<UfsGeographicalRegion> findByregionNameAndIntrash(String name, String intrash);
}
