package ke.tra.ufs.webportal.resources;

/**
 * @author kenny
 */

import io.swagger.annotations.ApiOperation;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.exceptions.ExpectationFailed;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ActionWrapper;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsBankBranches;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.service.BankBranchesService;
import ke.tra.ufs.webportal.utils.AppConstants;
import ke.tra.ufs.webportal.utils.exceptions.ItemNotFoundException;
import ke.tra.ufs.webportal.utils.exceptions.UnapprovedActionsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.Arrays;

@RestController
@RequestMapping(value = "/bank-branches")
public class BankBranchesResources extends ChasisResource<UfsBankBranches, Long, UfsEdittedRecord> {

    private final BankBranchesService bankBranchesService;

    public BankBranchesResources(LoggerService loggerService, EntityManager entityManager,BankBranchesService bankBranchesService) {
        super(loggerService, entityManager);
        this.bankBranchesService = bankBranchesService;
    }

    @RequestMapping(value = "/suspend" , method = RequestMethod.PUT)
    @Transactional
    @ApiOperation(value = "Suspend Bank Branch", notes = "Suspend multiple bank branches.")
    public ResponseEntity<ResponseWrapper<UfsBankBranches>> suspendBankBranch(@Valid @RequestBody ActionWrapper<Long> actions) {
        ResponseWrapper response = new ResponseWrapper();

        Arrays.stream(actions.getIds()).forEach(id->{
          UfsBankBranches bankBranch = this.bankBranchesService.findByBranchId(id);
           if(bankBranch == null){
               throw new ItemNotFoundException("Sorry Bank Branch Not Found");
           }

            if(bankBranch.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)){
                throw new UnapprovedActionsException("Sorry resource contains unapproved actions");
            }

            bankBranch.setAction(AppConstants.ACTIVITY_SUSPEND);
            bankBranch.setActionStatus(AppConstants.STATUS_UNAPPROVED);
            this.bankBranchesService.saveBranch(bankBranch);

            loggerService.log("Successfully to suspended Bank Branch",
                    UfsBankBranches.class.getSimpleName(), id, AppConstants.ACTIVITY_SUSPEND, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());
        });

        response.setMessage("Bank branch(es) suspended successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @RequestMapping(value = "/reactivate" , method = RequestMethod.PUT)
    @Transactional
    @ApiOperation(value = "Reactivate Bank Branch", notes = "Reactivate multiple bank branches.")
    public ResponseEntity<ResponseWrapper<UfsBankBranches>> reactivateBankBranch(@Valid @RequestBody ActionWrapper<Long> actions) {
        ResponseWrapper response = new ResponseWrapper();

        Arrays.stream(actions.getIds()).forEach(id->{
            UfsBankBranches reactivateBankBranch = this.bankBranchesService.findByBranchId(id);

            if(reactivateBankBranch == null){
                throw new ItemNotFoundException("Sorry Bank Branch Not Found");
            }

            reactivateBankBranch.setAction(AppConstants.ACTIVITY_ACTIVATION);
            reactivateBankBranch.setActionStatus(AppConstants.STATUS_UNAPPROVED);
            this.bankBranchesService.saveBranch(reactivateBankBranch);

            loggerService.log("Successfully To Reactivate Bank Branch",
                    UfsBankBranches.class.getSimpleName(), id, ke.axle.chassis.utils.AppConstants.ACTIVITY_ACTIVATION, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());
        });

        response.setMessage("Bank branch(es) reactivated successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper> approveActions(@Valid @RequestBody ActionWrapper<Long> actions) throws ExpectationFailed {

        ResponseWrapper response =  new ResponseWrapper<>();
        Arrays.stream(actions.getIds()).forEach(id->{
            UfsBankBranches bankBranch = this.bankBranchesService.findByBranchId(id);
            if((bankBranch.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_ACTIVATION) && bankBranch.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) ||
                    (bankBranch.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE) && bankBranch.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) ||
                    (bankBranch.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_CREATE) && bankBranch.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) ||
                    (bankBranch.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_SUSPEND) && bankBranch.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED))){

                bankBranch.setActionStatus(AppConstants.STATUS_APPROVED);
                this.bankBranchesService.saveBranch(bankBranch);
                loggerService.log("Successfully Approved Bank Branch",
                        UfsBankBranches.class.getSimpleName(), id, ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());


                if(bankBranch.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DELETE) && bankBranch.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)){
                    loggerService.log("Successfully Approved Bank Branch",
                            UfsBankBranches.class.getSimpleName(), id, ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());
                    bankBranch.setActionStatus(AppConstants.STATUS_APPROVED);
                    bankBranch.setIntrash(AppConstants.INTRASH_YES);
                    this.bankBranchesService.saveBranch(bankBranch);
                }

            }else {

                loggerService.log("Failed To Approve Bank Branch",
                        UfsBankBranches.class.getSimpleName(), id, ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_FAILED, actions.getNotes());
            }

        });

        response.setCode(200);
        response.setMessage("Bank Branch Approved Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
