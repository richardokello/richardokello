package ke.tra.ufs.webportal.resources;

import com.netflix.ribbon.proxy.annotation.Http;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.UfsMcc;
import ke.tra.ufs.webportal.repository.UfsMccRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/mcc")
public class UfsMccResources extends ChasisResource<UfsMcc, BigDecimal, UfsEdittedRecord> {

    private final UfsMccRepository ufsMccRepository;

    public UfsMccResources(LoggerService loggerService, EntityManager entityManager, UfsMccRepository ufsMccRepository) {
        super(loggerService, entityManager);
        this.ufsMccRepository = ufsMccRepository;
    }


    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsMcc>> create(@Valid @RequestBody UfsMcc ufsMcc) {
        ResponseWrapper responseWrapper = new ResponseWrapper();

        UfsMcc ufsMcc1 = ufsMccRepository.findByName(ufsMcc.getName());
        if (ufsMcc1 != null) {
            responseWrapper.setCode(HttpStatus.CONFLICT.value());
            responseWrapper.setMessage(ufsMcc.getName()+" Mcc Name already exist");

            return ResponseEntity.ok(responseWrapper);
        }
        return super.create(ufsMcc);
    }
}
