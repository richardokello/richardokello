package ke.co.tra.ufs.tms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import ke.co.tra.ufs.tms.config.messageSource.Message;
import ke.co.tra.ufs.tms.entities.TmsEstateHierarchy;
import ke.co.tra.ufs.tms.entities.TmsEstateItem;
import ke.co.tra.ufs.tms.entities.UfsProduct;
import ke.co.tra.ufs.tms.entities.wrappers.ActionWrapper;
import ke.co.tra.ufs.tms.entities.wrappers.BusinessUnitItemWrapper;
import ke.co.tra.ufs.tms.entities.wrappers.filters.BusinessUnitFilter;
import ke.co.tra.ufs.tms.entities.wrappers.filters.MakeFilter;
import ke.co.tra.ufs.tms.repository.SupportRepository;
import ke.co.tra.ufs.tms.service.*;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import ke.co.tra.ufs.tms.utils.exceptions.GeneralBadRequest;
import ke.co.tra.ufs.tms.wrappers.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;

/**
 *
 * @author Owori Juma
 */
@RestController
@RequestMapping("/business-units")
@Api(value = "Business Units Management")
public class BusinessUnitResource {
    
    private final LoggerServiceLocal loggerService;
    private final SupportRepository supportRepo;
    private final ConfigService configService;
    private SupportService supportService;
    private final MasterRecordService recordService;
    private final ProductService productService;
    private final BusinessUnitService businessUnitService;
    private final Logger log;
    private final DeviceService deviceService;
    private final Message messageLocale;
    
    public BusinessUnitResource(LoggerServiceLocal loggerService, SupportRepository supportRepo, ConfigService configService, MasterRecordService recordService, ProductService productService, BusinessUnitService businessUnitService, DeviceService deviceService, Message messageLocale) {
        this.loggerService = loggerService;
        this.supportRepo = supportRepo;
        this.configService = configService;
        this.recordService = recordService;
        this.productService = productService;
        this.businessUnitService = businessUnitService;
        this.messageLocale = messageLocale;
        log = LoggerFactory.getLogger(this.getClass());
        this.deviceService = deviceService;
    }
    
