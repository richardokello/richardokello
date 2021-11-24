package ke.tra.ufs.webportal.resources;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.exceptions.ExpectationFailed;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ActionWrapper;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsCustomerOutlet;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.filters.CustomerIdFilter;
import ke.tra.ufs.webportal.service.TmsDeviceService;
import ke.tra.ufs.webportal.service.UfsCustomerOutletService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/customer-outlet")
public class UfsCustomerOutletResource extends ChasisResource<UfsCustomerOutlet, Long, UfsEdittedRecord> {

    private final TmsDeviceService deviceService;
    private final UfsCustomerOutletService customerOutletService;

    public UfsCustomerOutletResource(LoggerService loggerService, EntityManager entityManager, TmsDeviceService deviceService, UfsCustomerOutletService customerOutletService) {
        super(loggerService, entityManager);
        this.deviceService = deviceService;
        this.customerOutletService = customerOutletService;
    }


    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper> approveActions(@RequestBody @Valid ActionWrapper<Long> actions) throws ExpectationFailed {
        ResponseEntity<ResponseWrapper> resp = super.approveActions(actions);
        if (!resp.getStatusCode().equals(HttpStatus.OK)) {
            return resp;
        } else {
            // approve devices by outlet Ids
            List<Long> outletsIds = Arrays.stream(actions.getIds()).collect(Collectors.toList());
            deviceService.activateDevicesByOutletsIds(outletsIds, actions.getNotes());
            deviceService.addDevicesTaskByOutletsIds(outletsIds);
            deviceService.updateDeviceOwnerByOutletId(outletsIds);
        }
        return resp;
    }

    @ApiOperation(value = "Fetch outlets belonging to a certain customer")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "int", required = false, value = "Pagination size e.g 20", paramType = "query")
            ,
            @ApiImplicitParam(name = "page", dataType = "int", required = false, value = "Page number e.g 0", paramType = "query")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "status,desc", paramType = "query")
    })
    @RequestMapping(value = "/customer", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Page<UfsCustomerOutlet>>> getOutletByCustomerId(@Valid CustomerIdFilter filter, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(this.customerOutletService.getOutletByCustomerId(filter.getActionStatus(),
                filter.getCustomerIds(),filter.getFrom(),filter.getTo(),filter.getNeedle(), pg));
        return ResponseEntity.ok(response);

    }
}
