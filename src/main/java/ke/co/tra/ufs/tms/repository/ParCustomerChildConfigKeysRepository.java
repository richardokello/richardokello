package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParCustomerConfigChildKeysIndices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ParCustomerChildConfigKeysRepository extends JpaRepository<ParCustomerConfigChildKeysIndices, BigDecimal> {
}
