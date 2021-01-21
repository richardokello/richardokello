package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsCounty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface UfsCountyRepository extends JpaRepository<UfsCounty, BigDecimal> {
}
