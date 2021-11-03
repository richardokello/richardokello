package ke.co.tra.ufs.tms.controller;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.co.tra.ufs.tms.entities.ParMenuIndices;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import ke.co.tra.ufs.tms.entities.enums.ParameterCategory;
import ke.co.tra.ufs.tms.entities.enums.ParameterType;
import ke.co.tra.ufs.tms.entities.wrappers.ParameterCreateRequest;
import ke.co.tra.ufs.tms.service.ParameterIndexingService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// todo take care of menu items in trash
@RestController
@RequestMapping("/menu-indices")
public class ParMenuIndexingResource extends ChasisResource<ParMenuIndices, BigDecimal, UfsEdittedRecord> {
    private final ParameterIndexingService parameterIndexingService;

    public ParMenuIndexingResource(LoggerService loggerService, EntityManager entityManager, ParameterIndexingService parameterIndexingService) {
        super(loggerService, entityManager);
        this.parameterIndexingService = parameterIndexingService;
    }

    @Override
    public ResponseEntity<ResponseWrapper<ParMenuIndices>> create(@Valid @RequestBody ParMenuIndices parameterIndex) {
        ResponseWrapper<ParMenuIndices> wrapper = new ResponseWrapper<>();
        wrapper.setCode(HttpStatus.NOT_ACCEPTABLE.value());
        wrapper.setMessage("Process not allowed");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(wrapper);
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/menus")
    public ResponseEntity<ResponseWrapper> createParameterIndices(@Valid @RequestBody ParameterCreateRequest<ParMenuIndices> params) {
        ResponseWrapper<Object> wrapper = new ResponseWrapper<>();
        wrapper.setData(parameterIndexingService.saveAllMenus(params));
        return ResponseEntity.ok(wrapper);
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/categories")
    public ResponseEntity<ResponseWrapper> getParameterCategories() {
        ResponseWrapper<Object> wrapper = new ResponseWrapper<>();
        List<ParamValue> data = new ArrayList<>();
        for (ParameterCategory type : ParameterCategory.values()) {
            data.add(new ParamValue(type.name()));
        }
        wrapper.setData(data);
        return ResponseEntity.ok(wrapper);
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/types")
    public ResponseEntity<ResponseWrapper> getParameterType() {
        ResponseWrapper<Object> wrapper = new ResponseWrapper<>();
        List<ParamValue> data = new ArrayList<>();
        for (ParameterType type : ParameterType.values()) {
            data.add(new ParamValue(type.name()));
        }
        wrapper.setData(data);
        return ResponseEntity.ok(wrapper);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ParamValue {
        String name;
    }


}
