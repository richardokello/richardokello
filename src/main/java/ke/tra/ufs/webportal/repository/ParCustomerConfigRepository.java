package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.ParCustomerConfigKeys;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ParCustomerConfigRepository extends CrudRepository<ParCustomerConfigKeys, BigDecimal> {
    List<ParCustomerConfigKeys> findAllByIsAllowed(short isAllowed);
}
