package ke.tra.com.tsync.services;

import ke.tra.com.tsync.config.SpringContextBridge;
import ke.tra.com.tsync.entities.*;
import ke.tra.com.tsync.repository.*;
import ke.tra.com.tsync.services.template.UserServiceTempl;
import ke.tra.com.tsync.utils.annotations.AppConstants;
import ke.tra.com.tsync.wrappers.*;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;


@Service
@CommonsLog
public class UserService implements UserServiceTempl {
    private final UfsSysConfigRepository ufsSysConfigRepository;
    private final UfsContactPersonRepository contactPersonRepository;
    private final OtpCategoryRepository otpCategoryRepository;
    private final OtpService otpService;



    public UserService(UfsSysConfigRepository ufsSysConfigRepository, UfsContactPersonRepository contactPersonRepository, OtpCategoryRepository otpCategoryRepository, OtpService otpService) {
        this.ufsSysConfigRepository = ufsSysConfigRepository;
        this.contactPersonRepository = contactPersonRepository;
        this.otpCategoryRepository = otpCategoryRepository;
        this.otpService = otpService;
    }

    @Value("${communicationBaseUrl}")
    private String communicationBaseUrl;
    @Autowired
    PasswordEncoder encoder;



    @Transactional
    public ResponseWrapper changePin(ChangePin wrapper) {
        ResponseWrapper responseWrapper = new ResponseWrapper();

        PosUserWrapper posUserWrapper = new PosUserWrapper();
        posUserWrapper.setTID(wrapper.getTID());
        posUserWrapper.setMID(wrapper.getMID());
        posUserWrapper.setSerialNumber(wrapper.getSerialNumber());
        posUserWrapper.setUsername(wrapper.getUsername());
        posUserWrapper.setCurrentPin(wrapper.getPin());
        posUserWrapper.setPin(wrapper.getNewpin());
        posUserWrapper.setConfirmPin(wrapper.getConfirmPin());

        // create audit log instance to log this activity
        UfsPosAuditLog auditLog = new UfsPosAuditLog();
        auditLog.setOccurenceTime(new Date());
        auditLog.setActivityType(AppConstants.ACTIVITY_PIN_CHANGE);
        UfsPosAuditLogRepository auditLogRepository = SpringContextBridge.services().getPosAuditLogRepo();
        UfsPosUserRepository ufsPosUserRepository = SpringContextBridge.services().getPOSUserRepo();

        ResponseWrapper validate = validatePosRequest(posUserWrapper, false, auditLog);
        Optional<UfsPosUser> ufsPosUser = validate.getPosUser();

        try {
            if(validate.getError()){
                auditLogRepository.save(auditLog);
                return validate;
            }

            if (ufsPosUser.get().getPinStatus().equalsIgnoreCase(AppConstants.PIN_STATUS_INACTIVE) || ufsPosUser.get().getActiveStatus().equalsIgnoreCase(AppConstants.STATUS_INACTIVE)) {
                responseWrapper.setMessage("Account is inactive");
                auditLog.setNotes("Pin status "+AppConstants.PIN_STATUS_INACTIVE + "+ User status "+AppConstants.STATUS_INACTIVE);

                responseWrapper.setCode(AppConstants.POS_ACCOUNT_INACTIVE);

            }

            else if (!encoder.matches(wrapper.getPin(), ufsPosUser.get().getPin())) {
                responseWrapper.setCode(AppConstants.POS_INCORRECT_CREDENTIALS);
                responseWrapper.setMessage("Invalid Old Password");
                auditLog.setNotes("Invalid Old Password");

            } else {


                ufsPosUser.get().setPin(encoder.encode(wrapper.getNewpin()));
                ufsPosUser.get().setPinChangeDate(Calendar.getInstance().getTime());
                ufsPosUserRepository.save(ufsPosUser.get());
                responseWrapper.setCode(AppConstants.POS_APPROVED);
                auditLog.setStatus(AppConstants.STATUS_COMPLETED);
                responseWrapper.setMessage("Pin reset was a success.");
            }
        }catch(Exception ex){
            ex.printStackTrace();
            responseWrapper.setCode(AppConstants.POS_SERVER_ERROR);
            responseWrapper.setMessage("System error occurred");
            auditLog.setNotes("System error occurred");
            auditLog.setDescription("application error occurred while terminal with "+ wrapper.getSerialNumber() +"tried to login for the first time");

        }
        auditLogRepository.save(auditLog);
        return responseWrapper;
    }



