package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParGlobalConfigFormValues;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface ParGlobalConfigFormValuesRepository extends CrudRepository<ParGlobalConfigFormValues, BigDecimal> {
    Optional<ParGlobalConfigFormValues> findDistinctByTypeId(BigDecimal decimal);
}
