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
import javax.validation.Valid;
import ke.co.tra.ufs.tms.entities.TmsEstateHierarchy;
import ke.co.tra.ufs.tms.entities.TmsEstateItem;
import ke.co.tra.ufs.tms.entities.UfsProduct;
import ke.co.tra.ufs.tms.entities.wrappers.ActionWrapper;
import ke.co.tra.ufs.tms.entities.wrappers.filters.ProductFilter;
import ke.co.tra.ufs.tms.repository.SupportRepository;
import ke.co.tra.ufs.tms.service.BusinessUnitService;
import ke.co.tra.ufs.tms.service.ConfigService;
import ke.co.tra.ufs.tms.service.MasterRecordService;
import ke.co.tra.ufs.tms.service.ProductService;
import ke.co.tra.ufs.tms.service.SupportService;
import ke.co.tra.ufs.tms.service.SysConfigService;
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
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;
import ke.co.tra.ufs.tms.service.TmsDeviceParamService;
import ke.co.tra.ufs.tms.utils.ErrorList;
import ke.co.tra.ufs.tms.utils.exceptions.ExpectationFailed;

/**
 *
 * @author Owori Juma
 */
@RestController
@RequestMapping("/products")
@Api(value = "Products Management")
public class ProductsResource {
    
    private final LoggerServiceLocal loggerService;
    private final SupportRepository supportRepo;
    private final ConfigService configService;
    private SupportService supportService;
    private final MasterRecordService recordService;
    private final ProductService productService;
    private final SysConfigService sysconfigService;
    private final BusinessUnitService businessUnitService;
    private final TmsDeviceParamService deviceParamService;
    
    public ProductsResource(LoggerServiceLocal loggerService, SupportRepository supportRepo, ConfigService configService, SupportService supportService, MasterRecordService recordService, ProductService productService, SysConfigService sysconfigService, BusinessUnitService businessUnitService, TmsDeviceParamService deviceParamService) {
        this.loggerService = loggerService;
        this.supportRepo = supportRepo;
        this.configService = configService;
        this.supportService = supportService;
        this.recordService = recordService;
        this.productService = productService;
        this.sysconfigService = sysconfigService;
        this.businessUnitService = businessUnitService;
        this.deviceParamService = deviceParamService;
    }
    
