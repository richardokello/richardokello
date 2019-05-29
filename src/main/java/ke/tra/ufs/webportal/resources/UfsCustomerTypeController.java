/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.resources;

import com.cm.projects.spring.resource.chasis.ChasisResource;
import com.cm.projects.spring.resource.chasis.utils.LoggerService;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import com.cm.projects.spring.resource.chasis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsCustomerType;
import ke.tra.ufs.webportal.entities.UfsCustomerTypeRuleMap;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.service.CustomerTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kenny
 */
@Controller
@Transactional
@RequestMapping(value = "/customer-type")
public class UfsCustomerTypeController extends ChasisResource<UfsCustomerType, BigDecimal, UfsEdittedRecord> {

    private final CustomerTypeService typeService;

    public UfsCustomerTypeController(LoggerService loggerService, EntityManager entityManager, CustomerTypeService typeService) {
        super(loggerService, entityManager);
        this.typeService = typeService;
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
}
