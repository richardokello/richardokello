package ke.tra.ufs.webportal.resources;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.views.VwRevenueCollected;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.ParseException;

@RestController
@RequestMapping("/revenue_collected")
public class RevenueCollectedResources extends ChasisResource<VwRevenueCollected, BigDecimal, UfsEdittedRecord> {

    public RevenueCollectedResources(LoggerService loggerService, EntityManager entityManager){
        super(loggerService, entityManager);
    }


}
