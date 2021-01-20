package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParCustomerConfigKeysIndices;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ParCustomerConfigIndicesRepository extends CrudRepository<ParCustomerConfigKeysIndices, BigDecimal> {
    List<ParCustomerConfigKeysIndices> findAll(Sort sort);
}
