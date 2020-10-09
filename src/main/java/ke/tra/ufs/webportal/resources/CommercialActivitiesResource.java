package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsCommercialActivities;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.repository.UfsCommercialActivitiesRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;


@RestController
@RequestMapping(value = "/commercial-activities")
public class CommercialActivitiesResource extends ChasisResource<UfsCommercialActivities,Long, UfsEdittedRecord> {

    private final UfsCommercialActivitiesRepository ufsCommercialActivitiesRepository;

    public CommercialActivitiesResource(LoggerService loggerService, EntityManager entityManager, UfsCommercialActivitiesRepository ufsCommercialActivitiesRepository) {
        super(loggerService, entityManager);
        this.ufsCommercialActivitiesRepository = ufsCommercialActivitiesRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsCommercialActivities>> create(@Valid @RequestBody UfsCommercialActivities ufsCommercialActivities) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        UfsCommercialActivities ufsCommercialActivities1 = ufsCommercialActivitiesRepository.findByCommercialActivityAndIntrash(ufsCommercialActivities.getCommercialActivity(), AppConstants.NO);
        if (ufsCommercialActivities1 != null ) {
            responseWrapper.setCode(HttpStatus.CONFLICT.value());
            responseWrapper.setMessage(ufsCommercialActivities1.getCommercialActivity()+" Commercial Activity already exist");

            return ResponseEntity.ok(responseWrapper);
        }
        return super.create(ufsCommercialActivities);
    }
}
