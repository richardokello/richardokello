package co.ke.tracom.bprgateway.web.bankbranches.service;

import co.ke.tracom.bprgateway.web.bankbranches.entity.BPRBranches;
import co.ke.tracom.bprgateway.web.bankbranches.repository.BPRBranchesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class BPRBranchService {
  private final BPRBranchesRepository bprBranchesRepository;

  public BPRBranches fetchBranchAccountsByBranchCode(String agentFloatAccount) {
    Optional<BPRBranches> bySubDivisionCode =
        bprBranchesRepository.findBySubDivisionCode(agentFloatAccount);
    // TODO Unfinished
    return bySubDivisionCode.get();
  }
}
