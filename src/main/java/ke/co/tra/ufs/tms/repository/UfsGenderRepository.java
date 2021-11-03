package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.UfsGender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface UfsGenderRepository extends JpaRepository<UfsGender, BigDecimal> {
    UfsGender findByGender(String gender);
}
