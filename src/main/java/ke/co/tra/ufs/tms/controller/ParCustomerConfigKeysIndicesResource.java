package ke.co.tra.ufs.tms.controller;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.co.tra.ufs.tms.entities.ParCustomerConfigKeysIndices;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import ke.co.tra.ufs.tms.entities.wrappers.CustomerIndexRequest;
import ke.co.tra.ufs.tms.service.ParCustomerConfigKeysService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/customer-config-indices")
public class ParCustomerConfigKeysIndicesResource extends ChasisResource<ParCustomerConfigKeysIndices, BigDecimal, UfsEdittedRecord> {

    private final ParCustomerConfigKeysService parCustomerConfigKeysService;

    public ParCustomerConfigKeysIndicesResource(LoggerService loggerService, EntityManager entityManager, ParCustomerConfigKeysService parCustomerConfigKeysService) {
        super(loggerService, entityManager);
        this.parCustomerConfigKeysService = parCustomerConfigKeysService;
    }

    @Override
    public ResponseEntity<ResponseWrapper<ParCustomerConfigKeysIndices>> create(@RequestBody ParCustomerConfigKeysIndices parCustomerConfigKeysIndices) {
        ResponseWrapper<ParCustomerConfigKeysIndices> wrapper = new ResponseWrapper<>();
        wrapper.setCode(HttpStatus.NOT_ACCEPTABLE.value());
        wrapper.setMessage("Process not allowed");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(wrapper);
    }

    @SuppressWarnings("rawtypes, unchecked")
    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper> createIndices(@RequestBody List<CustomerIndexRequest> request) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setData(parCustomerConfigKeysService.save(request));
        return ResponseEntity.ok(responseWrapper);
    }
}
