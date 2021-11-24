package ke.co.tra.ufs.tms.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.co.tra.ufs.tms.entities.ParGlobalConfigFormValues;
import ke.co.tra.ufs.tms.entities.UfsEdittedRecord;
import ke.co.tra.ufs.tms.entities.wrappers.filters.ParCommonFilter;
import ke.co.tra.ufs.tms.service.ParGlobalConfigFormValuesService;
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
@RequestMapping("/global-config-forms")
public class ParGlobalConfigFormValuesResource extends ChasisResource<ParGlobalConfigFormValues, BigDecimal, UfsEdittedRecord> {

    private final ParGlobalConfigFormValuesService configFormValuesService;

    public ParGlobalConfigFormValuesResource(LoggerService loggerService, EntityManager entityManager, ParGlobalConfigFormValuesService configFormValuesService) {
        super(loggerService, entityManager);
        this.configFormValuesService = configFormValuesService;
    }

//    @Override
//    public ResponseEntity<ResponseWrapper<ParGlobalConfigFormValues>> create(@Valid @RequestBody ParGlobalConfigFormValues parGlobalConfigFormValues) {
//        // check if type exist
//        Optional<ParGlobalConfigFormValues> optional = parGlobalConfigFormValuesService.findFormValue(parGlobalConfigFormValues.getId());
//        if (optional.isPresent()) {
//            ResponseWrapper<ParGlobalConfigFormValues> wrapper = new ResponseWrapper<>();
//            ParGlobalConfigFormValues formValues = optional.get();
//            formValues.setFormValues(parGlobalConfigFormValues.getFormValues());
//            formValues = parGlobalConfigFormValuesService.save(parGlobalConfigFormValues);
//            wrapper.setData(formValues);
//            return ResponseEntity.ok(wrapper);
//        } else {
//            // if new create
//            return super.create(parGlobalConfigFormValues);
//        }
//    }

    @ApiOperation(value = "Fetch menu items belonging to a certain customer type")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "int", required = false, value = "Pagination size e.g 20", paramType = "query")
            ,
            @ApiImplicitParam(name = "page", dataType = "int", required = false, value = "Page number e.g 0", paramType = "query")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "status,desc", paramType = "query")
    })
    @RequestMapping(value = "/config-type", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Page<ParGlobalConfigFormValues>>> getConfigFormByCustomertype(@Valid ParCommonFilter filter, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(this.configFormValuesService.getConfigFormByConfigType(filter.getActionStatus(),
                filter.getType(),filter.getFrom(), filter.getTo(),filter.getNeedle(), pg));
        return ResponseEntity.ok(response);

    }
}
