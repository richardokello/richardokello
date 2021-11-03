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
import ke.tra.ufs.webportal.repository.AuthenticationRepository;
import ke.tra.ufs.webportal.repository.CustomerRepository;
import ke.tra.ufs.webportal.service.*;
import ke.tra.ufs.webportal.utils.AppConstants;
import ke.tra.ufs.webportal.utils.CodeGenerator;
import ke.tra.ufs.webportal.wrappers.AgentTerminationWrapper;
import ke.tra.ufs.webportal.wrappers.LogExtras;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(value = "/customers")
@CommonsLog
public class CustomerResource extends ChasisResource<UfsCustomer, Long, UfsEdittedRecord> {

    private final CustomerService customerService;
    private final TmsDeviceService deviceService;
    private final CustomerOwnersService ownersService;
    private final UfsCustomerOutletService outletService;
    private final ContactPersonService contactPersonService;
    private final CustomerRepository customerRepository;
    private final AuthenticationRepository urepo;
    private final LogExtras logExtras;
    private final PosUserIdGenerator posUserIdGenerator;


    public CustomerResource(LoggerService loggerService, EntityManager entityManager, CustomerService customerService, TmsDeviceService deviceService,
                            CustomerOwnersService ownersService, UfsCustomerOutletService outletService, ContactPersonService contactPersonService, CustomerRepository customerRepository, AuthenticationRepository urepo, LogExtras logExtras, PosUserIdGenerator posUserIdGenerator) {
        super(loggerService, entityManager);
        this.customerService = customerService;
        this.deviceService = deviceService;
        this.ownersService = ownersService;
        this.outletService = outletService;
        this.contactPersonService = contactPersonService;
        this.customerRepository = customerRepository;
        this.urepo = urepo;
        this.logExtras = logExtras;
        this.posUserIdGenerator = posUserIdGenerator;
    }

