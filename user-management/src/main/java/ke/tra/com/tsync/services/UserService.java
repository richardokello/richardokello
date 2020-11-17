package ke.tra.com.tsync.services;

import entities.*;
import ke.tra.com.tsync.services.template.UserServiceTempl;
import ke.tra.com.tsync.utils.annotations.AppConstants;
import ke.tra.com.tsync.wrappers.*;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import repository.*;


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
    private final OtpCategoryRepository otpCategoryRepository;
    private final OtpService otpService;
    private final TmsDeviceRepository tmsDeviceRepository;
    private final UfsPosUserRepository ufsPosUserRepository;
    private final UfsPosAuditLogRepository auditLogRepository;


    public UserService(UfsSysConfigRepository ufsSysConfigRepository, UfsContactPersonRepository contactPersonRepository, OtpCategoryRepository otpCategoryRepository, OtpService otpService, TmsDeviceRepository tmsDeviceRepository, UfsPosUserRepository ufsPosUserRepository, UfsPosAuditLogRepository auditLogRepository) {
        this.ufsSysConfigRepository = ufsSysConfigRepository;
        this.otpCategoryRepository = otpCategoryRepository;
        this.otpService = otpService;

        this.tmsDeviceRepository = tmsDeviceRepository;
        this.ufsPosUserRepository = ufsPosUserRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Value("${communicationBaseUrl}")
    private String communicationBaseUrl;
    @Autowired
    PasswordEncoder encoder;

    private UserExistWrapper checkUserExist(PosUserWrapper wrapper) {
        UserExistWrapper userExistWrapper = new UserExistWrapper();
        // lets check if a user exist by the unique username provide during creation


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


    private ResponseWrapper validateOutlet(ResponseWrapper responseWrapper, PosUserWrapper wrapper, UfsPosAuditLog auditLog, UfsPosUser ufsPosUser){
        // for non admins
        try{
            tmsDeviceRepository.findBySerialNoAndIntrash(wrapper.getSerialNumber(), "NO");

        }catch (NoSuchElementException e){
            responseWrapper.setMessage("This Terminal device is not authorised to transact, its either deleted or non existing.");
            // common to all methods so lets set them at a common place
            auditLog.setNotes("Unauthorised to transact");
            auditLog.setDescription("This Terminal device is not authorised to transact, its either deleted or not existing. Device SNO " + wrapper.getSerialNumber());
            responseWrapper.setCode(AppConstants.POS_TERMINAL_NOT_FOUND);
            responseWrapper.setError(true);
            return responseWrapper;
        }
        log.info("WE ARE c");
        String serialNo = ufsPosUser.getSerialNumber()==null?"-1":ufsPosUser.getSerialNumber();
        String username = ufsPosUser.getUsername();
        // validate serial number if the user is not from bank ie not in the list of admins group

        log.info(serialNo.equals(wrapper.getSerialNumber()) && username.toUpperCase().equals(wrapper.getUsername().toUpperCase()));
        if(!(serialNo.equals(wrapper.getSerialNumber()) &&
                username.toUpperCase().equals(wrapper.getUsername().toUpperCase()))){
            responseWrapper.setMessage("User with username " + wrapper.getUsername() + " does not belong to this terminal");
            auditLog.setNotes("User  does not belong to terminal");
            auditLog.setDescription("User with username " + wrapper.getUsername() + " does not belong to this terminal");
            responseWrapper.setCode(AppConstants.POS_TERMINAL_NOT_ATTACHED_TO_OUTLET);

            responseWrapper.setError(true);

        }

        return  responseWrapper;

    }

    public boolean authenticateUserCredentials(String pin,UfsPosUser ufsPosUser){
        boolean validCreds = false;
        log.info("9999999999999----- " +ufsPosUser.getUsername());
        if (!encoder.matches(pin,ufsPosUser.getPin())) {

            UfsSysConfig posPinAttempts = ufsSysConfigRepository.findByEntityAndParameter("Pos Configuration","posPinAttempts");
            Integer allowedAttempts = Integer.valueOf(posPinAttempts == null ? "30": posPinAttempts.getValue());
            BigInteger pinLoginAttempts = ufsPosUser.getPinLoginAttemtps() == null?new BigInteger("0"):ufsPosUser.getPinLoginAttemtps();
            if (pinLoginAttempts.intValue() == allowedAttempts) {
                ufsPosUser.setActiveStatus(AppConstants.PASS_LOCKED_STATUS);
            }else{
                BigInteger attempts = ufsPosUser.getPinLoginAttemtps();
                ufsPosUser.setPinLoginAttemtps((attempts != null) ? attempts.add(new BigInteger("1")) : new BigInteger("1"));
            }


        }else{
            validCreds =true;
        }
        return validCreds;
    }

    private ResponseWrapper authenticateAccount(PosUserWrapper wrapper, UfsPosUser ufsPosUser, ResponseWrapper responseWrapper){
        if (ufsPosUser.getActiveStatus().equalsIgnoreCase(AppConstants.PASS_LOCKED_STATUS)) {
            responseWrapper.setCode(AppConstants.POS_LOCKED_ACCOUNT);
            responseWrapper.setError(true);
            responseWrapper.setMessage("Account Locked for "+wrapper.getUsername());

        }
        else if (ufsPosUser.getActiveStatus().equalsIgnoreCase(AppConstants.STATUS_DISABLED)) {
            responseWrapper.setCode(AppConstants.POS_USER_DISABLED);
            responseWrapper.setError(true);
            responseWrapper.setMessage("Account deactivated "+wrapper.getUsername());

        }else if (ufsPosUser.getPinStatus().equalsIgnoreCase(AppConstants.PIN_STATUS_INACTIVE) || ufsPosUser.getActiveStatus().equalsIgnoreCase(AppConstants.STATUS_INACTIVE)) {
            responseWrapper.setCode(AppConstants.POS_FIRSTTIME_LOGIN_IN); // login for the first time code
            responseWrapper.setMessage("Account is inactive for "+wrapper.getUsername());
            responseWrapper.setError(true);


        }

        return responseWrapper;
    }
    private ResponseWrapper findByUsernameAndSerialNumber(String username, String SN, ResponseWrapper responseWrapper){

        Optional<UfsPosUser> posUser = ufsPosUserRepository.findByUsernameIgnoreCaseAndIntrashAndSerialNumber(username, "NO",SN);
        if (posUser.isEmpty()){
            responseWrapper.setMessage("User not found with the username on this outlet");
            responseWrapper.setCode(AppConstants.POS_USER_NOT_FOUND);
            responseWrapper.setPosUser(posUser);

            log.info("0000000000000>>>><<<<000");
        }else{
            responseWrapper.setPosUser(posUser);
        }
        return responseWrapper;
    }
    private ResponseWrapper validatePosRequest(PosUserWrapper wrapper, boolean validateUname, UfsPosAuditLog auditLog) {
        // common validator method for members that need its

        var responseWrapper = new ResponseWrapper();
        auditLog.setOccurenceTime(new Date());
        auditLog.setStatus(AppConstants.STATUS_FAILED);
        auditLog.setEntityName("UfsPosUser");
        auditLog.setClientId(AppConstants.CLIENT_ID);
        // log Ip
        setLogIpAddress(auditLog);

        validateDeviceDetails(wrapper, responseWrapper, auditLog);
        //  error will be set true if validateDeviceDetails fail

        if(responseWrapper.getError()){
            return responseWrapper;
        }
        // validation for login
        if (validateUname) {
            if (wrapper.getUsername() == null) {
                responseWrapper.setError(true);
                responseWrapper.setMessage("username cannot be null.");
                auditLog.setDescription("Username is null");
                responseWrapper.setCode(AppConstants.POS_USERNAME_MISSING);
                responseWrapper.setError(true);
                return responseWrapper;
            }
        }



        return responseWrapper;

        //Steps:
        // check if a user exist with the username provided
        // check if a device exist with the supplied serial number
        // If it does do a lookout for the outlet the device belongs to
        // if an out let exist then check if the user belongs to that outlet


    }

    private void loginSuccess(ResponseWrapper responseWrapper, UfsPosUser posUser, UfsPosAuditLog auditLog){

        responseWrapper.setMessage(posUser.getPosRole());
        responseWrapper.setCode(AppConstants.POS_APPROVED);
        auditLog.setNotes("Login Successfully by "+posUser.getUsername());

        auditLog.setUserId(posUser.getPosUserId().longValue());
        //reset pin attempts after successfully login
        posUser.setPinLoginAttemtps(new BigInteger("0"));
        posUser.setPinLastLogin(new Date());

        auditLog.setStatus(AppConstants.STATUS_COMPLETED);
        auditLog.setEntityId(String.valueOf(posUser.getPosUserId()));
        auditLog.setUserId(posUser.getPosUserId().longValue());

//        EmailAndMessageWrapper request = new EmailAndMessageWrapper();
//        Optional<UfsOtpCategory> category = otpCategoryRepository.findDistinctByCategoryAndIntrash("Authentication", "NO");
//
//        sendMessage(request, ufsBankOfficial, category);
    }
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

        responseWrapper = validatePosRequest(posUserWrapper, false, auditLog);

        try {
            if(responseWrapper.getError()){
                auditLogRepository.save(auditLog);
                return responseWrapper;
            }
            responseWrapper = findByUsernameAndSerialNumber(wrapper.getUsername(), wrapper.getSerialNumber(),responseWrapper);
            Optional<UfsPosUser> ufsPosUser = responseWrapper.getPosUser();
            if(ufsPosUser.isPresent()) {
                auditLog.setEntityId(String.valueOf(ufsPosUser.get().getPosUserId()));
                auditLog.setUserId(ufsPosUser.get().getPosUserId().longValue());
                if (ufsPosUser.get().getPinStatus().equalsIgnoreCase(AppConstants.PIN_STATUS_INACTIVE) || ufsPosUser.get().getActiveStatus().equalsIgnoreCase(AppConstants.STATUS_INACTIVE)) {
                    responseWrapper.setMessage("Account is inactive");
                    auditLog.setNotes("Pin status " + AppConstants.PIN_STATUS_INACTIVE + "+ User status " + AppConstants.STATUS_INACTIVE);

                    responseWrapper.setCode(AppConstants.POS_ACCOUNT_INACTIVE);

                } else if (!encoder.matches(wrapper.getPin(), ufsPosUser.get().getPin())) {
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
        responseWrapper = validatePosRequest(wrapper, true, auditLog);

        auditLog.setOccurenceTime(new Date());
        auditLog.setActivityType(AppConstants.FIRST_TIME_LOGIN);


        setLogIpAddress(auditLog);
        try {

            if(responseWrapper.getError()){
                auditLogRepository.save(auditLog);
                return responseWrapper;
            }
            responseWrapper = findByUsernameAndSerialNumber(wrapper.getUsername(), wrapper.getSerialNumber(),responseWrapper);
            Optional<UfsPosUser> ufsPosUser = responseWrapper.getPosUser();
            if(ufsPosUser.isPresent()) {
                auditLog.setEntityId(String.valueOf(ufsPosUser.get().getPosUserId()));
                auditLog.setUserId(ufsPosUser.get().getPosUserId().longValue());
                if (!encoder.matches(wrapper.getPin(), ufsPosUser.get().getPin())) {
                    responseWrapper.setCode(AppConstants.POS_INCORRECT_CREDENTIALS);
                    responseWrapper.setMessage("Invalid credentials");
                    auditLog.setNotes("Wrong Password");
                    auditLog.setDescription("Wrong Password for "+ wrapper.getUsername());

                } else {

                    ufsPosUser.get().setPinStatus(AppConstants.PIN_STATUS_ACTIVE);
                    ufsPosUser.get().setActiveStatus(AppConstants.STATUS_ACTIVE);
                    ufsPosUser.get().setPinLastLogin(Calendar.getInstance().getTime());

                    ufsPosUserRepository.save(ufsPosUser.get());
                    auditLog.setStatus(AppConstants.STATUS_COMPLETED);
                    responseWrapper.setMessage("First time login was successful");
                    responseWrapper.setCode(AppConstants.POS_APPROVED);
                }
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
        var responseWrapper = new ResponseWrapper();
        UfsPosAuditLog auditLog = new UfsPosAuditLog();
        auditLog.setOccurenceTime(new Date());
        auditLog.setActivityType(AppConstants.ACTIVITY_AUTHENTICATION);
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

        // validate Device details

        List <String> doesNotExist = new ArrayList<>();
        List <UfsPosUser> disable = new ArrayList<>();

        //split the username string at ,
        String[] usernames = wrapper.getUsername().split(",");
        Arrays.stream(usernames)
                .forEach(username->{
                    ufsPosUserRepository.findByUsernameIgnoreCaseAndIntrashAndSerialNumber(username, "NO", wrapper.getSerialNumber())
                            .ifPresentOrElse(user->{
                                user.setActiveStatus(AppConstants.STATUS_DISABLED);
                                user.setPinStatus(AppConstants.PIN_STATUS_INACTIVE);
                                disable.add(user);
                            },()->{

                                doesNotExist.add(username);
                            });

                });
        return getResponseWrapper(responseWrapper, ufsPosUserRepository, doesNotExist, disable);
    }


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
            List<UfsPosUser> matchingUsers;

            String roleIProvided = wrapper.getWorkgroup()==null?"":wrapper.getWorkgroup(); // pos role

            switch(roleIProvided.strip().toUpperCase()){
                // GROUP 3
                // teller/operators/cashier/customer load  by tid(THIRD CATEGORY)
                case AppConstants.USER_ROLE_CASHIER:
                case AppConstants.USER_ROLE_OPERATOR:
                case AppConstants.USER_ROLE_TELLER :
                    matchingUsers = ufsPosUserRepository.findByUsernameStartsWithIgnoreCaseAndSerialNumberAndIntrashAndPosRole(
                            prefix, wrapper.getSerialNumber(),"NO", wrapper.getWorkgroup());
                    break;
                // GROUP 2
                case AppConstants.USER_ROLE_SUPERVISOR:
                case "MERCHANT OWNER":
                case AppConstants.USER_ROLE_MERCHANT:

                    String prefixLike = prefix+"%";
                    // if outlet/Agent owner/Supervisor we want to load users of the outlet
                    matchingUsers = ufsPosUserRepository.findByUsernamePrefix(prefixLike.toUpperCase(),wrapper.getSerialNumber());
                    break;

                // FIRST CATEGORY WHICH BELONG TO THE BANK
                // GROUP 1
                case AppConstants.USER_ROLE_BANK_ADMIN:
                case AppConstants.USER_ROLE_BANK_ADMINISTRATOR:
                case AppConstants.USER_ROLE_BANK_FIELD_SUPERVISOR:
                    // if BANK Agent we want to give access to the entire bank pos users
                default:
                    matchingUsers = ufsPosUserRepository.findByUsernameStartsWithIgnoreCaseAndIntrash(
                            wrapper.getUsername(), "NO"
                    );
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

        EmailAndMessageWrapper request = new EmailAndMessageWrapper();

         responseWrapper = validatePosRequest(wrapper, true, auditLog);

        if(responseWrapper.getError()){
            auditLogRepository.save(auditLog);
            return responseWrapper;
        }

        // validate Device details

        // get list of users
        // on each call check case if admin or not
        // if admin check for authentication only
        // if not admin call validate against outlet func


        List<UfsPosUser> ufsPosUserList = ufsPosUserRepository.findAllByUsernameIgnoreCaseAndIntrash(wrapper.getUsername(), "NO");
        log.info("00000000000000>>>>> "+ufsPosUserList.size());
        log.info(wrapper.getUsername());
        boolean wasAuthenticatedAsAdmin = false;

        String adminsGroup[] = {
                "ADMIN", "ADMINISTRATOR", "ACTIVATION SUPPORT","SUPPORT", "FIELD SUPERVISOR"};
        // admin
        List<UfsPosUser> adminsFound = ufsPosUserList
                .stream()
                .filter(user->Arrays.asList(adminsGroup).contains(user.getPosRole().strip().toUpperCase())==true)
                .collect(Collectors.toList());

        List<UfsPosUser> ufsPosUsersDb = ufsPosUserList
                .stream()
                .filter(user->Arrays.asList(adminsGroup).contains(user.getPosRole().strip().toUpperCase())==false)
                .collect(Collectors.toList());


        if(!adminsFound.isEmpty()){
            for (UfsPosUser ufsBankOfficial:adminsFound){
                log.info("Admin Size=== "+ adminsFound.size());
                // validate creds
                if(!authenticateUserCredentials(wrapper.getPin(), ufsBankOfficial)){
                    log.info("invalid password for at admin");
                    log.info("invalid password for "+wrapper.getUsername());
                    // invalid user credentials were provided then check against the next user on this list
                    // however users of this group should have unique username, just incase this becomes false
                    // hence we include the check for this group as a for loop(even though we could just get(0))
                    responseWrapper.setCode(AppConstants.POS_INCORRECT_CREDENTIALS);
                    responseWrapper.setMessage("Invalid Credentials..");

                    continue;
                }else {
                    // we validate account status if correct credentials were provides
                     responseWrapper = authenticateAccount(wrapper, ufsBankOfficial, responseWrapper);
                    log.info("Correct creds.. "+ ufsBankOfficial.getUsername());
                    if (responseWrapper.getError()) {
                        auditLog.setNotes("authentication failed");
                        auditLog.setNotes(responseWrapper.getMessage());
                        log.info("==" + responseWrapper.getMessage());
                        // we break coz we have found the user but the account is has issues

                        continue;

                    }else {

                        wasAuthenticatedAsAdmin = true;
                        ufsPosUserRepository.save(ufsBankOfficial);
                        loginSuccess(responseWrapper,ufsBankOfficial,auditLog);

                    }

                }
            }

        }

        if(ufsPosUsersDb.isEmpty() == false && wasAuthenticatedAsAdmin == false){
            log.error("ufsPosUsersDb+ "+ ufsPosUsersDb.size());

            for (UfsPosUser ufsPosUser:ufsPosUsersDb){
                // validate creds
                if(!authenticateUserCredentials(wrapper.getPin(), ufsPosUser)){
                    // invalid user credentials were provided then check agains the next user on this list
                    // however users of this group should have unique username, just incase this becomes false
                    // hence we include the check for this group
                    log.info("invalid password for at normal");
                    responseWrapper.setCode(AppConstants.POS_INCORRECT_CREDENTIALS);
                    responseWrapper.setMessage("Invalid Credentials");
                    responseWrapper.setError(true);
                    continue;
                }else if (validateOutlet(responseWrapper,wrapper,auditLog,ufsPosUser).getError() == true){
                    log.info("This user does not belong to this outlet");
                    // this method has already set response message

                }
                else {
                    // we validate account status if correct credentials were provides
                    responseWrapper = authenticateAccount(wrapper, ufsPosUser, responseWrapper);
                    if (responseWrapper.getError()) {
                        auditLog.setNotes(responseWrapper.getMessage());
                        continue;
                        // TODO: 22/10/2020 what will happen if the next user in the list was the user and their account was okay?

                    }else {

                        ufsPosUserRepository.save(ufsPosUser);
                        loginSuccess(responseWrapper,ufsPosUser,auditLog);

                    }

                }
            }
        }else{
            responseWrapper.setMessage("User not found with the username "+wrapper.getUsername());
            responseWrapper.setCode(AppConstants.POS_USER_NOT_FOUND);
            auditLog.setNotes("user not found");
            auditLog.setNotes("User not found with the username " +wrapper.getUsername());
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

        setLogIpAddress(auditLog);

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

                        return responseWrapper;

                    }

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
                                short ftimeUser = 0;
                                user.setFirstTimeUser(ftimeUser); // used by portal for now to avoid resending password multple times


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

        // validate Device details
        responseWrapper = validatePosRequest(wrapper, true, auditLog);

        if(responseWrapper.getError()){
            return responseWrapper;

        } else {
            UfsSysConfig ufsSysConfig = ufsSysConfigRepository.findByEntityAndParameter("Pos Configuration","posPin");
            responseWrapper = findByUsernameAndSerialNumber(wrapper.getUsername(), wrapper.getSerialNumber(), responseWrapper);
            Optional<UfsPosUser> ufsPosUser = responseWrapper.getPosUser();
            if(ufsPosUser.isPresent()){
                String randomPin;
                randomPin = AppConstants.DEFAULT_RESET_PWD;
                //randomPin = RandomStringUtils.random(Integer.parseInt(ufsSysConfig.getValue()),false,true);
                ufsPosUser.get().setPin(encoder.encode(randomPin));
                ufsPosUser.get().setActiveStatus(AppConstants.STATUS_INACTIVE);
                ufsPosUser.get().setPinStatus(AppConstants.PIN_STATUS_INACTIVE);
                responseWrapper.setMessage(randomPin);

                auditLog.setNotes("Reset password successful for "+ wrapper.getUsername());
                auditLog.setEntityId(String.valueOf(ufsPosUser.get().getPosUserId()));
                auditLog.setUserId(ufsPosUser.get().getPosUserId().longValue());
                auditLog.setStatus(AppConstants.STATUS_COMPLETED);

                responseWrapper.setCode(AppConstants.POS_APPROVED);
                auditLog.setNotes("Reset password for "+ wrapper.getUsername() +" was success");
                auditLog.setDescription("Reset password for "+ wrapper.getUsername() +" was success");
                ufsPosUserRepository.save(ufsPosUser.get());
            }
            log.info("000***********<<<000 "+ responseWrapper.getMessage());

        }
        auditLogRepository.save(auditLog);
        return responseWrapper;
    }

    @Override
    @Transactional
    public ResponseWrapper deletePosUser(PosUserWrapper wrapper){
        UfsPosAuditLog auditLog = new UfsPosAuditLog();

        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper = validatePosRequest(wrapper, true, auditLog);

        auditLog.setActivityType("Deletion");
        setLogIpAddress(auditLog);

        try{

            if(responseWrapper.getError()){
                return responseWrapper;
            }
            responseWrapper = findByUsernameAndSerialNumber(wrapper.getUsername(),wrapper.getSerialNumber(), responseWrapper);
            Optional<UfsPosUser> posUser = responseWrapper.getPosUser();
            if(posUser.isPresent()){
                auditLog.setNotes("User deleted successful for "+ wrapper.getUsername());
                auditLog.setEntityId(String.valueOf(posUser.get().getPosUserId()));
                auditLog.setUserId(posUser.get().getPosUserId().longValue());
                UfsCustomerOutlet posCustomerOutlet = posUser.get().getDeviceId().getOutletId();
                if(posCustomerOutlet != null){

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
            }


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
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper = validatePosRequest(wrapper, true, auditLog);

        setLogIpAddress(auditLog); // log ip address

        auditLog.setOccurenceTime(new Date());
        auditLog.setActivityType("Logout");

        if (responseWrapper.getError()){
            return responseWrapper;
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

        String users = ufsPosUserRepository.findAll()
                .stream()
                .map(user->userDetails(user))
                .collect(Collectors.joining("|"));
        responseWrapper.setMessage(users);

        responseWrapper.setCode(AppConstants.POS_APPROVED);

        return responseWrapper;
    }
}
