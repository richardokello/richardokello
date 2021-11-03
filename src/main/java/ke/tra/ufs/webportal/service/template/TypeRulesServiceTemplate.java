package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.UfsCustomerTypeRules;
import ke.tra.ufs.webportal.repository.UfsCustomerTypeRulesRepository;
import ke.tra.ufs.webportal.service.TypeRulesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TypeRulesServiceTemplate implements TypeRulesService {
    private final UfsCustomerTypeRulesRepository ufsCustomerTypeRulesRepository;

    public TypeRulesServiceTemplate(UfsCustomerTypeRulesRepository ufsCustomerTypeRulesRepository) {
        this.ufsCustomerTypeRulesRepository = ufsCustomerTypeRulesRepository;
    }

    @Override
    public Optional<UfsCustomerTypeRules> findById(Long id) {
        return ufsCustomerTypeRulesRepository.findById(id);
    }

    @Override
    public void saveAll(List<UfsCustomerTypeRules> rules) {
        ufsCustomerTypeRulesRepository.saveAll(rules);
    }
}
