/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;

import ke.co.tra.ufs.tms.config.messageSource.Message;
import ke.co.tra.ufs.tms.entities.TmsDeviceFileExt;
import ke.co.tra.ufs.tms.repository.TmsDeviceFileExtRepository;
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Owori Juma
 */
@RequestMapping(value = "/device/file-extension")
@Api(value = "Device Model File Extensions Resource")
@Controller
public class DeviceFileExtensionResource {

    private final TmsDeviceFileExtRepository deviceFileExtRepository;
    private final LoggerServiceLocal loggerService;
    private final Message message;

    public DeviceFileExtensionResource(TmsDeviceFileExtRepository deviceFileExtRepository, LoggerServiceLocal loggerService, Message message) {
        this.deviceFileExtRepository = deviceFileExtRepository;
        this.loggerService = loggerService;
        this.message = message;
    }

    @ApiOperation(value = "Create Device Model Extension", notes = "used to create a product within the system")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
        ,
        @ApiResponse(code = 404, message = "Device Model Extension with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<ResponseWrapper> createModelExtension(@ApiParam(value = "Ignore status and extensionId it will be used when fetching Extension")
            @Valid @RequestBody TmsDeviceFileExt deviceFileExt, BindingResult validation) {
        ResponseWrapper response = new ResponseWrapper();
        if (validation.hasErrors()) {
            loggerService.logCreate("Creating new Model extension failed due to validation errors", SharedMethods.getEntityName(TmsDeviceFileExt.class), deviceFileExt.getId(), AppConstants.STATUS_FAILED);
            response.setCode(400);
            response.setMessage(message.setMessage(AppConstants.VALIDATION_ERROR));
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }

        TmsDeviceFileExt fileExt = deviceFileExtRepository.findBymodelId(deviceFileExt.getModelId());
        if (fileExt != null) {
            fileExt.setParamFileExt(deviceFileExt.getParamFileExt());
            deviceFileExtRepository.save(fileExt);
        } else {
            deviceFileExtRepository.save(deviceFileExt);
        }

        response.setData(deviceFileExt);
        loggerService.logCreate("Creating new Model extension", SharedMethods.getEntityName(TmsDeviceFileExt.class), deviceFileExt.getId(), AppConstants.STATUS_COMPLETED);
        response.setCode(201);

        return new ResponseEntity(response, HttpStatus.CREATED);
    }

}
