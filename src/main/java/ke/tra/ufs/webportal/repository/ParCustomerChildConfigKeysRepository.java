package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.ParCustomerConfigChildKeysIndices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ParCustomerChildConfigKeysRepository extends JpaRepository<ParCustomerConfigChildKeysIndices, BigDecimal> {
}
