package repository;

import entities.UfsGender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;

@Repository
public interface UfsGenderRepository extends JpaRepository<UfsGender, BigDecimal> {
    UfsGender findByGender(String gender);
}