    @Override
    @Transactional
    public ResponseWrapper firstTimeLogin(PosUserWrapper wrapper) {
        ResponseWrapper responseWrapper = new ResponseWrapper();

        // create audit log instance to log this activity
        UfsPosAuditLog auditLog = new UfsPosAuditLog();
        ResponseWrapper validate = validatePosRequest(wrapper, true, auditLog);

        auditLog.setOccurenceTime(new Date());
        auditLog.setActivityType(AppConstants.FIRST_TIME_LOGIN);

        UfsPosAuditLogRepository auditLogRepository = SpringContextBridge.services().getPosAuditLogRepo();
        UfsPosUserRepository ufsPosUserRepository = SpringContextBridge.services().getPOSUserRepo();

        setLogIpAddress(auditLog);
        try {
            Optional<UfsPosUser> ufsPosUser = validate.getPosUser();
            log.info(wrapper);
            log.info(ufsPosUser.get().getPin());
//            log.info("00000000000w0000000 "+ wrapper.getPin());
            if(validate.getError()){
                responseWrapper = validate;
            }

            else if (!encoder.matches(wrapper.getPin(), ufsPosUser.get().getPin())) {
                responseWrapper.setCode(AppConstants.POS_INCORRECT_CREDENTIALS);
                responseWrapper.setMessage("Invalid credentials");
                auditLog.setNotes("Wrong Password");

            }else {

                ufsPosUser.get().setPinStatus(AppConstants.PIN_STATUS_ACTIVE);
                ufsPosUser.get().setActiveStatus(AppConstants.STATUS_ACTIVE);
                ufsPosUser.get().setPinLastLogin(Calendar.getInstance().getTime());

                ufsPosUserRepository.save(ufsPosUser.get());
                auditLog.setStatus(AppConstants.STATUS_COMPLETED);
                responseWrapper.setMessage("First time login was successful");
                responseWrapper.setCode(AppConstants.POS_APPROVED);
            }


        }catch (Exception ex){
            ex.printStackTrace();
            responseWrapper.setMessage("System error occurred");
            responseWrapper.setCode(AppConstants.POS_SERVER_ERROR);

            auditLog.setNotes("System error occurred");
            auditLog.setDescription("application error occurred while terminal with "+ wrapper.getSerialNumber() +"tried to login for the first time");

        }
        auditLogRepository.save(auditLog);
        return responseWrapper;
    }


    @Transactional
    public ResponseWrapper enableUser(PosUserWrapper wrapper) { // deactivate user
        ResponseWrapper responseWrapper = new ResponseWrapper();
        UfsPosAuditLog auditLog = new UfsPosAuditLog();
        auditLog.setOccurenceTime(new Date());
        auditLog.setActivityType(AppConstants.ACTIVITY_AUTHENTICATION);
        UfsPosAuditLogRepository auditLogRepository = SpringContextBridge.services().getPosAuditLogRepo();
        UfsPosUserRepository ufsPosUserRepository = SpringContextBridge.services().getPOSUserRepo();
        // validate Device details

        List <String> doesNotExist = new ArrayList<>();
        List <UfsPosUser> enable = new ArrayList<>();

        //split the username string at ,
        try {
            String[] usernames = wrapper.getUsername().split(",");
            Arrays.stream(usernames)
                    .forEach(username -> {
                        ufsPosUserRepository.findByUsernameIgnoreCaseAndIntrashAndSerialNumber(username, "NO", wrapper.getSerialNumber())
                                .ifPresentOrElse(user -> {
                                    user.setActiveStatus(AppConstants.STATUS_ACTIVE);
                                    user.setPinStatus(AppConstants.STATUS_ACTIVE);
                                    enable.add(user);
                                }, () -> {

                                    doesNotExist.add(username);
                                });

                    });
            return getResponseWrapper(responseWrapper, ufsPosUserRepository, doesNotExist, enable);
        }catch(Exception e){
            responseWrapper.setCode(AppConstants.POS_SERVER_ERROR);
            responseWrapper.setMessage("System error occurred");
//            auditLog.setActivityType();
            return responseWrapper;

        }

    }

    private ResponseWrapper getResponseWrapper(ResponseWrapper responseWrapper, UfsPosUserRepository ufsPosUserRepository, List<String> doesNotExist, List<UfsPosUser> disable) {
        try{
            responseWrapper.setMessage(doesNotExist.isEmpty()?"Action was successful":"Some users were not found: "+doesNotExist.stream()
                    .collect(Collectors.joining(","))
            );
            responseWrapper.setCode(AppConstants.POS_APPROVED);
            if(!disable.isEmpty()) ufsPosUserRepository.saveAll(disable);
            if(!doesNotExist.isEmpty()) responseWrapper.setCode(AppConstants.PARTIAL_SUCCESS);
        }catch(Exception ex){
            responseWrapper.setMessage("An error occurred while persisting disabled uses");
            responseWrapper.setCode(AppConstants.POS_SERVER_ERROR);
            ex.printStackTrace();
        }

        return responseWrapper;
    }


    @Override
    @Transactional
    public ResponseWrapper disableUsers(PosUserWrapper wrapper) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        UfsPosAuditLog auditLog = new UfsPosAuditLog();
        auditLog.setOccurenceTime(new Date());
        auditLog.setActivityType(AppConstants.ACTIVITY_AUTHENTICATION);
        UfsPosAuditLogRepository auditLogRepository = SpringContextBridge.services().getPosAuditLogRepo();
        UfsPosUserRepository ufsPosUserRepository = SpringContextBridge.services().getPOSUserRepo();
        // validate Device details

        List <String> doesNotExist = new ArrayList<>();
        List <UfsPosUser> disable = new ArrayList<>();

