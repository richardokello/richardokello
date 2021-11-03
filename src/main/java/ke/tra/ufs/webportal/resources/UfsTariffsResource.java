package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.UfsTariffs;
import ke.tra.ufs.webportal.entities.enums.TariffType;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.math.BigInteger;
import java.util.Arrays;

@RestController
@RequestMapping("tariffs")
public class UfsTariffsResource extends ChasisResource<UfsTariffs, BigInteger, UfsEdittedRecord> {
    public UfsTariffsResource(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }

    @GetMapping("/types")
    public ResponseEntity<ResponseWrapper<Object>> getTariffTypes() {
        ResponseWrapper<Object> wrapper = new ResponseWrapper<>();
        wrapper.setData(Arrays.asList(TariffType.values()));
        return ResponseEntity.status(HttpStatus.OK).body(wrapper);
    }
}
