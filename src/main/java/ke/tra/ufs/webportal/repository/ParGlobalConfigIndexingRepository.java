package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.ParGlobalConfigIndices;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ParGlobalConfigIndexingRepository extends CrudRepository<ParGlobalConfigIndices, BigDecimal> {
    List<ParGlobalConfigIndices> findAllByConfigTypeAndActionStatus(BigDecimal configType, String actionStatus, Sort sort);

    List<ParGlobalConfigIndices> findAllByConfigType(BigDecimal configType, Sort sort);
}
