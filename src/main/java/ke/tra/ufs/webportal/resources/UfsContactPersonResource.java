package ke.tra.ufs.webportal.resources;

import io.swagger.annotations.ApiOperation;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.exceptions.ExpectationFailed;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ActionWrapper;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsContactPerson;
import ke.tra.ufs.webportal.entities.UfsCustomer;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.UfsPosUser;
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
public class UfsContactPersonResource extends ChasisResource<UfsContactPerson,Long, UfsEdittedRecord> {

    private final ContactPersonService contactPersonService;
    private final PosUserService posUserService;
    private final SysConfigService configService;
    private final PasswordEncoder encoder;
    private final NotifyService notifyService;
    private final TmsDeviceService tmsDeviceService;

    public UfsContactPersonResource(LoggerService loggerService, EntityManager entityManager,ContactPersonService contactPersonService,PosUserService posUserService,
                                    SysConfigService configService,PasswordEncoder encoder,NotifyService notifyService,TmsDeviceService tmsDeviceService ) {
        super(loggerService, entityManager);
        this.contactPersonService = contactPersonService;
        this.posUserService = posUserService;
        this.configService = configService;
        this.encoder = encoder;
        this.notifyService = notifyService;
        this.tmsDeviceService = tmsDeviceService;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsContactPerson>> create(@RequestBody @Valid UfsContactPerson ufsContactPerson) {
        ResponseWrapper response = new ResponseWrapper();

        //check if contact person user name already exists
        UfsContactPerson contactPerson = contactPersonService.findByUsername(ufsContactPerson.getUserName());
        if (Objects.nonNull(contactPerson)) {
            response.setCode(417);
            response.setMessage("Contact Person With That username Already Exists");
            return new ResponseEntity(response, HttpStatus.FAILED_DEPENDENCY);
        }
        ResponseEntity<ResponseWrapper<UfsContactPerson>> creationResp = super.create(ufsContactPerson);
        if ((!creationResp.getStatusCode().equals(HttpStatus.CREATED)) || (ufsContactPerson.getDeviceId() == null)) {
            return creationResp;
        }
        String serialNumber = tmsDeviceService.findByDeviceIdAndIntrash(ufsContactPerson.getDeviceId()).getSerialNo();
        UfsPosUser posUser = posUserService.findByContactPersonIdAndDeviceIdAndSerialNumber(ufsContactPerson.getId(), ufsContactPerson.getDeviceId(),serialNumber);

        UfsPosUser savedUser = posUser;

        //generate random pin
        String randomPin = RandomStringUtils.random(Integer.parseInt(configService.findByEntityAndParameter(AppConstants.ENTITY_POS_CONFIGURATION, AppConstants.PARAMETER_POS_PIN_LENGTH).getValue()), false, true);
        log.info("The generated pin is: " + randomPin);
        if (Objects.isNull(posUser)) {
            UfsPosUser ufsPosUser = new UfsPosUser();
            ufsPosUser.setPosRole(ufsContactPerson.getPosRole());
            ufsPosUser.setUsername(ufsContactPerson.getUserName());
            ufsPosUser.setContactPersonId(ufsContactPerson.getId());
            ufsPosUser.setPin(encoder.encode(randomPin));
            ufsPosUser.setTmsDeviceId(ufsContactPerson.getDeviceId());
            ufsPosUser.setActiveStatus(AppConstants.STATUS_INACTIVE);
            ufsPosUser.setPinStatus(AppConstants.PIN_STATUS_INACTIVE);
            ufsPosUser.setPhoneNumber(ufsContactPerson.getPhoneNumber());
            ufsPosUser.setIdNumber(ufsContactPerson.getIdNumber());
            ufsPosUser.setSerialNumber(serialNumber);

            String[] name = ufsContactPerson.getName().split("\\s+");
            if (name.length > 0) {
                ufsPosUser.setFirstName(name[0]);
                if (name.length > 1) {
                    ufsPosUser.setOtherName(name[1]);
                }
            }
            savedUser = posUserService.savePosUser(ufsPosUser);

            String message = "Your username is " + savedUser.getUsername() + ". Use password :" + randomPin + " to login to POS terminal";
            if (!ufsContactPerson.getEmail().isEmpty()) {
                notifyService.sendEmail(ufsContactPerson.getEmail(), "Login Credentials", message);
                loggerService.log("Sent login credentials for " + ufsContactPerson.getName(),UfsPosUser.class.getName(),savedUser.getPosUserId(),
                        AppConstants.ACTIVITY_CREATE, AppConstants.STATUS_COMPLETED,"Sent login credentials");

            } else {
                if (!ufsContactPerson.getPhoneNumber().isEmpty()) {
                    // send sms
                    posUserService.sendSmsMessage(ufsContactPerson.getPhoneNumber(), message);
                    loggerService.log("Sent login credentials for " + ufsContactPerson.getName(),UfsPosUser.class.getName(),savedUser.getPosUserId(),
                            AppConstants.ACTIVITY_CREATE, AppConstants.STATUS_COMPLETED,"Sent login credentials");

                } else {

                    loggerService.log("Failed to send login credentials for " + ufsContactPerson.getName(),UfsPosUser.class.getName(),savedUser.getPosUserId(),
                            AppConstants.ACTIVITY_CREATE, AppConstants.STATUS_FAILED_STRING,"No valid email or phone number.");

                }
            }
        }


        response.setCode(201);
        response.setData(ufsContactPerson);
        response.setMessage("Agent Owner Created Successfully.");
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{customerId}/all" , method = RequestMethod.GET)
    @Transactional
    @ApiOperation(value = "Contact Person", notes = "Get All Contact Person By Customer Id.")
    public ResponseWrapper<Object> getContactPersonByCustomerId(Pageable pg, @PathVariable("customerId") BigDecimal customerId) {
        ResponseWrapper response =  new ResponseWrapper<>();

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

        //approve also contact person in the  Pos User Table
        Arrays.stream(actions.getIds()).forEach(id->{
            UfsContactPerson ufsContactPerson = this.contactPersonService.findContactPersonById(id);
            if(ufsContactPerson.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_CREATE)){
                List<UfsPosUser> posUsers = this.posUserService.findByContactPersonId(id);
                if(!posUsers.isEmpty()){
                   posUsers.forEach(posUser->{
                       posUser.setActionStatus(AppConstants.STATUS_APPROVED);
                       posUserService.savePosUser(posUser);
                   });
                }
            }
        });

        return resp;

    }
}
