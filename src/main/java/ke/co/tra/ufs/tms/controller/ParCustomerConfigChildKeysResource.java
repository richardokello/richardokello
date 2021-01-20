package ke.co.tra.ufs.tms.controller;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.co.tra.ufs.tms.entities.ParCustomerConfigChildKeys;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

@RestController
@RequestMapping("/customer-child-configs")
public class ParCustomerConfigChildKeysResource extends ChasisResource<ParCustomerConfigChildKeys, BigDecimal, UfsEdittedRecord> {
    public ParCustomerConfigChildKeysResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }

    @Override
    public ResponseEntity<ResponseWrapper<ParCustomerConfigChildKeys>> create(@RequestBody ParCustomerConfigChildKeys parCustomerConfigChildKeys) {
        ResponseWrapper<ParCustomerConfigChildKeys> wrapper = new ResponseWrapper<>();
        wrapper.setCode(HttpStatus.NOT_ACCEPTABLE.value());
        wrapper.setMessage("Process not allowed");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(wrapper);
    }
}
