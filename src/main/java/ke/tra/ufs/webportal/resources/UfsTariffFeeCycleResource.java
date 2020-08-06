package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.UfsFeeCycle;
import ke.tra.ufs.webportal.entities.enums.FeeCycleType;
import ke.tra.ufs.webportal.entities.enums.TariffType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.Arrays;

@RestController
@RequestMapping("/fee_cycles")
public class UfsTariffFeeCycleResource  extends ChasisResource<UfsFeeCycle, BigInteger, UfsEdittedRecord> {
    public UfsTariffFeeCycleResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }

    @GetMapping("/types")
    public ResponseEntity<ResponseWrapper<Object>> getCycleTypes() {
        ResponseWrapper<Object> wrapper = new ResponseWrapper<>();
        wrapper.setData(Arrays.asList(FeeCycleType.values()));
        return ResponseEntity.status(HttpStatus.OK).body(wrapper);
    }
}
