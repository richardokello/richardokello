package ke.tra.ufs.webportal.resources;

import io.swagger.annotations.ApiOperation;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ActionWrapper;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.TmsDevice;
import ke.tra.ufs.webportal.entities.UfsBankBranches;
import ke.tra.ufs.webportal.entities.UfsCustomer;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.service.CustomerService;
import ke.tra.ufs.webportal.service.TmsDeviceService;
import ke.tra.ufs.webportal.utils.AppConstants;
import ke.tra.ufs.webportal.utils.exceptions.AgentAssignedException;
import ke.tra.ufs.webportal.utils.exceptions.ItemNotFoundException;
import ke.tra.ufs.webportal.utils.exceptions.UnapprovedActionsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Arrays;

@RestController
@RequestMapping(value = "/customers")
public class CustomerResource extends ChasisResource<UfsCustomer, Long, UfsEdittedRecord> {

    private final CustomerService customerService;
    private final TmsDeviceService deviceService;

    public CustomerResource(LoggerService loggerService, EntityManager entityManager,CustomerService customerService,TmsDeviceService deviceService) {
        super(loggerService, entityManager);
        this.customerService = customerService;
        this.deviceService = deviceService;
    }


    @RequestMapping(value = "/search-account", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<UfsCustomer>> searchCustomer(@NotNull @RequestParam String accountNumber) {
        ResponseWrapper<UfsCustomer> response = new ResponseWrapper<>();
        if (!accountNumber.equals("1234355")) {
            throw new ItemNotFoundException("Customer with account number :" + accountNumber);
        }
        UfsCustomer cust = new UfsCustomer();
        cust.setAccountNumber(accountNumber);
        cust.setPin("1234355");
        cust.setLocalRegNumber("QWER122334");
        cust.setBusinessLicenceNumber("AS1234");
        response.setData(cust);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @RequestMapping(value = "/terminate" , method = RequestMethod.PUT)
    @Transactional
    @ApiOperation(value = "Terminate Agent", notes = "Terminate multiple agents.")
    public ResponseEntity<ResponseWrapper<UfsCustomer>> terminateAgent(@Valid @RequestBody ActionWrapper<Long> actions) {
        ResponseWrapper response = new ResponseWrapper();

        Arrays.stream(actions.getIds()).forEach(id->{
            UfsCustomer customer = this.customerService.findByCustomerId(id);
            TmsDevice deviceCustomer = this.deviceService.findByCustomerIds(new BigDecimal(id));

            if(deviceCustomer != null){
              throw new AgentAssignedException("Customer Already Assigned Device.Please UnAssign The Device First to Proceeed");

            }

            if(customer.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)){
                throw new UnapprovedActionsException("Sorry resource contains unapproved actions");
            }

            customer.setAction(AppConstants.ACTIVITY_TERMINATION);
            customer.setActionStatus(AppConstants.STATUS_UNAPPROVED);
            this.customerService.saveCustomer(customer);
            loggerService.log("Successfully Terminated Customer",
                    UfsCustomer.class.getSimpleName(), id, AppConstants.ACTIVITY_TERMINATION, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());


         });

        response.setMessage("Agent Terminated successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
