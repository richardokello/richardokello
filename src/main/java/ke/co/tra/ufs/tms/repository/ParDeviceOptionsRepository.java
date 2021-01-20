package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParDeviceOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ParDeviceOptionsRepository extends JpaRepository<ParDeviceOptions, BigDecimal> {

    ParDeviceOptions findByNameAndIntrash(String name,String intrash);
}
