package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.ParGlobalConfigFormValues;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface ParGlobalConfigFormValuesRepository extends CrudRepository<ParGlobalConfigFormValues, BigDecimal> {
    Optional<ParGlobalConfigFormValues> findDistinctByTypeId(BigDecimal decimal);
}
