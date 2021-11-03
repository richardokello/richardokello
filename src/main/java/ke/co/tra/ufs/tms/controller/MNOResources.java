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
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import ke.co.tra.ufs.tms.entities.UfsCurrency;
import ke.co.tra.ufs.tms.entities.UfsMno;
import ke.co.tra.ufs.tms.entities.wrappers.ActionWrapper;
import ke.co.tra.ufs.tms.entities.wrappers.filters.ProductFilter;
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;
import ke.co.tra.ufs.tms.service.MNOService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import ke.co.tra.ufs.tms.utils.exceptions.GeneralBadRequest;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ke.co.tra.ufs.tms.utils.ErrorList;

/**
 *
 * @author Owori Juma
 */
@RestController
@RequestMapping("/mno")
@Api(value = "Mobile Network Operators Management")
public class MNOResources {
    
    private final LoggerServiceLocal loggerService;
    private final MNOService mNOService;
    
    public MNOResources(LoggerServiceLocal loggerService, MNOService mNOService) {
        this.loggerService = loggerService;
        this.mNOService = mNOService;
    }
    
    @ApiOperation(value = "Create MNO", notes = "used to create a MNO within the system")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
        ,
        @ApiResponse(code = 404, message = "MNO with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<ResponseWrapper> createMNO(@ApiParam(value = "Ignore status and mnoId it will be used when fetching MNOs")
            @Valid @RequestBody UfsMno mno, BindingResult validation) {
        ResponseWrapper response = new ResponseWrapper();
        if (validation.hasErrors()) {
            loggerService.logCreate("Creating new MNO failed due to validation errors", SharedMethods.getEntityName(UfsMno.class), mno.getMnoId(), AppConstants.STATUS_FAILED);
            response.setCode(400);
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        
        try {
            this.validateMNOAddons(mno, false);
        } catch (GeneralBadRequest ex) {
            response.setMessage(ex.getMessage());
            response.setCode(ex.getHttpStatus().value());
            return new ResponseEntity(response, ex.getHttpStatus());
        }
        
        mno.setMnoId(null);
        mno.setAction(AppConstants.ACTIVITY_CREATE);
        mno.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        mno.setIntrash(AppConstants.NO);
        mNOService.saveMno(mno);
        
        response.setData(mno);
        loggerService.logCreate("Creating new MNO", SharedMethods.getEntityName(UfsMno.class), mno.getMnoId(), AppConstants.STATUS_COMPLETED);
        response.setCode(201);
        
        return new ResponseEntity(response, HttpStatus.CREATED);
    }
    
    private void validateMNOAddons(UfsMno mno, boolean isUpdate) throws GeneralBadRequest {
        if (isUpdate) {
        } else {
            if (mNOService.findByMnoName(mno.getMnoName()) != null) {
                loggerService.logCreate("Creating new MNO failed due to the provided"
                        + " MNO Name exists (Name: " + mno.getMnoName() + ")", SharedMethods.getEntityName(UfsMno.class), mno.getMnoId(), AppConstants.STATUS_FAILED);
                throw new GeneralBadRequest("MNO Name is already in use", HttpStatus.CONFLICT);
            }
        }
    }
    
    @ApiOperation(value = "Approve a MNO", notes = "used to approve a MNO that has already been created")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
        ,
        @ApiResponse(code = 404, message = "MNO with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/approve-actions")
    @Transactional
    public ResponseEntity<ResponseWrapper> approveCurrency(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        ArrayList<BigDecimal> errors = new ArrayList<>();
        for (BigDecimal id : action.getIds()) {
            loggerService.logApprove("Approving a MNO  (Id: " + id + ")",
                    SharedMethods.getEntityName(UfsMno.class), id, AppConstants.STATUS_PENDING, action.getNotes());
            UfsMno mno = mNOService.findMno(id).get();
            if (mno == null) {
                loggerService.logApprove("Failed to approve MNO (MNO id: " + id + "). MNO doesn't exist",
                        SharedMethods.getEntityName(UfsMno.class), id, AppConstants.STATUS_FAILED, action.getNotes());
                errors.add(id);
                continue;
            }
            mno.setAction(AppConstants.ACTIVITY_CREATE);
            mno.setActionStatus(AppConstants.STATUS_APPROVED);
            mno.setIntrash(AppConstants.NO);
            mNOService.saveMno(mno);
            
            loggerService.logApprove("Approved (" + mno.getMnoId() + ") successfully",
                    SharedMethods.getEntityName(UfsMno.class), id, AppConstants.STATUS_COMPLETED, action.getNotes());
        }
        
        if (errors.size() > 0) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setMessage(AppConstants.CHECKER_GENERAL_ERROR);
            response.setData(errors);
            return new ResponseEntity(response, HttpStatus.MULTI_STATUS);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    @ApiOperation(value = "Reject a MNO", notes = "used to reject a MNO that has already been created")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
        ,
        @ApiResponse(code = 404, message = "MNO with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/decline-actions")
    @Transactional
    public ResponseEntity<ResponseWrapper> declineCurrency(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        ArrayList<BigDecimal> errors = new ArrayList<>();
        for (BigDecimal id : action.getIds()) {
            loggerService.logLock("Rejecting a MNO  (Id: " + id + ")",
                    SharedMethods.getEntityName(UfsMno.class), id, AppConstants.STATUS_PENDING, action.getNotes());
            UfsMno mno = mNOService.findMno(id).get();
            if (mno == null) {
                loggerService.logDeactivate("Failed to reject MNO (MNO id: " + id + "). MNO doesn't exist",
                        SharedMethods.getEntityName(UfsCurrency.class), id, AppConstants.STATUS_FAILED, action.getNotes());
                errors.add(id);
                continue;
            }
            mno.setActionStatus(AppConstants.STATUS_DECLINED);
            mno.setIntrash(AppConstants.YES);
            mNOService.saveMno(mno);
            
            loggerService.logDeactivate("Rejected (" + mno.getMnoName() + ") successfully",
                    SharedMethods.getEntityName(UfsMno.class), id, AppConstants.STATUS_COMPLETED, action.getNotes());
        }
        
        if (errors.size() > 0) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setMessage(AppConstants.CHECKER_GENERAL_ERROR);
            response.setData(errors);
            return new ResponseEntity(response, HttpStatus.MULTI_STATUS);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    @ApiOperation(value = "Update MNO", notes = "used to update a mno within the system")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
        ,
        @ApiResponse(code = 404, message = "MNO with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity<ResponseWrapper> updateMNO(
            @Valid @RequestBody UfsMno mno, BindingResult validation) {
        ResponseWrapper response = new ResponseWrapper();
        if (validation.hasErrors()) {
            loggerService.logUpdate("Creating new mno failed due to validation errors", SharedMethods.getEntityName(UfsMno.class), mno.getMnoId(), AppConstants.STATUS_FAILED);
            response.setCode(400);
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        
        UfsMno dbUser = mNOService.findByMnoName(mno.getMnoName());
        if (dbUser == null) {
            loggerService.logUpdate("Updating MNO (MNO id: " + mno.getMnoId()
                    + ") failed due to MNO not found", SharedMethods.getEntityName(UfsMno.class), mno.getMnoId(), AppConstants.STATUS_FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Sorry failed to locate product with the specified id");
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        //check if user has pending approvals
        if (dbUser.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
            loggerService.logUpdate("Updating mno (" + mno.getMnoName() + ") failed. Mno has unapproved actions", SharedMethods.getEntityName(UfsMno.class), mno.getMnoId(), AppConstants.STATUS_FAILED);
            response.setCode(HttpStatus.EXPECTATION_FAILED.value());
            response.setMessage("Sorry the user has pending unapproved actions");
            return new ResponseEntity(response, HttpStatus.EXPECTATION_FAILED);
        }
        
        try {
            this.validateMNOAddons(mno, true);
        } catch (GeneralBadRequest ex) {
            response.setMessage(ex.getMessage());
            response.setCode(ex.getHttpStatus().value());
            return new ResponseEntity(response, ex.getHttpStatus());
        }
        
        mno.setAction(AppConstants.ACTIVITY_UPDATE);
        mno.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        mno.setIntrash(AppConstants.NO);
        mNOService.saveMno(mno);
        
        response.setData(mno);
        loggerService.logUpdate("Updating MNO", SharedMethods.getEntityName(UfsMno.class), mno.getMnoId(), AppConstants.STATUS_COMPLETED);
        response.setCode(201);
        
        return new ResponseEntity(response, HttpStatus.CREATED);
    }
    
    @ApiOperation(value = "Fetch MNO", notes = "Used to fetch all MNO")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
        ,
        @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
        ,
        @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g status")
        ,
        @ApiImplicitParam(name = "dir", dataType = "integer", required = false, value = "Sorting direction e.g desc or asc")
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Page<UfsMno>>> fetchMNOs(@Valid ProductFilter filter, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(mNOService.fetchMnosExclude(filter.getActionStatus(), pg));
        return new ResponseEntity(response, HttpStatus.OK);
    }    
    
    @ApiOperation(value = "Fetch MNO by Id", notes = "Used to fetch a MNO")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<ResponseWrapper<UfsMno>> fetchMNObyId(@PathVariable("id") BigDecimal id) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(mNOService.findMno(id));
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete UfsMno")
    public ResponseEntity<ResponseWrapper> deleteUfsMno(@RequestBody @Valid ActionWrapper<BigDecimal> actions) {
        ResponseWrapper response = new ResponseWrapper();
        List<String> errors = new ErrorList();
        for (BigDecimal id : actions.getIds()) {
            UfsMno dbMake = mNOService.findMno(id).get();
            if (dbMake == null) {
                loggerService.logDelete("Failed to delete UfsMno (id " + id + "). Failed to locate make with specified id",
                        UfsMno.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                errors.add("UfsMno with id " + id + " doesn't exist");
            } else {
                dbMake.setAction(AppConstants.ACTIVITY_DELETE);
                dbMake.setActionStatus(AppConstants.STATUS_UNAPPROVED);
                loggerService.logDelete("Deleted UfsMno (" + dbMake.getMnoName() + ") successfully", UfsMno.class.getSimpleName(), id, AppConstants.STATUS_COMPLETED);
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
}
