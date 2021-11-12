package ke.co.tra.ufs.tms.controller;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.co.tra.ufs.tms.entities.ParReceiptItems;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import ke.co.tra.ufs.tms.service.ParReceiptItemsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Objects;


@Controller
@RequestMapping("/receipt-items")
public class ParReceiptItemsResource extends ChasisResource<ParReceiptItems, BigDecimal, UfsEdittedRecord> {

    private final ParReceiptItemsService parReceiptItemsService;

    public ParReceiptItemsResource(LoggerService loggerService, EntityManager entityManager, ParReceiptItemsService parReceiptItemsService) {
        super(loggerService, entityManager);
        this.parReceiptItemsService = parReceiptItemsService;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<ParReceiptItems>> create(@Valid @RequestBody ParReceiptItems parReceiptItems) {
        // check if the receipt item exists
        ParReceiptItems receiptItem = parReceiptItemsService.findByItemName(parReceiptItems.getName());
        if (Objects.nonNull(receiptItem)) {
            ResponseWrapper<ParReceiptItems> wrapper = new ResponseWrapper<>();
            wrapper.setCode(HttpStatus.CONFLICT.value());
            wrapper.setMessage("Receipt item already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(wrapper);
        }
        return super.create(parReceiptItems);
    }
}
