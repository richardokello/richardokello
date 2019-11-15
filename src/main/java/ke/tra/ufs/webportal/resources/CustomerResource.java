package ke.tra.ufs.webportal.resources;

import io.swagger.annotations.ApiOperation;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.exceptions.ExpectationFailed;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ActionWrapper;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.*;
import ke.tra.ufs.webportal.entities.wrapper.AgentTerminationResponseWrapper;
import ke.tra.ufs.webportal.entities.wrapper.OwnerDetailsWrapper;
import ke.tra.ufs.webportal.service.CustomerOwnersService;
import ke.tra.ufs.webportal.service.CustomerService;
import ke.tra.ufs.webportal.service.TmsDeviceService;
import ke.tra.ufs.webportal.utils.AppConstants;
import ke.tra.ufs.webportal.utils.exceptions.AgentAssignedException;
import ke.tra.ufs.webportal.utils.exceptions.ItemNotFoundException;
import ke.tra.ufs.webportal.utils.exceptions.UnapprovedActionsException;
import ke.tra.ufs.webportal.wrappers.AgentTerminationWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(value = "/customers")
public class CustomerResource extends ChasisResource<UfsCustomer, Long, UfsEdittedRecord> {

    private final CustomerService customerService;
    private final TmsDeviceService deviceService;
    private final CustomerOwnersService ownersService;

    public CustomerResource(LoggerService loggerService, EntityManager entityManager,CustomerService customerService,TmsDeviceService deviceService,
                            CustomerOwnersService ownersService) {
        super(loggerService, entityManager);
        this.customerService = customerService;
        this.deviceService = deviceService;
        this.ownersService = ownersService;
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
    public ResponseEntity<ResponseWrapper<UfsCustomer>> terminateAgent(@Valid @RequestBody AgentTerminationWrapper<Long> actions) {
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
            customer.setTerminationReason(actions.getTerminationReason());
            customer.setTerminationDate(new Date());
            this.customerService.saveCustomer(customer);
            loggerService.log("Successfully Terminated Customer",
                    UfsCustomer.class.getSimpleName(), id, AppConstants.ACTIVITY_TERMINATION, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());


         });

