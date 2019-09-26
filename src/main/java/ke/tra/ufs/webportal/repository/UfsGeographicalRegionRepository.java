package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsGeographicalRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface UfsGeographicalRegionRepository extends JpaRepository<UfsGeographicalRegion, BigDecimal> {
}
