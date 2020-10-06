package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsBankRegion;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.repository.UfsBankRegionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;


@RestController
@RequestMapping(value = "/bank-region")
public class BankRegionResource extends ChasisResource<UfsBankRegion, Long, UfsEdittedRecord> {

    private final UfsBankRegionRepository ufsBankRegionRepository;

    public BankRegionResource(LoggerService loggerService, EntityManager entityManager, UfsBankRegionRepository ufsBankRegionRepository) {
        super(loggerService, entityManager);
        this.ufsBankRegionRepository = ufsBankRegionRepository;
    }


    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsBankRegion>> create(@Valid @RequestBody UfsBankRegion ufsBankRegion) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        UfsBankRegion ufsBankRegion1 = ufsBankRegionRepository.findByRegionName(ufsBankRegion.getRegionName());
        if (ufsBankRegion1 != null ) {
            responseWrapper.setCode(HttpStatus.CONFLICT.value());
            responseWrapper.setMessage(ufsBankRegion.getRegionName()+" Region Name already exist");

            return ResponseEntity.ok(responseWrapper);
        }
        return super.create(ufsBankRegion);
    }
}

