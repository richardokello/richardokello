package ke.tra.ufs.webportal.service;
/**
 * @author kenny
 */

import ke.tra.ufs.webportal.entities.UfsBankBranches;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface BankBranchesService {

    UfsBankBranches findByBranchId(Long id);

    void saveBranch(UfsBankBranches bankBranch);

    UfsBankBranches findByBankBranchesName(String bankName);

    /**
     *
     * @param actionStatus
     * @param needle
     * @param pg
     * @return
     */
    public Page<UfsBankBranches> getBankBranches(String actionStatus, String needle, Date from, Date to, Pageable pg);
}
