package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.ParMenuIndices;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ParMenuIndexingRepository extends CrudRepository<ParMenuIndices, BigDecimal> {
    List<ParMenuIndices> findAllByCustomerType(BigDecimal customerType, Sort sort);
}
