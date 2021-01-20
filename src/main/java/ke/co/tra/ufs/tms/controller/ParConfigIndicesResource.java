package ke.co.tra.ufs.tms.controller;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.co.tra.ufs.tms.entities.ParGlobalConfigIndices;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import ke.co.tra.ufs.tms.entities.wrappers.ParameterCreateRequest;
import ke.co.tra.ufs.tms.service.ParameterIndexingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/global-config-indices")
public class ParConfigIndicesResource extends ChasisResource<ParGlobalConfigIndices, BigDecimal, UfsEdittedRecord> {

    private final ParameterIndexingService parameterIndexingService;

    public ParConfigIndicesResource(LoggerService loggerService, EntityManager entityManager, ParameterIndexingService parameterIndexingService) {
        super(loggerService, entityManager);
        this.parameterIndexingService = parameterIndexingService;
    }

    @Override
    public ResponseEntity<ResponseWrapper<ParGlobalConfigIndices>> create(@RequestBody ParGlobalConfigIndices parameterIndex) {
        ResponseWrapper<ParGlobalConfigIndices> wrapper = new ResponseWrapper<>();
        wrapper.setCode(HttpStatus.NOT_ACCEPTABLE.value());
        wrapper.setMessage("Process not allowed");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(wrapper);
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/configs")
    public ResponseEntity<ResponseWrapper> createParameterIndices(@Valid @RequestBody ParameterCreateRequest<ParGlobalConfigIndices> params) {
        ResponseWrapper<Object> wrapper = new ResponseWrapper<>();
        wrapper.setData(parameterIndexingService.saveAllConfigs(params));
        return ResponseEntity.ok(wrapper);
    }
}
