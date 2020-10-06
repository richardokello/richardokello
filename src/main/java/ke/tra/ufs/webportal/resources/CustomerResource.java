package ke.tra.ufs.webportal.resources;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.exceptions.ExpectationFailed;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.utils.SharedMethods;
import ke.axle.chassis.wrappers.ActionWrapper;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.*;
import ke.tra.ufs.webportal.entities.wrapper.*;
import ke.tra.ufs.webportal.service.*;
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
import org.springframework.validation.BindingResult;
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
    private final UfsCustomerOutletService outletService;
    private final ContactPersonService contactPersonService;

    public CustomerResource(LoggerService loggerService, EntityManager entityManager,CustomerService customerService,TmsDeviceService deviceService,
                            CustomerOwnersService ownersService,UfsCustomerOutletService outletService,ContactPersonService contactPersonService) {
        super(loggerService, entityManager);
        this.customerService = customerService;
        this.deviceService = deviceService;
        this.ownersService = ownersService;
        this.outletService = outletService;
        this.contactPersonService = contactPersonService;
    }

    /**
     * Onboarding customer,locations,directors,outlets
     * @param customerOnboarding
     * @return
     */
    @Transactional
    @ApiOperation(value = "Create new principal customer", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request as a result of validation errors")
            ,
            @ApiResponse(code = 409, message = "Similar customer exists")
    })
    @RequestMapping(value = "/onboard", method = RequestMethod.POST)
    public ResponseEntity<ResponseWrapper<UfsCustomer>> onboardCustomer(@Valid @RequestBody CustomerOnboardingWrapper customerOnboarding,
                                                                        BindingResult validation) {
        ResponseWrapper response = new ResponseWrapper<>();
        if (validation.hasErrors()) {
            loggerService.log("Creating new customer category failed due to validation errors",
                    UfsCustomer.class.getSimpleName(), null,AppConstants.ACTIVITY_CREATE, ke.axle.chassis.utils.AppConstants.STATUS_FAILED, "Creating new customer category failed due to validation errors");
            response.setMessage("Validation error occured");
            response.setCode(400);
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        //Errors Occurred While Onboarding Customer
        ArrayList<String> errors = new ArrayList<>();

        //saving customer Info
        UfsCustomer customer = new UfsCustomer();
        customer.setBusinessPrimaryContactNo(customerOnboarding.getBusinessPrimaryContactNo());
        customer.setDateIssued(customerOnboarding.getDateIssued());
        customer.setValidTo(customerOnboarding.getValidTo());
        customer.setBusinessLicenceNumber(customerOnboarding.getBusinessLicenseNumber());
        customer.setLocalRegistrationNumber(customerOnboarding.getLocalRegistrationNumber());
        customer.setBusinessName(customerOnboarding.getBusinessName());
        customer.setCustomerClassId(customerOnboarding.getCustomerClassId());
        customer.setAddress(customerOnboarding.getAddress());
        customer.setBusinessEmailAddress(customerOnboarding.getBusinessEmailAddress());
        customer.setPinNumber(customerOnboarding.getPinNumber());
        customer.setBusinessTypeIds(customerOnboarding.getBusinessTypeId());
        customer.setBusinessSecondaryContactNo(customerOnboarding.getBusinessSecondaryContactNo());
        customer.setCustomerTypeId(customerOnboarding.getCustomerTypeId());
        customer.setCommercialActivityId(customerOnboarding.getCommercialActivityId());
        customer.setEstateId(customerOnboarding.getEstateId());
        customerService.saveCustomer(customer);

        //saving directors
        if(!customerOnboarding.getDirectors().isEmpty()){
            for(BusinessDirectorsWrapper director : customerOnboarding.getDirectors()){
                UfsCustomerOwners customerOwners = ownersService.findByUsername(director.getUserName());
                if(Objects.nonNull(customerOwners)) {
                    errors.add(director.getUserName());
                    continue;
                }
                  UfsCustomerOwners dir = new UfsCustomerOwners();
                  dir.setDirectorName(director.getDirectorName());
                  dir.setCustomerIds(BigDecimal.valueOf(customer.getId()));
                  dir.setDirectorEmailAddress(director.getDirectorEmailAddress());
                  dir.setDirectorDesignationId(director.getDirectorDesignationId());
                  dir.setDirectorPrimaryContactNumber(director.getDirectorPrimaryContactNumber());
                  dir.setDirectorSecondaryContactNumber(director.getDirectorSecondaryContactNumber());
                  dir.setDirectorIdNumber(director.getDirectorIdNumber());
                  dir.setUserName(director.getUserName());
                  ownersService.saveOwner(dir);


            };
        }

        //saving outlets
        if(!customerOnboarding.getOutletsInfo().isEmpty()){
            customerOnboarding.getOutletsInfo().stream().forEach(outlet->{
              UfsCustomerOutlet custOutlet = new UfsCustomerOutlet();
              custOutlet.setCustomerIds(BigDecimal.valueOf(customer.getId()));
              custOutlet.setBankBranchIds(outlet.getBankBranchId());
              custOutlet.setLongitude(outlet.getLongitude());
              custOutlet.setLatitude(outlet.getLatitude());
              custOutlet.setOutletCode(outlet.getOutletCode());
              custOutlet.setOutletName(outlet.getOutletName());
              custOutlet.setOperatingHours(outlet.getOperatingHours());
              custOutlet.setGeographicalRegionIds(outlet.getGeographicalRegionIds());
              outletService.saveOutlet(custOutlet);
               //save contact person
              if(!outlet.getContactPerson().isEmpty()){
                  for(OutletContactPerson outletContactPerson : outlet.getContactPerson()){

                      UfsContactPerson contactPersonCheck = contactPersonService.findByUsername(outletContactPerson.getUserName());
                      if(Objects.nonNull(contactPersonCheck)){
                          errors.add(outletContactPerson.getUserName());
                          continue;
                      }
                    UfsContactPerson person = new UfsContactPerson();
                    person.setName(outletContactPerson.getContactPersonName());
                    person.setIdNumber(outletContactPerson.getContactPersonIdNumber());
                    person.setPosRole(outletContactPerson.getPosRole());
                    person.setEmail(outletContactPerson.getContactPersonEmail());
                    person.setPhoneNumber(outletContactPerson.getContactPersonTelephone());
                    person.setCustomerOutletId(custOutlet.getId());
                    person.setUserName(outletContactPerson.getUserName());
                    contactPersonService.saveContactPerson(person);
                  };
              }
            });

        }
        if (errors.size() > 0) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setMessage("Agent Owner/Contact Person Username Already Exists:" + errors);
            response.setData(errors);
            return new ResponseEntity(response, HttpStatus.MULTI_STATUS);
        } else {
            response.setCode(201);
            response.setMessage("Customer Onboarded successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

    }


   /* @RequestMapping(value = "/terminate" , method = RequestMethod.PUT)
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
    }*/

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper> approveActions(@Valid @RequestBody ActionWrapper<Long> actions) throws ExpectationFailed {

        ResponseWrapper response =  new ResponseWrapper<>();
        Arrays.stream(actions.getIds()).forEach(id->{
            UfsCustomer customer = this.customerService.findByCustomerId(id);
            if(Objects.nonNull(customer)){
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
                    List<UfsCustomerOwners> customerOwners = this.ownersService.findOwnersByCustomerIds(new BigDecimal(id));
                    if(!customerOwners.isEmpty()){
                       for(UfsCustomerOwners customerOwner:customerOwners){
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
                       }
                    }


                    //approving customer outlet
                    List<UfsCustomerOutlet> customerOutlets = this.customerService.findOutletsByCustomerIds(new BigDecimal(id));
                    if(!customerOutlets.isEmpty()){
                        for(UfsCustomerOutlet customerOutlet : customerOutlets){
                            if((customerOutlet.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE) && customerOutlet.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) ||
                                    (customerOutlet.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_CREATE) && customerOutlet.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED))
                            ){
                                customerOutlet.setActionStatus(AppConstants.STATUS_APPROVED);
                                this.customerService.saveOutlet(customerOutlet);
                                loggerService.log("Successfully Approved Customer Outlet",
                                        UfsCustomerOutlet.class.getSimpleName(), customerOutlet.getId(), ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());

                                if(customerOutlet.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DELETE) && customerOutlet.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)){
                                    customerOutlet.setActionStatus(AppConstants.STATUS_APPROVED);
                                    customerOutlet.setIntrash(AppConstants.INTRASH_YES);
                                    this.customerService.saveOutlet(customerOutlet);

                                    loggerService.log("Successfully Approved Customer Outlet Deletion",
                                            UfsCustomerOutlet.class.getSimpleName(), customerOutlet.getId(), ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());
                                }
                            }
                        }

                    }



                }else {

                    loggerService.log("Failed To Approve Customer",
                            UfsCustomer.class.getSimpleName(), id, ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_FAILED, actions.getNotes());
                }
            }else{

                loggerService.log("Failed To Approve Customer.Customer With Id: "+id+" does not exist",
                        UfsCustomer.class.getSimpleName(), id, ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_FAILED, actions.getNotes());
            }

        });

        response.setCode(200);
        response.setMessage("Customer Approved Successfully");
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
            terminationResponse.setAgentName(customer.getBusinessName());
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
            detailsWrapper.setOwnerName(owner.get().getDirectorName());
            detailsWrapper.setEmail(owner.get().getDirectorEmailAddress());

        }
        return detailsWrapper;
    }
}
