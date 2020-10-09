package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsBusinessDesignations;
import ke.tra.ufs.webportal.entities.UfsBusinessType;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.repository.UfsBusinessTypeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;


@RestController
@RequestMapping(value = "/business-types")
public class UfsBusinessTypeResource extends ChasisResource<UfsBusinessType,Long, UfsEdittedRecord> {

    private final UfsBusinessTypeRepository ufsBusinessTypeRepository;

    public UfsBusinessTypeResource(LoggerService loggerService, EntityManager entityManager, UfsBusinessTypeRepository ufsBusinessTypeRepository) {
        super(loggerService, entityManager);
        this.ufsBusinessTypeRepository = ufsBusinessTypeRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsBusinessType>> create(@Valid @RequestBody UfsBusinessType ufsBusinessType) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        UfsBusinessType businessType = ufsBusinessTypeRepository.findByBusinessTypeAndIntrash(ufsBusinessType.getBusinessType(), AppConstants.NO);
        if (businessType != null ) {
            responseWrapper.setCode(HttpStatus.CONFLICT.value());
            responseWrapper.setMessage(ufsBusinessType.getBusinessType()+" Business Type already exist");

            return ResponseEntity.ok(responseWrapper);
        }
        return super.create(ufsBusinessType);
    }
}
