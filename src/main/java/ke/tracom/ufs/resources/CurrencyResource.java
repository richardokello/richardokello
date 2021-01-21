package ke.tracom.ufs.resources;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.ErrorList;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ActionWrapper;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tracom.ufs.entities.UfsCountries;
import ke.tracom.ufs.entities.UfsCurrency;
import ke.tracom.ufs.entities.UfsEdittedRecord;
import ke.tracom.ufs.services.CurrencyService;
import ke.tracom.ufs.wrappers.LogExtras;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/currency")
public class CurrencyResource extends ChasisResource<UfsCurrency, BigDecimal, UfsEdittedRecord> {

    private final LogExtras logExtras;
    private final CurrencyService currencyService;


    public CurrencyResource(LoggerService loggerService, EntityManager entityManager,LogExtras logExtras,CurrencyService currencyService) {
        super(loggerService, entityManager);
        this.logExtras = logExtras;
        this.currencyService = currencyService;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsCurrency>> create(@Valid @RequestBody UfsCurrency ufsCurrency) {
        ResponseWrapper wrapper = new ResponseWrapper();
        UfsCurrency currency = currencyService.findByNameAndIntrash(ufsCurrency.getName(),AppConstants.NO);
        if (currency != null ) {
            wrapper.setCode(HttpStatus.CONFLICT.value());
            wrapper.setMessage(ufsCurrency.getName()+" Currency already exist");

            return ResponseEntity.status(HttpStatus.CONFLICT).body(wrapper);
        }
        return super.create(ufsCurrency);
    }

    @ApiOperation(value = "Deleting A Currency")
    @RequestMapping(value = "/delete-action", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 207,message = "Some records could not be processed successfully" )})
    @Transactional
    public ResponseEntity<ResponseWrapper> deleteCurrency(@Valid @RequestBody ActionWrapper<BigDecimal> actions) {
        ResponseWrapper response = new ResponseWrapper();
        List<String> errors = new ErrorList();

        for(BigDecimal id:actions.getIds()){
            UfsCurrency currency = currencyService.findByIdAndIntrash(id);
            if(Objects.isNull(currency)){
                this.loggerService.log("Failed to delete currency.Record has unapproved actions", UfsCurrency.class.getSimpleName(), null, "Deletion", "Failed", "");
                errors.add("Country with id " + id + " doesn't exist");
            }else if(!currency.getActionStatus().isEmpty() && currency.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)){
                this.loggerService.log("Failed to delete currency. Record has unapproved actions", UfsCurrency.class.getSimpleName(), id, "Deletion", "Failed", "");
                errors.add("Record has unapproved actions");

            }else{
                currency.setAction(AppConstants.ACTIVITY_DELETE);
                currency.setActionStatus(AppConstants.STATUS_UNAPPROVED);
                currencyService.saveCurrency(currency);
                this.loggerService.log(currency.getName()+" Deleted Successfully by "+logExtras.getFullName(), UfsCurrency.class.getSimpleName(), id, "Deletion", "Completed", "");
            }

        }

        if (errors.isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setData(errors);
            response.setMessage("Some Actions could not be processed successfully check audit logs for more details");
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        }
    }
}
