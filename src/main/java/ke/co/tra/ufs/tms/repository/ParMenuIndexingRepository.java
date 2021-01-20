package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParMenuIndices;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ParMenuIndexingRepository extends CrudRepository<ParMenuIndices, BigDecimal> {
    List<ParMenuIndices> findAllByCustomerType(BigDecimal customerType, Sort sort);
}