        response.setMessage("Agent Terminated successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper> approveActions(@Valid @RequestBody ActionWrapper<Long> actions) throws ExpectationFailed {

        ResponseWrapper response =  new ResponseWrapper<>();
        Arrays.stream(actions.getIds()).forEach(id->{
            UfsCustomer customer = this.customerService.findByCustomerId(id);
            if((customer.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_ACTIVATION) && customer.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) ||
                    (customer.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE) && customer.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) ||
                    (customer.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_CREATE) && customer.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) ||
                    (customer.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_TERMINATION) && customer.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED))){

                customer.setActionStatus(AppConstants.STATUS_APPROVED);
                this.customerService.saveCustomer(customer);
                loggerService.log("Successfully Approved Customer",
                        UfsCustomer.class.getSimpleName(), id, ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());


                if(customer.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DELETE) && customer.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)){
                    customer.setActionStatus(AppConstants.STATUS_APPROVED);
                    customer.setIntrash(AppConstants.INTRASH_YES);
                    this.customerService.saveCustomer(customer);

                    loggerService.log("Successfully Approved Customer Deletion",
                            UfsCustomer.class.getSimpleName(), id, ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());
                }

                //approving customer owner
                UfsCustomerOwners customerOwner = this.ownersService.findByCustomerIds(new BigDecimal(id));
                if((customerOwner.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE) && customerOwner.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) ||
                        (customerOwner.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_CREATE) && customerOwner.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED))
                        ){
                    customerOwner.setActionStatus(AppConstants.STATUS_APPROVED);
                    this.ownersService.saveOwner(customerOwner);
                    loggerService.log("Successfully Approved Customer Owner",
                            UfsCustomerOwners.class.getSimpleName(), customerOwner.getId(), ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());

                    if(customerOwner.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DELETE) && customerOwner.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)){
                        customerOwner.setActionStatus(AppConstants.STATUS_APPROVED);
                        customerOwner.setIntrash(AppConstants.INTRASH_YES);
                        this.ownersService.saveOwner(customerOwner);

                        loggerService.log("Successfully Approved Customer Owner Deletion",
                                UfsCustomerOwners.class.getSimpleName(), customerOwner.getId(), ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());
                    }
                }

                //approving customer outlet
                UfsCustomerOutlet customerOutlet = this.customerService.findByCustomerIds(new BigDecimal(id));
                if((customerOutlet.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE) && customerOutlet.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) ||
                        (customerOutlet.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_CREATE) && customerOutlet.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED))
                ){
                    customerOutlet.setActionStatus(AppConstants.STATUS_APPROVED);
                    this.ownersService.saveOwner(customerOwner);
                    loggerService.log("Successfully Approved Customer Outlet",
                            UfsCustomerOutlet.class.getSimpleName(), customerOutlet.getId(), ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());

                    if(customerOutlet.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DELETE) && customerOutlet.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)){
                        customerOutlet.setActionStatus(AppConstants.STATUS_APPROVED);
                        customerOutlet.setIntrash(AppConstants.INTRASH_YES);
                        this.ownersService.saveOwner(customerOwner);

                        loggerService.log("Successfully Approved Customer Outlet Deletion",
                                UfsCustomerOutlet.class.getSimpleName(), customerOutlet.getId(), ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());
                    }
                }

            }else {

                loggerService.log("Failed To Approve Customer",
                        UfsCustomer.class.getSimpleName(), id, ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_FAILED, actions.getNotes());
            }

        });

        response.setCode(200);
        response.setMessage("Bank Branch Approved Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @RequestMapping(value = "/terminated-agents" , method = RequestMethod.GET)
    @Transactional
    @ApiOperation(value = "Terminated Agents", notes = "Get All Terminated Agents.")
    public ResponseWrapper<Object> getTerminatedAgents(Pageable pg) {
        ResponseWrapper response =  new ResponseWrapper<>();

        List<UfsCustomer> terminatedCustomers = this.customerService.getAllTerminatedAgents(AppConstants.ACTIVITY_TERMINATION,AppConstants.STATUS_APPROVED);
        Iterable<UfsCustomerOwners> customerOwners  = this.customerService.getAllCustomerOwners();


        List<AgentTerminationResponseWrapper> terminatedCustomerList = new ArrayList<>();

        for(UfsCustomer customer: terminatedCustomers ){
            AgentTerminationResponseWrapper terminationResponse  = new AgentTerminationResponseWrapper();
            terminationResponse.setAgentName(customer.getCustomerName());
            terminationResponse.setGeographicalRegion(customer.getGeographicalRegId().getRegionName());
            terminationResponse.setEmail(this.getOwnerDetails(customerOwners,customer.getId()).getEmail());
            terminationResponse.setOwnerName(this.getOwnerDetails(customerOwners,customer.getId()).getOwnerName());
            terminationResponse.setTerminationDate(customer.getTerminationDate());
            terminationResponse.setTerminationReason(customer.getTerminationReason());

            terminatedCustomerList.add(terminationResponse);
        }

        Page<AgentTerminationResponseWrapper> pageResponse = new PageImpl<>(terminatedCustomerList, pg, terminatedCustomerList.size());
        response.setData(pageResponse);
        response.setCode(HttpStatus.OK.value());
        response.setTimestamp(Calendar.getInstance().getTimeInMillis());
        response.setMessage("Data fetched successfully");

        return response;
    }



    /**
     * get OwnerName and Email
     *
     * @param iterable
     * @param customerId
     * @return
     */
    private OwnerDetailsWrapper getOwnerDetails(Iterable<UfsCustomerOwners> iterable, Long customerId) {
        OwnerDetailsWrapper detailsWrapper = new OwnerDetailsWrapper();

        Optional<UfsCustomerOwners> owner = StreamSupport.stream(iterable.spliterator(), true)
                .filter(ownerFromDb -> ownerFromDb.getCustomerId().getId().equals(customerId))
                .findFirst();
        if (owner.isPresent()) {
            detailsWrapper.setOwnerName(owner.get().getName());
            detailsWrapper.setEmail(owner.get().getEmail());

        }
        return detailsWrapper;
    }
}
