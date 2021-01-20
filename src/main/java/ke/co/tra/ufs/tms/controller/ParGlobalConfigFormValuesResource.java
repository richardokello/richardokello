package ke.co.tra.ufs.tms.controller;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.co.tra.ufs.tms.entities.ParGlobalConfigFormValues;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import ke.co.tra.ufs.tms.service.ParGlobalConfigFormValuesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/global-config-forms")
public class ParGlobalConfigFormValuesResource extends ChasisResource<ParGlobalConfigFormValues, BigDecimal, UfsEdittedRecord> {

    public ParGlobalConfigFormValuesResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }

//    @Override
//    public ResponseEntity<ResponseWrapper<ParGlobalConfigFormValues>> create(@Valid @RequestBody ParGlobalConfigFormValues parGlobalConfigFormValues) {
//        // check if type exist
//        Optional<ParGlobalConfigFormValues> optional = parGlobalConfigFormValuesService.findFormValue(parGlobalConfigFormValues.getId());
//        if (optional.isPresent()) {
//            ResponseWrapper<ParGlobalConfigFormValues> wrapper = new ResponseWrapper<>();
//            ParGlobalConfigFormValues formValues = optional.get();
//            formValues.setFormValues(parGlobalConfigFormValues.getFormValues());
//            formValues = parGlobalConfigFormValuesService.save(parGlobalConfigFormValues);
//            wrapper.setData(formValues);
//            return ResponseEntity.ok(wrapper);
//        } else {
//            // if new create
//            return super.create(parGlobalConfigFormValues);
//        }
//    }
}
