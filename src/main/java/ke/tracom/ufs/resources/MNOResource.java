package ke.tracom.ufs.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tracom.ufs.entities.UfsEdittedRecord;
import ke.tracom.ufs.entities.UfsMno;
import ke.tracom.ufs.repositories.UfsMnoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/mno")
public class MNOResource extends ChasisResource<UfsMno, BigDecimal, UfsEdittedRecord> {

    public final UfsMnoRepository ufsMnoRepository;

    public MNOResource(LoggerService loggerService, EntityManager entityManager, UfsMnoRepository ufsMnoRepository) {
        super(loggerService, entityManager);
        this.ufsMnoRepository = ufsMnoRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsMno>> create(@Valid @RequestBody UfsMno ufsMno) {
        ResponseWrapper wrapper = new ResponseWrapper();
        UfsMno mno = ufsMnoRepository.findByMnoNameAndIntrash(ufsMno.getMnoName(), AppConstants.NO);

        if (mno != null ) {
            wrapper.setCode(HttpStatus.CONFLICT.value());
            wrapper.setMessage(ufsMno.getMnoName()+" Mno already exist");

            return ResponseEntity.status(HttpStatus.CONFLICT).body(wrapper);
        }
        return super.create(ufsMno);
    }
}
