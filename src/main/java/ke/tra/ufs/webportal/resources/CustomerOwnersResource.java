package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.CustomerOwnersCrime;
import ke.tra.ufs.webportal.entities.UfsCustomerOwners;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.service.CustomerOwnersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.math.BigDecimal;


@RestController
@RequestMapping(value = "/customer-owners")
public class CustomerOwnersResource extends ChasisResource<UfsCustomerOwners, Long, UfsEdittedRecord> {

    private final CustomerOwnersService customerOwnersService;

    public CustomerOwnersResource(LoggerService loggerService, EntityManager entityManager, CustomerOwnersService customerOwnersService) {
        super(loggerService, entityManager);
        this.customerOwnersService = customerOwnersService;
    }

    @Override
    public ResponseEntity<ResponseWrapper<UfsCustomerOwners>> create(@Valid @RequestBody UfsCustomerOwners ufsCustomerOwners) {
        ResponseEntity response = super.create(ufsCustomerOwners);
        if ((!response.getStatusCode().equals(HttpStatus.CREATED))) {
            return response;
        }

        if (ufsCustomerOwners.getOwnersCrime() != null) {
            CustomerOwnersCrime ownersCrime = new CustomerOwnersCrime();
            ownersCrime.setCustomerOwnerIds(new BigDecimal(ufsCustomerOwners.getId()));
            ownersCrime.setDescription(ufsCustomerOwners.getOwnersCrime().getDescription());
            ownersCrime.setCustomerIds(ufsCustomerOwners.getCustomerIds());
            customerOwnersService.save(ownersCrime);
        }
        return response;
    }
}
