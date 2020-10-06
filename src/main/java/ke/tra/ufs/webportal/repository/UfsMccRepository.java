package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsMcc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface UfsMccRepository extends JpaRepository<UfsMcc, BigDecimal> {

    UfsMcc findByName(String name);

}
