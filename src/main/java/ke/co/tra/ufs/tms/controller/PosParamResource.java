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
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.Valid;

import ke.co.tra.ufs.tms.entities.TmsDeviceParam;
import ke.co.tra.ufs.tms.entities.TmsParamDefinition;
import ke.co.tra.ufs.tms.entities.UfsProduct;
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;
import ke.co.tra.ufs.tms.service.ProductService;
import ke.co.tra.ufs.tms.service.TmsDeviceParamService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import ke.co.tra.ufs.tms.utils.exceptions.GeneralBadRequest;
import ke.co.tra.ufs.tms.utils.exceptions.NotFoundException;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Owori Juma
 */
@RestController
@RequestMapping("/posparam")
@Api(value = "Pos Param Configuration")
public class PosParamResource {

    private final TmsDeviceParamService deviceParamService;
    private final ProductService productService;
    private final LoggerServiceLocal loggerService;

    public PosParamResource(TmsDeviceParamService deviceParamService, ProductService productService, LoggerServiceLocal loggerService) {
        this.deviceParamService = deviceParamService;
        this.productService = productService;
        this.loggerService = loggerService;
    }

    @ApiOperation(value = "Fetch Configurations")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", dataType = "int", required = false, value = "Pagination size e.g 20", paramType = "query")
        ,
        @ApiImplicitParam(name = "page", dataType = "int", required = false, value = "Page number e.g 0", paramType = "query")
        ,
        @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "status,desc", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<ResponseWrapper<List<TmsParamDefinition>>> getModel(@PathVariable("id") BigDecimal id) throws NotFoundException {
        ResponseWrapper<List<TmsParamDefinition>> response = new ResponseWrapper();
        UfsProduct product = productService.getProduct(id).get();
        if (product != null) {
            response.setData(deviceParamService.getDefinitions(product));
        } else {
            throw new NotFoundException("Product not Found", HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Create Device Param", notes = "used to create a device parameter within the system")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
        ,
        @ApiResponse(code = 404, message = "Device parameter with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<ResponseWrapper> createDeviceParameter(@ApiParam(value = "Ignore status and deviceparamId it will be used when fetching MNOs")
            @Valid @RequestBody TmsDeviceParam tmsDeviceParam, BindingResult validation) {
        ResponseWrapper response = new ResponseWrapper();
        if (validation.hasErrors()) {
            loggerService.logCreate("Creating new Device Param failed due to validation errors", SharedMethods.getEntityName(TmsDeviceParam.class), tmsDeviceParam.getParamId(), AppConstants.STATUS_FAILED);
            response.setCode(400);
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }

        try {
            this.validateDeviceParamAddons(tmsDeviceParam, false);
        } catch (GeneralBadRequest ex) {
            response.setMessage(ex.getMessage());
            response.setCode(ex.getHttpStatus().value());
            return new ResponseEntity(response, ex.getHttpStatus());
        }

        tmsDeviceParam.setParamId(null);
        deviceParamService.saveDeviceParam(tmsDeviceParam);

        response.setData(tmsDeviceParam);
        loggerService.logCreate("Creating new Device", SharedMethods.getEntityName(TmsDeviceParam.class), tmsDeviceParam.getParamId(), AppConstants.STATUS_COMPLETED);
        response.setCode(201);

        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    private void validateDeviceParamAddons(TmsDeviceParam tmsDeviceParam, boolean isUpdate) throws GeneralBadRequest {

    }

}
