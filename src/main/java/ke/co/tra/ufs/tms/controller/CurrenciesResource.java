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

import ke.co.tra.ufs.tms.config.messageSource.Message;
import ke.co.tra.ufs.tms.entities.UfsCurrency;
import ke.co.tra.ufs.tms.entities.wrappers.ActionWrapper;
import ke.co.tra.ufs.tms.entities.wrappers.filters.ProductFilter;
import ke.co.tra.ufs.tms.service.CurrencyService;
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;
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
@RequestMapping("/currencies")
@Api(value = "Currencies Management")
public class CurrenciesResource {

    private final LoggerServiceLocal loggerService;
    private final CurrencyService currencyService;
    private final Message message;

    public CurrenciesResource(LoggerServiceLocal loggerService, CurrencyService currencyService, Message message) {
        this.loggerService = loggerService;
        this.currencyService = currencyService;
        this.message = message;
    }

    @ApiOperation(value = "Create Currency", notes = "used to create a product within the system")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
        ,
        @ApiResponse(code = 404, message = "Currency with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<ResponseWrapper> create(@ApiParam(value = "Ignore status and currencyId it will be used when fetching Currencies")
            @Valid @RequestBody UfsCurrency currency, BindingResult validation) {
        ResponseWrapper response = new ResponseWrapper();
        if (validation.hasErrors()) {
            loggerService.logCreate("Creating new Currency failed due to validation errors", SharedMethods.getEntityName(UfsCurrency.class), currency.getId(), AppConstants.STATUS_FAILED);
            response.setCode(400);
            response.setMessage(message.setMessage(AppConstants.VALIDATION_ERROR));
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }

        try {
            this.validateCurrencyAddons(currency, false);
        } catch (GeneralBadRequest ex) {
            response.setMessage(ex.getMessage());
            response.setCode(ex.getHttpStatus().value());
            return new ResponseEntity(response, ex.getHttpStatus());
        }

        currency.setId(null);
        currency.setAction(AppConstants.ACTIVITY_CREATE);
        currency.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        currency.setIntrash(AppConstants.NO);
        currencyService.saveCurrency(currency);

        response.setData(currency);
        loggerService.logCreate("Creating new Currency", SharedMethods.getEntityName(UfsCurrency.class), currency.getId(), AppConstants.STATUS_COMPLETED);
        response.setCode(201);

        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    private void validateCurrencyAddons(UfsCurrency currency, boolean isUpdate) throws GeneralBadRequest {
        if (isUpdate) {
        } else {
            if (currencyService.findByCurrencyCode(currency.getCode()) != null) {
                loggerService.logCreate("Creating new Currency failed due to the provided"
                        + " Currency Code exists (Code: " + currency.getCode() + ")", SharedMethods.getEntityName(UfsCurrency.class), currency.getId(), AppConstants.STATUS_FAILED);
                throw new GeneralBadRequest(message.setMessage(AppConstants.CURRENCY_CODE_IN_USE), HttpStatus.CONFLICT);
            }
        }
    }

    @ApiOperation(value = "Approve a Currency", notes = "used to approve a currency that has already been created")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
        ,
        @ApiResponse(code = 404, message = "Currency with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/approve-actions")
    @Transactional
    public ResponseEntity<ResponseWrapper> approveCurrency(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        ArrayList<BigDecimal> errors = new ArrayList<>();
        for (BigDecimal id : action.getIds()) {
            loggerService.logApprove("Approving a currency  (Id: " + id + ")",
                    SharedMethods.getEntityName(UfsCurrency.class), id, AppConstants.STATUS_PENDING, action.getNotes());
            UfsCurrency currency = currencyService.findCurrency(id).get();
            if (currency == null) {
                loggerService.logApprove("Failed to approve Currency (Currency id: " + id + "). Currency doesn't exist",
                        SharedMethods.getEntityName(UfsCurrency.class), id, AppConstants.STATUS_FAILED, action.getNotes());
                errors.add(id);
                continue;
            } else if (currency.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DELETE)
                    && currency.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                if (!this.processApproveDeletion(currency, action.getNotes())) {
                    //errors.add("Department make with id " + id + " doesn't exist");
                    continue;
                }
            } else {
                currency.setAction(AppConstants.ACTIVITY_UPDATE);
                currency.setActionStatus(AppConstants.STATUS_APPROVED);
                currency.setIntrash(AppConstants.NO);
                currencyService.saveCurrency(currency);

                loggerService.logApprove("Approved (" + currency.getName() + ") successfully",
                        SharedMethods.getEntityName(UfsCurrency.class), id, AppConstants.STATUS_COMPLETED, action.getNotes());
            }
        }

        if (errors.size() > 0) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setMessage(AppConstants.CHECKER_GENERAL_ERROR);
            response.setData(errors);
            return new ResponseEntity(response, HttpStatus.MULTI_STATUS);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Reject a Currency", notes = "used to reject a Currency that has already been created")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
        ,
        @ApiResponse(code = 404, message = "Currency with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/decline-actions")
    @Transactional
    public ResponseEntity<ResponseWrapper> declineCurrency(@RequestBody @Valid ActionWrapper<BigDecimal> action) {
        ResponseWrapper response = new ResponseWrapper();
        ArrayList<BigDecimal> errors = new ArrayList<>();
        for (BigDecimal id : action.getIds()) {
            loggerService.logLock("Rejecting a Currency  (Id: " + id + ")",
                    SharedMethods.getEntityName(UfsCurrency.class), id, AppConstants.STATUS_PENDING, action.getNotes());
            UfsCurrency currency = currencyService.findCurrency(id).get();
            if (currency == null) {
                loggerService.logDeactivate("Failed to reject Currency (Currency id: " + id + "). Currency doesn't exist",
                        SharedMethods.getEntityName(UfsCurrency.class), id, AppConstants.STATUS_FAILED, action.getNotes());
                errors.add(id);
                continue;
            }
            currency.setActionStatus(AppConstants.STATUS_DECLINED);
            currency.setIntrash(AppConstants.YES);
            currencyService.saveCurrency(currency);

            loggerService.logDeactivate("Rejected (" + currency.getName() + ") successfully",
                    SharedMethods.getEntityName(UfsCurrency.class), id, AppConstants.STATUS_COMPLETED, action.getNotes());
        }

        if (errors.size() > 0) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setMessage(AppConstants.CHECKER_GENERAL_ERROR);
            response.setData(errors);
            return new ResponseEntity(response, HttpStatus.MULTI_STATUS);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Update Currency", notes = "used to update a product within the system")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Mostly when validation errors are encountered")
        ,
        @ApiResponse(code = 404, message = "Currency with specified id doesn't exist")
    })
    @RequestMapping(method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity<ResponseWrapper> updateCurrency(
            @Valid @RequestBody UfsCurrency currency, BindingResult validation) {
        ResponseWrapper response = new ResponseWrapper();
        if (validation.hasErrors()) {
            loggerService.logUpdate("Creating new Currency failed due to validation errors", SharedMethods.getEntityName(UfsCurrency.class), currency.getId(), AppConstants.STATUS_FAILED);
            response.setCode(400);
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }

        UfsCurrency ufsCurrency = currencyService.findCurrency(currency.getId()).get();
        if (ufsCurrency == null) {
            loggerService.logUpdate("Updating Currency (Currency id: " + currency.getId()
                    + ") failed due to currency not found", SharedMethods.getEntityName(UfsCurrency.class), currency.getId(), AppConstants.STATUS_FAILED);
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(message.setMessage(AppConstants.RECORD_WITH_ID_NOT_FOUND));
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }

        try {
            this.validateCurrencyAddons(currency, true);
        } catch (GeneralBadRequest ex) {
            response.setMessage(ex.getMessage());
            response.setCode(ex.getHttpStatus().value());
            return new ResponseEntity(response, ex.getHttpStatus());
        }

        currency.setAction(AppConstants.ACTIVITY_UPDATE);
        currency.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        currency.setIntrash(AppConstants.NO);
        currencyService.saveCurrency(currency);

        response.setData(currency);
        loggerService.logUpdate("Updating Currency", SharedMethods.getEntityName(UfsCurrency.class), currency.getId(), AppConstants.STATUS_COMPLETED);
        response.setCode(201);

        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Fetch Currency", notes = "Used to fetch all Currencies")
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
    public ResponseEntity<ResponseWrapper<Page<UfsCurrency>>> fetchCurrency(@Valid ProductFilter filter, Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(currencyService.fetchCurrenciesExclude(filter.getStatus(), filter.getActionStatus(), filter.getFrom(), filter.getTo(), pg));
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Fetch Currency by ID", notes = "Used to fetch a Currencies")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<ResponseWrapper<UfsCurrency>> fetchCurrencyById(@PathVariable("id") BigDecimal id) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(currencyService.findCurrency(id));
        return new ResponseEntity(response, HttpStatus.OK);
    }

    private boolean processApproveDeletion(UfsCurrency currency, String notes) {
        currency.setIntrash(AppConstants.YES);
        currency.setActionStatus(AppConstants.STATUS_APPROVED);
        currencyService.saveCurrency(currency);
        loggerService.logApprove("Completed Approving Currency (" + currency.getCodeName() + ") deletion", SharedMethods.getEntityName(UfsCurrency.class), currency.getId(), AppConstants.STATUS_COMPLETED, notes);
        return true;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete Currency")
    public ResponseEntity<ResponseWrapper> deleteUfsCurrency(@RequestBody @Valid ActionWrapper<BigDecimal> actions) {
        ResponseWrapper response = new ResponseWrapper();
        List<String> errors = new ErrorList();
        for (BigDecimal id : actions.getIds()) {
            UfsCurrency dbMake = currencyService.findCurrency(id).get();
            if (dbMake == null) {
                loggerService.logDelete("Failed to delete UfsCurrency (id " + id + "). Failed to locate make with specified id",
                        UfsCurrency.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                errors.add(message.setMessage(AppConstants.RECORD_WITH_ID_NOT_FOUND) +" " + id);
            } else {
                dbMake.setAction(AppConstants.ACTIVITY_DELETE);
                dbMake.setActionStatus(AppConstants.STATUS_UNAPPROVED);
                loggerService.logDelete("Deleted UfsCurrency (" + dbMake.getName() + ") successfully", UfsCurrency.class.getSimpleName(), id, AppConstants.STATUS_COMPLETED);
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
