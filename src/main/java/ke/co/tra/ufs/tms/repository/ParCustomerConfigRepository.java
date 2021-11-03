package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParCustomerConfigKeys;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ParCustomerConfigRepository extends CrudRepository<ParCustomerConfigKeys, BigDecimal> {
    List<ParCustomerConfigKeys> findAllByIsAllowed(short isAllowed);
}
