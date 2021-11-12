package ke.co.tra.ufs.tms.controller;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.co.tra.ufs.tms.entities.ParReceiptItems;
import ke.co.tra.ufs.tms.entities.ParReceiptProfile;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import ke.co.tra.ufs.tms.service.ParReceiptProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Objects;

@RequestMapping("/receipt-profiles")
@RestController
public class ParReceiptProfileResource extends ChasisResource<ParReceiptProfile, BigDecimal, UfsEdittedRecord> {

     private final ParReceiptProfileService parReceiptProfileService;

    public ParReceiptProfileResource(LoggerService loggerService, EntityManager entityManager, ParReceiptProfileService parReceiptProfileService) {
        super(loggerService, entityManager);
        this.parReceiptProfileService = parReceiptProfileService;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<ParReceiptProfile>> create(@Valid @RequestBody ParReceiptProfile parReceiptProfile) {
        // check if the receipt profile exists
        ParReceiptProfile receiptProfile = parReceiptProfileService.findByProfileName(parReceiptProfile.getName());
        if (Objects.nonNull(receiptProfile)) {
            ResponseWrapper<ParReceiptProfile> wrapper = new ResponseWrapper<>();
            wrapper.setCode(HttpStatus.CONFLICT.value());
            wrapper.setMessage("Receipt profile already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(wrapper);
        }
        return super.create(parReceiptProfile);
    }
}
