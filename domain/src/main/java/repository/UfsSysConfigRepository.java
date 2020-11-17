package repository;

import entities.UfsSysConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;

@Repository
public interface UfsSysConfigRepository extends JpaRepository<UfsSysConfig, BigDecimal> {

    /**
     * find Pos Configurations
     * @param entity
     * @param parameter
     * @return
     */
    UfsSysConfig findByEntityAndParameter(String  entity,String parameter);
}
