package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.ParDeviceOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ParDeviceOptionsRepository extends JpaRepository<ParDeviceOptions, BigDecimal> {

    ParDeviceOptions findByNameAndIntrash(String name,String intrash);
}
