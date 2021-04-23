package ke.tra.ufs.webportal.service.template;

/**
 * @author kenny
 */

import ke.tra.ufs.webportal.entities.UfsBankBranches;
import ke.tra.ufs.webportal.repository.UfsBankBranchesRepository;
import ke.tra.ufs.webportal.service.BankBranchesService;
import ke.tra.ufs.webportal.utils.AppConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class BankBranchesServiceTemplate implements BankBranchesService {

    private final UfsBankBranchesRepository ufsBankBranchesRepository;

    public BankBranchesServiceTemplate(UfsBankBranchesRepository ufsBankBranchesRepository) {
        this.ufsBankBranchesRepository = ufsBankBranchesRepository;
    }

    @Override
    public UfsBankBranches findByBranchId(Long id) {
        return this.ufsBankBranchesRepository.findByBranchId(id);
    }

    @Override
    public void saveBranch(UfsBankBranches bankBranch) {
        this.ufsBankBranchesRepository.save(bankBranch);
    }

    @Override
    public UfsBankBranches findByBankBranchesName(String bankName) {
        return ufsBankBranchesRepository.findByNameAndIntrash(bankName, AppConstants.INTRASH_NO);
    }

    @Override
    public Page<UfsBankBranches> getBankBranches(String actionStatus, String needle, Date from, Date to, Pageable pg) {
        return ufsBankBranchesRepository.findAll(actionStatus,needle,AppConstants.INTRASH_NO,from,to,pg);
    }
}
