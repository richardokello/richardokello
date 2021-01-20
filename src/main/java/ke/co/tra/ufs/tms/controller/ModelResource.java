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

import ke.co.tra.ufs.tms.entities.UfsDeviceModel;
import ke.co.tra.ufs.tms.entities.wrappers.filters.ModelFilters;
import ke.co.tra.ufs.tms.repository.SupportRepository;
import ke.co.tra.ufs.tms.repository.TmsDeviceFileExtRepository;
import ke.co.tra.ufs.tms.service.DeviceService;
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ke.co.tra.ufs.tms.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;

/**
 *
 * @author Cornelius M
 */
@RequestMapping(value = "/device/model")
@Api(value = "Model Resource")
@Controller
public class ModelResource extends ChasisResourceLocal<UfsDeviceModel> {
    
    private final DeviceService deviceService;
    private final TmsDeviceFileExtRepository deviceFileExtRepository;
    
    public ModelResource(LoggerServiceLocal loggerService, EntityManager entityManager,
                         SupportRepository supportRepo, DeviceService deviceService, TmsDeviceFileExtRepository deviceFileExtRepository) {
        super(loggerService, entityManager, supportRepo);
        this.deviceService = deviceService;
        this.deviceFileExtRepository = deviceFileExtRepository;
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
    public ResponseEntity<ResponseWrapper<Page<UfsDeviceModel>>> getModel(Pageable pg,
            @Valid @ApiParam(value = "Entity filters and search parameters") ModelFilters filter) {
        ResponseWrapper<Page<UfsDeviceModel>> response = new ResponseWrapper();
        response.setData(deviceService.getModel(filter.getActionStatus(), filter.getMakeId(), filter.getDeviceTypeId(), filter.getNeedle(), pg));
        return ResponseEntity.ok(response);
    }
/*
    @Override
    public ResponseEntity<ResponseWrapper<UfsDeviceModel>> create(UfsDeviceModel model) {
        ResponseWrapper<UfsDeviceModel> response = new ResponseWrapper();
        model.setAction(AppConstants.ACTIVITY_CREATE);
        model.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        model.setIntrash(AppConstants.NO);
        model = deviceService.saveModel(model);
        response.setData(model);
        response.setCode(201);
        loggerService.logCreate("Created new terminal Model (" + model.getMake() + ")",
                UfsDeviceModel.class.getSimpleName(), model.getModelId(), AppConstants.STATUS_COMPLETED);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
        //return super.create(t); //To change body of generated methods, choose Tools | Templates.
    }*/
    
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    @ApiOperation(value = "Create Device Model")
    public ResponseEntity<ResponseWrapper<UfsDeviceModel>> createMake(@RequestBody @Valid UfsDeviceModel model) {
        ResponseWrapper<UfsDeviceModel> response = new ResponseWrapper();
        UfsDeviceModel modelDb = deviceService.getModelByName(model.getModel());
        if(Objects.nonNull(modelDb)){
            response.setMessage("Model Name Already Exists");
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(response);
        }
        model.setAction(AppConstants.ACTIVITY_CREATE);
        model.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        model.setIntrash(AppConstants.NO);
        deviceService.saveModel(model);
        
        
        /*TmsDeviceFileExt fileExt = new TmsDeviceFileExt();
        fileExt.setModel(model);
        fileExt.setParamFileExt(model.getpFileExt());
        deviceFileExtRepository.save(fileExt);*/
        
        response.setData(model);
        response.setCode(201);
        loggerService.logCreate("Created new terminal Model (" + model.getMake() + ")",
                UfsDeviceModel.class.getSimpleName(), model.getModelId(), AppConstants.STATUS_COMPLETED);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    

}
