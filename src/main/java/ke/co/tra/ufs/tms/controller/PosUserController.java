package ke.co.tra.ufs.tms.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.exceptions.ExpectationFailed;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.co.tra.ufs.tms.entities.TmsDevice;
import ke.co.tra.ufs.tms.entities.UfsDeviceMake;
import ke.co.tra.ufs.tms.entities.UfsPosUser;
import ke.co.tra.ufs.tms.entities.UfsSysConfig;
import ke.co.tra.ufs.tms.entities.wrappers.filters.CommonFilter;
import ke.co.tra.ufs.tms.entities.wrappers.filters.MakeFilter;
import ke.co.tra.ufs.tms.entities.wrappers.filters.PosUserFilter;
import ke.co.tra.ufs.tms.repository.SysConfigRepository;
import ke.co.tra.ufs.tms.repository.UfsPosUserRepository;
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;
import ke.co.tra.ufs.tms.service.NotificationService;
import ke.co.tra.ufs.tms.service.NotifyService;
import ke.co.tra.ufs.tms.service.PosUserService;
import ke.co.tra.ufs.tms.service.templates.PosUserServiceTemplate;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import ke.co.tra.ufs.tms.wrappers.ActionWrapper;
import ke.co.tra.ufs.tms.wrappers.UserPinWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping(value = "/pos_user")
@Slf4j
public class PosUserController{

    private final PosUserServiceTemplate userServiceTemplate;
    private final SysConfigRepository sysConfigRepository;
    private final UfsPosUserRepository ufsPosUserRepository;
    private final PasswordEncoder encoder;
    private final LoggerServiceLocal loggerService;
    private final PosUserService posUserService;
    private final NotifyService notifyService;

    public PosUserController(LoggerServiceLocal loggerService, PosUserServiceTemplate userServiceTemplate,
                             SysConfigRepository sysConfigRepository,UfsPosUserRepository ufsPosUserRepository,
                             PasswordEncoder encoder,PosUserService posUserService,NotifyService notifyService) {
        this.userServiceTemplate = userServiceTemplate;
        this.sysConfigRepository = sysConfigRepository;
        this.ufsPosUserRepository = ufsPosUserRepository;
        this.encoder = encoder;
        this.loggerService = loggerService;
        this.posUserService = posUserService;
        this.notifyService = notifyService;
    }