    @ApiOperation(value = "Create Business Unit", notes = "used to create a business unit within the system")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
        ,
        @ApiResponse(code = 404, message = "Business Unit with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<ResponseWrapper> createBusinessUnit(@ApiParam(value = "Ignore status and unitId it will be used when fetching products")
            @Valid @RequestBody TmsEstateHierarchy businessUnit, BindingResult validation) {
        ResponseWrapper response = new ResponseWrapper();
        if (validation.hasErrors()) {
            loggerService.logCreate("Creating new Business Unit failed due to validation errors", SharedMethods.getEntityName(TmsEstateHierarchy.class), businessUnit.getUnitId(), AppConstants.STATUS_FAILED);
            response.setCode(400);
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        
        try {
            this.validateBusinessUnitAddons(businessUnit, false);
        } catch (GeneralBadRequest ex) {
            response.setMessage(ex.getMessage());
            response.setCode(ex.getHttpStatus().value());
            return new ResponseEntity(response, ex.getHttpStatus());
        }
        
        BigInteger level = getNextUnitLevel(businessUnit);
        
        businessUnit.setUnitId(null);//avoid update
        businessUnit.setLevelNo(level);
        businessUnit.setStatus(AppConstants.STATUS_NEW);
        businessUnit.setAction(AppConstants.ACTIVITY_CREATE);
        businessUnit.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        businessUnit.setIntrash(AppConstants.NO);
        businessUnitService.saveUnit(businessUnit);
        
        response.setData(businessUnit);
        loggerService.logCreate("Creating new Business Unit", SharedMethods.getEntityName(TmsEstateHierarchy.class), businessUnit.getUnitId(), AppConstants.STATUS_COMPLETED);
        response.setCode(201);
        
        return new ResponseEntity(response, HttpStatus.CREATED);
    }
    
    private void validateBusinessUnitAddons(TmsEstateHierarchy businessUnit, boolean isUpdate) throws GeneralBadRequest {
        if (isUpdate) {
        } else {
            if (businessUnitService.findByUnitName(businessUnit.getUnitName(), businessUnit.getProductId()) != null) {
                loggerService.logCreate("Creating new Business Unit failed due to the provided"
                        + " Business Unit name exists (Name: " + businessUnit.getUnitName() + ")", SharedMethods.getEntityName(TmsEstateHierarchy.class), businessUnit.getUnitId(), AppConstants.STATUS_FAILED);
                throw new GeneralBadRequest("Unit Name is already in use", HttpStatus.CONFLICT);
            }
            
            if (businessUnitService.findByLevelNo(businessUnit.getLevelNo(), businessUnit.getProductId()) != null) {
                loggerService.logCreate("Creating new Business Unit failed due to the provided"
                        + " Business Unit level exists (Name: " + businessUnit.getLevelNo() + ")", SharedMethods.getEntityName(TmsEstateHierarchy.class), businessUnit.getUnitId(), AppConstants.STATUS_FAILED);
                throw new GeneralBadRequest(AppConstants.UNIT_LEVEL_IN_USE, HttpStatus.CONFLICT);
            }
        }
    }
    
    @ApiOperation(value = "Approve a Business Unit", notes = "used to approve a business unit that has already been created")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
        ,
        @ApiResponse(code = 404, message = "Business Unit with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/approve-actions")
    @Transactional
    public ResponseEntity<ResponseWrapper> approveBusinessUnit(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        ArrayList<String> errors = new ArrayList<>();
        for (BigDecimal id : action.getIds()) {

            TmsEstateHierarchy businessUnit = businessUnitService.getUnit(id).get();

            loggerService.logApprove("Approving a Unit  (Id: " + id + ")",
                    SharedMethods.getEntityName(TmsEstateHierarchy.class), id, AppConstants.STATUS_PENDING, action.getNotes());
            if (businessUnit == null) {
                String messageR = "Failed to approve Unit (unit id: " + id + "). Unit doesn't exist";
                loggerService.logApprove(messageR,
                        SharedMethods.getEntityName(TmsEstateHierarchy.class), id, AppConstants.STATUS_FAILED, action.getNotes());
                errors.add(messageLocale.setMessage(AppConstants.FAILED_TO_APPROVE_UNIT) + " (unit id: " + id + ")");
                continue;
            }
            if(loggerService.isInitiator(TmsEstateHierarchy.class.getSimpleName(), id, businessUnit.getAction())) {
                errors.add(messageLocale.setMessage(AppConstants.MAKER_CANNOT_APPROVE_RECORD_WITH_ID)+" (" + businessUnit.getUnitName() + ")");
                loggerService.logUpdate("Failed to approve Estate hierarchy (" + businessUnit.getUnitName() + "). Maker can't approve their own record", SharedMethods.getEntityName(TmsEstateHierarchy.class), id, AppConstants.STATUS_FAILED);
                continue;
            }
            businessUnit.setStatus(AppConstants.STATUS_ACTIVE);
            businessUnit.setAction(AppConstants.ACTIVITY_CREATE);
            businessUnit.setActionStatus(AppConstants.STATUS_APPROVED);
            businessUnit.setIntrash(AppConstants.NO);
            businessUnitService.saveUnit(businessUnit);
            loggerService.logApprove("Approved (" + businessUnit.getUnitName() + ") successfully",
                    SharedMethods.getEntityName(UfsProduct.class), id, AppConstants.STATUS_COMPLETED, action.getNotes());
        }
        
        if (errors.size() > 0) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setMessage(AppConstants.CHECKER_GENERAL_ERROR);
            response.setData(errors);
            return new ResponseEntity(response, HttpStatus.MULTI_STATUS);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    @ApiOperation(value = "Reject a Business Unit", notes = "used to reject a product that has already been created")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
        ,
        @ApiResponse(code = 404, message = "Business Unit with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/decline-actions")
    @Transactional
    public ResponseEntity<ResponseWrapper> declineBusinessUnits(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        ArrayList<String> errors = new ArrayList<>();
        for (BigDecimal id : action.getIds()) {
            loggerService.logLock("Rejecting a estate hierarchy  (Id: " + id + ")",
                    SharedMethods.getEntityName(TmsEstateHierarchy.class), id, AppConstants.STATUS_PENDING, action.getNotes());
            TmsEstateHierarchy businessUnit = businessUnitService.getUnit(id).get();
            if (businessUnit == null) {
                String message = "Failed to reject hierarchy (unit id: " + id + "). unit doesn't exist";
                loggerService.logDeactivate(message,
                        SharedMethods.getEntityName(TmsEstateHierarchy.class), id, AppConstants.STATUS_FAILED, action.getNotes());
                errors.add(messageLocale.setMessage(AppConstants.FAILED_TO_REJECT_HIERARCHY)+ " (unit id: " + id + ")");
                continue;
            }
            if(loggerService.isInitiator(TmsEstateHierarchy.class.getSimpleName(), id, businessUnit.getAction())) {
                errors.add("Sorry maker can't approve their own record (" + businessUnit.getUnitName() + ")");
                loggerService.logUpdate("Failed to approve Estate hierarchy (" + businessUnit.getUnitName() + "). Maker can't approve their own record", SharedMethods.getEntityName(TmsEstateHierarchy.class), id, AppConstants.STATUS_FAILED);
                continue;
            }
            businessUnit.setAction(AppConstants.ACTIVITY_CREATE);
            businessUnit.setActionStatus(AppConstants.STATUS_DECLINED);
            businessUnit.setIntrash(AppConstants.YES);
            businessUnitService.saveUnit(businessUnit);
            
            loggerService.logDeactivate("Rejected (" + businessUnit.getUnitId() + ") successfully",
                    SharedMethods.getEntityName(TmsEstateHierarchy.class), id, AppConstants.STATUS_COMPLETED, action.getNotes());
        }
        
        if (errors.size() > 0) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setMessage(AppConstants.CHECKER_GENERAL_ERROR);
            response.setData(errors);
            return new ResponseEntity(response, HttpStatus.MULTI_STATUS);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    @ApiOperation(value = "Update Business Unit", notes = "used to update a business unit within the system")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
        ,
        @ApiResponse(code = 404, message = "Business Unit with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity<ResponseWrapper> updateBusinessUnits(
            @Valid @RequestBody TmsEstateHierarchy businessUnit, BindingResult validation) {
        ResponseWrapper response = new ResponseWrapper();
        if (validation.hasErrors()) {
            loggerService.logUpdate("Updating Business unit failed due to validation errors", SharedMethods.getEntityName(TmsEstateHierarchy.class), businessUnit.getUnitId(), AppConstants.STATUS_FAILED);
            response.setCode(400);
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        
        TmsEstateHierarchy dbUser = businessUnitService.findByUnitId(businessUnit.getUnitId(), businessUnit.getProductId());
        if (dbUser == null) {
            loggerService.logUpdate("Updating Business Unit (unit id: " + businessUnit.getUnitId()
                    + ") failed due to unit not found", SharedMethods.getEntityName(TmsEstateHierarchy.class), businessUnit.getUnitId(), AppConstants.STATUS_FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(messageLocale.setMessage(AppConstants.BUSINESS_WITH_ID_NOT_FOUND));
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        
        try {
            this.validateBusinessUnitAddons(businessUnit, true);
        } catch (GeneralBadRequest ex) {
            response.setMessage(ex.getMessage());
            response.setCode(ex.getHttpStatus().value());
            return new ResponseEntity(response, ex.getHttpStatus());
        }
        dbUser.setUnitName(businessUnit.getUnitName());
        dbUser.setAction(AppConstants.ACTIVITY_UPDATE);
        dbUser.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        dbUser.setIntrash(AppConstants.NO);
        businessUnitService.saveUnit(dbUser);
        
        response.setData(businessUnit);
        loggerService.logCreate("updating Business Unit", SharedMethods.getEntityName(TmsEstateHierarchy.class), businessUnit.getUnitId(), AppConstants.STATUS_COMPLETED);
        response.setCode(201);
        
        return new ResponseEntity(response, HttpStatus.CREATED);
    }
    
    @ApiOperation(value = "Fetch Business Units", notes = "Used to fetch all business Unit")
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
    @Transactional
    public ResponseEntity<ResponseWrapper<Page<TmsEstateHierarchy>>> fetchBusinessUnits(@Valid BusinessUnitFilter filter, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(businessUnitService.fetchBusinessUnitsExclude(filter.getStatus(), filter.getActionStatus(), filter.getFrom(), filter.getTo(), filter.getProductId(), pg));
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    @ApiOperation(value = "Fetch Business Units By Id", notes = "Used to fetch a business Unit by itemId")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @Transactional
    public ResponseEntity<ResponseWrapper<TmsEstateHierarchy>> fetchBusinessUnitsbyItemId(@PathVariable("id") BigDecimal id) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(businessUnitService.getUnit(id));
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    @ApiOperation(value = "Fetch Business Units By Product Id", notes = "Used to fetch a business Unit by product Id")
    @RequestMapping(method = RequestMethod.GET, value = "/product/{id}")
    @Transactional
    public ResponseEntity<ResponseWrapper<Page<UfsProduct>>> fetchBusinessUnitsbyproductId(@PathVariable("id") BigDecimal id, Pageable pg,
            @Valid @ApiParam(value = "Entity filters and search parameters") MakeFilter filter) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(businessUnitService.getUnitItem(productService.getProduct(id).get(), pg));
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    @ApiOperation(value = "Create Estate", notes = "used to create a business unit item within the system")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
        ,
        @ApiResponse(code = 404, message = "Estate with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/unititems")
    @Transactional
    public ResponseEntity<ResponseWrapper> createBusinessUnitItem(@ApiParam(value = "Ignore status and unititemId it will be used when fetching products")
            @Valid @RequestBody BusinessUnitItemWrapper unitItemWrapper, BindingResult validation) {
        ResponseWrapper response = new ResponseWrapper();
        
        if (validation.hasErrors()) {
            loggerService.logCreate("Creating new Estate due to validation errors", SharedMethods.getEntityName(TmsEstateHierarchy.class), unitItemWrapper.getUnitId(), AppConstants.STATUS_FAILED);
            response.setCode(400);
            response.setMessage(messageLocale.setMessage(AppConstants.VALIDATION_ERROR));
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        
        TmsEstateHierarchy bunit = businessUnitService.getUnit(unitItemWrapper.getUnitId()).get();
        log.debug("level Passed " + bunit.getLevelNo());
        
        TmsEstateHierarchy businessUnit = businessUnitService.findByLevelNo(bunit.getLevelNo().add(BigInteger.ONE), bunit.getProductId());
        
        if (businessUnit == null) {
            loggerService.logCreate("Not allowed to add an Estate, You have reached your Hierarchy limit, kindly Update your organization hierarchy before proceeding ", SharedMethods.getEntityName(TmsEstateHierarchy.class), bunit.getUnitId(), AppConstants.STATUS_FAILED);
            response.setCode(403);
            response.setMessage(messageLocale.setMessage(AppConstants.HIERARCHY_LIMIT_REACHED));
            return new ResponseEntity(response, HttpStatus.FORBIDDEN);
        }
        
        TmsEstateItem parent = businessUnitService.getUnitItem(unitItemWrapper.getParentId()).get();
        
        TmsEstateItem businessUnitItem = new TmsEstateItem();
        businessUnitItem.setUnitId(businessUnit);
        businessUnitItem.setName(unitItemWrapper.getName());
        businessUnitItem.setDescription(unitItemWrapper.getDescription());
        businessUnitItem.setIsParent(BigInteger.ONE);
        businessUnitItem.setParentId(parent);
        try {
            this.validateBusinessUnititemAddons(businessUnitItem, false);
        } catch (GeneralBadRequest ex) {
            response.setMessage(ex.getMessage());
            response.setCode(ex.getHttpStatus().value());
            return new ResponseEntity(response, ex.getHttpStatus());
        }
        
        businessUnitItem.setUnitItemId(null);
        businessUnitItem.setStatus(AppConstants.STATUS_NEW);
        businessUnitItem.setAction(AppConstants.ACTIVITY_CREATE);
        businessUnitItem.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        businessUnitItem.setIntrash(AppConstants.NO);
        businessUnitService.saveUnitItem(businessUnitItem);
        
        response.setData(businessUnitItem);
        loggerService.logCreate("Creating new Estate", SharedMethods.getEntityName(TmsEstateItem.class), businessUnitItem.getUnitItemId(), AppConstants.STATUS_COMPLETED);
        response.setCode(201);
        
        return new ResponseEntity(response, HttpStatus.CREATED);
    }
    
    private void validateBusinessUnititemAddons(TmsEstateItem businessUnitItem, boolean isUpdate) throws GeneralBadRequest {
        if (isUpdate) {
        } else {
            if (businessUnitService.findByLevelAndName(businessUnitItem.getUnitId(), businessUnitItem.getName()) != null) {
                loggerService.logCreate("Creating new Estate failed due to the provided"
                        + " Estate name exists (Name: " + businessUnitItem.getName() + ")", SharedMethods.getEntityName(TmsEstateItem.class), businessUnitItem.getUnitItemId(), AppConstants.STATUS_FAILED);
                throw new GeneralBadRequest(messageLocale.setMessage(AppConstants.ESTATE_NAME_IN_USE), HttpStatus.CONFLICT);
            }            
        }
    }
    
    @ApiOperation(value = "Update Estate", notes = "used to update a business unit item within the system")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
        ,
        @ApiResponse(code = 404, message = "Business Unit item with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/unititems")
    @Transactional
    public ResponseEntity<ResponseWrapper> updateBusinessUnitsItem(@Valid @RequestBody BusinessUnitItemWrapper unitItemWrapper, BindingResult validation) {
        ResponseWrapper response = new ResponseWrapper();
        if (validation.hasErrors()) {
            loggerService.logUpdate("Upating Business unit failed due to validation errors", SharedMethods.getEntityName(TmsEstateItem.class), unitItemWrapper.getUnitItemId(), AppConstants.STATUS_FAILED);
            response.setCode(400);
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        
        TmsEstateItem dbUser = businessUnitService.findByLevelAndName(businessUnitService.getUnit(unitItemWrapper.getUnitId()).get(), unitItemWrapper.getName());
        if (dbUser == null) {
            loggerService.logUpdate("Updating Business Unit Item (unit id: " + unitItemWrapper.getUnitId()
                    + ") failed due to unit not found", SharedMethods.getEntityName(TmsEstateItem.class), unitItemWrapper.getUnitItemId(), AppConstants.STATUS_FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(messageLocale.setMessage(AppConstants.BUSINESS_WITH_ID_NOT_FOUND));
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        //check if user has pending approvals
        /*if (dbUser.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
            loggerService.logUpdate("Updating business unit Item (" + businessUnitItem.getName() + ") failed. Uni Item has unapproved actions", SharedMethods.getEntityName(TmsEstateItem.class), businessUnitItem.getUnitItemId(), AppConstants.STATUS_FAILED);
            response.setCode(HttpStatus.EXPECTATION_FAILED.value());
            response.setMessage("Sorry the business unit item has pending unapproved actions");
            return new ResponseEntity(response, HttpStatus.EXPECTATION_FAILED);
        }*/
        
        TmsEstateItem businessUnitItem = businessUnitService.getUnitItem(unitItemWrapper.getUnitItemId()).get();
        businessUnitItem.setName(unitItemWrapper.getName());
        businessUnitItem.setDescription(unitItemWrapper.getDescription());
        businessUnitItem.setUnitId(businessUnitService.getUnit(unitItemWrapper.getUnitId()).get());
        
        try {
            this.validateBusinessUnititemAddons(businessUnitItem, true);
        } catch (GeneralBadRequest ex) {
            response.setMessage(ex.getMessage());
            response.setCode(ex.getHttpStatus().value());
            return new ResponseEntity(response, ex.getHttpStatus());
        }
        
        businessUnitItem.setAction(AppConstants.ACTIVITY_UPDATE);
        businessUnitItem.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        businessUnitItem.setIntrash(AppConstants.NO);
        businessUnitService.saveUnitItem(businessUnitItem);
        
        response.setData(businessUnitItem);
        loggerService.logCreate("updating Business Unit", SharedMethods.getEntityName(TmsEstateItem.class), businessUnitItem.getUnitItemId(), AppConstants.STATUS_COMPLETED);
        response.setCode(201);
        
        return new ResponseEntity(response, HttpStatus.CREATED);
    }
    
    @ApiOperation(value = "Approve a Business Unit Item", notes = "used to approve a business unit item that has already been created")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
        ,
        @ApiResponse(code = 404, message = "Business Unit item with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/unititems/approve-actions")
    @Transactional
    public ResponseEntity<ResponseWrapper> approveBusinessUnititem(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        ArrayList<String> errors = new ArrayList<>();
        for (BigDecimal id : action.getIds()) {
            loggerService.logApprove("Approving a Unit item  (Id: " + id + ")",
                    SharedMethods.getEntityName(TmsEstateItem.class), id, AppConstants.STATUS_PENDING, action.getNotes());
            TmsEstateItem businessUnitItem = businessUnitService.getUnitItem(id).get();
            if (businessUnitItem == null) {
                loggerService.logApprove("Failed to approve Unit item (unit id: " + id + "). Unit doesn't exist",
                        SharedMethods.getEntityName(TmsEstateItem.class), id, AppConstants.STATUS_FAILED, action.getNotes());
                errors.add("Failed to approve Unit item (unit id: " + id + "). Unit doesn't exist");
                continue;
            }

            if(loggerService.isInitiator(TmsEstateItem.class.getSimpleName(), id, businessUnitItem.getAction())) {
                errors.add(messageLocale.setMessage(AppConstants.MAKER_CANNOT_APPROVE_RECORD)+" (" + businessUnitItem.getName() + ")");
                loggerService.logUpdate("Failed to approve Estate (" + businessUnitItem.getName() + "). Maker can't approve their own record", SharedMethods.getEntityName(TmsEstateItem.class), id, AppConstants.STATUS_FAILED);
                continue;
            }
            businessUnitItem.setStatus(AppConstants.STATUS_ACTIVE);
            businessUnitItem.setAction(AppConstants.ACTIVITY_UPDATE);
            businessUnitItem.setActionStatus(AppConstants.STATUS_APPROVED);
            businessUnitItem.setIntrash(AppConstants.NO);
            businessUnitService.saveUnitItem(businessUnitItem);            
            
            response.setData(businessUnitItem);
            
            System.out.println("Updated");
            loggerService.logApprove("Approved (" + businessUnitItem.getUnitItemId() + ") successfully",
                    SharedMethods.getEntityName(TmsEstateItem.class), id, AppConstants.STATUS_COMPLETED, action.getNotes());
        }
        
        if (errors.size() > 0) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setMessage(AppConstants.CHECKER_GENERAL_ERROR);
            response.setData(errors);
            return new ResponseEntity(response, HttpStatus.MULTI_STATUS);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    @ApiOperation(value = "Reject a Business Unit item", notes = "used to reject a unit item that has already been created")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
        ,
        @ApiResponse(code = 404, message = "Business Unit item with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/unititems/decline-actions")
    @Transactional
    public ResponseEntity<ResponseWrapper> declineBusinessUnitsItems(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        ArrayList<String> errors = new ArrayList<>();
        for (BigDecimal id : action.getIds()) {
            loggerService.logLock("Rejecting a Business Unit  (Id: " + id + ")",
                    SharedMethods.getEntityName(TmsEstateItem.class), id, AppConstants.STATUS_PENDING, action.getNotes());
            TmsEstateItem businessUnitItem = businessUnitService.getUnitItem(id).get();
            if (businessUnitItem == null) {
                loggerService.logDeactivate("Failed to reject Estate (unit id: " + id + "). unit item doesn't exist",
                        SharedMethods.getEntityName(TmsEstateItem.class), id, AppConstants.STATUS_FAILED, action.getNotes());
                errors.add(messageLocale.setMessage(AppConstants.FAILED_TO_REJECT_BUSINESS_UNIT_ITEM)+ " (unit id: " + id + ")");
                continue;
            }
            if(loggerService.isInitiator(TmsEstateItem.class.getSimpleName(), id, businessUnitItem.getAction())) {
                errors.add(messageLocale.setMessage(AppConstants.MAKER_CANNOT_APPROVE_RECORD)+" (" + businessUnitItem.getName() + ")");
                loggerService.logUpdate("Failed to approve Estate (" + businessUnitItem.getName() + "). Maker can't approve their own record", SharedMethods.getEntityName(TmsEstateItem.class), id, AppConstants.STATUS_FAILED);
                continue;
            }
            businessUnitItem.setAction(AppConstants.ACTIVITY_CREATE);
            businessUnitItem.setActionStatus(AppConstants.STATUS_DECLINED);
            businessUnitItem.setIntrash(AppConstants.YES);
            businessUnitService.saveUnitItem(businessUnitItem);
            
            loggerService.logDeactivate("Rejected (" + businessUnitItem.getUnitItemId() + ") successfully",
                    SharedMethods.getEntityName(TmsEstateItem.class), id, AppConstants.STATUS_COMPLETED, action.getNotes());
        }
        
        if (errors.size() > 0) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setMessage(AppConstants.CHECKER_GENERAL_ERROR);
            response.setData(errors);
            return new ResponseEntity(response, HttpStatus.MULTI_STATUS);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    @ApiOperation(value = "Fetch Business Units Items", notes = "Used to fetch all business Unit items")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
        ,
        @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
        ,
        @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g status")
        ,
        @ApiImplicitParam(name = "dir", dataType = "integer", required = false, value = "Sorting direction e.g desc or asc")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/unititems/{id}")
    public ResponseEntity<ResponseWrapper<TmsEstateItem>> fetchBusinessUnitsItems(@PathVariable("id") BigDecimal id, @Valid BusinessUnitFilter filter, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(businessUnitService.getUnitItem(id));
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    
    @ApiOperation(value = "Fetch Business Units Item By device ID", notes = "Used to fetch all business Unit items")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
        ,
        @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
        ,
        @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g status")
        ,
        @ApiImplicitParam(name = "dir", dataType = "integer", required = false, value = "Sorting direction e.g desc or asc")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/unititems/device/{id}")
    public ResponseEntity<ResponseWrapper<TmsEstateItem>> fetchBusinessUnitsItemsByDeviceId(@PathVariable("id") BigDecimal id, @Valid BusinessUnitFilter filter, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(deviceService.getDevice(id).get().getEstateId());
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    
    
    
    @ApiOperation(value = "Fetch Business Units Items Tree", notes = "Used to fetch all business Unit items in tree")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
        ,
        @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
        ,
        @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g status")
        ,
        @ApiImplicitParam(name = "dir", dataType = "integer", required = false, value = "Sorting direction e.g desc or asc")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/unititems/tree/{id}")
    public ResponseEntity<ResponseWrapper<Page<TmsEstateItem>>> fetchBusinessUnitsItemsTree(@PathVariable("id") BigDecimal id, @Valid BusinessUnitFilter filter, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        
        List<TmsEstateItem> emptyUnits = new ArrayList<>();
        List<TmsEstateHierarchy> businessUnits = businessUnitService.businessUnitsList();
        businessUnits.forEach((t) -> {
            List<TmsEstateItem> businessUnitItems = businessUnitService.businessUnitItemById(t);
            businessUnitItems.forEach((TmsEstateItem bit) -> {
                if (bit.getIsParent().intValue() == 1) {
                    //bit.setTmsEstateItemList(findChildBusinessUnits(bit));
                }
                emptyUnits.add(bit);
            });
            
            t.setTmsEstateItemList(businessUnitItems);
            
        });
        
        response.setData(emptyUnits.stream()
                .distinct()
                .collect(Collectors.toList()));
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    @ApiOperation(value = "Fetch Business Units Items Parents Tree", notes = "Used to fetch all business Unit items parents tree")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
        ,
        @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
        ,
        @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g status")
        ,
        @ApiImplicitParam(name = "dir", dataType = "integer", required = false, value = "Sorting direction e.g desc or asc")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/unititems/parents")
    public ResponseEntity<ResponseWrapper<Page<TmsEstateItem>>> fetchBusinessUnitsItemsParent(@Valid BusinessUnitFilter filter, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(businessUnitService.getParentsUnitItemId());
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    @ApiOperation(value = "Fetch Business Units Items by Parent", notes = "Used to fetch all business Unit items by parents tree")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
        ,
        @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
        ,
        @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g status")
        ,
        @ApiImplicitParam(name = "dir", dataType = "integer", required = false, value = "Sorting direction e.g desc or asc")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/unititems/parents/{id}")
    public ResponseEntity<ResponseWrapper<Page<TmsEstateItem>>> fetchBusinessUnitsItemsByParent(@PathVariable("id") BigDecimal id, @Valid BusinessUnitFilter filter, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(businessUnitService.businessUnitItemByparentId(businessUnitService.getUnitItem(id).get()));
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    @ApiOperation(value = "Fetch Business Units Items Parents with Product ID Tree", notes = "Used to fetch all business Unit items parents tree")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "size", dataType = "integer", required = false, value = "Pagination size e.g 20")
        ,
        @ApiImplicitParam(name = "page", dataType = "integer", required = false, value = "Page number e.g 0")
        ,
        @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "Field name e.g status")
        ,
        @ApiImplicitParam(name = "dir", dataType = "integer", required = false, value = "Sorting direction e.g desc or asc")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/unititems/product/{id}")
    public ResponseEntity<ResponseWrapper<Page<TmsEstateItem>>> fetchBusinessUnitsItemsParentWithProductId(@PathVariable("id") BigDecimal id, @Valid BusinessUnitFilter filter, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        TmsEstateHierarchy businessUnit = businessUnitService.findByLevelNo(BigInteger.ONE, productService.getProduct(id).get());
        if (businessUnit != null) {
            response.setData(businessUnitService.businessUnitItemById(businessUnit));
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    private BigInteger getNextUnitLevel(TmsEstateHierarchy businessUnit) {
        List<TmsEstateHierarchy> tmsEstateHierarchy = businessUnitService.findByproductIdOrderByLevelNo(businessUnit.getProductId());
        if (tmsEstateHierarchy.size() > 0) {
            if (tmsEstateHierarchy.get(0) != null) {
                return tmsEstateHierarchy.get(0).getLevelNo().add(BigInteger.ONE);
            }
        }
        return BigInteger.ONE;
    }
}
