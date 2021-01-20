/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import ke.co.tra.ufs.tms.entities.TmsScheduler;
import ke.co.tra.ufs.tms.entities.wrappers.filters.ScheduleFilter;
import ke.co.tra.ufs.tms.service.DeviceService;
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;
import ke.co.tra.ufs.tms.service.ProductService;
import ke.co.tra.ufs.tms.service.SchedulerService;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author ojuma
 */
@Controller
@RequestMapping("/device-reports")
@Api(value = "Devices Reports Resource")
public class DeviceReportsResource {    
    private final SchedulerService schedulerService;
    private final DeviceService deviceService;
    private final LoggerServiceLocal loggerService;
    private final ProductService productService;

    public DeviceReportsResource(SchedulerService schedulerService, DeviceService deviceService, LoggerServiceLocal loggerService, ProductService productService) {
        this.schedulerService = schedulerService;
        this.deviceService = deviceService;
        this.loggerService = loggerService;
        this.productService = productService;
    }
    
    
    @ApiOperation(value = "Fetch All Devices Reports", notes = "Used to fetch All devices Reports")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
        ,
        @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
        ,
        @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g status")
        ,
        @ApiImplicitParam(name = "dir", dataType = "integer", required = false, value = "Sorting direction e.g desc or asc")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/all-devices")
    public ResponseEntity<ResponseWrapper<Page<TmsScheduler>>> fetchSchedules(@Valid ScheduleFilter filter, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(schedulerService.findAll(filter.getActionStatus(), filter.getStatus(), filter.getAction(), filter.getDownloadType(), filter.getScheduleType(),filter.getNeedle(), pg));
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
}
