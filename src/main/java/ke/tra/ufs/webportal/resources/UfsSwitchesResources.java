package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.UfsSwitches;
import ke.tra.ufs.webportal.repository.UfsSwitchesRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;


@RestController
@RequestMapping("/switch")
public class UfsSwitchesResources extends ChasisResource<UfsSwitches,Long, UfsEdittedRecord> {

    private final UfsSwitchesRepository ufsSwitchesRepository;

    public UfsSwitchesResources(LoggerService loggerService, EntityManager entityManager, UfsSwitchesRepository ufsSwitchesRepository) {
        super(loggerService, entityManager);
        this.ufsSwitchesRepository = ufsSwitchesRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsSwitches>> create(@Valid @RequestBody UfsSwitches ufsSwitches) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        UfsSwitches ufsSwitches1 = ufsSwitchesRepository.findBySwitchName(ufsSwitches.getSwitchName());

        if (ufsSwitches1 != null ) {
            responseWrapper.setCode(HttpStatus.CONFLICT.value());
            responseWrapper.setMessage(ufsSwitches.getSwitchName()+" Switch Name already exist");

            return ResponseEntity.ok(responseWrapper);
        }
        return super.create(ufsSwitches);
    }
}
