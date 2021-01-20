package ke.co.tra.ufs.tms.controller;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.co.tra.ufs.tms.entities.ParGlobalFileConfigType;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import ke.co.tra.ufs.tms.entities.enums.ParameterFileType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("global-config-files")
public class ParGlobalConfigTypeResource extends ChasisResource<ParGlobalFileConfigType, BigDecimal, UfsEdittedRecord> {
    public ParGlobalConfigTypeResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }

    @Override
    public ResponseEntity<ResponseWrapper<ParGlobalFileConfigType>> create(@Valid @RequestBody ParGlobalFileConfigType parGlobalFileConfigType) {
        parGlobalFileConfigType.setName(parGlobalFileConfigType.getName().toUpperCase(Locale.ENGLISH));
        return super.create(parGlobalFileConfigType);
    }

    @GetMapping("/types")
    public ResponseEntity<ResponseWrapper<Object>> getTypes() {
        List<String> types = new ArrayList<>();
        for (ParameterFileType value : ParameterFileType.values()) {
            types.add(value.toString());
        }
        ResponseWrapper<Object> wrapper = new ResponseWrapper<>();
        wrapper.setData(types);
        return ResponseEntity.ok(wrapper);
    }
}
