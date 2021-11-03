package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.UfsSysConfig;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface UfsSysConfigRepository extends CrudRepository<UfsSysConfig, BigDecimal> {
    UfsSysConfig findDistinctByParameter(String parameter);
}
