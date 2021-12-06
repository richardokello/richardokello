/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

import ke.co.tra.ufs.tms.config.messageSource.Message;
import ke.co.tra.ufs.tms.entities.UfsDepartment;
import ke.co.tra.ufs.tms.entities.UfsModifiedRecord;
import ke.co.tra.ufs.tms.entities.wrappers.ActionWrapper;
import ke.co.tra.ufs.tms.entities.wrappers.filters.CommonFilter;
import ke.co.tra.ufs.tms.repository.SupportRepository;
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;
import ke.co.tra.ufs.tms.service.MasterRecordService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.utils.ErrorList;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import ke.co.tra.ufs.tms.utils.exceptions.ExpectationFailed;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(value = "/departments")
@Api(value = "Departments")
public class DepartmentResource{
    
    private final Logger log;
    private final LoggerServiceLocal loggerService;
    private final MasterRecordService recordService;
    private final SupportRepository supportRepo;
    private final Message message;
    
    public DepartmentResource(LoggerServiceLocal loggerService, MasterRecordService recordService, SupportRepository supportRepo, Message message) {
        this.loggerService = loggerService;
        this.recordService = recordService;
        this.message = message;
        this.log = LoggerFactory.getLogger(this.getClass());
        this.supportRepo = supportRepo;
    }
    
    @ApiOperation(value = "Create Department")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ResponseWrapper<UfsDepartment>> create(@Valid @RequestBody UfsDepartment department) {
        ResponseWrapper response = new ResponseWrapper();
        //check if department name exists
        UfsDepartment dbDeprt = recordService.getDepartment(department.getDepartmentName());
        if (dbDeprt != null) {
            response.setCode(409);
            response.setMessage(message.setMessage(AppConstants.RECORD_WITH_SIMILAR_NAME_EXIST));
            return new ResponseEntity(response, HttpStatus.CONFLICT);
        }
        department.setId(null);
        department.setAction(AppConstants.ACTIVITY_CREATE);
        department.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        department.setIntrash(AppConstants.NO);
        response.setCode(201);
        response.setData(recordService.saveDepartment(department));
        loggerService.logCreate("Done creating department ", SharedMethods.getEntityName(UfsDepartment.class), department.getId(), AppConstants.STATUS_COMPLETED);
        return new ResponseEntity(response, HttpStatus.CREATED);
    }
    
    @ApiOperation(value = "View Departments")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Page<UfsDepartment>>> fetch(Pageable pg,
            @Valid @ApiParam(value = "Entity filters and search parameters") CommonFilter filter) {
        ResponseWrapper<Page<UfsDepartment>> response = new ResponseWrapper();
        response.setData(recordService.getDepartments(filter.getActionStatus(), filter.getNeedle(),filter.getFrom(),filter.getTo(), pg));
        return new ResponseEntity(response, HttpStatus.OK);
        
    }
    
    @ApiOperation(value = "Fetch Departments")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<ResponseWrapper<UfsDepartment>> fetchDepartment(@PathVariable("id") BigDecimal id) {
        ResponseWrapper<UfsDepartment> response = new ResponseWrapper();
        response.setData(recordService.getDepartment(id));
        return new ResponseEntity(response, HttpStatus.OK);
        
    }
    