    /**
     * Onboarding customer,locations,directors,outlets
     *
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
    public ResponseEntity<ResponseWrapper<UfsCustomer>> onboardCustomer(@Valid @RequestBody CustomerOnboardingWrapper customerOnboarding, Authentication a,
                                                                        BindingResult validation) {
        ResponseWrapper response = new ResponseWrapper<>();
        if (validation.hasErrors()) {
            loggerService.log("Creating new customer category failed due to validation errors",
                    UfsCustomer.class.getSimpleName(), null, AppConstants.ACTIVITY_CREATE, ke.axle.chassis.utils.AppConstants.STATUS_FAILED, "Creating new customer category failed due to validation errors");
            response.setMessage("Validation error occured");
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setData(SharedMethods.getFieldMapErrors(validation));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        if (customerOnboarding.getMid() != null) {
            if (customerService.findIfMidIsActive(customerOnboarding.getMid(), AppConstants.INTRASH_NO)) {
                loggerService.log("MID already Exists",
                        UfsCustomer.class.getSimpleName(), null, AppConstants.ACTIVITY_CREATE, ke.axle.chassis.utils.AppConstants.STATUS_FAILED, "Creating new customer category failed due to MID already Exists");
                response.setMessage("MID already Exists");
                response.setCode(HttpStatus.BAD_REQUEST.value());
                response.setData(SharedMethods.getFieldMapErrors(validation));
                return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
            }
        }

        //check business name
        if (customerOnboarding.getBusinessName() != null) {
            if (customerRepository.findByBusinessNameAndIntrash(customerOnboarding.getBusinessName(), AppConstants.INTRASH_NO).isPresent()) {
                loggerService.log("Business Name Already Exists",
                        UfsCustomer.class.getSimpleName(), null, AppConstants.ACTIVITY_CREATE, ke.axle.chassis.utils.AppConstants.STATUS_FAILED, "Creating new customer category failed due to Business Name Already Exists");
                response.setMessage("Business Name Already Exists");
                response.setCode(HttpStatus.BAD_REQUEST.value());
                response.setData(SharedMethods.getFieldMapErrors(validation));
                return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
            }
        }

        String[] usernames = customerOnboarding.getDirectors().stream().map(BusinessDirectorsWrapper::getDirectorName).findFirst().get().split("\\s+");

        //generate username
        String username = null;
        if (usernames.length > 0) {
            username = (usernames.length == 1) ? posUserIdGenerator.generateUsername(new PosUserWrapper(usernames[0])) : posUserIdGenerator.generateUsername(new PosUserWrapper(usernames[0], usernames[1]));
        } else {
            String[] businessName = customerOnboarding.getBusinessName().split("\\s+");
            username = posUserIdGenerator.generateUsername(new PosUserWrapper(businessName[0]));
        }

        UfsAuthentication userAuth = urepo.findByusernameIgnoreCase(a.getName());
        String fullName = userAuth.getUser().getFullName();

        //saving customer Info
        UfsCustomer customer = new UfsCustomer();
        customer.setBusinessPrimaryContactNo(customerOnboarding.getBusinessPrimaryContactNo());
        customer.setDateIssued(customerOnboarding.getDateIssued());
        customer.setValidTo(customerOnboarding.getValidTo());
        customer.setMccIds(customerOnboarding.getMccIds());
        customer.setMainBank(customerOnboarding.getMainBank());
        customer.setBusinessLicenceNumber(customerOnboarding.getBusinessLicenseNumber());
        customer.setLocalRegistrationNumber(customerOnboarding.getLocalRegistrationNumber());
        customer.setBusinessName(customerOnboarding.getBusinessName());
        if (customerOnboarding.getCustomerClassId() != null) {
            customer.setCustomerClassId(customerOnboarding.getCustomerClassId());
        }
        customer.setAddress(customerOnboarding.getAddress());
        customer.setBusinessEmailAddress(customerOnboarding.getBusinessEmailAddress());
        customer.setPinNumber(customerOnboarding.getPinNumber());
        customer.setBusinessTypeIds(customerOnboarding.getBusinessTypeId());
        customer.setBusinessSecondaryContactNo(customerOnboarding.getBusinessSecondaryContactNo());
        customer.setCustomerTypeId(customerOnboarding.getCustomerTypeId());
        customer.setCommercialActivityId(customerOnboarding.getCommercialActivityId());
        customer.setEstateId(customerOnboarding.getEstateId());
        customer.setMid(customerOnboarding.getMid());
        customer.setCreatedBy(fullName);
        UfsCustomer ufsCustomer = customerService.saveCustomer(customer);

        loggerService.log("Created Customer successfully ", UfsCustomer.class.getSimpleName(), ufsCustomer.getId(), AppConstants.ACTIVITY_CREATE, AppConstants.STATUS_COMPLETED, null);

        //saving directors
        if (!customerOnboarding.getDirectors().isEmpty()) {
            for (BusinessDirectorsWrapper director : customerOnboarding.getDirectors()) {
                int c = 0;
                if (director.getDirectorName() != null && director.getDirectorName().length() != 0) {
                    c += 1;
                }
                if (director.getDirectorEmailAddress() != null && director.getDirectorEmailAddress().length() != 0) {
                    c += 1;
                }
                if (director.getDirectorDesignationId() != null) {
                    c += 1;
                }
                if (director.getDirectorPrimaryContactNumber() != null && director.getDirectorPrimaryContactNumber().length() != 0) {
                    c += 1;
                }
                if (director.getDirectorSecondaryContactNumber() != null && director.getDirectorSecondaryContactNumber().length() != 0) {
                    c += 1;
                }
                if (director.getDirectorIdNumber() != null && director.getDirectorIdNumber().length() != 0) {
                    c += 1;
                }

                if (c > 0) {
                    UfsCustomerOwners dir = new UfsCustomerOwners();
                    dir.setDirectorName(director.getDirectorName());
                    dir.setCustomerIds(BigDecimal.valueOf(customer.getId()));
                    dir.setDirectorEmailAddress(director.getDirectorEmailAddress());
                    dir.setDirectorDesignationId(director.getDirectorDesignationId());
                    dir.setDirectorPrimaryContactNumber(director.getDirectorPrimaryContactNumber());
                    dir.setDirectorSecondaryContactNumber(director.getDirectorSecondaryContactNumber());
                    dir.setDirectorIdNumber(director.getDirectorIdNumber());
                    dir.setUserName(username);
                    UfsCustomerOwners ufsCustomerOwners = ownersService.saveOwner(dir);

                    loggerService.log("Created Record successfully ", UfsCustomerOwners.class.getSimpleName(), ufsCustomerOwners.getId(), AppConstants.ACTIVITY_CREATE, AppConstants.STATUS_COMPLETED, null);

                }
            }

        }

        //saving outlets
        if (!customerOnboarding.getOutletsInfo().isEmpty()) {
            String finalUsername = username;
            customerOnboarding.getOutletsInfo().forEach(outlet -> {
                UfsCustomerOutlet custOutlet = new UfsCustomerOutlet();
                custOutlet.setCustomerIds(BigDecimal.valueOf(customer.getId()));
                custOutlet.setBankBranchIds(outlet.getBankBranchId());
                custOutlet.setLongitude(outlet.getLongitude());
                custOutlet.setLatitude(outlet.getLatitude());
                if (outlet.getOutletCode().equalsIgnoreCase("") || outlet.getOutletCode() == null) {

                    String code = CodeGenerator.generateOutletCode();
                    custOutlet.setOutletCode(code);
                } else {
                    custOutlet.setOutletCode(outlet.getOutletCode());
                }
                custOutlet.setOutletName(outlet.getOutletName());
                custOutlet.setOperatingHours(outlet.getOperatingHours());
                custOutlet.setGeographicalRegionIds(outlet.getGeographicalRegionIds());
                UfsCustomerOutlet ufsCustomerOutlet = outletService.saveOutlet(custOutlet);

                loggerService.log("Created Record successfully ", UfsCustomerOutlet.class.getSimpleName(), ufsCustomerOutlet.getId(), AppConstants.ACTIVITY_CREATE, AppConstants.STATUS_COMPLETED, null);
                //save contact person
                if (!outlet.getContactPerson().isEmpty()) {
                    for (OutletContactPerson outletContactPerson : outlet.getContactPerson()) {
                        int count = 0;
                        if (outletContactPerson.getContactPersonName() != null && outletContactPerson.getContactPersonName().length() != 0) {
                            count += 1;
                        }
                        if (outletContactPerson.getContactPersonIdNumber() != null && outletContactPerson.getContactPersonIdNumber().length() != 0) {
                            count += 1;
                        }
                        if (outletContactPerson.getPosRole() != null && outletContactPerson.getPosRole().length() != 0) {
                            count += 1;
                        }
                        if (outletContactPerson.getContactPersonEmail() != null && outletContactPerson.getContactPersonEmail().length() != 0) {
                            count += 1;
                        }
                        if (outletContactPerson.getContactPersonTelephone() != null && outletContactPerson.getContactPersonTelephone().length() != 0) {
                            count += 1;
                        }

                        if (count > 0) {
                            UfsContactPerson person = new UfsContactPerson();
                            person.setName(outletContactPerson.getContactPersonName());
                            person.setIdNumber(outletContactPerson.getContactPersonIdNumber());
                            person.setPosRole(outletContactPerson.getPosRole());
                            person.setEmail(outletContactPerson.getContactPersonEmail());
                            person.setPhoneNumber(outletContactPerson.getContactPersonTelephone());
                            person.setCustomerOutletId(custOutlet.getId());
                            person.setUserName(finalUsername);
                            UfsContactPerson ufsContactPerson = contactPersonService.saveContactPerson(person);

                            loggerService.log("Created Record successfully ", UfsContactPerson.class.getSimpleName(), ufsContactPerson.getId(), AppConstants.ACTIVITY_CREATE, AppConstants.STATUS_COMPLETED, null);
                        }
                    }
                }
            });

        }
        response.setCode(201);
        response.setMessage("Customer Onboarded successfully");
        response.setData(customer);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    private boolean validateUsernames(CustomerOnboardingWrapper customerOnboarding) {
        final Set<String> usernames = new LinkedHashSet<>();

        if (customerOnboarding.getDirectors() != null) {
            customerOnboarding.getDirectors().forEach(director -> {
                usernames.add(director.getUserName());
            });
        }

        List<UfsCustomerOwners> customerOwners = ownersService.findByUsernameIn(usernames);

        if (customerOnboarding.getOutletsInfo() != null) {
            for (OutletsInformationWrapper outlet : customerOnboarding.getOutletsInfo()) {
                if (outlet.getContactPerson() != null) {
                    outlet.getContactPerson().forEach(person -> {
                        if (person.getUserName() != null) {
                            usernames.add(person.getUserName());
                        }
                    });
                }
            }
        }
        List<UfsContactPerson> contactPeople = contactPersonService.findByUsernameIn(usernames);
        return customerOwners.size() > 0 || contactPeople.size() > 0;
    }

    private boolean validateUsernameList(Set<String> usernames) {
        List<UfsCustomerOwners> customerOwners = ownersService.findByUsernameIn(usernames);
        List<UfsContactPerson> contactPeople = contactPersonService.findByUsernameIn(usernames);
        return customerOwners.size() > 0 || contactPeople.size() > 0;
    }


    @RequestMapping(value = "/terminate", method = RequestMethod.PUT)
    @Transactional
    @ApiOperation(value = "Terminate Agent", notes = "Terminate multiple agents.")
    public ResponseEntity<ResponseWrapper<UfsCustomer>> terminateAgent(@Valid @RequestBody AgentTerminationWrapper<Long> actions) {
        ResponseWrapper response = new ResponseWrapper();

        List<BigDecimal> custIds = Stream.of(actions.getIds()).map(BigDecimal::new).collect(Collectors.toList());
        List<UfsCustomerOutlet> custOutlets = this.outletService.findByCustomerIdIn(custIds, AppConstants.INTRASH_NO);
        List<TmsDevice> devCustomer = this.deviceService.findByOutletIds(custOutlets.stream().map(x -> new BigDecimal(x.getId())).collect(Collectors.toList()));

        for (TmsDevice tmsDevice : devCustomer) {
            if (tmsDevice.getStatus().equals(AppConstants.STATUS_ACTIVE_STRING)) {
                String message = "Device with " + tmsDevice.getSerialNo() + " Already Assigned To This Customer.Please UnAssign The Device First to Proceeed";
                List<String> err = new ArrayList<>();
                err.add(message);
                response.setCode(HttpStatus.MULTI_STATUS.value());
                response.setData(err);
                response.setMessage(message);
                return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
            }
        }


        List<String> errors = new ArrayList<>();
        Arrays.stream(actions.getIds()).forEach(id -> {
            UfsCustomer customer = this.customerService.findByCustomerId(id);
            List<UfsCustomerOutlet> customerOutlets = this.outletService.findByCustomerId(new BigDecimal(id), AppConstants.INTRASH_NO);
            List<BigDecimal> outletIds = customerOutlets.stream().map(outletId -> new BigDecimal(outletId.getId())).collect(Collectors.toList());
            log.info("*******************" + Arrays.asList(outletIds));
            List<TmsDevice> deviceCustomer = this.deviceService.findByOutletIds(outletIds);

            //If the customer doesnt have any device
            if (deviceCustomer.size() < 1) {
                customer.setAction(AppConstants.ACTIVITY_TERMINATION);
                customer.setActionStatus(AppConstants.STATUS_UNAPPROVED);
                customer.setTerminationReason(actions.getTerminationReason());
                customer.setTerminationDate(new Date());
                this.customerService.saveCustomer(customer);
                loggerService.log("Successfully Terminated Customer",
                        UfsCustomer.class.getSimpleName(), id, AppConstants.ACTIVITY_UPDATE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());

            }

            for (TmsDevice tmsDevice : deviceCustomer) {
                customer.setAction(AppConstants.ACTIVITY_TERMINATION);
                customer.setActionStatus(AppConstants.STATUS_UNAPPROVED);
                customer.setTerminationReason(actions.getTerminationReason());
                customer.setTerminationDate(new Date());
                this.customerService.saveCustomer(customer);
                loggerService.log("Successfully Terminated Customer",
                        UfsCustomer.class.getSimpleName(), id, AppConstants.ACTIVITY_UPDATE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());

            }

        });

        if (errors.isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setData(errors);
            response.setMessage(AppConstants.CHECKER_GENERAL_ERROR);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        }
    }

    @RequestMapping(value = "/reactivate", method = RequestMethod.PUT)
    @Transactional
    @ApiOperation(value = "Reactivate Agent", notes = "Reactivate multiple agents.")
    public ResponseEntity<ResponseWrapper<UfsCustomer>> reactivateAgent(@Valid @RequestBody ActionWrapper<Long> actions) {
        ResponseWrapper response = new ResponseWrapper();

        List<String> errors = new ArrayList<>();
        Arrays.stream(actions.getIds()).forEach(id -> {
            UfsCustomer customer = this.customerService.findByCustomerId(id);
            customer.setAction(AppConstants.ACTIVITY_ACTIVATION);
            customer.setActionStatus(AppConstants.STATUS_UNAPPROVED);
            this.customerService.saveCustomer(customer);
            loggerService.log("Successfully Reactivated Customer",
                    UfsCustomer.class.getSimpleName(), id, AppConstants.ACTIVITY_UPDATE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());

        });



        if (errors.isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setData(errors);
            response.setMessage(AppConstants.CHECKER_GENERAL_ERROR);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper> approveActions(@Valid @RequestBody ActionWrapper<Long> actions) throws ExpectationFailed {
        ResponseWrapper response = new ResponseWrapper<>();
        List<Long> errors = new ArrayList<>();
        Arrays.stream(actions.getIds()).forEach(id -> {
            UfsCustomer customer = this.customerService.findByCustomerId(id);

            if ((customer.getAction().equals(AppConstants.ACTIVITY_UPDATE) && loggerService.isInitiator(UfsCustomer.class.getSimpleName(), id, AppConstants.ACTIVITY_UPDATE)) ||
                    (customer.getAction().equals(AppConstants.ACTIVITY_CREATE) && loggerService.isInitiator(UfsCustomer.class.getSimpleName(), id, AppConstants.ACTIVITY_CREATE)) ||
                    (customer.getAction().equals(AppConstants.ACTIVITY_ACTIVATION) && loggerService.isInitiator(UfsCustomer.class.getSimpleName(), id, AppConstants.ACTIVITY_ACTIVATION)) ||
                    (customer.getAction().equals(AppConstants.ACTIVITY_TERMINATION) && loggerService.isInitiator(UfsCustomer.class.getSimpleName(), id, AppConstants.ACTIVITY_TERMINATION)) ||
                    (customer.getAction().equals(AppConstants.ACTIVITY_DELETE) && loggerService.isInitiator(UfsCustomer.class.getSimpleName(), id, AppConstants.ACTIVITY_DELETE))

            ) {
                loggerService.log("Failed to approve customer. Maker can't approve their own record",
                        UfsCustomer.class.getSimpleName(), id, ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_FAILED, actions.getNotes());
                errors.add(id);
                return;
            }

            if (Objects.nonNull(customer)) {
                String action = customer.getAction();
                String actionStatus = customer.getActionStatus();
                //approving customer owner
                List<UfsCustomerOwners> customerOwners = this.ownersService.findOwnersByCustomerIds(new BigDecimal(id));
                //approving customer outlet
                List<UfsCustomerOutlet> customerOutlets = this.customerService.findOutletsByCustomerIds(new BigDecimal(id));


                if ((action.equalsIgnoreCase(AppConstants.ACTIVITY_ACTIVATION) && actionStatus.equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) ||
                        (action.equalsIgnoreCase(AppConstants.ACTIVITY_CREATE) && actionStatus.equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED))
                ) {
<<<<<<< HEAD
                    customer.setStatus(AppConstants.STATUS_ACTIVE_STRING);
=======
>>>>>>> brb-webportal
                    customer.setActionStatus(AppConstants.STATUS_APPROVED);
                    this.customerService.saveCustomer(customer);
                    loggerService.log("Successfully Approved Customer",
                            UfsCustomer.class.getSimpleName(), id, AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());
                }

                //terminating customer
                if ((action.equalsIgnoreCase(AppConstants.ACTIVITY_TERMINATION) && actionStatus.equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED))) {
                    customer.setStatus(AppConstants.STATUS_INACTIVE);
                    customer.setActionStatus(AppConstants.STATUS_APPROVED);
                    this.customerService.saveCustomer(customer);
                    loggerService.log("Successfully Approved Customer",
                            UfsCustomer.class.getSimpleName(), id, ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());

                }


                if (action.equalsIgnoreCase(AppConstants.ACTIVITY_CREATE) && actionStatus.equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                    if (!customerOwners.isEmpty()) {
                        for (UfsCustomerOwners customerOwner : customerOwners) {
                            if ((customerOwner.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE) && customerOwner.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) ||
                                    (customerOwner.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_CREATE) && customerOwner.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED))
                            ) {
                                customerOwner.setActionStatus(AppConstants.STATUS_APPROVED);
                                this.ownersService.saveOwner(customerOwner);
                                loggerService.log("Successfully Approved Customer Owner",
                                        UfsCustomerOwners.class.getSimpleName(), customerOwner.getId(), ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());

                                if (customerOwner.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DELETE) && customerOwner.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                                    customerOwner.setActionStatus(AppConstants.STATUS_APPROVED);
                                    customerOwner.setIntrash(AppConstants.INTRASH_YES);
                                    this.ownersService.saveOwner(customerOwner);

                                    loggerService.log("Successfully Approved Customer Owner Deletion",
                                            UfsCustomerOwners.class.getSimpleName(), customerOwner.getId(), ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());
                                }
                            }
                        }
                    }
                    if (!customerOutlets.isEmpty()) {
                        for (UfsCustomerOutlet customerOutlet : customerOutlets) {
                            if ((customerOutlet.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE) && customerOutlet.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) ||
                                    (customerOutlet.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_CREATE) && customerOutlet.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED))
                            ) {
                                customerOutlet.setActionStatus(AppConstants.STATUS_APPROVED);
                                this.customerService.saveOutlet(customerOutlet);
                                loggerService.log("Successfully Approved Customer Outlet",
                                        UfsCustomerOutlet.class.getSimpleName(), customerOutlet.getId(), ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());

                                if (customerOutlet.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DELETE) && customerOutlet.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                                    customerOutlet.setActionStatus(AppConstants.STATUS_APPROVED);
                                    customerOutlet.setIntrash(AppConstants.INTRASH_YES);
                                    this.customerService.saveOutlet(customerOutlet);

                                    loggerService.log("Successfully Approved Customer Outlet Deletion",
                                            UfsCustomerOutlet.class.getSimpleName(), customerOutlet.getId(), ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());
                                }
                            }
                        }
                    }

                    deviceService.activateDevicesByOutlets(customerOutlets, actions.getNotes());

                    deviceService.approveContactPersons(id, actions.getNotes());
                }
                //updating customer
                if (action.equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE) && customer.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                    try {
                        UfsCustomer entity = supportRepo.mergeChanges(id, customer);
                        customer.setBusinessName(entity.getBusinessName());
                        customer.setBusinessEmailAddress(entity.getBusinessEmailAddress());
                        customer.setBusinessLicenceNumber(entity.getBusinessLicenceNumber());
                        customer.setBusinessPrimaryContactNo(entity.getBusinessPrimaryContactNo());
                        customer.setBusinessSecondaryContactNo(entity.getBusinessSecondaryContactNo());
                        customer.setBusinessTypeIds(entity.getBusinessTypeIds());
                        customer.setPinNumber(entity.getPinNumber());
                        customer.setDateIssued(entity.getDateIssued());
                        customer.setValidTo(entity.getValidTo());
                        customer.setAddress(entity.getAddress());
                        customer.setCommercialActivityId(entity.getCommercialActivityId());
                        if (entity.getCustomerClassId() != null) {
                            customer.setCustomerClassId(entity.getCustomerClassId());
                        }
                        customer.setMccIds(entity.getMccIds());
                        customer.setMainBank(entity.getMainBank());
                        customer.setCustomerTypeId(entity.getCustomerTypeId());
                        customer.setEstateId(entity.getEstateId());
                        customer.setMid(entity.getMid());
                        customer.setActionStatus(AppConstants.STATUS_APPROVED);
                        this.customerService.saveCustomer(customer);
                        loggerService.log("Successfully Approved Customer Update",
                                UfsCustomer.class.getSimpleName(), id, ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());

                        // update device owner name
                        List<Long> outletids = customerOutlets.stream().map(UfsCustomerOutlet::getId).collect(Collectors.toList());
                        // deviceService.updateDeviceOwnerByOutletId(outletids, entity.getBusinessName());

                        //update director contact, Contact person details
                        deviceService.updateContactPersonsDetails(entity);

                        deviceService.addDevicesTaskByOutletsIds(outletids);

                    } catch (IOException | IllegalAccessException e) {
                        e.printStackTrace();
                    }


                    if (customer.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DELETE) && customer.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)) {
                        customer.setActionStatus(AppConstants.STATUS_APPROVED);
                        customer.setIntrash(AppConstants.INTRASH_YES);
                        this.customerService.saveCustomer(customer);

                        loggerService.log("Successfully Approved Customer Deletion",
                                UfsCustomer.class.getSimpleName(), id, ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, actions.getNotes());
                    }

                }
            } else {

                loggerService.log("Failed To Approve Customer.Customer With Id: " + id + " does not exist",
                        UfsCustomer.class.getSimpleName(), id, ke.axle.chassis.utils.AppConstants.ACTIVITY_APPROVE, ke.axle.chassis.utils.AppConstants.STATUS_FAILED, actions.getNotes());
            }

        });

        if (!errors.isEmpty()) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setData(errors);
            response.setMessage("Failed to approve customer. Maker can't approve their own record");
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        }

        response.setCode(200);
        response.setMessage("Customer Approved Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper> declineActions(@Valid @RequestBody ActionWrapper<Long> actions) {
        ResponseEntity<ResponseWrapper> resp = super.declineActions(actions);

        if (!resp.getStatusCode().equals(HttpStatus.OK)) {
            return resp;
        }
        Arrays.stream(actions.getIds()).forEach(id -> {
            UfsCustomer customer = this.customerService.findByCustomerId(id);
            String action = customer.getAction();
            String actionStatus = customer.getActionStatus();

            //terminating customer
            if ((action.equalsIgnoreCase(AppConstants.ACTIVITY_TERMINATION) && actionStatus.equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED))) {
                customer.setStatus(AppConstants.STATUS_ACTIVE_STRING);
                customer.setActionStatus(AppConstants.STATUS_REJECTED);
                this.customerService.saveCustomer(customer);
                loggerService.log("Successfully Declined Customer Termination",
                        UfsCustomer.class.getSimpleName(), id, AppConstants.ACTIVITY_APPROVE, AppConstants.STATUS_REJECTED, actions.getNotes());
            }
            if (action.equals(AppConstants.ACTIVITY_CREATE) && actionStatus.equals(AppConstants.STATUS_REJECTED)) {
                //approving customer owner
                List<UfsCustomerOwners> customerOwners = this.ownersService.findOwnersByCustomerIds(new BigDecimal(id));
                //approving customer outlet
                List<UfsCustomerOutlet> customerOutlets = this.customerService.findOutletsByCustomerIds(new BigDecimal(id));
                deviceService.deActivateDevicesByOutlets(customerOutlets, actions.getNotes());
                ownersService.deactivateByOwnersList(customerOwners);
                deviceService.delineContactPersons(id, actions.getNotes());
                outletService.deleteByCustomerId(new BigDecimal(id));
            }
        });

        return resp;
    }

    @PutMapping(value = "/update-mid")
    public ResponseEntity<ResponseWrapper> updateMids(@Valid @RequestBody ActionWrapper<Long> actions) {
        ResponseWrapper response = new ResponseWrapper<>();
        Arrays.stream(actions.getIds()).forEach(id -> {
            UfsCustomer customer = this.customerService.findByCustomerId(id);
            if (customer != null) {
                if (customer.getMid() == null) {
                    this.customerService.updateCustomerMidPerId(customer);
                }
            }

        });

        response.setCode(200);
        response.setMessage("Customer MID updated Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/terminated-agents", method = RequestMethod.GET)
    @Transactional
    @ApiOperation(value = "Terminated Agents", notes = "Get All Terminated Agents.")
    public ResponseWrapper<Object> getTerminatedAgents(Pageable pg) {
        ResponseWrapper response = new ResponseWrapper<>();

        List<UfsCustomer> terminatedCustomers = this.customerService.getAllTerminatedAgents(AppConstants.ACTIVITY_TERMINATION, AppConstants.STATUS_APPROVED);
        Iterable<UfsCustomerOwners> customerOwners = this.customerService.getAllCustomerOwners();


        List<AgentTerminationResponseWrapper> terminatedCustomerList = new ArrayList<>();

        for (UfsCustomer customer : terminatedCustomers) {
            AgentTerminationResponseWrapper terminationResponse = new AgentTerminationResponseWrapper();
            terminationResponse.setAgentName(customer.getBusinessName());
            terminationResponse.setEmail(this.getOwnerDetails(customerOwners, customer.getId()).getEmail());
            terminationResponse.setOwnerName(this.getOwnerDetails(customerOwners, customer.getId()).getOwnerName());
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
