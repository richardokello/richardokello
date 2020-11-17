package repository;

import entities.UfsDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;

@Repository
public interface UfsDepartmentRepository extends JpaRepository<UfsDepartment, BigDecimal> {

    UfsDepartment findByDepartmentName(String departmentName);
    UfsDepartment findByDepartmentNameIgnoreCaseAndIntrash(String departmentName, String intrash);
}
