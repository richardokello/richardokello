package repository;

import entities.UfsUserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;

@Repository
public interface UfsUserTypeRepository extends JpaRepository<UfsUserType, BigDecimal> {
    UfsUserType findByUserType(String  userType);
    UfsUserType findByUserTypeIgnoreCaseAndIntrash(String userType, String intrash);
}
