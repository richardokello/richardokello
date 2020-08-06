package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.UfsTariffs;
import ke.tra.ufs.webportal.entities.wrapper.TariffType;
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

    @PostMapping
    @Override
    public ResponseEntity<ResponseWrapper<UfsTariffs>> create(@Valid @RequestBody UfsTariffs ufsTariffs) {

        if (isValidJson(ufsTariffs.getValues())) {
            System.out.println(ufsTariffs.getValues());
            return super.create(ufsTariffs);
        }

        ResponseWrapper<UfsTariffs> responseWrapper = new ResponseWrapper<>();
        responseWrapper.setMessage("Tariff values is not valid");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
    }

    @GetMapping("/types")
    public ResponseEntity<ResponseWrapper<Object>> getTariffTypes() {
        ResponseWrapper<Object> wrapper = new ResponseWrapper<>();
        wrapper.setData(Arrays.asList(TariffType.values()));
        return ResponseEntity.status(HttpStatus.OK).body(wrapper);
    }

    private boolean isValidJson(String value) {
        boolean valid = true;

        if (!value.startsWith("{") || !value.startsWith("[")) valid = false;
        if (!value.endsWith("}") || !value.endsWith("]")) valid = false;

        return valid;
    }

    private boolean isValidTariff(JSONObject json) {
        return true;
    }
}
