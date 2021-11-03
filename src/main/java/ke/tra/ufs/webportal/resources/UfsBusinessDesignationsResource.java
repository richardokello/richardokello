package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsBusinessDesignations;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.repository.UfsBusinessDesignationsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;


@RestController
@RequestMapping(value = "/business-designations")
public class UfsBusinessDesignationsResource  extends ChasisResource<UfsBusinessDesignations,Long, UfsEdittedRecord> {

    private final UfsBusinessDesignationsRepository businessDesignationsRepository;

    public UfsBusinessDesignationsResource(LoggerService loggerService, EntityManager entityManager, UfsBusinessDesignationsRepository businessDesignationsRepository) {
        super(loggerService, entityManager);
        this.businessDesignationsRepository = businessDesignationsRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsBusinessDesignations>> create(@Valid @RequestBody UfsBusinessDesignations ufsBusinessDesignations) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        UfsBusinessDesignations businessDesignations = businessDesignationsRepository.findByDesignationAndIntrash(ufsBusinessDesignations.getDesignation(), AppConstants.NO);
        if (businessDesignations != null ) {
            responseWrapper.setCode(HttpStatus.CONFLICT.value());
            responseWrapper.setMessage(ufsBusinessDesignations.getDesignation()+" Designation already exist");

            return ResponseEntity.ok(responseWrapper);
        }
        return super.create(ufsBusinessDesignations);
    }
}
