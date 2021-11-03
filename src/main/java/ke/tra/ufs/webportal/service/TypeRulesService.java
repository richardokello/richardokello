package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.UfsCustomerTypeRules;

import java.util.List;
import java.util.Optional;

public interface TypeRulesService {

    public Optional<UfsCustomerTypeRules> findById(Long id);

    public void saveAll(List<UfsCustomerTypeRules> rules);

}
