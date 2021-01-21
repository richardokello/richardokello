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
import ke.tracom.ufs.entities.UfsEdittedRecord;
import ke.tracom.ufs.entities.UfsWorkgroup;
import ke.tracom.ufs.services.CountriesService;
import ke.tracom.ufs.wrappers.LogExtras;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/countries")
public class CountriesResource extends ChasisResource<UfsCountries, BigDecimal, UfsEdittedRecord> {

    private final LogExtras logExtras;
    private final CountriesService countriesService;

    public CountriesResource(LoggerService loggerService, EntityManager entityManager,LogExtras logExtras,CountriesService countriesService) {
        super(loggerService, entityManager);
        this.logExtras = logExtras;
        this.countriesService = countriesService;
    }


    @ApiOperation(value = "Deleting A Country")
    @RequestMapping(value = "/delete-action", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 207,message = "Some records could not be processed successfully" )})
    @Transactional
    public ResponseEntity<ResponseWrapper> deleteCountry(@Valid @RequestBody ActionWrapper<BigDecimal> actions) {
        ResponseWrapper response = new ResponseWrapper();
        List<String> errors = new ErrorList();

        for(BigDecimal id:actions.getIds()){
            UfsCountries country = countriesService.findByIdAndIntrash(id);
            if(Objects.isNull(country)){
                this.loggerService.log("Failed to delete country.Record has unapproved actions", UfsCountries.class.getSimpleName(), null, "Deletion", "Failed", "");
                errors.add("Country with id " + id + " doesn't exist");
            }else if(!country.getActionStatus().isEmpty() && country.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)){
                this.loggerService.log("Failed to delete country. Record has unapproved actions", UfsCountries.class.getSimpleName(), id, "Deletion", "Failed", "");
                errors.add("Record has unapproved actions");

            }else{
                country.setAction(AppConstants.ACTIVITY_DELETE);
                country.setActionStatus(AppConstants.STATUS_UNAPPROVED);
                countriesService.saveCountry(country);
                this.loggerService.log(country.getName()+" Deleted Successfully by "+logExtras.getFullName(), UfsCountries.class.getSimpleName(), id, "Deletion", "Completed", "");
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
