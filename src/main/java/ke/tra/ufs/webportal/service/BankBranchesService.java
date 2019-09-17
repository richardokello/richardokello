package ke.tra.ufs.webportal.service;
/**
 * @author kenny
 */

import ke.tra.ufs.webportal.entities.UfsBankBranches;

public interface BankBranchesService {

    UfsBankBranches findByBranchId(Long id);

    void saveBranch(UfsBankBranches bankBranch);
}
