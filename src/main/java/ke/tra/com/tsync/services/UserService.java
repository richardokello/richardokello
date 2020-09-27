package ke.tra.com.tsync.services;

import ke.tra.com.tsync.config.SpringContextBridge;
import ke.tra.com.tsync.entities.*;
import ke.tra.com.tsync.repository.*;
import ke.tra.com.tsync.services.template.UserServiceTempl;
import ke.tra.com.tsync.utils.annotations.AppConstants;
import ke.tra.com.tsync.wrappers.*;

import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.RandomStringUtils;
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

    @Autowired
    RestTemplate restTemplate;

    @Override
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
                responseWrapper.setCode(31);

            }

            else if (!encoder.matches(wrapper.getPin(), ufsPosUser.get().getPin())) {
                responseWrapper.setCode(01);
                responseWrapper.setMessage("Invalid Old Password");
                auditLog.setNotes("Invalid Old Password");

            } else {


                ufsPosUser.get().setPin(encoder.encode(wrapper.getNewpin()));
                ufsPosUser.get().setPinChangeDate(Calendar.getInstance().getTime());
                ufsPosUserRepository.save(ufsPosUser.get());
                responseWrapper.setCode(200);
                auditLog.setStatus(AppConstants.STATUS_COMPLETED);
                responseWrapper.setMessage("Pin reset was a success.");
            }
        }catch(Exception ex){
            ex.printStackTrace();
            responseWrapper.setCode(06);
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
                responseWrapper.setCode(01);
                responseWrapper.setMessage("Wrong Password");
                auditLog.setNotes("Wrong Password");

            }else {

                ufsPosUser.get().setPinStatus(AppConstants.PIN_STATUS_ACTIVE);
                ufsPosUser.get().setActiveStatus(AppConstants.STATUS_ACTIVE);
                ufsPosUser.get().setPinLastLogin(Calendar.getInstance().getTime());

                ufsPosUserRepository.save(ufsPosUser.get());
                auditLog.setStatus(AppConstants.STATUS_COMPLETED);
                responseWrapper.setMessage("First time login was successful");
                responseWrapper.setCode(200);
            }


        }catch (Exception ex){
            ex.printStackTrace();
            responseWrapper.setMessage("System error occurred");
            responseWrapper.setCode(06);

            auditLog.setNotes("System error occurred");
            auditLog.setDescription("application error occurred while terminal with "+ wrapper.getSerialNumber() +"tried to login for the first time");

        }
        auditLogRepository.save(auditLog);
        return responseWrapper;
    }

    @Override
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
                        ufsPosUserRepository.findByUsernameIgnoreCaseAndIntrash(username, "NO")
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
            responseWrapper.setCode(06);
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
            responseWrapper.setCode(200);
            if(!disable.isEmpty()) ufsPosUserRepository.saveAll(disable);
            if(!doesNotExist.isEmpty()) responseWrapper.setCode(32);
        }catch(Exception ex){
            responseWrapper.setMessage("An error occurred while persisting disabled uses");
            responseWrapper.setCode(06);
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
                    ufsPosUserRepository.findByUsernameIgnoreCaseAndIntrash(username, "NO")
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

    @Override
    @Transactional
    @Async
    public ResponseWrapper sendMessage(EmailAndMessageWrapper request) {
        UfsSysConfig enableNotification = ufsSysConfigRepository.findByEntityAndParameter("Pos Configuration", "Enable Notification");
        String notification = enableNotification==null ?"No":enableNotification.getValue();
        ResponseWrapper response = new ResponseWrapper();
        log.info(">>>>>>>>>>>>>>>>>>ex nn>>>>+++++ "+notification.toUpperCase());
        switch(notification.toUpperCase()){
            case "NO":
                try{
                    String url = communicationBaseUrl + "/send-email";
                    response = restTemplate.postForObject(url, request, ResponseWrapper.class);
                    log.info(">>>>>>>>>>>>>>>>>>>>>>+++++");

                    log.info(response.getMessage() + " "+response.getCode());
                    log.info(url);
                }catch (RestClientException ex){
                    log.info(">>>>>>>>>>>>>>>>>>ex>>>>+++++");
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

        ufsPosUserRepository.findByUsernameIgnoreCaseAndIntrash(wrapper.getUsername(), "NO").ifPresentOrElse(
                ufsPosUser->{

                    if (ufsPosUser.getActiveStatus().equalsIgnoreCase(AppConstants.PASS_LOCKED_STATUS)) {
                        responseWrapper.setCode(423);
                        responseWrapper.setMessage("Account is Locked");
                        auditLog.setNotes("Account Locked for "+wrapper.getUsername());

                    }

                    else if(ufsPosUser.getPinStatus().equalsIgnoreCase(AppConstants.STATUS_INACTIVE)){

                        // lets use Pin status to check for first time login so that we can use active status if need be to deactivate
                        // user account
                        responseWrapper.setMessage("Login for the first time to activate account");

                        responseWrapper.setCode(33); // login for the first time code
                    }
                    else if (!encoder.matches(wrapper.getPin(),ufsPosUser.getPin())) {
                        UfsSysConfig posPinAttempts = ufsSysConfigRepository.findByEntityAndParameter("Pos Configuration","posPinAttempts");
                        Integer allowedAttempts = Integer.valueOf(posPinAttempts == null ? "30": posPinAttempts.getValue());
                        BigInteger pinLoginAttempts = ufsPosUser.getPinLoginAttemtps() == null?new BigInteger("0"):ufsPosUser.getPinLoginAttemtps();
                        if (pinLoginAttempts.intValue() == allowedAttempts.intValue()) {
                            ufsPosUser.setActiveStatus(AppConstants.PASS_LOCKED_STATUS);
                        }else{
                            BigInteger attempts = ufsPosUser.getPinLoginAttemtps();
                            ufsPosUser.setPinLoginAttemtps((attempts != null) ? attempts.add(new BigInteger("1")) : new BigInteger("1"));
                        }


                        ufsPosUserRepository.save(ufsPosUser);
                        auditLog.setNotes("Wrong Password for " + wrapper.getUsername() );

                        responseWrapper.setMessage("Wrong Password");

                        responseWrapper.setCode(01);


                    } else {
                        responseWrapper.setMessage(ufsPosUser.getPosRole());
                        responseWrapper.setCode(HttpStatus.OK.value());
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

                        auditLog.setUserId(ufsPosUser.getPosUserId().longValue());
                        //reset pin attempts after successfully login
                        ufsPosUser.setPinLoginAttemtps(null);
                        ufsPosUser.setPinLastLogin(new Date());
                        ufsPosUserRepository.save(ufsPosUser);
                        auditLog.setStatus(AppConstants.STATUS_COMPLETED);

                    }

                },
                ()->{
                    auditLog.setNotes("No user found by " + wrapper.getUsername());
                    responseWrapper.setMessage("No user found by.. " + wrapper.getUsername());
                    responseWrapper.setCode(27);
                }

        );

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
                    // Validate user by ID
                    if(userExist.getUserExistByIdNumber()){
                        responseWrapper.setCode(402);
                        responseWrapper.setMessage("Pos user already exist for " + wrapper.getIdNumber());
                        auditLog.setNotes("Pos user already exist for " + wrapper.getIdNumber());
                        auditLog.setDescription("Pos user already exist for " + wrapper.getIdNumber());
                        // validate by username
                    } else if (userExist.getUserExistByUsername()){
                        responseWrapper.setCode(409);
                        responseWrapper.setMessage("Pos user already exist for " + wrapper.getUsername());
                        auditLog.setNotes("Pos user already exist for " + wrapper.getUsername());
                        auditLog.setDescription("Pos user already exist for " + wrapper.getUsername());

                    }
                    // passed then proceed to validating tms device
                    else{
                        TmsDeviceRepository tmsDeviceRepository = SpringContextBridge.services().getTmsDeviceRepo();

                        TmsDevice tmsDevice = tmsDeviceRepository.findBySerialNoAndIntrashAndActionStatus(wrapper.getSerialNumber(), "NO", AppConstants.STATUS_APPROVED);

                        if(Objects.nonNull(tmsDevice)){
                            try{
                                //if the ufsUserId exists not then create
                                UfsPosUser user = new UfsPosUser();
                                String workgroupStr = wrapper.getWorkgroup(); // role

                                if (workgroupStr != null) {

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


                                    auditLog.setStatus(AppConstants.STATUS_COMPLETED);
                                    auditLog.setEntityId(String.valueOf(user.getPosUserId()));
                                    auditLog.setNotes("Pos user created successfully");
                                    auditLog.setDescription("Pos user " + wrapper.getPhoneNumber() + " created successfully");
                                    responseWrapper.setMessage("Pos user created successfully");
                                    responseWrapper.setCode(200);
                                    ufsPosUserRepository.save(user);
                                }else{
                                    auditLog.setEntityId(String.valueOf(user.getPosUserId()));
                                    auditLog.setNotes("Provide role");

                                    responseWrapper.setMessage("Provide user role");
                                    responseWrapper.setCode(11);
                                }
                            }catch (NullPointerException ex){
                                ex.printStackTrace();
                                log.error(ex.getMessage());
                                responseWrapper.setMessage("System error");
                                responseWrapper.setCode(06);

                                auditLog.setNotes("Device with Serial No "+tmsDevice.getSerialNo() + " does not have an outlet attached to it");

                            }

                        } else {
                            // dont create user since this terminal does not belong
                            responseWrapper.setMessage("No Tms device found with the serial number");
                            auditLog.setNotes("No Tms device found with the serial number"+ wrapper.getSerialNumber());
                            responseWrapper.setCode(30);
                        }

                    }

                } catch (IncorrectResultSizeDataAccessException e) {
                    e.printStackTrace();
                    responseWrapper.setCode(12);
                    auditLog.setDescription("Either more that two devices have the same serial number or user creation failed due to duplicate username or Id number ");
                    auditLog.setNotes("Duplicate records");
                    responseWrapper.setMessage(e.getMessage());
                }
            }


        } catch (DataIntegrityViolationException ex){
            ex.printStackTrace();
            log.error(ex.getMessage());
            responseWrapper.setCode(06);
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

            String randomPin = RandomStringUtils.random(Integer.parseInt(ufsSysConfig.getValue()),false,true);
            user.get().setPin(encoder.encode(randomPin));
            user.get().setActiveStatus(AppConstants.STATUS_INACTIVE);
            user.get().setPinStatus(AppConstants.PIN_STATUS_INACTIVE);
            responseWrapper.setMessage(randomPin);

            auditLog.setNotes("Reset password successful for "+ wrapper.getUsername());
            auditLog.setEntityId(String.valueOf(user.get().getPosUserId()));
            auditLog.setStatus(AppConstants.STATUS_COMPLETED);

            responseWrapper.setCode(200);
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

        ufsPosUserRepository.findByIdNumberAndIntrash(wrapper.getIdNumber(), "NO")
                .ifPresent((posUser)->{
                    userExistWrapper.setUserExistByIdNumber(true);
                    userExistWrapper.setUserId(posUser.getPosUserId().longValue());
                });

        //lets check if a user exist by the unique username provide during creation
        ufsPosUserRepository.findByUsernameIgnoreCaseAndIntrash(wrapper.getUsername(), "NO")
                .ifPresent(posUser -> {
                    userExistWrapper.setUserExistByUsername(true);
                    userExistWrapper.setUserId(posUser.getPosUserId().longValue());
                });
        return userExistWrapper;
    }

    private ResponseWrapper validateUserDetails(PosUserWrapper wrapper, ResponseWrapper responseWrapper, UfsPosAuditLog auditLog) {
        // validation for first name

        if (wrapper.getFirstName() == null){
            responseWrapper.setCode(14);
            responseWrapper.setMessage("first name missing");
            auditLog.setNotes("first name missing");
            responseWrapper.setError(true);

        }
        else if (wrapper.getOtherName() == null){
            responseWrapper.setCode(15);
            auditLog.setNotes("other name(s) missing");
            responseWrapper.setMessage("other name(s) missing");
            responseWrapper.setError(true);

        }
        else if  (wrapper.getConfirmPin() == null){
            responseWrapper.setCode(16);
            responseWrapper.setMessage("pin/password missing");
            responseWrapper.setError(true);

        }
        else if  (wrapper.getIdNumber() == null){
            responseWrapper.setCode(17);
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
            responseWrapper.setCode(26);
            responseWrapper.setMessage("Serial Number Missing");
            auditLog.setNotes("Serial Number Missing");
            auditLog.setDescription("POS making the request did not send its serial Number");
            responseWrapper.setError(true);

        }
        // validation for MID
        else if (posMID == null || posMID.isBlank()){
            responseWrapper.setCode(42);
            responseWrapper.setMessage("Merchant ID Missing");
            auditLog.setDescription("Request from device with serial number " + wrapper.getSerialNumber() + " did not provide MID");
            auditLog.setNotes("Merchant ID Missing");
            responseWrapper.setError(true);

        }
        // validation for TID
        else if (posTID == null || posTID.isBlank()){
            responseWrapper.setCode(41);
            auditLog.setNotes("Terminal ID Missing");
            auditLog.setDescription("Request from device with serial number " + wrapper.getSerialNumber() + " did not provide TID");
            responseWrapper.setMessage("Terminal ID Missing");
            responseWrapper.setError(true);

        }
        return responseWrapper;
    }

    private ResponseWrapper validatePosRequest(PosUserWrapper wrapper, boolean b, UfsPosAuditLog auditLog) {
        // common validator method for members that need its
        // TODO: 03/07/2020 : if possible extend to validate for rogue terminal details.
        log.info("******************---0"+ wrapper.getUsername());
        ResponseWrapper responseWrapper = new ResponseWrapper();

        auditLog.setOccurenceTime(new Date());
        auditLog.setStatus(AppConstants.STATUS_FAILED);
        auditLog.setEntityName("UfsPosUser");
        auditLog.setClientId(AppConstants.CLIENT_ID);

        UfsPosUserRepository ufsPosUserRepository = SpringContextBridge.services().getPOSUserRepo();
        TmsDeviceRepository tmsRepo = SpringContextBridge.services().getTmsDeviceRepo();
        Optional<UfsPosUser> ufsPosUser = ufsPosUserRepository.findByUsernameIgnoreCaseAndIntrash(wrapper.getUsername(), "NO");
        ufsPosUser.ifPresent(posUser -> {
            // common to all methods so lets set them at a common place

            auditLog.setEntityId(String.valueOf(ufsPosUser.get().getPosUserId()));
            auditLog.setUserId(ufsPosUser.get().getPosUserId().longValue());
            responseWrapper.setPosUser(ufsPosUser);

        });
        // log Ip
        try {
            auditLog.setIpAddress(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            auditLog.setNotes("Unknown IP address");

        }

        // validation for login
        if (b) {
            if (wrapper.getUsername() == null) {
                responseWrapper.setError(true);
                responseWrapper.setMessage("username cannot be null.");
                auditLog.setDescription("Username is null");
                responseWrapper.setCode(13);
                return responseWrapper;
            }
        }

        validateDeviceDetails(wrapper, responseWrapper, auditLog);
        //  error will be set true if validateDeviceDetails fail

        if(responseWrapper.getError()){
            return responseWrapper;
        }
        //Steps:
        // check if a user exist with the username provided
        // check if a device exist with the supplied serial number
        // If it does do a lookout for the outlet the device belongs to
        // if an out let exist then check if the user belongs to that outlet

        if (!ufsPosUser.isPresent()) {
            // common to all methods so lets set them at a common place
            responseWrapper.setMessage("User with username " + wrapper.getUsername() + " Not found");
            auditLog.setNotes("User with username " + wrapper.getUsername() + " Not found");
            responseWrapper.setCode(27);
            responseWrapper.setError(true);
            return responseWrapper;
        }


        TmsDevice tmsDevice;
        try{
            tmsDevice = tmsRepo.findBySerialNoAndIntrashAndActionStatus(wrapper.getSerialNumber(), "NO", "Approved");
//            tmsDevice1 = tmsRepo.findBySer
        }catch (NoSuchElementException e){
            responseWrapper.setError(true);
            responseWrapper.setCode(27);
            auditLog.setNotes("User with username "+wrapper.getUsername() + "Not found");
            responseWrapper.setMessage("User with username "+wrapper.getUsername() + " Not found");
            return responseWrapper;
        }

        log.info("****");
        log.info(wrapper.getSerialNumber());
        if (Objects.nonNull(tmsDevice)) {
            try{
                if (tmsDevice.getOutletId() != null && ufsPosUser.get().getDeviceId() != null) {
                    log.info("****1");
                    //validate if user belongs to this outlet which the pos terminal belongs
                    if (ufsPosUser.get().getDeviceId().getOutletId() ==  null) {
                        log.info("****2");
                        responseWrapper.setMessage(wrapper.getSerialNumber() + " not attached to any outlet");
                        responseWrapper.setCode(28);
                        auditLog.setNotes(wrapper.getSerialNumber() + " not attached to any outlet");
                        auditLog.setDescription(wrapper.getUsername() + " not authorised to access terminal with serial number:" + wrapper.getSerialNumber()+ " Device not attached to any outlet");
                        responseWrapper.setError(true);
                        return responseWrapper;
                    } else if (ufsPosUser.get().getDeviceId().getOutletId() ==  null){
                        log.info("****3 ");
                        responseWrapper.setMessage("terminal device not attached to an outlet");
                        responseWrapper.setError(true);

                        responseWrapper.setCode(29);
                        auditLog.setNotes( "Orphan terminal " );
                        auditLog.setDescription("Terminal device not attached to an outlet");
                        return responseWrapper;

                    }
                    else if (!ufsPosUser.get().getDeviceId().getOutletId().getId().equals(tmsDevice.getOutletId().getId())) {
                        log.info("**** 4 ");
                        responseWrapper.setMessage(wrapper.getUsername() + " does not belong to "+tmsDevice.getOutletId().getOutletName() + " outlet");
                        responseWrapper.setCode(34);
                        auditLog.setNotes("access by " + wrapper.getUsername() + " denied");
                        auditLog.setDescription(wrapper.getUsername() + " does not belong to "+tmsDevice.getOutletId().getOutletName() + " outlet");
                        responseWrapper.setError(true);
                        return responseWrapper;
                    }
                }
                log.info("**** 5");

            }catch (NullPointerException ex){
                ex.printStackTrace();
                responseWrapper.setError(true);
                responseWrapper.setCode(06);
                auditLog.setNotes("System error");
                responseWrapper.setMessage("System error");
                return responseWrapper;
            }

        } else {

            responseWrapper.setMessage("This Terminal device is not authorised to transact, its either deleted or not approved yet.");
            // common to all methods so lets set them at a common place
            auditLog.setNotes("Unauthorised to transact");
            auditLog.setDescription("This Terminal device is not authorised to transact, its either deleted or not approved yet. Device SNO " + wrapper.getSerialNumber());
            responseWrapper.setCode(30);
            responseWrapper.setError(true);
            return responseWrapper;

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
            responseWrapper.setCode(200);
            posUser.get().setIntrash("YES");
            responseWrapper.setMessage("User deleted successfully");
            auditLog.setNotes(wrapper.getUsername() +" deleted successfully");

        }catch (Exception ex){
            log.error(ex);
            auditLog.setNotes("System Error, User could not be deleted");
            responseWrapper.setMessage("System Error, User could not be deleted");
            responseWrapper.setCode(06);
        }

        auditLogRepository.save(auditLog);
        return responseWrapper;
    }

    public ResponseWrapper logout(PosUserWrapper wrapper){
        log.info("---------111----------");
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
            responseWrapper.setCode(200);
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

        String users = ufsPosUserRepository.findAll().stream()
                .map(user->userDetails(user))
                .collect(Collectors.joining("|"));
        responseWrapper.setMessage(users);

        responseWrapper.setCode(200);

        return responseWrapper;
    }
}