    @ApiOperation(value = "Fetch Pos Users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "int", required = false, value = "Pagination size e.g 20", paramType = "query")
            ,
            @ApiImplicitParam(name = "page", dataType = "int", required = false, value = "Page number e.g 0", paramType = "query")
            ,
            @ApiImplicitParam(name = "sort", dataType = "string", required = false, value = "status,desc", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ke.co.tra.ufs.tms.wrappers.ResponseWrapper<Page<UfsPosUser>>> getPosUsers(Pageable pg, @Valid @ApiParam(value = "Entity filters and search parameters") PosUserFilter filter,
                                                                                                    HttpServletRequest request) {
        ke.co.tra.ufs.tms.wrappers.ResponseWrapper<Page<UfsPosUser>> response = new ke.co.tra.ufs.tms.wrappers.ResponseWrapper();

        if(request.getParameter("customerOwnersId") != null && !request.getParameter("customerOwnersId").isEmpty() &&
                request.getParameter("tmsDeviceId") != null && !request.getParameter("tmsDeviceId").isEmpty()){

            Long customerOwnersId = Long.parseLong(request.getParameter("customerOwnersId"));
            BigDecimal tmsDeviceId = new BigDecimal(request.getParameter("tmsDeviceId"));
            response.setData(posUserService.getPosUsers(filter.getAction(), filter.getActionStatus(), filter.getNeedle(),tmsDeviceId,customerOwnersId, pg));
        }else{
            response.setData(posUserService.getPosUsers(filter.getAction(), filter.getActionStatus(), filter.getNeedle(), pg));

        }
        return ResponseEntity.ok(response);
    }


    @ApiOperation(value = "Fetch Pos User by contactPersonId")
    @RequestMapping(method = RequestMethod.GET, value = "/contactPerson/{contactPersonId}")
    public ResponseEntity<ke.co.tra.ufs.tms.wrappers.ResponseWrapper> getPosUserByContactPersonId(@PathVariable Long contactPersonId) {
        ke.co.tra.ufs.tms.wrappers.ResponseWrapper response = new ke.co.tra.ufs.tms.wrappers.ResponseWrapper();
        List<UfsPosUser> posUser = posUserService.findByContactPersonId(contactPersonId);
        if (posUser.isEmpty()) {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Pos Users not found");

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        }
        response.setData(posUser);
        response.setCode(HttpStatus.OK.value());
        response.setMessage("success");
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Fetch Pos User by ID")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<ke.co.tra.ufs.tms.wrappers.ResponseWrapper> getPosUserById(@PathVariable("id") BigDecimal id) {
        ke.co.tra.ufs.tms.wrappers.ResponseWrapper response = new ke.co.tra.ufs.tms.wrappers.ResponseWrapper();
        UfsPosUser posUser = posUserService.findByPosUserId(id);
        if (posUser == null) {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Pos User not found");

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        }

        response.setData(posUser);
        response.setCode(HttpStatus.OK.value());
        response.setMessage("success");
        return ResponseEntity.ok(response);

    }



    @RequestMapping(value = "/reset-pin",method = RequestMethod.POST)
    public ResponseEntity resetPin(@RequestBody ActionWrapper<BigDecimal> ids) {
        return  userServiceTemplate.resetPin(ids);
    }

    @RequestMapping(value = "/lock-user",method = RequestMethod.POST)
    public ResponseEntity lockUser(@RequestBody ActionWrapper<BigDecimal> accounts) {
        return  userServiceTemplate.lockUser(accounts);
    }

    @RequestMapping(value = "/unlock-user",method = RequestMethod.POST)
    public ResponseEntity unlockUser(@RequestBody ActionWrapper<BigDecimal> accounts) {
        return  userServiceTemplate.unLockUser(accounts);
    }

    @RequestMapping(value = "/first-time-login",method = RequestMethod.POST)
    public ResponseEntity loginForFirstTime(@RequestBody UserPinWrapper wrapper) {
        return  userServiceTemplate.firstTimeLogin(wrapper);
    }

    @RequestMapping(value = "/change-pin",method = RequestMethod.POST)
    public ResponseEntity changePin(@RequestBody UserPinWrapper wrapper) {
        return  userServiceTemplate.changePin(wrapper);
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResponseEntity loginUser(@RequestBody UserPinWrapper wrapper) {
        return  userServiceTemplate.login(wrapper);
    }

    @ApiOperation(value = "Approve Pos User Actions")
    @RequestMapping(value = "/approve-actions", method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity<ResponseWrapper> approveActions(@Valid @RequestBody ActionWrapper<BigDecimal> actions) throws ExpectationFailed {
        ResponseWrapper response =  new ResponseWrapper<>();

        //pos system configurations
        UfsSysConfig ufsSysConfig = sysConfigRepository.findByEntityAndParameter("Pos Configuration", "posPin");
        ArrayList<String> errors = new ArrayList<>();

        for (BigDecimal id : actions.getIds()) {

            //generate random pin
            String randomPin = RandomStringUtils.random(Integer.parseInt(ufsSysConfig.getValue()), false, true);

            log.info("The generated pin is: " + randomPin);
            UfsPosUser ufsPosUser = ufsPosUserRepository.findByPosUserIdAndIntrash(id,AppConstants.NO);
            if(ufsPosUser == null){
                loggerService.logApprove("Failed to approve pos user (id " + id + "). Failed to locate make with specified id",
                        UfsPosUser.class.getSimpleName(), id, AppConstants.STATUS_FAILED);
                errors.add("Pos user with id " + id + " doesn't exist");
                continue;

            } else if (loggerService.isInitiator(UfsPosUser.class.getSimpleName(), id, ufsPosUser.getAction())) {

            errors.add("Sorry maker can't approve their own record (" + ufsPosUser.getUsername() + ")");
            loggerService.logUpdate("Failed to approve Pos User (" + ufsPosUser.getUsername() + ") with Device ("+ufsPosUser.getSerialNumber()+"). Maker can't approve their own record", SharedMethods.getEntityName(UfsPosUser.class), id, AppConstants.STATUS_FAILED);
            continue;

            }else if(ufsPosUser.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_CREATE) &&
                    ufsPosUser.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)){
                ufsPosUser.setActiveStatus(AppConstants.STATUS_INACTIVE);
                ufsPosUser.setPinStatus(AppConstants.PIN_STATUS_INACTIVE);
                ufsPosUser.setPin(encoder.encode(randomPin));
                ufsPosUser.setActionStatus(AppConstants.STATUS_APPROVED);
                ufsPosUserRepository.save(ufsPosUser);

                loggerService.logApprove("Done approving Pos User (" + ufsPosUser.getUsername() + ")  with Device ("+ufsPosUser.getSerialNumber()+").",
                        SharedMethods.getEntityName(UfsPosUser.class), ufsPosUser.getPosUserId(),
                        AppConstants.STATUS_COMPLETED, actions.getNotes());

                String message = "POS LOGIN CREDENTIALS.Use the following credentials to access the POS." +
                        "You username is: "+ufsPosUser.getUsername()+" and your pin is: " + randomPin;


                if(Objects.nonNull(ufsPosUser.getContactPersonId())){
                    System.out.println("Contact Person Id>>>>"+ufsPosUser.getContactPersonId());
                    notifyService.sendEmail(ufsPosUser.getContactPerson().getEmail(), "POS LOGIN CREDENTIALS", message);
                    notifyService.sendSms(ufsPosUser.getContactPerson().getPhoneNumber(), message);
                }else if(Objects.nonNull(ufsPosUser.getCustomerOwnersId())){
                    System.out.println("Customer Owner Id>>>>"+ufsPosUser.getCustomerOwnersId());
                    notifyService.sendEmail(ufsPosUser.getCustomerOwners().getDirectorEmailAddress(), "POS LOGIN CREDENTIALS", message);
                    notifyService.sendSms(ufsPosUser.getContactPerson().getPhoneNumber(), message);
                }

            }else if(ufsPosUser.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_RESET_POS_PIN) &&
                ufsPosUser.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)){
                ufsPosUser.setActiveStatus(AppConstants.STATUS_INACTIVE);
                ufsPosUser.setPinStatus(AppConstants.PIN_STATUS_INACTIVE);
                ufsPosUser.setPin(encoder.encode(randomPin));
                ufsPosUser.setPinChangeDate(Calendar.getInstance().getTime());
                ufsPosUser.setActionStatus(AppConstants.STATUS_APPROVED);
                ufsPosUser.setAction(AppConstants.ACTIVITY_RESET_POS_PIN);
                ufsPosUserRepository.save(ufsPosUser);

                loggerService.logApprove("Done approving Pos User (" + ufsPosUser.getUsername() + ") Pin Reset with Device ("+ufsPosUser.getSerialNumber()+").",
                        SharedMethods.getEntityName(UfsPosUser.class), ufsPosUser.getPosUserId(),
                        AppConstants.STATUS_COMPLETED, actions.getNotes());

                String message = "Pin Reset Successfully.Use the following credentials to access the POS." +
                        "You username is: "+ufsPosUser.getUsername()+" and your pin is: " + randomPin;


                if(Objects.nonNull(ufsPosUser.getContactPersonId())){
                    System.out.println("Contact Person Id>>>>"+ufsPosUser.getContactPersonId());
                    notifyService.sendEmail(ufsPosUser.getContactPerson().getEmail(), "Password Reset", message);
                    notifyService.sendSms(ufsPosUser.getContactPerson().getPhoneNumber(), message);
                }else if(Objects.nonNull(ufsPosUser.getCustomerOwnersId())){
                    System.out.println("Customer Owner Id>>>>"+ufsPosUser.getCustomerOwnersId());
                    notifyService.sendEmail(ufsPosUser.getCustomerOwners().getDirectorEmailAddress(), "Password Reset", message);
                    notifyService.sendSms(ufsPosUser.getContactPerson().getPhoneNumber(), message);
                }
            }else if(ufsPosUser.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DELETE) &&
                 ufsPosUser.getActionStatus().equalsIgnoreCase(AppConstants.STATUS_UNAPPROVED)){
                ufsPosUser.setActionStatus(AppConstants.STATUS_APPROVED);
                ufsPosUser.setIntrash(AppConstants.YES);
                ufsPosUserRepository.save(ufsPosUser);
                loggerService.logApprove("Done approving Pos User (" + ufsPosUser.getUsername() + ") deletion with Device ("+ufsPosUser.getSerialNumber()+").",
                        SharedMethods.getEntityName(UfsPosUser.class), ufsPosUser.getPosUserId(),
                        AppConstants.STATUS_COMPLETED, actions.getNotes());
            }else{
                    loggerService.logUpdate("Failed to approve Pos User (" + ufsPosUser.getUsername() + ") with Device ("+ufsPosUser.getSerialNumber()+"). Record doesn't have approve actions",
                            SharedMethods.getEntityName(UfsPosUser.class), id, AppConstants.STATUS_FAILED);
                    errors.add("Record doesn't have approve actions");

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
