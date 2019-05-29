package ke.tra.ufs.webportal.service;


import ke.tra.ufs.webportal.entities.UfsCustomerTypeRuleMap;

import java.util.List;

public interface CustomerTypeService {

    void deleteAllByCustomerTypeId(Long typeId);

    void saveAll(List<UfsCustomerTypeRuleMap> typeRules);


}
