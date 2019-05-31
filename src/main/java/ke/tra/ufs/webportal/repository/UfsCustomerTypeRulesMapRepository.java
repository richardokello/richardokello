/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.repository;


import ke.tra.ufs.webportal.entities.UfsCustomerTypeRuleMap;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tracom
 */
@Repository
public interface UfsCustomerTypeRulesMapRepository extends CrudRepository<UfsCustomerTypeRuleMap, Long>{

    public List<UfsCustomerTypeRuleMap> findAllByTypeIds(BigDecimal typeIds);
}
