package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsCustomerTypeRules;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.wrapper.BulkTypeRulesWrapper;
import ke.tra.ufs.webportal.service.TypeRulesService;
import ke.tra.ufs.webportal.utils.AppConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/type-rules")
public class TypeRulesResource extends ChasisResource<UfsCustomerTypeRules, Long, UfsEdittedRecord> {

    private final TypeRulesService rulesService;

    public TypeRulesResource(LoggerService loggerService, EntityManager entityManager, TypeRulesService rulesService) {
        super(loggerService, entityManager);
        this.rulesService = rulesService;
    }

    @Override
    public ResponseEntity<ResponseWrapper<UfsCustomerTypeRules>> create(@Valid UfsCustomerTypeRules ufsCustomerTypeRules) {
        throw new UnsupportedOperationException("Sorry action not support for the current resource");
    }

    @RequestMapping(value = "/bulk", method = RequestMethod.POST)
    public ResponseEntity<ResponseWrapper> bulkUpdate(@Valid @RequestBody BulkTypeRulesWrapper rulesWrapper) {
        List<UfsCustomerTypeRules> ufsCustomerTypeRules = new ArrayList<>();
        rulesWrapper.getRulesWrapper().stream().forEach(rule -> {
            Optional<UfsCustomerTypeRules> typeRules = rulesService.findById(rule.getId());
            if (typeRules.isPresent()) {
                UfsCustomerTypeRules customerTypeRules = typeRules.get();
                customerTypeRules.setActive(rule.isActive());
                customerTypeRules.setAction(AppConstants.ACTIVITY_UPDATE);
                customerTypeRules.setActionStatus(AppConstants.STATUS_UNAPPROVED);
                ufsCustomerTypeRules.add(customerTypeRules);
            }
        });
        rulesService.saveAll(ufsCustomerTypeRules);
        ResponseWrapper response = new ResponseWrapper();
        response.setMessage("Successfully Updated Rules");
        return ResponseEntity.ok(response);
    }
}
