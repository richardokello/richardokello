package co.ke.tracom.bprgateway.web.bankbranches.service;

import co.ke.tracom.bprgateway.web.bankbranches.entity.BPRBranches;
import co.ke.tracom.bprgateway.web.bankbranches.repository.BPRBranchesRepository;
import co.ke.tracom.bprgateway.web.exceptions.custom.BankBranchException;
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
            log.info("Fetch bank branch details error. Invalid debit account input :" + account);
            throw new BankBranchException("Error fetching bank branch details. Invalid account used.");
        }
       // 408457748810186
        String branchCode;
        if (account.toUpperCase().startsWith("RWF")) {
            branchCode = "0" + account.substring(account.length() - 3);
        } else {
            branchCode = "0" + account.substring(0, 3);
        }

        Optional<BPRBranches> optionalBPRBranches = bprBranchesRepository.findBySubDivisionCode(branchCode);

        if (optionalBPRBranches.isEmpty()) {
            throw new BankBranchException("Missing branch detail configuration for the branch code :" + branchCode);

        }
        log.info("Branch code [" + branchCode + "] for account [" + account + "] found >>> branch {}", optionalBPRBranches.get());
        return optionalBPRBranches.get();
    }
}
