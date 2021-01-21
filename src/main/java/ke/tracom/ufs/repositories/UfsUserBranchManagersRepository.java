package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsUserBranchManagers;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface UfsUserBranchManagersRepository extends CrudRepository<UfsUserBranchManagers, BigDecimal> {
}
