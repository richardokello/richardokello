package ke.co.tra.ufs.tms.controller;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.co.tra.ufs.tms.entities.ParDeviceOptions;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import ke.co.tra.ufs.tms.repository.ParDeviceOptionsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/par-device-options")
public class ParDeviceOptionsResource extends ChasisResource<ParDeviceOptions, BigDecimal, UfsEdittedRecord> {

    public final ParDeviceOptionsRepository parDeviceOptionsRepository;

    public ParDeviceOptionsResource(LoggerService loggerService, EntityManager entityManager, ParDeviceOptionsRepository parDeviceOptionsRepository) {
        super(loggerService, entityManager);
        this.parDeviceOptionsRepository = parDeviceOptionsRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<ParDeviceOptions>> create(@Valid @RequestBody ParDeviceOptions parDeviceOptions) {
        ResponseWrapper wrapper = new ResponseWrapper();
        ParDeviceOptions deviceOptions = parDeviceOptionsRepository.findByNameAndIntrash(parDeviceOptions.getName(), AppConstants.NO);
        if (deviceOptions != null ) {
            wrapper.setCode(HttpStatus.CONFLICT.value());
            wrapper.setMessage(parDeviceOptions.getName()+" Device Option already exist");

            return ResponseEntity.status(HttpStatus.CONFLICT).body(wrapper);
        }
        return super.create(parDeviceOptions);
    }
}
