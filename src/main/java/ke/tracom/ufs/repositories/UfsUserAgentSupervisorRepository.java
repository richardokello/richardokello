package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsUserAgentSupervisor;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface UfsUserAgentSupervisorRepository extends CrudRepository<UfsUserAgentSupervisor, BigDecimal> {
}
