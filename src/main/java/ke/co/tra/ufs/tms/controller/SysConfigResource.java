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

import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.validation.Valid;
import ke.co.tra.ufs.tms.entities.UfsSysConfig;
import ke.co.tra.ufs.tms.entities.wrappers.ActionWrapper;
import ke.co.tra.ufs.tms.entities.wrappers.filters.ConfigFilter;
import ke.co.tra.ufs.tms.repository.SupportRepository;
import ke.co.tra.ufs.tms.service.ConfigService;
import ke.co.tra.ufs.tms.service.NotificationService;
import ke.co.tra.ufs.tms.service.SysConfigService;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;

/**
 *
 * @author tracom9
 */
@RestController
@RequestMapping("/sys-config")
@Api(value = "System Configuration")
public class SysConfigResource extends  ChasisResourceLocal<UfsSysConfig>{

    private final SysConfigService sysConfigService;
    private final LoggerServiceLocal loggerService;
    private final ConfigService configService;
    private final SharedMethods sharedMethod;
    private final NotificationService notificationService;

    public SysConfigResource(SysConfigService sysConfigService, LoggerServiceLocal loggerService,
            ConfigService configService, SharedMethods sharedMethod, NotificationService notificationService, 
            EntityManager entityManager, SupportRepository supportRepo) {
        super(loggerService, entityManager, supportRepo);
        this.sysConfigService = sysConfigService;
        this.loggerService = loggerService;
        this.configService = configService;
        this.sharedMethod = sharedMethod;
        this.notificationService = notificationService;
    }

    @ApiOperation(value = "Fetch System Config", notes = "Used to fetch a single System Config")
    @RequestMapping(value = "/{sysConfigId}", method = RequestMethod.GET)
    @Override
    public ResponseEntity<ResponseWrapper<UfsSysConfig>> getEntity(@PathVariable("sysConfigId") BigDecimal sysConfigId) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(sysConfigService.fetchSysConfigById(sysConfigId));
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Override
    @RequestMapping(method = RequestMethod.DELETE)
    @ApiIgnore
    public ResponseEntity deleteEntity(ActionWrapper<BigDecimal> actions) {
        throw new UnsupportedOperationException("Sorry deletion not allowed on this entity");
    }
    
    

