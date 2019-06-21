package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.UfsBankBins;
import ke.tra.ufs.webportal.repository.UfsBankBinsRepository;
import ke.tra.ufs.webportal.service.BankService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BankServiceTemplate implements BankService {
    private final UfsBankBinsRepository binsRepository;

    public BankServiceTemplate(UfsBankBinsRepository binsRepository) {
        this.binsRepository = binsRepository;
    }

    @Override
    public void saveAllBins(List<UfsBankBins> bins) {
        binsRepository.saveAll(bins);
    }
}
