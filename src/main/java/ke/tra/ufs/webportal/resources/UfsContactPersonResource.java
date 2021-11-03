package ke.tra.ufs.webportal.resources;

import io.swagger.annotations.ApiOperation;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.exceptions.ExpectationFailed;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ActionWrapper;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsContactPerson;
import ke.tra.ufs.webportal.entities.UfsCustomerOutlet;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.UfsPosUser;
import ke.tra.ufs.webportal.entities.wrapper.PosUserWrapper;
import ke.tra.ufs.webportal.entities.wrapper.contactPersonDeviceWrapper;
import ke.tra.ufs.webportal.service.*;
import ke.tra.ufs.webportal.utils.AppConstants;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;


@RestController
@RequestMapping(value = "/contact-person")
public class UfsContactPersonResource extends ChasisResource<UfsContactPerson, Long, UfsEdittedRecord> {

    private final ContactPersonService contactPersonService;
    private final UfsCustomerOutletService customerOutletService;
    private final PosUserService posUserService;
    private final SysConfigService configService;
    private final PasswordEncoder encoder;
    private final NotifyService notifyService;
    private final TmsDeviceService tmsDeviceService;
    private final PosUserIdGenerator posUserIdGenerator;

    public UfsContactPersonResource(LoggerService loggerService, EntityManager entityManager, ContactPersonService contactPersonService, PosUserService posUserService,
                                    SysConfigService configService, PasswordEncoder encoder, NotifyService notifyService, TmsDeviceService tmsDeviceService, UfsCustomerOutletService customerOutletService, PosUserIdGenerator posUserIdGenerator) {
        super(loggerService, entityManager);
        this.contactPersonService = contactPersonService;
        this.posUserService = posUserService;
        this.configService = configService;
        this.encoder = encoder;
        this.notifyService = notifyService;
        this.tmsDeviceService = tmsDeviceService;
        this.customerOutletService = customerOutletService;
        this.posUserIdGenerator = posUserIdGenerator;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsContactPerson>> create(@RequestBody @Valid UfsContactPerson ufsContactPerson) {
        ResponseWrapper response = new ResponseWrapper();
        String outletName = null;
        String merchantName = null;
        String[] usernames = ufsContactPerson.getName().split("\\s+");

        //generate username
        String username = (usernames.length == 1) ? posUserIdGenerator.generateUsername(new PosUserWrapper(usernames[0])) : posUserIdGenerator.generateUsername(new PosUserWrapper(usernames[0], usernames[1]));
        ufsContactPerson.setUserName(username);
        ResponseEntity<ResponseWrapper<UfsContactPerson>> creationResp = super.create(ufsContactPerson);
        if ((!creationResp.getStatusCode().equals(HttpStatus.CREATED)) || (ufsContactPerson.getDeviceId() == null)) {
            return creationResp;
        }

        //check outlet by outletId
        UfsCustomerOutlet customerOutlet = customerOutletService.findById(ufsContactPerson.getCustomerOutletId());
        if (Objects.nonNull(customerOutlet)) {
            outletName = customerOutlet.getOutletName();
            merchantName = customerOutlet.getCustomerId().getBusinessName();

        }

        String serialNumber = tmsDeviceService.findByDeviceIdAndIntrash(ufsContactPerson.getDeviceId()).getSerialNo();
        UfsPosUser posUser = posUserService.findByContactPersonIdAndDeviceIdAndSerialNumber(ufsContactPerson.getId(), ufsContactPerson.getDeviceId(), serialNumber);

        //generate random pin
        String randomPin = RandomStringUtils.random(Integer.parseInt(configService.findByEntityAndParameter(AppConstants.ENTITY_POS_CONFIGURATION, AppConstants.PARAMETER_POS_PIN_LENGTH).getValue()), false, true);
        log.info("The generated pin is: " + randomPin);
        if (Objects.isNull(posUser)) {
            UfsPosUser ufsPosUser = new UfsPosUser();
            ufsPosUser.setPosRole(ufsContactPerson.getPosRole());
            ufsPosUser.setUsername(username);
            ufsPosUser.setContactPersonId(ufsContactPerson.getId());
            ufsPosUser.setPin(encoder.encode(randomPin));
            ufsPosUser.setTmsDeviceId(ufsContactPerson.getDeviceId());
            ufsPosUser.setActiveStatus(AppConstants.STATUS_INACTIVE);
            ufsPosUser.setPinStatus(AppConstants.PIN_STATUS_INACTIVE);
            ufsPosUser.setPhoneNumber(ufsContactPerson.getPhoneNumber());
            ufsPosUser.setIdNumber(ufsContactPerson.getIdNumber());
            ufsPosUser.setSerialNumber(serialNumber);
            ufsPosUser.setFirstTimeUser((short) 0);
            ufsPosUser.setMerchantName(merchantName);
            ufsPosUser.setOutletName(outletName);

            String[] name = ufsContactPerson.getName().split("\\s+");
            if (name.length > 0) {
                ufsPosUser.setFirstName(name[0]);
                if (name.length > 1) {
                    ufsPosUser.setOtherName(name[1]);
                }
            }
            posUserService.savePosUser(ufsPosUser);

            loggerService.log("Contact Person Created  Successfully",
                    UfsPosUser.class.getSimpleName(), ufsPosUser.getPosUserId(), ke.axle.chassis.utils.AppConstants.ACTIVITY_CREATE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, "Contact Person Created Successfully");

        }

        response.setCode(201);
        response.setData(ufsContactPerson);
        response.setMessage("Contact Person Created Successfully.");
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{customerId}/all", method = RequestMethod.GET)
    @Transactional
    @ApiOperation(value = "Contact Person", notes = "Get All Contact Person By Customer Id.")
    public ResponseWrapper<Object> getContactPersonByCustomerId(Pageable pg, @PathVariable("customerId") BigDecimal customerId) {
        ResponseWrapper response = new ResponseWrapper<>();

        List<UfsContactPerson> contactPersonList = this.contactPersonService.getAllContactPersonByCustomerId(customerId);

        Page<UfsContactPerson> pageResponse = new PageImpl<>(contactPersonList, pg, contactPersonList.size());
        response.setData(pageResponse);
        response.setCode(HttpStatus.OK.value());
        response.setTimestamp(Calendar.getInstance().getTimeInMillis());
        response.setMessage("Request Was successful");

        return response;
    }


    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper> approveActions(@Valid @RequestBody ActionWrapper<Long> actions) throws ExpectationFailed {
        ResponseEntity<ResponseWrapper> resp = super.approveActions(actions);

        if (!resp.getStatusCode().equals(HttpStatus.OK)) {
            return resp;
        }

        //approve/delete also contact person in the  Pos User Table
        Arrays.stream(actions.getIds()).forEach(id -> {

            Optional<UfsContactPerson> ufsContactPerson = this.contactPersonService.findContactPersonById(id);
            if (ufsContactPerson.isPresent()) {
                if (ufsContactPerson.get().getAction().equalsIgnoreCase(AppConstants.ACTIVITY_CREATE)) {
                    List<UfsPosUser> posUsers = this.posUserService.findByContactPersonId(id);
                    if (!posUsers.isEmpty()) {
                        posUsers.forEach(posUser -> {

                            UfsPosUser savedUser = posUser;

                            //generate random pin
                            String randomPin = RandomStringUtils.random(Integer.parseInt(configService.findByEntityAndParameter(AppConstants.ENTITY_POS_CONFIGURATION, AppConstants.PARAMETER_POS_PIN_LENGTH).getValue()), false, true);
                            log.info("The generated pin is: " + randomPin);

                            posUser.setActionStatus(AppConstants.STATUS_APPROVED);
                            posUser.setPin(encoder.encode(randomPin));
                            savedUser = posUserService.savePosUser(posUser);

                            String message = "Your username is " + savedUser.getUsername() + ". Use password :" + randomPin + " to login to POS terminal";
                            if (ufsContactPerson.get().getEmail()!=null) {
                                notifyService.sendEmail(ufsContactPerson.get().getEmail(), "Login Credentials", message);
                                notifyService.sendSms(ufsContactPerson.get().getPhoneNumber(), message);
                                loggerService.log("Sent login credentials for " + ufsContactPerson.get().getName(), UfsPosUser.class.getName(), savedUser.getPosUserId(),
                                        AppConstants.ACTIVITY_CREATE, AppConstants.STATUS_COMPLETED, "Sent login credentials");

                            } else {
                                if (ufsContactPerson.get().getPhoneNumber()!=null) {
                                    // send sms
                                    notifyService.sendSms(ufsContactPerson.get().getPhoneNumber(), message);
                                    loggerService.log("Sent login credentials for " + ufsContactPerson.get().getName(), UfsPosUser.class.getName(), savedUser.getPosUserId(),
                                            AppConstants.ACTIVITY_CREATE, AppConstants.STATUS_COMPLETED, "Sent login credentials");

                                } else {

                                    loggerService.log("Failed to send login credentials for " + ufsContactPerson.get().getName(), UfsPosUser.class.getName(), savedUser.getPosUserId(),
                                            AppConstants.ACTIVITY_CREATE, AppConstants.STATUS_FAILED_STRING, "No valid email or phone number.");

                                }
                            }
                        });
                    }
                }

                if (ufsContactPerson.get().getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DELETE)) {
                    List<UfsPosUser> posUsers = this.posUserService.findByContactPersonId(id);
                    if (!posUsers.isEmpty()) {
                        posUsers.forEach(posUser -> {
                            posUser.setActionStatus(AppConstants.STATUS_APPROVED);
                            posUser.setIntrash(AppConstants.YES);
                            posUser.setAction(AppConstants.ACTIVITY_DELETE);
                            posUserService.savePosUser(posUser);

                        });
                    }

                }

            }

        });

        return resp;

    }


    @Transactional
    @RequestMapping(value = "/assign-device", method = RequestMethod.POST)
    @ApiOperation(value = "Contact Person", notes = "Assign contact person to a specific device")
    public ResponseEntity<ResponseWrapper> contactPersonDeviceAssign(@Valid @RequestBody contactPersonDeviceWrapper personDeviceWrapper) {
        ResponseWrapper response = new ResponseWrapper<>();

        UfsContactPerson contactPerson = contactPersonService.findContactPersonByIdAndIntrash(personDeviceWrapper.getContactPersonId());

        String serialNumber = tmsDeviceService.findByDeviceIdAndIntrash(personDeviceWrapper.getDeviceId()).getSerialNo();
        UfsPosUser posUser = posUserService.findByContactPersonIdAndDeviceIdAndSerialNumber(personDeviceWrapper.getContactPersonId(), personDeviceWrapper.getDeviceId(), serialNumber);

        //generate random pin
        String randomPin = RandomStringUtils.random(Integer.parseInt(configService.findByEntityAndParameter(AppConstants.ENTITY_POS_CONFIGURATION, AppConstants.PARAMETER_POS_PIN_LENGTH).getValue()), false, true);
        log.info("The generated pin is: " + randomPin);


        if (Objects.isNull(posUser)) {
            UfsPosUser ufsPosUser = new UfsPosUser();
            ufsPosUser.setPosRole(contactPerson.getPosRole());
            ufsPosUser.setUsername(contactPerson.getUserName());
            ufsPosUser.setContactPersonId(personDeviceWrapper.getContactPersonId());
            ufsPosUser.setPin(encoder.encode(randomPin));
            ufsPosUser.setTmsDeviceId(personDeviceWrapper.getDeviceId());
            ufsPosUser.setActiveStatus(AppConstants.STATUS_INACTIVE);
            ufsPosUser.setPinStatus(AppConstants.PIN_STATUS_INACTIVE);
            ufsPosUser.setPhoneNumber(contactPerson.getPhoneNumber());
            ufsPosUser.setIdNumber(contactPerson.getIdNumber());
            ufsPosUser.setSerialNumber(serialNumber);

            String[] name = contactPerson.getName().split("\\s+");
            if (name.length > 0) {
                ufsPosUser.setFirstName(name[0]);
                if (name.length > 1) {
                    ufsPosUser.setOtherName(name[1]);
                }
            }
            posUserService.savePosUser(ufsPosUser);
            loggerService.log("Contact Person Assigned Device Successfully",
                    UfsPosUser.class.getSimpleName(), ufsPosUser.getPosUserId(), ke.axle.chassis.utils.AppConstants.ACTIVITY_CREATE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, "Contact Person Assigned Device Successfully");
            response.setCode(200);
            response.setMessage("Contact Person Assigned Device Successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } else {
            loggerService.log("Contact Person Already Assigned Device",
                    UfsPosUser.class.getSimpleName(), null, ke.axle.chassis.utils.AppConstants.ACTIVITY_CREATE, ke.axle.chassis.utils.AppConstants.STATUS_FAILED, "Contact Person Already Assigned Device");
            response.setCode(HttpStatus.CONFLICT.value());
            response.setMessage("Contact Person Already Assigned Device");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

    }
}
