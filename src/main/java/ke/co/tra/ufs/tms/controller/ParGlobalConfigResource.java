package ke.co.tra.ufs.tms.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.co.tra.ufs.tms.entities.ParGlobalConfig;
import ke.co.tra.ufs.tms.entities.ParGlobalConfigFormValues;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import ke.co.tra.ufs.tms.entities.wrappers.filters.ParCommonFilter;
import ke.co.tra.ufs.tms.service.ParGlobalConfigService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("global-configs")
public class ParGlobalConfigResource extends ChasisResource<ParGlobalConfig, BigDecimal, UfsEdittedRecord> {

    private final ParGlobalConfigService globalConfigService;

    public ParGlobalConfigResource(LoggerService loggerService, EntityManager entityManager, ParGlobalConfigService globalConfigService) {
        super(loggerService, entityManager);
        this.globalConfigService = globalConfigService;
    }

    @ApiOperation(value = "Fetch config belonging to a certain profile")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "int", required = false, value = "Pagination size e.g 20", paramType = "query")
            ,
            @ApiImplicitParam(name = "page", dataType = "int", required = false, value = "Page number e.g 0", paramType = "query")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "status,desc", paramType = "query")
    })
    @RequestMapping(value = "/config-profile", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Page<ParGlobalConfigFormValues>>> getGlobalConfigByProfile(@Valid ParCommonFilter filter, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(this.globalConfigService.getGlobalConfigByProfile(filter.getActionStatus(),
                filter.getProfile(),filter.getFrom(), filter.getTo(),filter.getNeedle(), pg));
        return ResponseEntity.ok(response);

    }
}
