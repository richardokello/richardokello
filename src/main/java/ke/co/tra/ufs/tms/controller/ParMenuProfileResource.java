package ke.co.tra.ufs.tms.controller;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.co.tra.ufs.tms.entities.ParMenuProfile;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import ke.co.tra.ufs.tms.entities.wrappers.MenuFileRequest;
import ke.co.tra.ufs.tms.service.ParFileMenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.math.BigDecimal;

@RequestMapping("/menu-profiles")
@RestController
public class ParMenuProfileResource extends ChasisResource<ParMenuProfile, BigDecimal, UfsEdittedRecord> {
    private final ParFileMenuService parFileMenuService;

    public ParMenuProfileResource(LoggerService loggerService, EntityManager entityManager, ParFileMenuService parFileMenuService) {
        super(loggerService, entityManager);
        this.parFileMenuService = parFileMenuService;
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/generate")
    public ResponseEntity<ResponseWrapper> generateMenuFile(@Valid @RequestBody MenuFileRequest fileRequest) {
        ResponseWrapper<Object> wrapper = new ResponseWrapper<>();
        parFileMenuService.generateMenuFileAsync(fileRequest, "/home/kenn/");
        return ResponseEntity.ok(wrapper);
    }
}