    @ApiOperation(value = "Update Departments")
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<ResponseWrapper<UfsDepartment>> update(@Valid @RequestBody UfsDepartment department) throws JsonProcessingException, IllegalAccessException {
        ResponseWrapper response = new ResponseWrapper();
        //Check if department exists
        UfsDepartment dbDepartment = recordService.getDepartment(department.getId());
        if (dbDepartment == null) {
            loggerService.logUpdate("Updating department failed. Department with specified id ( "
                    + department.getId() + ") doesn't exist", SharedMethods.getEntityName(UfsDepartment.class), department.getId(), AppConstants.STATUS_FAILED);
            response.setCode(404);
            response.setMessage(message.setMessage(AppConstants.RECORD_WITH_ID_NOT_FOUND));
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        /*dbDepartment.setAction(AppConstants.ACTIVITY_UPDATE);
        dbDepartment.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        dbDepartment.setDepartmentName(department.getDepartmentName());
        dbDepartment.setDescription(department.getDescription());
        response.setCode(200);
        response.setData(recordService.saveDepartment(dbDepartment));
        loggerService.logUpdate("Done updating department (" + department.getDepartmentName() + ")", SharedMethods.getEntityName(UfsDepartment.class), department.getId(), AppConstants.STATUS_COMPLETED);
        return new ResponseEntity(response, HttpStatus.CREATED);*/

        dbDepartment.setAction(AppConstants.ACTIVITY_UPDATE);
        dbDepartment.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        recordService.saveDepartment(dbDepartment);

        supportRepo.setMapping(UfsDepartment.class);
        if (supportRepo.handleEditRequest(department, UfsModifiedRecord.class) == false) {
            response.setCode(HttpStatus.EXPECTATION_FAILED.value());
            response.setMessage(message.setMessage(AppConstants.RECORD_NOT_MODIFIED));
            return new ResponseEntity(response, HttpStatus.EXPECTATION_FAILED);
        } else {
            response.setData(department);
            loggerService.logUpdate("Updated  ("+ department.getDepartmentName() + ")department successfully.", SharedMethods.getEntityName(UfsDepartment.class), department.getId(), AppConstants.STATUS_COMPLETED);

        }
        return ResponseEntity.ok(response);
    }
    
    @ApiOperation(value = "Delete Departments")
    @ApiResponses(value = {
        @ApiResponse(code = 207, message = "Some Department(s) don't exist")
    })
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<ResponseWrapper> deleteDepartment(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        ArrayList<BigDecimal> errors = new ArrayList<>();
        for (BigDecimal id : action.getIds()) {
            UfsDepartment department = this.recordService.getDepartment(id);
            if (department == null) {
                loggerService.logUpdate("Failed to delete Department (id: "
                        + id + "). Department doesn't exist", SharedMethods.getEntityName(UfsDepartment.class), id,
                        AppConstants.STATUS_FAILED, action.getNotes());
                errors.add(id);
                continue;
            }
            department.setAction(AppConstants.ACTIVITY_DELETE);
            department.setActionStatus(AppConstants.STATUS_UNAPPROVED);
            this.recordService.saveDepartment(department);
            loggerService.logUpdate("Done deleting Department (" + department.getDepartmentName() + ")",
                    SharedMethods.getEntityName(UfsDepartment.class), id, AppConstants.STATUS_COMPLETED, action.getNotes());
        }
        if (errors.size() > 0) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setMessage(message.setMessage(AppConstants.SOME_RECORDS_ID_NOT_FOUND)+" " + errors.toString());
            response.setData(errors);
            return new ResponseEntity(response, HttpStatus.MULTI_STATUS);
        } else {
            return new ResponseEntity(response, HttpStatus.OK);
        }
    }
    