    @ApiOperation(value = "Create Product", notes = "used to create a product within the system")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
        ,
        @ApiResponse(code = 404, message = "Product with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<ResponseWrapper> createProduct(@ApiParam(value = "Ignore status and productID it will be used when fetching products")
            @Valid @RequestBody UfsProduct product, BindingResult validation) {
        ResponseWrapper response = new ResponseWrapper();
        if (validation.hasErrors()) {
            loggerService.logCreate("Creating new product failed due to validation errors", SharedMethods.getEntityName(UfsProduct.class), product.getProductId(), AppConstants.STATUS_FAILED);
            response.setCode(400);
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        
        try {
            this.validateProductAddons(product, false);
        } catch (GeneralBadRequest ex) {
            response.setMessage(ex.getMessage());
            response.setCode(ex.getHttpStatus().value());
            return new ResponseEntity(response, ex.getHttpStatus());
        }
        
        product.setProductId(null);
        product.setStatus(AppConstants.STATUS_NEW);
        product.setAction(AppConstants.ACTIVITY_CREATE);
        product.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        product.setIntrash(AppConstants.NO);
        productService.saveProduct(product);
        
        TmsEstateHierarchy businessUnit = new TmsEstateHierarchy();
        businessUnit.setUnitName("Organization");
        businessUnit.setProductId(product);
        businessUnit.setUnitId(null);//avoid update
        businessUnit.setLevelNo(BigInteger.ONE);
        businessUnit.setStatus(AppConstants.STATUS_NEW);
        businessUnit.setAction(AppConstants.ACTIVITY_CREATE);
        businessUnit.setActionStatus(AppConstants.STATUS_APPROVED);
        businessUnit.setIntrash(AppConstants.NO);
        businessUnitService.saveUnit(businessUnit);
        
        TmsEstateItem businessUnitItem = new TmsEstateItem();
        businessUnitItem.setUnitId(businessUnit);
        businessUnitItem.setName(product.getProductName()+"(root)");
        businessUnitItem.setDescription(product.getDescription());
        businessUnitItem.setIsParent(BigInteger.ONE);
        
        businessUnitItem.setUnitItemId(null);
        businessUnitItem.setStatus(AppConstants.STATUS_NEW);
        businessUnitItem.setAction(AppConstants.ACTIVITY_CREATE);
        businessUnitItem.setActionStatus(AppConstants.STATUS_APPROVED);
        businessUnitItem.setIntrash(AppConstants.NO);
        businessUnitService.saveUnitItem(businessUnitItem);
        
        /*TmsParamDefinition paramDefinition = new TmsParamDefinition();
        paramDefinition.setParamDefId(null);
        paramDefinition.setFileOutputName("CONFIG");
        paramDefinition.setParamType("Confirm Device Configuration Parameters");
        paramDefinition.setParams("[\n"
                + "{\"key\":\"name\", \"datatype\":\"input\", \"label\":\"Merchant Name\", \"required\":true,\"controlType\":\"textbox\"},\n"
                + "{\"key\":\"tid\", \"datatype\":\"input\", \"label\":\"Terminal ID\", \"required\":true,\"controlType\":\"textbox\"},\n"
                + "{\"key\":\"merchantId\", \"datatype\":\"input\", \"label\":\"Merchant ID\", \"required\":true,\"controlType\":\"textbox\"},\n"
                + "{\"key\":\"outletNumber\", \"datatype\":\"input\", \"label\":\"Outlet Number\", \"required\":false,\"controlType\":\"textbox\"},\n"
                + "{\"key\":\"address\", \"datatype\":\"input\", \"label\":\"Address\", \"required\":true,\"controlType\":\"textbox\"},\n"
                + "{\"key\":\"location\", \"datatype\":\"input\", \"label\":\"Location\", \"required\":true,\"controlType\":\"textbox\"},\n"
                + "{\"key\":\"phone\", \"datatype\":\"input\", \"label\":\"Phone Number\", \"required\":true,\"controlType\":\"textbox\"},\n"
                + "{\"key\":\"pmsIp\", \"datatype\":\"input\", \"label\":\"Postilion IP\", \"required\":true,\"controlType\":\"textbox\"},\n"
                + "{\"key\":\"pmsPort\", \"datatype\":\"input\", \"label\":\"Postilion Port\", \"required\":true,\"controlType\":\"textbox\"},\n"
                + "{\"key\":\"netserverIp\", \"datatype\":\"input\", \"label\":\"TMS Server IP\", \"required\":true,\"controlType\":\"textbox\"},\n"
                + "{\"key\":\"NetserverPort\", \"datatype\":\"input\", \"label\":\"TMS Server Port\", \"required\":true,\"controlType\":\"textbox\"},\n"
                + "{\"key\":\"tsyncIp\", \"datatype\":\"input\", \"label\":\"Tsync IP\", \"required\":false,\"controlType\":\"textbox\"},\n"
                + "{\"key\":\"tsyncPort\", \"datatype\":\"input\", \"label\":\"Tsync Port\", \"required\":false,\"controlType\":\"textbox\"},\n"
                + "{\"key\":\"currency\", \"datatype\":\"input\", \"label\":\"Currency\", \"required\":false,\"controlType\":\"textbox\"},\n"
                + "{\"key\":\"currencyValue\", \"datatype\":\"input\", \"label\":\"Currency Value\", \"required\":false,\"controlType\":\"textbox\"},\n"
                + "{\"key\":\"decimalPlace\", \"datatype\":\"input\", \"label\":\"Decimal Place\", \"required\":false,\"controlType\":\"textbox\"},\n"
                + "{\"key\":\"adminPassword\", \"datatype\":\"input\", \"label\":\"Admin Password\", \"required\":true,\"controlType\":\"textbox\"},\n"
                + "{\"key\":\"merchantPassword\", \"datatype\":\"input\", \"label\":\"Merchant Password\", \"required\":true,\"controlType\":\"textbox\"},\n"
                + "{\"key\":\"receiptProfile\", \"datatype\":\"input\", \"label\":\"Receipt Profile\", \"required\":true,\"controlType\":\"textbox\"},\n"
                + "{\"key\":\"transactionCounter\", \"datatype\":\"input\", \"label\":\"Transaction Counter\", \"required\":false,\"controlType\":\"textbox\"}\n"
                + " ]");
        paramDefinition.setProductId(product);
        
        deviceParamService.saveParam(paramDefinition);*/
        
        response.setData(product);
        loggerService.logCreate("Creating new Product", SharedMethods.getEntityName(UfsProduct.class), product.getProductId(), AppConstants.STATUS_COMPLETED);
        response.setCode(201);
        
        return new ResponseEntity(response, HttpStatus.CREATED);
    }
    
    private void validateProductAddons(UfsProduct product, boolean isUpdate) throws GeneralBadRequest {
        if (isUpdate) {
        } else {
            if (productService.findByProductName(product.getProductName()) != null) {
                loggerService.logCreate("Creating new Product failed due to the provided"
                        + " Product name exists (Name: " + product.getProductName() + ")", SharedMethods.getEntityName(UfsProduct.class), product.getProductId(), AppConstants.STATUS_FAILED);
                throw new GeneralBadRequest("Product Name is already in use", HttpStatus.CONFLICT);
            }
        }
    }
    
    @ApiOperation(value = "Approve a Product", notes = "used to approve a product that has already been created")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
        ,
        @ApiResponse(code = 404, message = "Product with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/approve-actions")
    @Transactional
    public ResponseEntity<ResponseWrapper> approveProduct(@RequestBody @Valid ActionWrapper<BigDecimal> action) throws ExpectationFailed {
        ResponseWrapper response = new ResponseWrapper();
        ArrayList<BigDecimal> errors = new ArrayList<>();
        for (BigDecimal id : action.getIds()) {
            loggerService.logApprove("Approving a product  (Id: " + id + ")",
                    SharedMethods.getEntityName(UfsProduct.class), id, AppConstants.STATUS_PENDING, action.getNotes());
            UfsProduct product = productService.getProduct(id).get();

            boolean log = loggerService.isInitiator(UfsProduct.class.getSimpleName(),id,product.getAction());
            if (product == null || log ) {
                loggerService.logApprove("Failed to approve Product (product id: " + id + "). product doesn't exist",
                        SharedMethods.getEntityName(UfsProduct.class), id, AppConstants.STATUS_FAILED, action.getNotes());
                errors.add(id);
                continue;
            } else if (product.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DELETE)
                    && product.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                this.processApproveDeletion(product, action.getNotes());
            }else if (product.getAction().equalsIgnoreCase(ke.axle.chassis.utils.AppConstants.ACTIVITY_UPDATE)) {
                product.setStatus(AppConstants.STATUS_ACTIVE);
                product.setAction(AppConstants.ACTIVITY_UPDATE);
                product.setActionStatus(AppConstants.STATUS_APPROVED);
            } else {
                product.setStatus(AppConstants.STATUS_ACTIVE);
                product.setAction(AppConstants.ACTIVITY_CREATE);
                product.setActionStatus(AppConstants.STATUS_APPROVED);
            }

            productService.saveProduct(product);
            loggerService.logApprove("Approved (" + product.getProductName() + ") successfully",
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
    
    @ApiOperation(value = "Reject a Product", notes = "used to reject a product that has already been created")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
        ,
        @ApiResponse(code = 404, message = "Product with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/decline-actions")
    @Transactional
    public ResponseEntity<ResponseWrapper> declineProduct(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        ArrayList<BigDecimal> errors = new ArrayList<>();
        for (BigDecimal id : action.getIds()) {
            loggerService.logLock("Rejecting a product  (Id: " + id + ")",
                    SharedMethods.getEntityName(UfsProduct.class), id, AppConstants.STATUS_PENDING, action.getNotes());
            UfsProduct product = productService.getProduct(id).get();
            if (product == null) {
                loggerService.logDeactivate("Failed to reject Product (product id: " + id + "). product doesn't exist",
                        SharedMethods.getEntityName(UfsProduct.class), id, AppConstants.STATUS_FAILED, action.getNotes());
                errors.add(id);
                continue;
            }
            product.setAction(AppConstants.ACTIVITY_CREATE);
            product.setActionStatus(AppConstants.STATUS_DECLINED);
            product.setIntrash(AppConstants.YES);
            productService.saveProduct(product);
            loggerService.logDeactivate("Rejected (" + product.getProductName() + ") successfully",
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
    
    @ApiOperation(value = "Update Product", notes = "used to update a product within the system")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
        ,
        @ApiResponse(code = 404, message = "Product with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity<ResponseWrapper> updateProduct(
            @Valid @RequestBody UfsProduct product, BindingResult validation) {
        ResponseWrapper response = new ResponseWrapper();
        if (validation.hasErrors()) {
            loggerService.logUpdate("Creating new product failed due to validation errors", SharedMethods.getEntityName(UfsProduct.class), product.getProductId(), AppConstants.STATUS_FAILED);
            response.setCode(400);
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        
        UfsProduct dbUser = productService.getProduct(product.getProductId()).get();
        if (dbUser == null) {
            loggerService.logUpdate("Updating product (product id: " + product.getProductId()
                    + ") failed due to product not found", SharedMethods.getEntityName(UfsProduct.class), product.getProductId(), AppConstants.STATUS_FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Sorry failed to locate product with the specified id");
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        //check if user has pending approvals
        if (dbUser.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
            loggerService.logUpdate("Updating product (" + product.getProductName() + ") failed. Product has unapproved actions", SharedMethods.getEntityName(UfsProduct.class), product.getProductId(), AppConstants.STATUS_FAILED);
            response.setCode(HttpStatus.EXPECTATION_FAILED.value());
            response.setMessage("Sorry the user has pending unapproved actions");
            return new ResponseEntity(response, HttpStatus.EXPECTATION_FAILED);
        }
        
        try {
            this.validateProductAddons(product, true);
        } catch (GeneralBadRequest ex) {
            response.setMessage(ex.getMessage());
            response.setCode(ex.getHttpStatus().value());
            return new ResponseEntity(response, ex.getHttpStatus());
        }
        
        product.setAction(AppConstants.ACTIVITY_UPDATE);
        product.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        product.setIntrash(AppConstants.NO);
        this.productService.saveProduct(product);
        
        response.setData(product);
        loggerService.logCreate("Creating new Product", SharedMethods.getEntityName(UfsProduct.class), product.getProductId(), AppConstants.STATUS_COMPLETED);
        response.setCode(201);
        
        return new ResponseEntity(response, HttpStatus.CREATED);
    }
    
    @ApiOperation(value = "Fetch Products", notes = "Used to fetch all products")
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
    public ResponseEntity<ResponseWrapper<Page<UfsProduct>>> fetchProducts(@Valid ProductFilter filter, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(productService.fetchProductsExclude(filter.getStatus(), filter.getActionStatus(), filter.getFrom(), filter.getTo(), pg));
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    @ApiOperation(value = "Fetch Products by ID", notes = "Used to fetch a product")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<ResponseWrapper<UfsProduct>> fetchProductsbyId(@PathVariable("id") BigDecimal id) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(productService.getProduct(id).get());
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete UfsProduct")
    public ResponseEntity<ResponseWrapper> deleteUfsCurrency(@RequestBody @Valid ActionWrapper<BigDecimal> actions) {
        ResponseWrapper response = new ResponseWrapper();
        List<String> errors = new ErrorList();
        for (BigDecimal id : actions.getIds()) {
            UfsProduct dbMake = productService.getProduct(id).get();
            if (dbMake == null) {
                loggerService.logDelete("Failed to delete UfsProduct (id " + id + "). Failed to locate make with specified id",
                        UfsProduct.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                errors.add("UfsProduct with id " + id + " doesn't exist");
            } else {
                dbMake.setAction(AppConstants.ACTIVITY_DELETE);
                dbMake.setActionStatus(AppConstants.STATUS_UNAPPROVED);
                loggerService.logDelete("Deleted UfsProduct (" + dbMake.getProductName() + ") successfully", UfsProduct.class.getSimpleName(), id, AppConstants.STATUS_COMPLETED);
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
    
    private void processApproveDeletion(UfsProduct entity, String notes) throws ExpectationFailed {
        entity.setIntrash(AppConstants.YES);
        loggerService.logApprove("Done approving UfsProduct (" + entity.getProductName() + ") deletion.",
                SharedMethods.getEntityName(UfsProduct.class), entity.getProductId(),
                AppConstants.STATUS_COMPLETED, notes);
        
        productService.updateDeletedProducts(entity);
    }
    
}
