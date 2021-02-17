package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.ParCustomerConfigKeysIndices;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ParCustomerConfigIndicesRepository extends CrudRepository<ParCustomerConfigKeysIndices, BigDecimal> {
    List<ParCustomerConfigKeysIndices> findAll(Sort sort);
}
