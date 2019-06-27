/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.resources;


import javax.persistence.EntityManager;
import javax.validation.Valid;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsCustomerType;
import ke.tra.ufs.webportal.entities.UfsCustomerTypeRuleMap;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.repository.UfsCustomerTypeRulesMapRepository;
import ke.tra.ufs.webportal.service.CustomerTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kenny
 */
@Controller
@Transactional
@RequestMapping(value = "/customer-type")
public class UfsCustomerTypeController extends ChasisResource<UfsCustomerType, BigDecimal, UfsEdittedRecord> {

    private final CustomerTypeService typeService;
    private final UfsCustomerTypeRulesMapRepository typeRulesMapRepository;

    public UfsCustomerTypeController(LoggerService loggerService, EntityManager entityManager,
                                     CustomerTypeService typeService, UfsCustomerTypeRulesMapRepository typeRulesMapRepository) {
        super(loggerService, entityManager);
        this.typeService = typeService;
        this.typeRulesMapRepository = typeRulesMapRepository;
    }

    @Override
    public ResponseEntity<ResponseWrapper<UfsCustomerType>> create(@Valid @RequestBody UfsCustomerType ufsCustomerType) {
        ResponseEntity<ResponseWrapper<UfsCustomerType>> response = super.create(ufsCustomerType);

        /*Saving Into Rule Map Table*/
        if (response.getStatusCode().equals(HttpStatus.CREATED)) {
            List<UfsCustomerTypeRuleMap> custRuleMaps = new ArrayList();
            ufsCustomerType.getRuleIds().stream().forEach(ruleId->{

                UfsCustomerTypeRuleMap typeRuleMap = new UfsCustomerTypeRuleMap();
                typeRuleMap.setRuleIds(new BigDecimal(ruleId));
                typeRuleMap.setTypeIds(ufsCustomerType.getId());
                custRuleMaps.add(typeRuleMap);
            });

            typeService.saveAll(custRuleMaps);

        }

        return response;
    }

    /*Getting Customer Type Rules */
    @RequestMapping(value = "/type-rules/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper> typeRules(@PathVariable("id") BigDecimal id) {
        ResponseWrapper response = new ResponseWrapper();
        List<Object> customerTypeRuleMap = new ArrayList();


        List<UfsCustomerTypeRuleMap> custTypeRuleMapDb = typeRulesMapRepository.findAllByTypeIds(id);

        if (custTypeRuleMapDb.isEmpty()) {

            response.setData(customerTypeRuleMap);
            return ResponseEntity.ok(response);
        }
        custTypeRuleMapDb.forEach(custType -> {

            Map<String, Object> r = new HashMap<>();
            r.put("ruleId",custType.getRuleId().getId() );
            r.put("ruleName", custType.getRuleId().getName());

            customerTypeRuleMap.add(r);
        });


        response.setData(custTypeRuleMapDb);
        return ResponseEntity.ok(response);
    }
}
