package co.ke.tracom.bprgateway.web.bankbranches.repository;

import co.ke.tracom.bprgateway.web.bankbranches.entity.BPRBranches;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BPRBranchesRepository extends CrudRepository<BPRBranches, String> {
  Optional<BPRBranches> findBySubDivisionCode(String branchCode);
}
