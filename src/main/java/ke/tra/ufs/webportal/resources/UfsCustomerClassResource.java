package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsCustomerClass;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.UfsTieredCommissionAmount;
import ke.tra.ufs.webportal.repository.UfsTieredCommissionAmountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.persistence.EntityManager;
import javax.validation.Valid;


@Controller
@RequestMapping("/customer-class")
public class UfsCustomerClassResource extends ChasisResource<UfsCustomerClass, Long, UfsEdittedRecord> {

    private final UfsTieredCommissionAmountRepository repository;

    public UfsCustomerClassResource(LoggerService loggerService, EntityManager entityManager, UfsTieredCommissionAmountRepository repository) {
        super(loggerService, entityManager);
        this.repository = repository;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsCustomerClass>> create(@Valid @RequestBody UfsCustomerClass ufsCustomerClass) {
        ResponseEntity<ResponseWrapper<UfsCustomerClass>> response = super.create(ufsCustomerClass);
        log.info("TIER SIZE " + ufsCustomerClass.getTieredId().size() );
        if (!response.getStatusCode().equals(HttpStatus.CREATED) ||ufsCustomerClass.getTieredId().size() == 0 ) {
            return response;
        }

            UfsCustomerClass customerClass = response.getBody().getData();
            ufsCustomerClass.getTieredId().forEach(x -> {
                UfsTieredCommissionAmount ufsTieredCommissionAmount = new UfsTieredCommissionAmount();
                ufsTieredCommissionAmount.setLimitType(x.getLimitType());
                ufsTieredCommissionAmount.setLowerLimit(x.getLowerLimit());
                ufsTieredCommissionAmount.setUpperLimit(x.getUpperLimit());
                ufsTieredCommissionAmount.setTieredValue(x.getTieredValue());
                ufsTieredCommissionAmount.setCustomerClassId(customerClass.getId());

                repository.save(ufsTieredCommissionAmount);
            });

        return response;
    }
}