    @ApiOperation(value = "Fetch System Configs")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
        ,
        @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
        ,
        @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g createdOn")
        ,
        @ApiImplicitParam(name = "dir", dataType = "string", required = false, value = "Sorting direction e.g desc or asc")
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Page<UfsSysConfig>>> fetchSysConfigs(@Valid ConfigFilter filter, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
//        response.setData(sysConfigService.getSysConfigs(pg, true));
        response.setData(sysConfigService.getSysConfigs(filter.getActionStatus(), filter.getEntity(), filter.getNeedle(), pg));
        return new ResponseEntity(response, HttpStatus.OK);

    }

//    @ApiOperation(value = "Fetch System Configs", notes = "Used to fetch System "
//            + "Configs not including password configuration")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
//        ,
//        @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
//        ,
//        @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g createdOn")
//        ,
//        @ApiImplicitParam(name = "dir", dataType = "string", required = false, value = "Sorting direction e.g desc or asc")
//    })
//    @RequestMapping(method = RequestMethod.GET, value = "/search")
//    public ResponseEntity<ResponseWrapper<Page<UfsSysConfig>>> searchSysConfigs(Pageable pg,
//            @RequestParam("needle") String needle) {
//        ResponseWrapper response = new ResponseWrapper();
//        response.setData(sysConfigService.searchSysConfigs(needle, pg));
//        return new ResponseEntity(response, HttpStatus.OK);
//
//    }

//    @RequestMapping(value = "/password", method = RequestMethod.GET)
//    @ApiOperation(value = "Password Configurations", notes = "Used to fetch password configuration")
//    public ResponseEntity<ResponseWrapper<PasswordConfig>> getPasswordConfig() {
//        ResponseWrapper response = new ResponseWrapper();
//        response.setData(sysConfigService.passwordConfigs());
//        return new ResponseEntity(response, HttpStatus.OK);
//    }

//    @RequestMapping(value = "/password", method = RequestMethod.PUT)
//    @ApiOperation(value = "Password Configurations", notes = "Used to update password configuration")
//    public ResponseEntity<ResponseWrapper<PasswordConfig>> updatePasswordConfig(@Valid @RequestBody PasswordConfig config,
//            BindingResult validation) {
//        ResponseWrapper response = new ResponseWrapper();
//        String entityName = SharedMethods.getEntityName(UfsSysConfig.class);
//        if (validation.hasErrors()) {
//            loggerService.logUpdate("Failed to update password configurations due to validation errors", entityName, null, AppConstants.STATUS_FAILED);
//            response.setCode(400);
//            response.setData(SharedMethods.getFieldMapErrors(validation));
//            response.setMessage("Validation errors occured check your input before trying again");
//            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
//        }
//        //check if configurations make sense
//        int length = config.characters + config.lowerCase + config.upperCase + config.specialChar + config.numeric;
//        if (config.length < length) {
//            loggerService.logUpdate("Failed to update password configurations due to misconfiguration errors", entityName, null, AppConstants.STATUS_FAILED);
//            response.setCode(400);
//            response.setMessage("Sorry the sum of characters, numbers, uppercase, "
//                    + "lowercase and special characters exceeds minimum password length");
//            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
//        }
//        configService.savePasswordConfig(config, loggerService);
//        return new ResponseEntity(response, HttpStatus.OK);
//    }

//    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.POST}, value = "/{configId}")
//    @ApiResponses(value = {
//        @ApiResponse(code = 404, message = "Configuration not found")
//    })
//    @ApiOperation(value = "Configurations", notes = "Used to update configuration")
//    public ResponseEntity<ResponseWrapper> updateConfiguration(@RequestParam("value") String value,
//            @PathVariable("configId") Long configId) {
//        ResponseWrapper response = new ResponseWrapper();
//        String entityName = SharedMethods.getEntityName(UfsSysConfig.class);
//        UfsSysConfig config = sysConfigService.fetchSysConfigById(BigDecimal.valueOf(configId));
//        if (config == null) {
//            loggerService.logUpdate("Failed to update system configurations "
//                    + "due to configuration not found", entityName, null, AppConstants.STATUS_FAILED);
//            response.setCode(404);
//            response.setMessage("Configuration not found");
//            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
//        }
//        config.setValue(value);
//        config = sysConfigService.saveSysConfig(config);
//        loggerService.logUpdate("Updating system configurations completed successfully", entityName, config.getId().toString(), AppConstants.STATUS_COMPLETED);
//        return new ResponseEntity(response, HttpStatus.OK);
//    }

//    @ApiOperation(value = "Fetch System Configs By Entity", notes = "Used to fetch System "
//            + "Configs By Entity not including password configuration")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
//        ,
//        @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
//        ,
//        @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g createdOn")
//        ,
//        @ApiImplicitParam(name = "dir", dataType = "string", required = false, value = "Sorting direction e.g desc or asc")
//    })
//    @RequestMapping(method = RequestMethod.GET, value = "/entity/{entity}")
//    public ResponseEntity<ResponseWrapper<Page<UfsSysConfig>>> fetchEntitySysConfigs(@PathVariable("entity") String entity,
//            Pageable pg) {
//        ResponseWrapper response = new ResponseWrapper();
//        if (entity.equalsIgnoreCase("system")) {
//            entity = AppConstants.ENTITY_GLOBAL_INTEGRATION;
//        } else if (entity.equalsIgnoreCase("integration")) {
//            entity = AppConstants.ENTITY_SYSTEM_INTEGRATION;
//        }
//        response.setData(sysConfigService.getSysConfigs(pg, entity, false));
//        return new ResponseEntity(response, HttpStatus.OK);
//
//    }   

}
