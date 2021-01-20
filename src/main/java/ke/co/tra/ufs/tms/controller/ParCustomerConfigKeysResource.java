package ke.co.tra.ufs.tms.controller;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.co.tra.ufs.tms.entities.ParCustomerConfigKeys;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import ke.co.tra.ufs.tms.service.CustomerConfigFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

@RestController
@RequestMapping("/customer-configs")
public class ParCustomerConfigKeysResource extends ChasisResource<ParCustomerConfigKeys, BigDecimal, UfsEdittedRecord> {

    private final CustomerConfigFileService customerConfigFileService;

    public ParCustomerConfigKeysResource(LoggerService loggerService, EntityManager entityManager, CustomerConfigFileService customerConfigFileService) {
        super(loggerService, entityManager);
        this.customerConfigFileService = customerConfigFileService;
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/{deviceId}/generate")
    ResponseEntity<ResponseWrapper> generateCustomerConfigFile(@PathVariable(name = "deviceId") BigDecimal deviceId) throws NoSuchFieldException, IllegalAccessException {
        ResponseWrapper<Object> wrapper = new ResponseWrapper<>();
        customerConfigFileService.generateCustomerFile(deviceId, "/home/kenn/");
        return ResponseEntity.ok(wrapper);
    }
}
