package co.ke.tracom.bprgateway.web.bankbranches.service;

import co.ke.tracom.bprgateway.web.bankbranches.entity.BPRBranches;
import co.ke.tracom.bprgateway.web.bankbranches.repository.BPRBranchesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class BPRBranchService {
    private final BPRBranchesRepository bprBranchesRepository;

    public BPRBranches fetchBranchAccountsByBranchCode(String account) {
        if (account.length() < 10) {
            System.out.println("Invalid debit account " + account);
            return null;
        }

        String branchCode = "";
        if (account.toUpperCase().startsWith("RWF")) {
            branchCode = "0" + account.substring(account.length() - 3);
        } else {
            branchCode = "0" + account.substring(0, 3);
        }

        log.info("Branch code [" + branchCode + "] for account [" + account + "] found");

        Optional<BPRBranches> bySubDivisionCode =
                bprBranchesRepository.findBySubDivisionCode(branchCode);
        return   bySubDivisionCode.isPresent()? bySubDivisionCode.get() : null;
    }
}
