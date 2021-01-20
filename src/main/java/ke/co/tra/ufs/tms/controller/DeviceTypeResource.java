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
import io.swagger.annotations.ApiParam;
import javax.persistence.EntityManager;
import javax.validation.Valid;
import ke.co.tra.ufs.tms.entities.UfsDeviceType;
import ke.co.tra.ufs.tms.entities.wrappers.filters.DeviceTypeFilter;
import ke.co.tra.ufs.tms.repository.SupportRepository;
import ke.co.tra.ufs.tms.service.DeviceService;
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 *
 * @author Cornelius M
 */
@RequestMapping(value = "/device/type")
@Api(value = "Device Type Resource")
@Controller
public class DeviceTypeResource extends ChasisResourceLocal<UfsDeviceType>  {
    
    private final DeviceService deviceService;

    public DeviceTypeResource(DeviceService deviceService, LoggerServiceLocal loggerService,
            EntityManager entityManager, SupportRepository supportRepo) {
        super(loggerService, entityManager, supportRepo);
        this.deviceService = deviceService;
    }
    
    @ApiOperation(value = "Fetch Record")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", dataType = "int", required = false, value = "Pagination size e.g 20", paramType = "query")
        ,
        @ApiImplicitParam(name = "page", dataType = "int", required = false, value = "Page number e.g 0", paramType = "query")
        ,
        @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "status,desc", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Page<UfsDeviceType>>> getType(Pageable pg,
            @Valid @ApiParam(value = "Entity filters and search parameters") DeviceTypeFilter filter) {
        ResponseWrapper<Page<UfsDeviceType>> response = new ResponseWrapper();
        response.setData(deviceService.getType(filter.getActionStatus(), filter.getNeedle(), pg));
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsDeviceType>> create(@Valid @RequestBody UfsDeviceType ufsDeviceType) {
        ResponseWrapper response = new ResponseWrapper();
        List<UfsDeviceType> deviceType = deviceService.findByTypeAndIntrash(ufsDeviceType.getType());
        if (deviceType.size() > 0 ) {
            response.setCode(HttpStatus.CONFLICT.value());
            response.setMessage("Error!, Device type already exist");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        return super.create(ufsDeviceType);
    }
}
