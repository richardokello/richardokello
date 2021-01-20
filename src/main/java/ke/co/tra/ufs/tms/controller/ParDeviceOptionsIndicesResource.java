package ke.co.tra.ufs.tms.controller;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.co.tra.ufs.tms.entities.ParDeviceOptionsIndices;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import ke.co.tra.ufs.tms.service.ParDeviceOptionsIndicesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/par-device-option-indices")
public class ParDeviceOptionsIndicesResource extends ChasisResource<ParDeviceOptionsIndices, BigDecimal, UfsEdittedRecord> {

    private final ParDeviceOptionsIndicesService parDeviceOptionsIndicesService;

    public ParDeviceOptionsIndicesResource(LoggerService loggerService, EntityManager entityManager, ParDeviceOptionsIndicesService parDeviceOptionsIndicesService) {
        super(loggerService, entityManager);
        this.parDeviceOptionsIndicesService = parDeviceOptionsIndicesService;
    }


    @Override
    public ResponseEntity<ResponseWrapper<ParDeviceOptionsIndices>> create(@RequestBody ParDeviceOptionsIndices parDeviceOptionsIndices) {
        ResponseWrapper<ParDeviceOptionsIndices> wrapper = new ResponseWrapper<>();
        wrapper.setCode(HttpStatus.NOT_ACCEPTABLE.value());
        wrapper.setMessage("Process not allowed");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(wrapper);
    }


    @SuppressWarnings("rawtypes")
    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper> createParameterIndices(@Valid @RequestBody List<ParDeviceOptionsIndices> params) {
        ResponseWrapper<Object> wrapper = new ResponseWrapper<>();
        wrapper.setData(parDeviceOptionsIndicesService.saveAll(params));
        return ResponseEntity.ok(wrapper);
    }
}
