/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.Valid;
import ke.co.tra.ufs.tms.entities.UfsDeviceMake;
import ke.co.tra.ufs.tms.entities.UfsModifiedRecord;
import ke.co.tra.ufs.tms.entities.UfsUserRole;
import ke.co.tra.ufs.tms.entities.wrappers.ActionWrapper;
import ke.co.tra.ufs.tms.entities.wrappers.filters.MakeFilter;
import ke.co.tra.ufs.tms.repository.SupportRepository;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ke.co.tra.ufs.tms.service.DeviceService;
import ke.co.tra.ufs.tms.utils.ErrorList;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import ke.co.tra.ufs.tms.utils.exceptions.ExpectationFailed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;

/**
 *
 * @author Cornelius M
 */
@Controller
@RequestMapping("/device/make")
@Api(value = "Device Make Resource")
public class MakeResource {

    private final LoggerServiceLocal loggerService;
    private final DeviceService deviceService;
    private final SupportRepository supportRepo;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public MakeResource(LoggerServiceLocal loggerService, DeviceService deviceService,
                        SupportRepository supportRepo) {
        this.loggerService = loggerService;
        this.deviceService = deviceService;
        this.supportRepo = supportRepo;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Create Device Make")
    public ResponseEntity<ResponseWrapper<UfsDeviceMake>> createMake(@RequestBody @Valid UfsDeviceMake make) {
        ResponseWrapper<UfsDeviceMake> response = new ResponseWrapper();

        List<UfsDeviceMake> deviceMakesList = deviceService.findAllByDeviceMake(make.getMake());
        if (deviceMakesList.size() > 0 ) {
            response.setCode(HttpStatus.CONFLICT.value());
            response.setMessage("Error!, Device make already exist");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }


        make.setAction(AppConstants.ACTIVITY_CREATE);
        make.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        make.setIntrash(AppConstants.NO);
        make = deviceService.saveMake(make);
        response.setData(make);
        response.setCode(201);
        loggerService.logCreate("Created new terminal make (" + make.getMake() + ")",
                UfsDeviceMake.class.getSimpleName(), make.getMakeId(), AppConstants.STATUS_COMPLETED);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{makeId}")
    @ApiOperation(value = "Fetch device make by id")
    public ResponseEntity<ResponseWrapper<UfsDeviceMake>> getMake(@PathVariable("makeId") BigDecimal makeId) {
        ResponseWrapper<UfsDeviceMake> response = new ResponseWrapper();
        response.setData(deviceService.getMake(makeId));
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Fetch Device Make")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", dataType = "int", required = false, value = "Pagination size e.g 20", paramType = "query")
        ,
        @ApiImplicitParam(name = "page", dataType = "int", required = false, value = "Page number e.g 0", paramType = "query")
        ,
        @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "status,desc", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Page<UfsDeviceMake>>> getMake(Pageable pg,
            @Valid @ApiParam(value = "Entity filters and search parameters") MakeFilter filter) {
        ResponseWrapper<Page<UfsDeviceMake>> response = new ResponseWrapper();
        response.setData(deviceService.getMake(filter.getActionStatus(), filter.getNeedle(), pg));
        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ApiOperation(value = "Update Device Make")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Device not found")
    })
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsDeviceMake>> updateMake(@RequestBody UfsDeviceMake make) throws IllegalAccessException, JsonProcessingException {
        ResponseWrapper<UfsDeviceMake> response = new ResponseWrapper();
        UfsDeviceMake dbMake = deviceService.getMake(make.getMakeId());
        if (dbMake == null) {
            loggerService.logUpdate("Updating device make (" + make.getMake()
                    + ") failed due to make doesn't exist", UfsDeviceMake.class.getSimpleName(), make.getMakeId(), AppConstants.STATUS_FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Sorry failed to locate device with the specified id");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } 
        /*
        else if (dbMake.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
            loggerService.logUpdate("Updating device make (" + make.getMake()
                    + ") failed due to make has unapproved actions", UfsDeviceMake.class.getSimpleName(), make.getMakeId(), AppConstants.STATUS_FAILED);
            response.setCode(HttpStatus.EXPECTATION_FAILED.value());
            response.setMessage("Sorry device make has Unapproved actions");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
        }*/

        dbMake.setAction(AppConstants.ACTIVITY_UPDATE);
        dbMake.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        deviceService.saveMake(dbMake);

        supportRepo.setMapping(UfsDeviceMake.class);
        if (supportRepo.handleEditRequest(make, UfsModifiedRecord.class) == false) {
            response.setCode(HttpStatus.EXPECTATION_FAILED.value());
            response.setMessage("Sorry device make has not been modified");
            return new ResponseEntity(response, HttpStatus.EXPECTATION_FAILED);
        } else {
            response.setData(make);
        }
        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{makeId}/changes")
    @ApiOperation(value = "Fetch Device Make Changes")
    public ResponseEntity<ResponseWrapper<List<String>>> fetchChanges(@PathVariable("makeId") BigDecimal makeId) {
        ResponseWrapper<List<String>> response = new ResponseWrapper();
        supportRepo.setMapping(UfsDeviceMake.class);
        response.setData(supportRepo.fetchChanges(makeId, UfsModifiedRecord.class));
        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete Device Make")
    public ResponseEntity<ResponseWrapper> deleteMake(@RequestBody @Valid ActionWrapper<BigDecimal> actions) {
        ResponseWrapper response = new ResponseWrapper();
        List<String> errors = new ErrorList();
        for (BigDecimal id : actions.getIds()) {
            UfsDeviceMake dbMake = deviceService.getMake(id);
            if (dbMake == null) {
                loggerService.logDelete("Failed to delete device make (id " + id + "). Failed to locate make with specified id",
                        UfsDeviceMake.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                errors.add("Device make with id " + id + " doesn't exist");
            } 
            /*
            else if (dbMake.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                loggerService.logDelete("Failed to delete device make ( " + dbMake.getMake() + "). Record has unapproved actions",
                        UfsDeviceMake.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                errors.add("Record (" + dbMake.getMake() + ") has unapproved actions");
            }*/
            
            else {
                dbMake.setAction(AppConstants.ACTIVITY_DELETE);
                dbMake.setActionStatus(AppConstants.STATUS_UNAPPROVED);
                loggerService.logDelete("Deleted device make (" + dbMake.getMake() + ") successfully", UfsDeviceMake.class.getSimpleName(), id, AppConstants.STATUS_COMPLETED);
            }
        }

        if (errors.isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setData(errors);
            response.setMessage(AppConstants.CHECKER_GENERAL_ERROR);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        }
    }

    @ApiOperation(value = "Approve Device Make Actions")
    @RequestMapping(value = "/approve-actions", method = RequestMethod.PUT)
    public ResponseEntity<ResponseWrapper> approveActions(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        List<String> errors = new ErrorList();
        for (BigDecimal id : action.getIds()) {
            UfsDeviceMake dbMake = deviceService.getMake(id);
            try {
                if (dbMake == null) {
                    loggerService.logApprove("Failed to approve device make (id " + id + "). Failed to locate make with specified id",
                            UfsDeviceMake.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                    errors.add("Device make with id " + id + " doesn't exist");
                    continue;
                } else if (loggerService.isInitiator(UfsDeviceMake.class.getSimpleName(), id, dbMake.getAction())) {
                    errors.add("Sorry maker can't approve their own record (" + dbMake.getMake() + ")");
                    loggerService.logApprove("Failed to approve device make (" + dbMake.getMake() + "). Maker can't approve their own record", SharedMethods.getEntityName(UfsDeviceMake.class), id, AppConstants.STATUS_FAILED);
                    continue;
                } else if (dbMake.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_CREATE)
                        && dbMake.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process new record
                    this.processApproveNew(dbMake, action.getNotes());
                } else if (dbMake.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE)
                        && dbMake.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process updated record
                    this.processApproveChanges(dbMake, action.getNotes());
                } else if (dbMake.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DELETE)
                        && dbMake.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                    this.processApproveDeletion(dbMake, action.getNotes());
                } else {
                    loggerService.logUpdate("Failed to approve record (" + dbMake.getMake() + "). Record doesn't have approve actions",
                            SharedMethods.getEntityName(UfsDeviceMake.class), id, AppConstants.STATUS_FAILED);
                    errors.add("Record doesn't have approve actions");
                }
                dbMake.setActionStatus(AppConstants.STATUS_APPROVED);
                deviceService.saveMake(dbMake);
            } catch (ExpectationFailed ex) {
                errors.add(ex.getMessage());
            }
        }
        if (errors.isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setData(errors);
            response.setMessage(AppConstants.CHECKER_GENERAL_ERROR);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        }
    }

    /**
     * Approve new records
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processApproveNew(UfsDeviceMake entity, String notes) throws ExpectationFailed {
        loggerService.logApprove("Done approving new Device make (" + entity.getMake() + ")",
                SharedMethods.getEntityName(UfsDeviceMake.class), entity.getMakeId(), AppConstants.STATUS_COMPLETED, notes);
    }

    /**
     * Approve edit changes
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processApproveChanges(UfsDeviceMake entity, String notes) throws ExpectationFailed {
        try {
            supportRepo.setMapping(UfsDeviceMake.class);
            if ((UfsDeviceMake) supportRepo.mergeChanges(entity.getMakeId(), UfsModifiedRecord.class) == null) {
                throw new ExpectationFailed("Failed to approve Device make (" + entity.getMake() + "). Changes not found");
            }
        } catch (IOException | IllegalArgumentException | IllegalAccessException ex) {
            log.error(AppConstants.AUDIT_LOG, "Failed to approve record changes", ex);
            throw new ExpectationFailed("Failed to approve record changes please contact the administrator for more help");
        }
        loggerService.logApprove("Done approving Device Make (" + entity.getMake() + ") changes",
                SharedMethods.getEntityName(UfsUserRole.class), entity.getMakeId(),
                AppConstants.STATUS_COMPLETED, notes);
    }

    /**
     * Approve Deletion
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processApproveDeletion(UfsDeviceMake entity, String notes) throws ExpectationFailed {
        entity.setIntrash(AppConstants.YES);
        loggerService.logApprove("Done approving device make (" + entity.getMake() + ") deletion.",
                SharedMethods.getEntityName(UfsDeviceMake.class), entity.getMakeId(),
                AppConstants.STATUS_COMPLETED, notes);
    }
    
    @ApiOperation(value = "Approve Device Make Actions")
    @RequestMapping(value = "/decline-actions", method = RequestMethod.PUT)
    public ResponseEntity<ResponseWrapper> declineActions(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        List<String> errors = new ErrorList();
        for (BigDecimal id : action.getIds()) {
            UfsDeviceMake dbMake = deviceService.getMake(id);
            try {
                if (dbMake == null) {
                    loggerService.logDelete("Failed to decline device make (id " + id + ") actions. Failed to locate make with specified id",
                            UfsDeviceMake.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                    errors.add("Device make with id " + id + " doesn't exist");
                    continue;
                } else if (loggerService.isInitiator(UfsDeviceMake.class.getSimpleName(), id, dbMake.getAction())) {
                    errors.add("Sorry maker can't decline their own record (" + dbMake.getMake() + ")");
                    loggerService.logUpdate("Failed to decline device make (" + dbMake.getMake() + "). Maker can't decline their own record", SharedMethods.getEntityName(UfsUserRole.class), id, AppConstants.STATUS_FAILED);
                    continue;
                } else if (dbMake.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_CREATE)
                        && dbMake.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process new record
                    this.processDeclineNew(dbMake, action.getNotes());
                } else if (dbMake.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE)
                        && dbMake.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process updated record
                    this.processDeclineChanges(dbMake, action.getNotes());
                } else if (dbMake.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DELETE)
                        && dbMake.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                    this.processDeclineDeletion(dbMake, action.getNotes());
                } else {
                    loggerService.logUpdate("Failed to decline record (" + dbMake.getMake() + "). Record doesn't have approve actions",
                            SharedMethods.getEntityName(UfsDeviceMake.class), id, AppConstants.STATUS_FAILED);
                    errors.add("Record doesn't have approve actions");
                }
                dbMake.setActionStatus(AppConstants.STATUS_DECLINED);
                deviceService.saveMake(dbMake);
            } catch (ExpectationFailed ex) {
                errors.add(ex.getMessage());
            }
        }
        if (errors.isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setData(errors);
            response.setMessage(AppConstants.CHECKER_GENERAL_ERROR);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        }
    }
    
    /**
     * Approve new records
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processDeclineNew(UfsDeviceMake entity, String notes) throws ExpectationFailed {
        loggerService.logApprove("Declined new Device make (" + entity.getMake() + ")",
                SharedMethods.getEntityName(UfsDeviceMake.class), entity.getMakeId(), AppConstants.STATUS_COMPLETED, notes);
    }

    /**
     * Approve edit changes
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processDeclineChanges(UfsDeviceMake entity, String notes) throws ExpectationFailed {
        try {
            supportRepo.setMapping(UfsDeviceMake.class);
            entity = (UfsDeviceMake) supportRepo.declineChanges(entity, UfsModifiedRecord.class);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            log.error(AppConstants.AUDIT_LOG, "Failed to decline record changes", ex);
            throw new ExpectationFailed("Failed to decline record changes please contact the administrator for more help");
        }
        loggerService.logApprove("Done declining Device Make (" + entity.getMake() + ") changes",
                SharedMethods.getEntityName(UfsUserRole.class), entity.getMakeId(),
                AppConstants.STATUS_COMPLETED, notes);
    }

    /**
     * Approve Deletion
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processDeclineDeletion(UfsDeviceMake entity, String notes) throws ExpectationFailed {
        loggerService.logApprove("Done declining device make (" + entity.getMake() + ") deletion.",
                SharedMethods.getEntityName(UfsDeviceMake.class), entity.getMakeId(),
                AppConstants.STATUS_COMPLETED, notes);
    }


}