        //split the username string at ,
        String[] usernames = wrapper.getUsername().split(",");
        Arrays.stream(usernames)
                .forEach(username->{
                    ufsPosUserRepository.findByUsernameIgnoreCaseAndIntrashAndSerialNumber(username, "NO", wrapper.getSerialNumber())
                            .ifPresentOrElse(user->{
                                user.setActiveStatus(AppConstants.STATUS_DEACTIVATED);
                                user.setPinStatus(AppConstants.PIN_STATUS_ACTIVE);
                                disable.add(user);
                            },()->{

                                doesNotExist.add(username);
                            });

                });
        return getResponseWrapper(responseWrapper, ufsPosUserRepository, doesNotExist, disable);
    }


    @Async
    public ResponseWrapper sendMessage(EmailAndMessageWrapper request) {
        UfsSysConfig enableNotification = ufsSysConfigRepository.findByEntityAndParameter("Pos Configuration", "Enable Notification");
        String notification = enableNotification==null ?"No":enableNotification.getValue();
        ResponseWrapper response = new ResponseWrapper();

        switch(notification.toUpperCase()){
            case "NO":
                try{
                    String url = communicationBaseUrl + "/send-email";
                    RestTemplate restTemplate = new RestTemplate();
                    response = restTemplate.postForObject(url, request, ResponseWrapper.class);
                    log.info(response.getMessage() + " "+response.getCode());
                    log.info(url);
                }catch (RestClientException ex){

                    log.info(ex.getMessage());
                    ex.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case "YES":
                log.info("Notification is disabled...");
                break;

        }

        return response;
    }


    private void setLogIpAddress(UfsPosAuditLog auditLog){
        try {
            auditLog.setIpAddress(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            auditLog.setNotes("Unknown IP address");
            e.printStackTrace();
        }
    }

    public ResponseWrapper loadUserNames(PosUserWrapper wrapper){
        ResponseWrapper responseWrapper = new ResponseWrapper<>();
        try{
            // list of all users from pos users whose names begin with the first two characters from Pos

            log.info("parmaaa,,,<<<<"+ wrapper.getUsername()+"%");
            // by default we want users created on this terminal only unless otherwise
            String tid = wrapper.getTID()==null?"-1":wrapper.getTID();
            String prefix = wrapper.getUsername()==null?"-1%":wrapper.getUsername().strip();

            List<UfsPosUser> matchingUsers = SpringContextBridge.services().getPOSUserRepo().findByUsernameStartsWithIgnoreCaseAndIntrash(
                    wrapper.getUsername(), "NO"
            );
            //List<UfsPosUser> matchingUsers = SpringContextBridge.services().getPOSUserRepo().findByUsernameStartsWithIgnoreCaseAndTidAndIntrash(prefix,tid,"NO");

            String roleIProvided = wrapper.getWorkgroup()==null?"":wrapper.getWorkgroup(); // pos role

            switch(roleIProvided.strip().toUpperCase()){
                // FIRST CATEGORY WHICH BELONG TO THE BANK
                case "ADMIN":
                case "ADMINISTRATOR":
                case "FIELD SUPERVISOR":
                    // if BANK Agent we want to give access to the entire bank pos users
                    matchingUsers = SpringContextBridge.services().getPOSUserRepo().findByUsernameStartsWithIgnoreCaseAndIntrash(
                            wrapper.getUsername(), "NO"
                    );
                    // do nothing hence allow a the bank admin to log to any device of the bank
                    break;
                case "SUPERVISOR":
                case "MERCHANT OWNER":
                case "MERCHANT":

                    String prefixLike = prefix+"%";
                    // if outlet/Agent owner/Supervisor we want to load users of the outlet
                    matchingUsers = SpringContextBridge.services().getPOSUserRepo().findByUsernamePrefix(prefixLike.toUpperCase(),wrapper.getSerialNumber());
                    break;
                default:
                    // teller/operators/cashier/customer load  by tid(THIRD CATEGORY)
                    break;

            }

            if(matchingUsers.isEmpty()){
                responseWrapper.setCode(AppConstants.POS_NO_USER_WITH_PREFIX);
                responseWrapper.setMessage("No users with "+wrapper.getUsername()+" prefix");
            }
            else{
                String users = matchingUsers
                        .stream()
                        .map(user1-> user1.getUsername())
                        .collect(joining("|"));

                responseWrapper.setCode(AppConstants.POS_APPROVED);
                responseWrapper.setMessage(users);
            }

        }
        catch (Exception ex){
            responseWrapper.setMessage("SYSTEM ERROR");
            responseWrapper.setCode(AppConstants.POS_SERVER_ERROR);
            log.error("An error occurred while loading user {} "+ ex.getMessage());
        }
        return responseWrapper;
    }


    @Override
    public ResponseWrapper login(PosUserWrapper wrapper) {
        ResponseWrapper responseWrapper = new ResponseWrapper();

        UfsPosAuditLog auditLog = new UfsPosAuditLog();
        auditLog.setOccurenceTime(new Date());
        auditLog.setActivityType(AppConstants.ACTIVITY_AUTHENTICATION);

        UfsPosAuditLogRepository auditLogRepository = SpringContextBridge.services().getPosAuditLogRepo();
        UfsPosUserRepository ufsPosUserRepository = SpringContextBridge.services().getPOSUserRepo();
        EmailAndMessageWrapper request = new EmailAndMessageWrapper();

        ResponseWrapper validate = validatePosRequest(wrapper, true, auditLog);

        if(validate.getError()){
            auditLogRepository.save(auditLog);
            return validate;
        }
        Optional<UfsPosUser> ufsPosUser = validate.getPosUser();

        if (ufsPosUser.get().getActiveStatus().equalsIgnoreCase(AppConstants.PASS_LOCKED_STATUS)) {
            responseWrapper.setCode(AppConstants.POS_LOCKED_ACCOUNT);
            responseWrapper.setMessage("Account is Locked");
            auditLog.setNotes("Account Locked for "+wrapper.getUsername());

        }
        else if (ufsPosUser.get().getActiveStatus().equalsIgnoreCase(AppConstants.STATUS_DEACTIVATED)) {
            responseWrapper.setCode(AppConstants.POS_USER_DEACTIVATED);
            responseWrapper.setMessage("Account is deactivated");
            auditLog.setNotes("Account deactivated "+wrapper.getUsername());

        }
        else if(ufsPosUser.get().getPinStatus().equalsIgnoreCase(AppConstants.STATUS_INACTIVE) || ufsPosUser.get().getActiveStatus().equals(AppConstants.STATUS_INACTIVE)){

            responseWrapper.setMessage("Login for the first time to activate account");

            responseWrapper.setCode(AppConstants.POS_FIRSTTIME_LOGIN_IN); // login for the first time code
        }
        else if (!encoder.matches(wrapper.getPin(),ufsPosUser.get().getPin())) {
            UfsSysConfig posPinAttempts = ufsSysConfigRepository.findByEntityAndParameter("Pos Configuration","posPinAttempts");
            Integer allowedAttempts = Integer.valueOf(posPinAttempts == null ? "30": posPinAttempts.getValue());
            BigInteger pinLoginAttempts = ufsPosUser.get().getPinLoginAttemtps() == null?new BigInteger("0"):ufsPosUser.get().getPinLoginAttemtps();
            if (pinLoginAttempts.intValue() == allowedAttempts.intValue()) {
                ufsPosUser.get().setActiveStatus(AppConstants.PASS_LOCKED_STATUS);
            }else{
                BigInteger attempts = ufsPosUser.get().getPinLoginAttemtps();
                ufsPosUser.get().setPinLoginAttemtps((attempts != null) ? attempts.add(new BigInteger("1")) : new BigInteger("1"));
            }


            ufsPosUserRepository.save(ufsPosUser.get());
            auditLog.setNotes("Wrong Password for " + wrapper.getUsername() );

            responseWrapper.setMessage("Invalid credentials");

            responseWrapper.setCode(AppConstants.POS_INCORRECT_CREDENTIALS);


        } else {
            responseWrapper.setMessage(ufsPosUser.get().getPosRole());
            responseWrapper.setCode(AppConstants.POS_APPROVED);
//                        Optional<UfsOtpCategory> otpCategory = otpCategoryRepository.findDistinctByCategoryAndIntrash("Authentication", "NO");
//                        if(otpCategory.isPresent()){
//                            String otp = otpService.generateOTP(ufsPosUser, otpCategory.get());
//                            request.setMessage("Hello, "+ ufsPosUser.getUsername() + ", here is your verification code to complete the login process "+otp);
//                            request.setMessageType("SMS");
//                            request.setSendTo("254723135671");
//                            request.setSubject("Otp");
//                            sendMessage(request);
//
//                            // validate Device details
//                        }
            auditLog.setNotes("Login Successfully by "+wrapper.getUsername());

            auditLog.setUserId(ufsPosUser.get().getPosUserId().longValue());
            //reset pin attempts after successfully login
            ufsPosUser.get().setPinLoginAttemtps(new BigInteger("0"));
            ufsPosUser.get().setPinLastLogin(new Date());

            ufsPosUserRepository.save(ufsPosUser.get());
            auditLog.setStatus(AppConstants.STATUS_COMPLETED);

        }

        auditLogRepository.save(auditLog);

        return responseWrapper;
    }

    @Override
    @Transactional
    public ResponseWrapper createPosUser(PosUserWrapper wrapper){
        ResponseWrapper responseWrapper =  new ResponseWrapper();
        UfsPosAuditLog auditLog = new UfsPosAuditLog();
        auditLog.setOccurenceTime(new Date());
        auditLog.setActivityType(AppConstants.ACTIVITY_CREATE);
        auditLog.setStatus(AppConstants.STATUS_FAILED);
        auditLog.setEntityName("UfsPosUser");
        auditLog.setClientId(AppConstants.CLIENT_ID);
        log.info("+++++++++++++++++++++---------------- "+ wrapper);

        UfsPosAuditLogRepository auditLogRepository = SpringContextBridge.services().getPosAuditLogRepo();
        UfsPosUserRepository ufsPosUserRepository = SpringContextBridge.services().getPOSUserRepo();

        try {
            auditLog.setIpAddress(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            auditLog.setNotes("Unknown IP address");
            e.printStackTrace();
        }

        try {
            // validate device details
            validateDeviceDetails(wrapper, responseWrapper, auditLog);
            //  error will be set true if validateDeviceDetails fail
            // Validate for missing user details
            validateUserDetails(wrapper, responseWrapper, auditLog);

            if(!responseWrapper.getError()){
                try {

                    UserExistWrapper userExist = checkUserExist(wrapper);

                    if (userExist.getUserExistByUsernameAndSerialNo()){
                        responseWrapper.setCode(AppConstants.POS_USER_ALREADY_ON_TERMINAL);
                        responseWrapper.setMessage("The username " +wrapper.getUsername() +" is already attached to this device");
                        auditLog.setNotes("Pos user already exist for " + wrapper.getUsername()+" and SN "+wrapper.getSerialNumber());
                        auditLog.setDescription("Pos user already exist" );
                        log.info("user exist!!!!!!!");

                        return responseWrapper;

                    }

                    TmsDeviceRepository tmsDeviceRepository = SpringContextBridge.services().getTmsDeviceRepo();

                    TmsDevice tmsDevice = tmsDeviceRepository.findBySerialNoAndIntrash(wrapper.getSerialNumber(), "NO");

                    if(Objects.nonNull(tmsDevice)){
                        log.info("HERE at tms device");
                        try{
                            //if the ufsUserId exists not then create
                            UfsPosUser user = new UfsPosUser();
                            String workgroupStr = wrapper.getWorkgroup() == null?"":wrapper.getWorkgroup().strip(); // role

                            if (workgroupStr != "") {
                                log.info("HERE at workgroup");

                                //UfsSysConfig ufsSysConfig = ufsSysConfigRepository.findByEntityAndParameter("Pos Configuration", "posPin");

                                //String randomPin = RandomStringUtils.random(Integer.parseInt(ufsSysConfig.getValue()), false, true);
                                user.setActionStatus(AppConstants.STATUS_APPROVED);
                                user.setPin(encoder.encode(wrapper.getPin()));
                                user.setActiveStatus(AppConstants.STATUS_INACTIVE);
                                user.setPinStatus(AppConstants.PIN_STATUS_INACTIVE);

                                String username = wrapper.getUsername();
                                user.setUsername(username);
                                user.setPosRole(workgroupStr);
                                user.setFirstName(wrapper.getFirstName());
                                user.setOtherName(wrapper.getOtherName());
                                user.setPhoneNumber(wrapper.getPhoneNumber());
                                user.setIdNumber(wrapper.getIdNumber());
                                user.setDeviceIds(tmsDevice.getDeviceId());
                                user.setSerialNumber(wrapper.getSerialNumber());


                                auditLog.setStatus(AppConstants.STATUS_COMPLETED);
                                auditLog.setEntityId(String.valueOf(user.getPosUserId()));
                                auditLog.setNotes("Pos user created successfully");
                                auditLog.setDescription("Pos user " + wrapper.getPhoneNumber() + " created successfully");
                                responseWrapper.setMessage("Pos user created successfully");
                                responseWrapper.setCode(AppConstants.POS_APPROVED);
                                ufsPosUserRepository.save(user);
                            }else{
                                log.info("HERE at workgroup was not found");
                                auditLog.setEntityId(String.valueOf(user.getPosUserId()));
                                auditLog.setNotes("Provide role");

                                responseWrapper.setMessage("Provide user role");
                                responseWrapper.setCode(AppConstants.POS_ROLE_MISSING);
                            }
                        }catch (NullPointerException ex){
                            ex.printStackTrace();
                            log.error(ex.getMessage());
                            responseWrapper.setMessage("Device with Serial No "+tmsDevice.getSerialNo() + " does not have an outlet attached to it");
                            responseWrapper.setCode(AppConstants.POS_SERVER_ERROR);

                            auditLog.setNotes("Device with Serial No "+tmsDevice.getSerialNo() + " does not have an outlet attached to it");

                        }

                    } else {
                        // dont create user since this terminal does not belong
                        responseWrapper.setMessage("This Terminal device is not authorised to transact, its either deleted or not approved yet.");
                        auditLog.setNotes("Unauthorised to transact");
                        auditLog.setDescription("This Terminal device is not authorised to transact, its either deleted or not approved yet. Device SNO " + wrapper.getSerialNumber());
                        responseWrapper.setCode(AppConstants.POS_TERMINAL_NOT_FOUND);
                        responseWrapper.setError(true);
                    }

                } catch (IncorrectResultSizeDataAccessException e) {
                    e.printStackTrace();
                    responseWrapper.setCode(AppConstants.POS_SERVER_ERROR);
                    auditLog.setDescription("More than two devices have the same serial number ");
                    auditLog.setNotes("Duplicate records");
                    responseWrapper.setMessage("More than two devices have the same serial number :"+wrapper.getSerialNumber());
                }
            }


        } catch (DataIntegrityViolationException ex){
            ex.printStackTrace();
            log.error(ex.getMessage());
            responseWrapper.setCode(AppConstants.POS_SERVER_ERROR);
            auditLog.setDescription("System error when trying to create User from terminal with serial No "+wrapper.getSerialNumber());
            responseWrapper.setMessage("Request could not be processed due to" + ex.getMessage());
        }

        auditLogRepository.save(auditLog);
        return responseWrapper;
    }

    @Override
    @Transactional
    public ResponseWrapper resetPassword( PosUserWrapper wrapper){
        ResponseWrapper responseWrapper =  new ResponseWrapper();

        UfsPosAuditLog auditLog = new UfsPosAuditLog();
        auditLog.setOccurenceTime(new Date());
        auditLog.setActivityType(AppConstants.ACTIVITY_AUTHENTICATION);

        UfsPosAuditLogRepository auditLogRepository = SpringContextBridge.services().getPosAuditLogRepo();
        UfsPosUserRepository ufsPosUserRepository = SpringContextBridge.services().getPOSUserRepo();


        // validate Device details
        ResponseWrapper validate = validatePosRequest(wrapper, true, auditLog);

        if(validate.getError()){
            responseWrapper = validate;

        } else {
            UfsSysConfig ufsSysConfig = ufsSysConfigRepository.findByEntityAndParameter("Pos Configuration","posPin");
            Optional<UfsPosUser> user = validate.getPosUser();

            String randomPin;
            randomPin = AppConstants.DEFAULT_RESET_PWD;
            //randomPin = RandomStringUtils.random(Integer.parseInt(ufsSysConfig.getValue()),false,true);
            user.get().setPin(encoder.encode(randomPin));
            user.get().setActiveStatus(AppConstants.STATUS_INACTIVE);
            user.get().setPinStatus(AppConstants.PIN_STATUS_INACTIVE);
            responseWrapper.setMessage(randomPin);

            auditLog.setNotes("Reset password successful for "+ wrapper.getUsername());
            auditLog.setEntityId(String.valueOf(user.get().getPosUserId()));
            auditLog.setStatus(AppConstants.STATUS_COMPLETED);

            responseWrapper.setCode(AppConstants.POS_APPROVED);
            auditLog.setNotes("Reset password for "+ wrapper.getUsername() +" was success");
            auditLog.setDescription("Reset password for "+ wrapper.getUsername() +" was success");
            ufsPosUserRepository.save(user.get());

        }
        auditLogRepository.save(auditLog);
        return responseWrapper;
    }

    private UserExistWrapper checkUserExist(PosUserWrapper wrapper) {
        UserExistWrapper userExistWrapper = new UserExistWrapper();
        // lets check if a user exist by the unique username provide during creation
        UfsPosAuditLogRepository auditLogRepository = SpringContextBridge.services().getPosAuditLogRepo();
        UfsPosUserRepository ufsPosUserRepository = SpringContextBridge.services().getPOSUserRepo();

        try {
            ufsPosUserRepository.findByUsernameIgnoreCaseAndIntrashAndSerialNumber(wrapper.getUsername(), "NO", wrapper.getSerialNumber())
                    .ifPresent(posUser -> {
                        userExistWrapper.setUserExistByUsernameAndSerialNo(true);
                        userExistWrapper.setUserId(posUser.getPosUserId().longValue());
                    });
        }catch(IncorrectResultSizeDataAccessException ex){
            userExistWrapper.setUserExistByUsernameAndSerialNo(true);
            log.info("Error "+ ex.getMessage());
        }
        return userExistWrapper;
    }

    private ResponseWrapper validateUserDetails(PosUserWrapper wrapper, ResponseWrapper responseWrapper, UfsPosAuditLog auditLog) {
        // validation for first name

        if (wrapper.getFirstName() == null){
            responseWrapper.setCode(AppConstants.POS_FIRSTNAME_MISSING);
            responseWrapper.setMessage("first name missing");
            auditLog.setNotes("first name missing");
            responseWrapper.setError(true);

        }
        else if (wrapper.getOtherName() == null){
            responseWrapper.setCode(AppConstants.POS_OTHERNAME_MISSING);
            auditLog.setNotes("other name(s) missing");
            responseWrapper.setMessage("other name(s) missing");
            responseWrapper.setError(true);

        }
        else if  (wrapper.getConfirmPin() == null){
            responseWrapper.setCode(AppConstants.POS_CONFIRM_PIN_MISSING);
            responseWrapper.setMessage("pin/password missing");
            responseWrapper.setError(true);

        }
        else if  (wrapper.getIdNumber() == null){
            responseWrapper.setCode(AppConstants.ID_NO_MISSING);
            responseWrapper.setMessage("Id number missing");
            responseWrapper.setError(true);

        }
        return responseWrapper;

    }


    private ResponseWrapper validateDeviceDetails(PosUserWrapper wrapper, ResponseWrapper responseWrapper, UfsPosAuditLog auditLog){
        String posSerialNumber = wrapper.getSerialNumber();
        String posTID = wrapper.getTID();
        String posMID = wrapper.getMID();

        // validation for serial number
        if (posSerialNumber == null || posSerialNumber.isBlank()){
            responseWrapper.setCode(AppConstants.POS_SERIAL_NO_MISSING);
            responseWrapper.setMessage("Serial Number Missing");
            auditLog.setNotes("Serial Number Missing");
            auditLog.setDescription("POS making the request did not send its serial Number");
            responseWrapper.setError(true);

        }
        // validation for MID
        else if (posMID == null || posMID.isBlank()){
            responseWrapper.setCode(AppConstants.MID_MISSING);
            responseWrapper.setMessage("Merchant ID Missing");
            auditLog.setDescription("Request from device with serial number " + wrapper.getSerialNumber() + " did not provide MID");
            auditLog.setNotes("Merchant ID Missing");
            responseWrapper.setError(true);

        }
        // validation for TID
        else if (posTID == null || posTID.isBlank()){
            responseWrapper.setCode(AppConstants.MID_MISSING);
            auditLog.setNotes("Terminal ID Missing");
            auditLog.setDescription("Request from device with serial number " + wrapper.getSerialNumber() + " did not provide TID");
            responseWrapper.setMessage("Terminal ID Missing");
            responseWrapper.setError(true);

        }
        return responseWrapper;
    }

    private ResponseWrapper validatePosRequest(PosUserWrapper wrapper, boolean b, UfsPosAuditLog auditLog) {
        // common validator method for members that need its

        log.info("******************---0"+ wrapper.getUsername());
        ResponseWrapper responseWrapper = new ResponseWrapper();

        // log Ip
        try {
            auditLog.setIpAddress(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            auditLog.setIpAddress("Unknown IP address");
            auditLog.setNotes("Unknown IP address");

        }
        validateDeviceDetails(wrapper, responseWrapper, auditLog);
        //  error will be set true if validateDeviceDetails fail

        if(responseWrapper.getError()){
            return responseWrapper;
        }
        // validation for login
        if (b) {
            if (wrapper.getUsername() == null) {
                responseWrapper.setError(true);
                responseWrapper.setMessage("username cannot be null.");
                auditLog.setDescription("Username is null");
                responseWrapper.setCode(AppConstants.POS_USERNAME_MISSING);
                return responseWrapper;
            }
        }
        auditLog.setOccurenceTime(new Date());
        auditLog.setStatus(AppConstants.STATUS_FAILED);
        auditLog.setEntityName("UfsPosUser");
        auditLog.setClientId(AppConstants.CLIENT_ID);

        UfsPosUserRepository ufsPosUserRepository = SpringContextBridge.services().getPOSUserRepo();
        TmsDeviceRepository tmsRepo = SpringContextBridge.services().getTmsDeviceRepo();
        log.info(">>>>>>>>*** "+ wrapper.getUsername()+ "NO"+wrapper.getSerialNumber() );


        Optional<UfsPosUser> ufsPosUser = ufsPosUserRepository.findByUsernameIgnoreCaseAndIntrash(wrapper.getUsername(), "NO");
        ufsPosUser.ifPresent(posUser -> {
            // common to all methods so lets set them at a common place
            auditLog.setEntityId(String.valueOf(ufsPosUser.get().getPosUserId()));
            auditLog.setUserId(ufsPosUser.get().getPosUserId().longValue());
            responseWrapper.setPosUser(ufsPosUser);

        });


        if (ufsPosUser.isEmpty()) {
            // common to all methods so lets set them at a common place
            responseWrapper.setMessage("User with username " + wrapper.getUsername() + "For this device not found");
            auditLog.setNotes("User with username Not found");
            auditLog.setDescription("User with username " + wrapper.getUsername() + " Not found for device with serial number "+wrapper.getSerialNumber());
            responseWrapper.setCode(AppConstants.POS_USER_NOT_FOUND);
            responseWrapper.setError(true);
            return responseWrapper;
        }


        String posRole = ufsPosUser.get().getPosRole();
        log.info(wrapper.getSerialNumber());

        boolean admin = false;

        switch (posRole.toUpperCase()){

            case "ADMIN":
            case "ADMINISTRATOR":
            case "ACTIVATION SUPPORT":
            case "SUPPORT":
                admin = true;

                break;
            default:
                log.info("WE ARE c");
                String serialNo = ufsPosUser.get().getSerialNumber()==null?"-1":ufsPosUser.get().getSerialNumber();
                String username = ufsPosUser.get().getUsername();
                // validate serial number if the user is not from bank ie not in the list of admins group

                log.info(serialNo.equals(wrapper.getSerialNumber()) && username.toUpperCase().equals(wrapper.getUsername().toUpperCase()));
                if(!(serialNo.equals(wrapper.getSerialNumber()) &&
                        username.toUpperCase().equals(wrapper.getUsername().toUpperCase()))){
                    log.info("user exist....");

                    responseWrapper.setMessage("User with username " + wrapper.getUsername() + " does not belong to this terminal");
                    auditLog.setNotes("User with username " + wrapper.getUsername() + " Not found");
                    responseWrapper.setCode(AppConstants.POS_USER_NOT_FOUND);

                    responseWrapper.setError(true);
                    return responseWrapper;

                }
        }


        //Steps:
        // check if a user exist with the username provided
        // check if a device exist with the supplied serial number
        // If it does do a lookout for the outlet the device belongs to
        // if an out let exist then check if the user belongs to that outlet



        TmsDevice tmsDevice;
        try{
            tmsDevice = tmsRepo.findBySerialNoAndIntrash(wrapper.getSerialNumber(), "NO");

        }catch (NoSuchElementException e){
            responseWrapper.setError(true);
            responseWrapper.setCode(AppConstants.POS_TERMINAL_NOT_FOUND);
            auditLog.setNotes("Terminal device  with"+wrapper.getSerialNumber() + "Not found");
            responseWrapper.setMessage("Terminal device  with"+wrapper.getSerialNumber() + "Not found");
            return responseWrapper;
        }


        if(!admin) {
            if (Objects.nonNull(tmsDevice)) {
                try {
                    if (tmsDevice.getOutletId() != null && ufsPosUser.get().getDeviceId() != null) {
                        log.info("****1");
                        //validate if user belongs to this outlet which the pos terminal belongs
                        if (ufsPosUser.get().getDeviceId().getOutletId() == null) {
                            responseWrapper.setMessage(wrapper.getSerialNumber() + " not attached to any outlet");
                            responseWrapper.setCode(AppConstants.POS_USER_NOT_ATTACHED_TO_OUTLET);
                            auditLog.setNotes(wrapper.getSerialNumber() + " not attached to any outlet");
                            auditLog.setDescription(wrapper.getUsername() + " not authorised to access terminal with serial number:" + wrapper.getSerialNumber() + " Device not attached to any outlet");
                            responseWrapper.setError(true);
                            return responseWrapper;
                        } else if (ufsPosUser.get().getDeviceId().getOutletId() == null) {
                            log.info("****3 ");
                            responseWrapper.setMessage("terminal device not attached to an outlet");
                            responseWrapper.setError(true);

                            responseWrapper.setCode(AppConstants.POS_TERMINAL_NOT_ATTACHED_TO_OUTLET);
                            auditLog.setNotes("Orphan terminal ");
                            auditLog.setDescription("Terminal device not attached to an outlet");
                            return responseWrapper;

                        } else if (!ufsPosUser.get().getDeviceId().getOutletId().getId().equals(tmsDevice.getOutletId().getId())) {
                            log.info("**** 4 ");
                            responseWrapper.setMessage(wrapper.getUsername() + " does not belong to " + tmsDevice.getOutletId().getOutletName() + " outlet");
                            responseWrapper.setCode(AppConstants.POS_USER_TERMINAL_NOT_SAME_OUTLET);
                            auditLog.setNotes("access by " + wrapper.getUsername() + " denied");
                            auditLog.setDescription(wrapper.getUsername() + " does not belong to " + tmsDevice.getOutletId().getOutletName() + " outlet");
                            responseWrapper.setError(true);
                            return responseWrapper;
                        }
                    }
                    log.info("**** 5");

                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                    responseWrapper.setError(true);
                    responseWrapper.setCode(AppConstants.POS_SERVER_ERROR);
                    auditLog.setNotes("System error");
                    responseWrapper.setMessage("System error");
                    return responseWrapper;
                }

            } else {

                responseWrapper.setMessage("This Terminal device is not authorised to transact, its either deleted or does not exist.");
                // common to all methods so lets set them at a common place
                auditLog.setNotes("Unauthorised to transact");
                auditLog.setDescription("This Terminal device is not authorised to transact, its either deleted or not approved yet. Device SNO " + wrapper.getSerialNumber());
                responseWrapper.setCode(AppConstants.POS_TERMINAL_NOT_FOUND);
                responseWrapper.setError(true);
                return responseWrapper;

            }
        }
        return responseWrapper;
    }

    @Override
    @Transactional
    public ResponseWrapper deletePosUser(PosUserWrapper wrapper){
        UfsPosAuditLog auditLog = new UfsPosAuditLog();
        ResponseWrapper validate = validatePosRequest(wrapper, true, auditLog);
        ResponseWrapper responseWrapper = new ResponseWrapper();

        UfsPosAuditLogRepository auditLogRepository = SpringContextBridge.services().getPosAuditLogRepo();


        auditLog.setOccurenceTime(new Date());
        auditLog.setActivityType("Deletion");
        setLogIpAddress(auditLog);

        try{
            Optional<UfsPosUser> posUser = validate.getPosUser();
            UfsCustomerOutlet posCustomerOutlet = posUser.get().getDeviceId().getOutletId();
            if(validate.getError()){
                responseWrapper = validate;
            }

            else if(posCustomerOutlet != null){

                Date terminationDate = new Date();
                UfsCustomer ufsCustomer = posCustomerOutlet.getCustomerId();
                ufsCustomer.setTerminationReason(wrapper.getTerminationReason());
                ufsCustomer.setTerminationDate(terminationDate);

            }

            auditLog.setStatus(AppConstants.STATUS_COMPLETED);
            responseWrapper.setCode(AppConstants.POS_APPROVED);
            posUser.get().setIntrash("YES");
            responseWrapper.setMessage("User deleted successfully");
            auditLog.setNotes(wrapper.getUsername() +" deleted successfully");

        }catch (Exception ex){
            log.error(ex);
            auditLog.setNotes("System Error, User could not be deleted");
            responseWrapper.setMessage("System Error, User could not be deleted");
            responseWrapper.setCode(AppConstants.POS_SERVER_ERROR);
        }

        auditLogRepository.save(auditLog);
        return responseWrapper;
    }

    public ResponseWrapper logout(PosUserWrapper wrapper){
        UfsPosAuditLog auditLog = new UfsPosAuditLog();
        ResponseWrapper validate = validatePosRequest(wrapper, true, auditLog);
        ResponseWrapper responseWrapper = new ResponseWrapper();
        UfsPosAuditLogRepository auditLogRepository = SpringContextBridge.services().getPosAuditLogRepo();

        log.info("^^^^^^^^^^^^ "+ validate);
        setLogIpAddress(auditLog); // log ip address

        auditLog.setOccurenceTime(new Date());
        auditLog.setActivityType("Logout");

        if (validate.getError()){
            return validate;
        }else{
            auditLog.setStatus(AppConstants.STATUS_COMPLETED);
            responseWrapper.setCode(AppConstants.POS_APPROVED);
            responseWrapper.setMessage("log out was successful");

            auditLog.setNotes(wrapper.getUsername() +" log out was successful");
        }

        auditLogRepository.save(auditLog);
        return responseWrapper;
    }

    private String userDetails(UfsPosUser user){
        String singleRecord = "" + user.getUsername() + ","+ user.getPosRole();
        return singleRecord;
    }

    @Override
    public ResponseWrapper terminalWasReset(PosUserWrapper wrapper) {
        ResponseWrapper responseWrapper = new ResponseWrapper();

        UfsPosUserRepository ufsPosUserRepository = SpringContextBridge.services().getPOSUserRepo();

        String users = ufsPosUserRepository.findAll()
                .stream()
                .map(user->userDetails(user))
                .collect(Collectors.joining("|"));
        responseWrapper.setMessage(users);

        responseWrapper.setCode(AppConstants.POS_APPROVED);

        return responseWrapper;
    }
}
