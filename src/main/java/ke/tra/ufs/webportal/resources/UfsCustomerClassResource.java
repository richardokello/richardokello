package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsCustomerClass;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.repository.UfsCustomerClassRepository;
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

    private final UfsCustomerClassRepository customerClassRepository;

    public UfsCustomerClassResource(LoggerService loggerService, EntityManager entityManager,
                                    UfsCustomerClassRepository customerClassRepository) {
        super(loggerService, entityManager);
        this.customerClassRepository = customerClassRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsCustomerClass>> create(@Valid @RequestBody UfsCustomerClass ufsCustomerClass) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        UfsCustomerClass customerClass = customerClassRepository.findByNameAndIntrash(ufsCustomerClass.getName(), AppConstants.NO);
        if (customerClass != null ) {
            responseWrapper.setCode(HttpStatus.CONFLICT.value());
            responseWrapper.setMessage(ufsCustomerClass.getName()+" Customer Class Name already exist");

            return new ResponseEntity(responseWrapper, HttpStatus.CONFLICT);
        }

        return super.create(ufsCustomerClass);
    }
}