    @ApiOperation(value = "Approve Device Make Actions")
    @RequestMapping(value = "/approve-actions", method = RequestMethod.PUT)
    public ResponseEntity<ResponseWrapper> approveActions(@RequestBody @Valid ActionWrapper<BigDecimal> action) throws ExpectationFailed {
        ResponseWrapper response = new ResponseWrapper();
        List<String> errors = new ErrorList();
        for (BigDecimal id : action.getIds()) {
            UfsDepartment department = this.recordService.getDepartment(id);
            if (department == null) {
                loggerService.logUpdate("Failed to approve department (id " + id + "). Failed to locate department with specified id",
                        UfsDepartment.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                errors.add(message.setMessage(AppConstants.RECORD_WITH_ID_NOT_FOUND) + " "+ id);
                continue;
            } else if (loggerService.isInitiator(SharedMethods.getEntityName(UfsDepartment.class), id, department.getAction())) {
                errors.add(message.setMessage(AppConstants.MAKER_CANNOT_APPROVE_RECORD_WITH_ID) + " " + id);
                loggerService.logApprove("Failed to approve department (" + id + "). You can't approve your own record", UfsDepartment.class.getSimpleName(), id, AppConstants.STATUS_FAILED, action.getNotes());
                continue;
            }else if (department.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_CREATE)
                    && department.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process new record
                this.processApproveNew(department, action.getNotes());
            }else if (department.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE)
                && department.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process updated record
                this.processApproveChanges(department, action.getNotes());
            }else if (department.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DELETE)
                    && department.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                this.processApproveDeletion(department, action.getNotes());
            } else {

                loggerService.logUpdate("Failed to approve record (" + department.getDepartmentName()+ "). Record doesn't have approve actions",
                        SharedMethods.getEntityName(UfsDepartment.class), id, AppConstants.STATUS_FAILED);
                errors.add(message.setMessage(AppConstants.NO_APPROPRIATE_ACTION));

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


    @ApiOperation(value = "Approve Department Actions")
    @RequestMapping(value = "/decline-actions", method = RequestMethod.PUT)
    public ResponseEntity<ResponseWrapper> declineActions(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        List<String> errors = new ErrorList();
        for (BigDecimal id : action.getIds()) {
            UfsDepartment department = this.recordService.getDepartment(id);
            try {
                if (department == null) {
                    loggerService.logDelete("Failed to decline department (id " + id + ") actions. Failed to locate department with specified id",
                            UfsDepartment.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                    errors.add(message.setMessage(AppConstants.RECORD_WITH_ID_NOT_FOUND)+" " + id);
                    continue;
                } else if (loggerService.isInitiator(UfsDepartment.class.getSimpleName(), id, department.getAction())) {
                    errors.add(message.setMessage(AppConstants.MAKER_CANNOT_DECLINE_OWN_RECORD));
                    loggerService.logUpdate("Failed to decline department (" + department.getDepartmentName() + "). Maker can't decline their own record", SharedMethods.getEntityName(UfsDepartment.class), id, AppConstants.STATUS_FAILED);
                    continue;
                } else if (department.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_CREATE)
                        && department.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process new record
                    this.processDeclineNew(department, action.getNotes());
                } else if (department.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE)
                        && department.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {//process updated record
                    this.processDeclineChanges(department, action.getNotes());
                } else if (department.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DELETE)
                        && department.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                    this.processDeclineDeletion(department, action.getNotes());
                } else {
                    loggerService.logUpdate("Failed to decline record (" + department.getDepartmentName() + "). Record doesn't have approve actions",
                            SharedMethods.getEntityName(UfsDepartment.class), id, AppConstants.STATUS_FAILED);
                    errors.add(message.setMessage(AppConstants.NO_APPROPRIATE_ACTION));
                }
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

    @RequestMapping(method = RequestMethod.GET, value = "/{departmentId}/changes")
    @ApiOperation(value = "Fetch Department Changes")
    public ResponseEntity<ResponseWrapper<List<String>>> fetchChanges(@PathVariable("departmentId") BigDecimal departmentId) {
        ResponseWrapper<List<String>> response = new ResponseWrapper();
        supportRepo.setMapping(UfsDepartment.class);
        response.setData(supportRepo.fetchChanges(departmentId, UfsModifiedRecord.class));
        return ResponseEntity.ok(response);
    }


    /**
     * Approve new records
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processApproveNew(UfsDepartment entity, String notes) throws ExpectationFailed {
        entity.setActionStatus(AppConstants.STATUS_APPROVED);
        this.recordService.saveDepartment(entity);
        loggerService.logApprove("Done approving new Department (" + entity.getDepartmentName() + ")",
                SharedMethods.getEntityName(UfsDepartment.class), entity.getId(), AppConstants.STATUS_COMPLETED, notes);
    }


    /**
     * Approve edit changes
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processApproveChanges(UfsDepartment entity, String notes) throws ExpectationFailed {
        try {
            supportRepo.setMapping(UfsDepartment.class);
            if ((UfsDepartment) supportRepo.mergeChanges(entity.getId(), UfsModifiedRecord.class) == null) {
                throw new ExpectationFailed(message.setMessage(AppConstants.FAILED_TO_APPROVE) + " "+ entity.getDepartmentName());
            }
        } catch (IOException | IllegalArgumentException | IllegalAccessException ex) {
            log.error(AppConstants.AUDIT_LOG, "Failed to approve record changes", ex);
            throw new ExpectationFailed(message.setMessage(AppConstants.CONTACT_ADMIN));
        }
        entity.setActionStatus(AppConstants.STATUS_APPROVED);
        this.recordService.saveDepartment(entity);
        loggerService.logApprove("Done approving Department (" + entity.getDepartmentName() + ") changes",
                SharedMethods.getEntityName(UfsDepartment.class), entity.getId(),
                AppConstants.STATUS_COMPLETED, notes);
    }
    
    private boolean processApproveDeletion(UfsDepartment department, String notes) {
        department.setIntrash(AppConstants.YES);
        department.setActionStatus(AppConstants.STATUS_APPROVED);
        this.recordService.saveDepartment(department);
        loggerService.logApprove("Completed Approving Department (" + department.getDepartmentName() + ") deletion", SharedMethods.getEntityName(UfsDepartment.class), department.getId(), AppConstants.STATUS_COMPLETED, notes);
        return true;
    }


    /**
     * Decline new records
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processDeclineNew(UfsDepartment entity, String notes) throws ExpectationFailed {
        entity.setActionStatus(AppConstants.STATUS_DECLINED);
        this.recordService.saveDepartment(entity);
        loggerService.logApprove("Declined new Department (" + entity.getDepartmentName() + ")",
                SharedMethods.getEntityName(UfsDepartment.class), entity.getId(), AppConstants.STATUS_COMPLETED, notes);
    }

    /**
     * Decline edit changes
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processDeclineChanges(UfsDepartment entity, String notes) throws ExpectationFailed {
        try {
            supportRepo.setMapping(UfsDepartment.class);
            entity = (UfsDepartment) supportRepo.declineChanges(entity, UfsModifiedRecord.class);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            log.error(AppConstants.AUDIT_LOG, "Failed to decline record changes", ex);
            throw new ExpectationFailed(message.setMessage(AppConstants.CONTACT_ADMIN));
        }
        entity.setActionStatus(AppConstants.STATUS_DECLINED);
        this.recordService.saveDepartment(entity);
        loggerService.logApprove("Done declining Department (" + entity.getDepartmentName() + ") changes",
                SharedMethods.getEntityName(UfsDepartment.class), entity.getId(),
                AppConstants.STATUS_COMPLETED, notes);
    }

    /**
     * Decline Deletion
     *
     * @param entity
     * @param notes
     * @return
     */
    private void processDeclineDeletion(UfsDepartment entity, String notes) throws ExpectationFailed {
        entity.setActionStatus(AppConstants.STATUS_DECLINED);
        this.recordService.saveDepartment(entity);
        loggerService.logApprove("Done declining department (" + entity.getDepartmentName() + ") deletion.",
                SharedMethods.getEntityName(UfsDepartment.class), entity.getId(),
                AppConstants.STATUS_COMPLETED, notes);
    }
}
