package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.UfsBankBins;
import ke.tra.ufs.webportal.entities.UfsBanks;
import ke.tra.ufs.webportal.repository.UfsBankBinsRepository;
import ke.tra.ufs.webportal.repository.UfsBankRepository;
import ke.tra.ufs.webportal.service.BankService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BankServiceTemplate implements BankService {
    private final UfsBankBinsRepository binsRepository;
    private final UfsBankRepository bankRepository;

    public BankServiceTemplate(UfsBankBinsRepository binsRepository, UfsBankRepository bankRepository) {
        this.binsRepository = binsRepository;
        this.bankRepository = bankRepository;
    }

    @Override
    public void saveAllBins(List<UfsBankBins> bins) {
        binsRepository.saveAll(bins);
    }

    @Override
    public UfsBanks findByNameOrCode(String bankName, String bankCode) {
        return bankRepository.findByBankNameOrBankCode(bankName,bankCode);
    }
}
