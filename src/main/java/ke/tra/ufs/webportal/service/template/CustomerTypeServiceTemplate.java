package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.UfsCustomerTypeRuleMap;
import ke.tra.ufs.webportal.repository.UfsCustomerTypeRulesMapRepository;
import ke.tra.ufs.webportal.service.CustomerTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerTypeServiceTemplate implements CustomerTypeService {

    private final UfsCustomerTypeRulesMapRepository typeRulesMapRepository;

    public CustomerTypeServiceTemplate(UfsCustomerTypeRulesMapRepository typeRulesMapRepository) {
        this.typeRulesMapRepository = typeRulesMapRepository;
    }

    @Override
    public void deleteAllByCustomerTypeId(Long typeId) {

    }

    @Override
    public void saveAll(List<UfsCustomerTypeRuleMap> typeRules) {
        typeRulesMapRepository.saveAll(typeRules);
    }
}
