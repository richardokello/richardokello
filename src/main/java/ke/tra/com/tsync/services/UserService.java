package ke.tra.com.tsync.services;

import ke.tra.com.tsync.entities.*;
import ke.tra.com.tsync.entities.wrappers.ActionWrapper;
import ke.tra.com.tsync.repository.*;
import ke.tra.com.tsync.services.template.UserServiceTempl;
import ke.tra.com.tsync.utils.annotations.AppConstants;
import ke.tra.com.tsync.wrappers.ChangePin;
import ke.tra.com.tsync.wrappers.PosUserWrapper;
import ke.tra.com.tsync.wrappers.ResponseWrapper;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Service
@CommonsLog
public class UserService implements UserServiceTempl {

    private final UfsUserWorkgroupRepository ufsUserWorkgroupRepository;
    private final UfsPosUserRepository ufsPosUserRepository;
    private final UfsWorkgroupRepository workgroupRepository;
    private final UfsGenderRepository ufsGenderRepository;
    private final UfsUserTypeRepository userTypeRepository;
    private final UfsSysConfigRepository ufsSysConfigRepository;
    private final TmsDeviceRepository tmsDeviceRepository;
    private final UfsCustomerRepository ufsCustomerRepository;
    private final UfsWorKGroupRoleRepository worKGroupRoleRepository;
    private final UfsDepartmentRepository ufsDepartmentRepository;
    private final UfsAuditLogRepository auditLogRepository;

    private final UfsUserRepository ufsUserRepository;



    public UserService(UfsPosUserRepository ufsPosUserRepository, UfsUserWorkgroupRepository ufsUserWorkgroupRepository, UfsWorkgroupRepository workgroupRepository, UfsDepartmentRepository ufsDepartmentRepository, UfsUserRepository ufsUserRepository, UfsGenderRepository ufsGenderRepository, UfsUserTypeRepository userTypeRepository, UfsSysConfigRepository ufsSysConfigRepository, TmsDeviceRepository tmsDeviceRepository, UfsCustomerRepository ufsCustomerRepository, UfsWorKGroupRoleRepository worKGroupRoleRepository, UfsDepartmentRepository departmentRepository, UfsAuditLogRepository auditLogRepository, UfsUserRepository ufsUserRepository1) {
        this.ufsPosUserRepository = ufsPosUserRepository;
        this.ufsUserWorkgroupRepository = ufsUserWorkgroupRepository;
        this.workgroupRepository = workgroupRepository;
        this.ufsGenderRepository = ufsGenderRepository;
        this.userTypeRepository = userTypeRepository;
        this.ufsSysConfigRepository = ufsSysConfigRepository;
        this.tmsDeviceRepository = tmsDeviceRepository;
        this.ufsCustomerRepository = ufsCustomerRepository;
        this.worKGroupRoleRepository = worKGroupRoleRepository;
        this.ufsDepartmentRepository = departmentRepository;
        this.auditLogRepository = auditLogRepository;
        this.ufsUserRepository = ufsUserRepository1;
    }
    @Autowired
    PasswordEncoder encoder;

    @Override
    @Transactional
    public ResponseWrapper changePin(ChangePin wrapper) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        System.out.println("--------------------+++_____-______=--"+ wrapper.getUsername());
        UfsPosUser ufsPosUser = ufsPosUserRepository.findByUsernameIgnoreCaseAndIntrash(wrapper.getUsername(), "NO");
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


        ResponseWrapper validate = validatePosRequest(posUserWrapper, false, auditLog);


        try {
            if(validate.getError()){
                responseWrapper = validate;
            }

            else if (ufsPosUser == null) {
                responseWrapper.setMessage("Username doesnt Exist");
                responseWrapper.setCode(HttpStatus.NOT_FOUND.value());

                auditLog.setNotes("Username doesnt Exist for "+wrapper.getUsername());

            }

            else if (ufsPosUser.getPinStatus().equalsIgnoreCase(AppConstants.PIN_STATUS_INACTIVE) || ufsPosUser.getActiveStatus().equalsIgnoreCase(AppConstants.STATUS_INACTIVE)) {
                responseWrapper.setMessage("Please Login For The First Time");
                auditLog.setNotes("Pin status "+AppConstants.PIN_STATUS_INACTIVE + "+ User status "+AppConstants.STATUS_INACTIVE);
                responseWrapper.setCode(HttpStatus.BAD_REQUEST.value());

            }

            else if (!encoder.matches(wrapper.getPin(), ufsPosUser.getPin())) {
                responseWrapper.setCode(HttpStatus.FORBIDDEN.value());
                responseWrapper.setMessage("Invalid Old Password");
                auditLog.setNotes("Invalid Old Password");

            }
            else if (wrapper.getPin() == null || wrapper.getNewpin() == null) {
                responseWrapper.setCode(400);
                responseWrapper.setMessage("Pin missing");
                auditLog.setNotes("Pin missing or confirm pin missing");
                auditLog.setDescription("User creation failed because no pin or confirm or both were not provided");

            }else {


                ufsPosUser.setPin(encoder.encode(wrapper.getNewpin()));
                ufsPosUser.setPinChangeDate(Calendar.getInstance().getTime());
                ufsPosUserRepository.save(ufsPosUser);
                responseWrapper.setCode(200);
                auditLog.setStatus(AppConstants.STATUS_COMPLETED);
                responseWrapper.setMessage("Pin reset was a success.");
            }
        }catch(Exception ex){
            responseWrapper.setCode(06);
            responseWrapper.setMessage("System error occurred");
            auditLog.setNotes("System error occurred");
            auditLog.setDescription("application error occurred while terminal with "+ wrapper.getSerialNumber() +"tried to login for the first time");
            ex.printStackTrace();
        }
        auditLogRepository.save(auditLog);
        return responseWrapper;
    }

    @Override
    @Transactional
    public ResponseWrapper firstTimeLogin(PosUserWrapper wrapper) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        UfsPosUser ufsPosUser = ufsPosUserRepository.findByUsername(wrapper.getUsername());
        // create audit log instance to log this activity
        UfsPosAuditLog auditLog = new UfsPosAuditLog();
        ResponseWrapper validate = validatePosRequest(wrapper, true, auditLog);

        auditLog.setOccurenceTime(new Date());
        auditLog.setActivityType(AppConstants.FIRST_TIME_LOGIN);
        System.out.println("++++++==================="+ wrapper);

        // validates Device details

        try {
            if(validate.getError()){
                responseWrapper = validate;
            }
            else if (!encoder.matches(wrapper.getPin(), ufsPosUser.getPin())) {
                responseWrapper.setCode(HttpStatus.BAD_REQUEST.value());
                responseWrapper.setMessage("Wrong Password");
                auditLog.setEntityId(String.valueOf(ufsPosUser.getPosUserId()));
                auditLog.setNotes("Wrong Password");

            }else {

                ufsPosUser.setPinStatus(AppConstants.PIN_STATUS_ACTIVE);
                ufsPosUser.setActiveStatus(AppConstants.STATUS_ACTIVE);
                ufsPosUser.setPinLastLogin(Calendar.getInstance().getTime());

                ufsPosUserRepository.save(ufsPosUser);
                auditLog.setStatus(AppConstants.STATUS_COMPLETED);
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
    public ResponseWrapper lockUser(ActionWrapper<BigDecimal> accounts) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        for (BigDecimal id: accounts.getIds()) {
            UfsPosUser user = ufsPosUserRepository.findById(id).get();
            user.setActiveStatus(AppConstants.PASS_LOCKED_STATUS);
            ufsPosUserRepository.save(user);
        }
        responseWrapper.setMessage("User account(s) locked successfully");

        return responseWrapper;
    }

    @Override
    @Transactional
    public ResponseWrapper unLockUser(ActionWrapper<BigDecimal> accounts) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        for (BigDecimal id: accounts.getIds()) {
            UfsPosUser user = ufsPosUserRepository.findById(id).get();
            user.setActiveStatus(AppConstants.ACTIVITY_UNLOCK);
            ufsPosUserRepository.save(user);

        }

        responseWrapper.setMessage("User account(s) unlocked successfully");

        return responseWrapper;
    }

    @Override
    @Transactional
    @Async
    public ResponseWrapper sendSmsMessage(String phoneNumber, String message) {
        ResponseWrapper responseWrapper = new ResponseWrapper();

        return responseWrapper;
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
    @Transactional
    public ResponseWrapper login(PosUserWrapper wrapper) {


        ResponseWrapper responseWrapper = new ResponseWrapper();

        UfsPosAuditLog auditLog = new UfsPosAuditLog();
        auditLog.setOccurenceTime(new Date());
        auditLog.setActivityType(AppConstants.ACTIVITY_AUTHENTICATION);
        setLogIpAddress(auditLog); // log ip address

        // validate Device details

        ResponseWrapper validate = validatePosRequest(wrapper, true, auditLog);
        if(validate.getError()){
            auditLogRepository.save(auditLog);
            return validate;
        }


        UfsPosUser ufsPosUser = ufsPosUserRepository.findByUsernameIgnoreCaseAndIntrash(wrapper.getUsername(), "NO");

        if (ufsPosUser.getActiveStatus().equalsIgnoreCase(AppConstants.PASS_LOCKED_STATUS)) {
            responseWrapper.setCode(423);
            responseWrapper.setMessage("Account is Locked");
            auditLog.setNotes("Account Locked for "+wrapper.getUsername());
            auditLogRepository.save(auditLog);
            return responseWrapper;
        }
        if (ufsPosUser.getActiveStatus().equalsIgnoreCase(AppConstants.PASS_INACTIVE_STATUS)) {
            responseWrapper.setCode(423);
            responseWrapper.setMessage("Account is Inactive");
            auditLog.setNotes("Account is Inactive for "+wrapper.getUsername());
            auditLogRepository.save(auditLog);
            return responseWrapper;
        }

        if (!encoder.matches(wrapper.getPin(),ufsPosUser.getPin())) {
            BigInteger attempts = ufsPosUser.getPinLoginAttemtps();

            ufsPosUser.setPinLoginAttemtps((attempts != null) ? attempts.add(new BigInteger("1")) : new BigInteger("1"));
            if (ufsPosUser.getPinLoginAttemtps().intValue() == 3) {
                ufsPosUser.setActiveStatus(AppConstants.PASS_LOCKED_STATUS);
                auditLog.setNotes("Account Locked for " + wrapper.getUsername());
            }

            ufsPosUserRepository.save(ufsPosUser);
            responseWrapper.setMessage("Wrong Password");
            responseWrapper.setCode(403);
            auditLogRepository.save(auditLog);
            return responseWrapper;
        }
        //reset pin attempts after successfully login
        if (ufsPosUser.getPinLoginAttemtps() != null ) {
            ufsPosUser.setPinLoginAttemtps(null);
            ufsPosUserRepository.save(ufsPosUser);
        }

        List<UfsUserWorkgroup> ufsUserWorkgroupList = ufsUserWorkgroupRepository.findAllByUserId(ufsPosUser.getUserIds());
        List<Long> workgroupIDs =  new ArrayList<>();
        if(Objects.nonNull(ufsUserWorkgroupList)){
            ufsUserWorkgroupList.forEach(usrwgrp ->{
                workgroupIDs.add(usrwgrp.getWorkgroupId());

            });
        }
        // if roles needs to be returned to the POS
        List<UfsWorkgroupRole> workgroupRoles = worKGroupRoleRepository.findAllByGroupIdIn(workgroupIDs);
        String userRoles = "";
        if(!workgroupRoles.isEmpty()){
            for (UfsWorkgroupRole workgroupRole : workgroupRoles) {

                userRoles += workgroupRole.getRole().getRoleName()+"|";

            }
        }


        responseWrapper.setMessage(userRoles);
        responseWrapper.setCode(HttpStatus.OK.value());
        auditLog.setNotes("Login Successfully by "+wrapper.getUsername());
        auditLog.setUserId(ufsPosUser.getUserIds());

        auditLog.setStatus(AppConstants.STATUS_COMPLETED);
        auditLogRepository.save(auditLog);

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
            responseWrapper.setError(true);
            auditLogRepository.save(auditLog);
            return responseWrapper;
        }
        // validation for MID
        if (posMID == null || posMID.trim() == ""){
            responseWrapper.setCode(42);
            responseWrapper.setMessage("Merchant ID Missing");
            auditLog.setNotes("Merchant ID Missing");
            responseWrapper.setError(true);
            auditLogRepository.save(auditLog);
            return responseWrapper;
        }
        // validation for TID
        if (posTID == null || posTID.trim() == ""){
            responseWrapper.setCode(41);
            auditLog.setNotes("Terminal ID Missing");
            responseWrapper.setMessage("Terminal ID Missing");
            responseWrapper.setError(true);
            auditLogRepository.save(auditLog);
            return responseWrapper;
        }
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


        setLogIpAddress(auditLog); // log ip address

        try {
            validateDeviceDetails(wrapper, responseWrapper, auditLog);

            if(responseWrapper.getError()){
                return responseWrapper;
            }

            // validation for first name
            if (wrapper.getFullName() == null){
                responseWrapper.setCode(400);
                responseWrapper.setMessage("first name missing");
                auditLog.setNotes("first name missing");
                responseWrapper.setError(true);
                auditLogRepository.save(auditLog);
                return responseWrapper;
            }

            if (wrapper.getConfirmPin() == null){
                responseWrapper.setCode(400);
                responseWrapper.setMessage("pin/password missing");
                responseWrapper.setError(true);
                auditLogRepository.save(auditLog);
                return responseWrapper;
            }


            UfsUser ufsUser;
            try {
                // get the user by phone number
                UfsUser ufsUserExist = ufsUserRepository.findByPhoneNumber(wrapper.getPhoneNumber());
                // check if a user exist on ufs user table and create if nONe
                if (ufsUserExist == null) {
                    /*Creating the user*/

                    UfsUser ufsUserNew = new UfsUser();

                    ufsUserNew.setFullName(wrapper.getFullName());
                    ufsUserNew.setPhoneNumber(wrapper.getPhoneNumber());
                    ufsUserNew.setGenderId(BigDecimal.valueOf(1)); // mean while use (1) ===>>>>> Gender is required
                    try {
                        //check if there is an entry on user type for the user type, if none create one will throw no such element
                        UfsUserType userType = userTypeRepository.findByUserTypeIgnoreCaseAndIntrash("Pos Agent", "NO");
                        ufsUserNew.setUserTypeId(userType.getTypeId());
                    } catch (NoSuchElementException ex) {
                        UfsUserType ufsUserType = new UfsUserType();
                        ufsUserType.setUserType("Pos Agent");
                        ufsUserType.setDescription("System generated Pos Agent");
                        userTypeRepository.save(ufsUserType);
                        ufsUserNew.setUserTypeId(ufsUserType.getTypeId());
                        ex.printStackTrace();
                    }
                    try {
                        // Get User department and if nOne create one
                        UfsDepartment department = ufsDepartmentRepository.findByDepartmentNameIgnoreCaseAndIntrash("POS Department", "NO");
                        ufsUserNew.setDepartmentIds(department.getId());
                    } catch (NoSuchElementException e) {

                        UfsDepartment departmentNew = new UfsDepartment();
                        departmentNew.setDepartmentName("POS Department");
                        departmentNew.setDescription("POS");
                        ufsDepartmentRepository.save(departmentNew);
                        ufsUserNew.setDepartmentIds(departmentNew.getId());
                        e.printStackTrace();

                    }
                    ufsUser = ufsUserNew;


                }// creating user ended successfully
                else{
                    //there is a user then lets scope them for accessibility
                    ufsUser = ufsUserExist;
                }
                // lets check if a user exist by the unique username provide during creation
                if (Objects.nonNull(ufsPosUserRepository.findByUsernameIgnoreCaseAndIntrash(wrapper.getUsername(), "NO"))) {
                    // if they exist then lets just communicate back to  the terminal
                    responseWrapper.setCode(409);
                    responseWrapper.setMessage("Pos user already exist for " + wrapper.getUsername());
                    auditLog.setNotes("Pos user already exist for " + wrapper.getPhoneNumber());
                    auditLogRepository.save(auditLog);
                    return responseWrapper;
                } else{
                    //if the ufsUserId exists not then create
                    UfsPosUser user = new UfsPosUser();
//                    UfsSysConfig ufsSysConfig = ufsSysConfigRepository.findByEntityAndParameter("Pos Configuration", "posPin");

//                    String randomPin = RandomStringUtils.random(Integer.parseInt(ufsSysConfig.getValue()), false, true);

                    user.setPin(encoder.encode(wrapper.getPin()));
                    user.setActiveStatus(AppConstants.STATUS_INACTIVE);
                    user.setPinStatus(AppConstants.PIN_STATUS_INACTIVE);
                    // save user
                    ufsUserRepository.save(ufsUser);
                    auditLog.setUserId(ufsUser.getUserId());

                    user.setUserIds(ufsUser.getUserId());
                    String username = wrapper.getUsername();
                    user.setUsername(username);
                    TmsDevice tmsDevice = tmsDeviceRepository.findBySerialNoAndIntrashAndActionStatus(wrapper.getSerialNumber(), "NO", AppConstants.STATUS_APPROVED);
                    // assign user to the outlet which the serial number belongs

                    if(Objects.nonNull(tmsDevice)){
                        try {
                            UfsCustomerOutlet customerOutlet = tmsDevice.getOutletId();
                            user.setOutletIds(customerOutlet.getId());
                        }
                        catch(NullPointerException ex){
                            responseWrapper.setMessage("Device does not have an outlet attached to it");
                            responseWrapper.setCode(400);

                            auditLog.setNotes("Device with Serial No "+tmsDevice.getSerialNo() + " does not have an outlet attached to it");
                            auditLogRepository.save(auditLog);
                            return responseWrapper;
                        }
                    }else {
                        // dont create user since this terminal does not belong
                        responseWrapper.setMessage("No Tms device found with the serial number");
                        auditLog.setNotes("No Tms device found with the serial number "+ wrapper.getSerialNumber());
                        responseWrapper.setCode(404);
                        auditLogRepository.save(auditLog);
                        return responseWrapper;
                    }


                    // in-case no new pos user is created then save ufs user will not be called
                    //save pos user
                    UfsPosUser PosUserAlreadyExists = ufsPosUserRepository.findByUserIds(ufsUser.getUserId());
                    if(Objects.isNull(PosUserAlreadyExists)) {
                        ufsPosUserRepository.save(user);
                        auditLog.setStatus(AppConstants.STATUS_COMPLETED);
                        auditLog.setEntityId(String.valueOf(user.getPosUserId()));
                        auditLog.setNotes("Pos user created successfully");
                        auditLog.setDescription("Pos user " + wrapper.getPhoneNumber() + " created successfully");
                        responseWrapper.setMessage("Pos user created successfully");
                        responseWrapper.setCode(200);
                    }else {
                        responseWrapper.setCode(400);
                        auditLog.setDescription("User with |"+ wrapper.getPhoneNumber()+ "and ID number " +wrapper.getIdNumber()+" has already been created as a POS user");
                        responseWrapper.setMessage("User with | "+ wrapper.getPhoneNumber()+ "and ID number " +wrapper.getIdNumber()+" has already been created as a POS user");
                        auditLogRepository.save(auditLog);
                        return responseWrapper;
                    }
                }

            } catch (IncorrectResultSizeDataAccessException e) {
                e.printStackTrace();
                responseWrapper.setCode(400);
                auditLog.setNotes("A user with |"+ wrapper.getPhoneNumber()+ " | exist");
                responseWrapper.setMessage("Phone number is duplicated");
                auditLogRepository.save(auditLog);
                return responseWrapper;
            }

          } catch (DataIntegrityViolationException ex){
            ex.printStackTrace();
            responseWrapper.setCode(06);
            auditLog.setDescription("System error when trying to create User from terminal with serial No "+wrapper.getSerialNumber());
            responseWrapper.setMessage("Request could not be processed");
        }

        auditLogRepository.save(auditLog);
        return responseWrapper;
    }


    @Override
    @Transactional
    public ResponseWrapper resetPassword( PosUserWrapper wrapper){
        ResponseWrapper responseWrapper =  new ResponseWrapper();
        UfsSysConfig ufsSysConfig = ufsSysConfigRepository.findByEntityAndParameter("Pos Configuration","posPin");

        UfsPosAuditLog auditLog = new UfsPosAuditLog();
        auditLog.setOccurenceTime(new Date());
        auditLog.setActivityType(AppConstants.ACTIVITY_AUTHENTICATION);

        setLogIpAddress(auditLog); // log ip address
        // validate Device details
        ResponseWrapper validate = validatePosRequest(wrapper, true, auditLog);
        if(validate.getError()){
            responseWrapper = validate;
        }else {

            UfsPosUser user = ufsPosUserRepository.findByUsername(wrapper.getUsername());
            String randomPin = RandomStringUtils.random(Integer.parseInt(ufsSysConfig.getValue()),false,true);
            user.setPin(encoder.encode(randomPin));

            user.setActiveStatus(AppConstants.STATUS_INACTIVE);
            user.setPinStatus(AppConstants.PIN_STATUS_INACTIVE);
            responseWrapper.setMessage(randomPin);
            responseWrapper.setCode(200);
            auditLog.setNotes("Reset password successful for "+ wrapper.getUsername());
            auditLog.setEntityId(String.valueOf(user.getPosUserId()));
            auditLog.setStatus(AppConstants.STATUS_COMPLETED);

            responseWrapper.setCode(200);
            auditLog.setNotes("Reset password for "+ wrapper.getUsername() +" was success");
            auditLog.setDescription("Reset password for "+ wrapper.getUsername() +" was success");
            ufsPosUserRepository.save(user);
        }
        //auditLogRepository.save(auditLog);
        return responseWrapper;
    }

    private ResponseWrapper validatePosRequest(PosUserWrapper wrapper, boolean b,UfsPosAuditLog auditLog){
        // common validator method for members that need its
        // TODO: 03/07/2020 : if possible extend to validate for rogue terminal details.
        String posSerialNumber = wrapper.getSerialNumber();
        String posMID =  wrapper.getMID();
        String posTID = wrapper.getTID();
        ResponseWrapper responseWrapper = new ResponseWrapper();

        auditLog.setOccurenceTime(new Date());
        auditLog.setStatus(AppConstants.STATUS_FAILED);
        auditLog.setEntityName("UfsPosUser");
        auditLog.setClientId(AppConstants.CLIENT_ID);
        System.out.println("*******************************");
        System.out.println(wrapper.getUsername());
        UfsPosUser ufsPosUser = ufsPosUserRepository.findByUsernameIgnoreCaseAndIntrash(wrapper.getUsername(), "NO");
        System.out.println(ufsPosUser);
        if(Objects.nonNull(ufsPosUser)){
            // common to all methods so lets set them at a common place
            auditLog.setEntityId(String.valueOf(ufsPosUser.getPosUserId()));
            auditLog.setUserId(ufsPosUser.getUserId().getUserId().longValue());
        }

        try {
            auditLog.setIpAddress(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            auditLog.setNotes("Unknown IP address");
            e.printStackTrace();
        }

        // validation for login
        if(b) {
            if (wrapper.getUsername() == null) {
                responseWrapper.setError(true);
                responseWrapper.setMessage("username cannot be null.");
                auditLog.setDescription("Username is null");
                responseWrapper.setCode(407);
                return responseWrapper;
            }
        }
        // validation for serial number
        if (posSerialNumber == null){
            responseWrapper.setCode(26);
            responseWrapper.setMessage("Serial Number Missing");
            auditLog.setNotes("Serial Number Missing");
            auditLog.setDescription("POS making the request did not send its serial Number");
            responseWrapper.setError(true);
            return responseWrapper;
        }
        // validation for MID
        if (posMID == null){
            responseWrapper.setCode(42);
            responseWrapper.setMessage("Merchant ID Missing");
            auditLog.setDescription("Request from device with serial number "+ wrapper.getSerialNumber() + " did not provide MID");
            auditLog.setNotes("MID is missing");
            responseWrapper.setError(true);
            return responseWrapper;
        }
        // validation for TID
        if (posTID == null){
            responseWrapper.setCode(41);
            responseWrapper.setMessage("Terminal ID Missing");
            auditLog.setDescription("Request from device with serial number "+ wrapper.getSerialNumber() + " did not provide TID");
            auditLog.setNotes("TID is missing");
            responseWrapper.setError(true);
            return responseWrapper;
        }
        //Steps:
        // check if a user exist with the username provided
        // check if a device exist with the supplied serial number
        // If it does do a lookout for the outlet the device belongs to
        // if an out let exist then check if the user belongs to that outlet
        TmsDevice tmsDevice = tmsDeviceRepository.findBySerialNoAndIntrashAndActionStatus(wrapper.getSerialNumber(), "NO", "Approved");

        if(Objects.isNull(ufsPosUser)){
            // common to all methods so lets set them at a common place
            responseWrapper.setMessage("User with username "+wrapper.getUsername() + " Not found");
            auditLog.setNotes("User with username " +wrapper.getUsername() + " Not found");
            responseWrapper.setCode(404);
            responseWrapper.setError(true);
            return responseWrapper;
        }

        if(Objects.nonNull(tmsDevice)){
            if(tmsDevice.getOutletIds()!=null && ufsPosUser.getOutletIds() != null){
                //validate if user belongs to this outlet which the pos terminal belongs
                if(ufsPosUser.getOutletIds()  != tmsDevice.getOutletId().getId()){
                    responseWrapper.setMessage(wrapper.getUsername()+" not authorised to access this terminal");
                    responseWrapper.setCode(403);
                    auditLog.setNotes("access by "+wrapper.getUsername()+"denied");
                    auditLog.setDescription(wrapper.getUsername()+" User not authorised to access terminal with serial number:"+ wrapper.getSerialNumber());
                    responseWrapper.setError(true);
                    return responseWrapper;
                }
            }else{
                responseWrapper.setMessage(tmsDevice.getOutletIds()==null?"Device does not have an outlet attached to it":"User not attached to an outlet");
                responseWrapper.setError(true);
                responseWrapper.setCode(400);
                auditLog.setNotes(tmsDevice.getOutletIds()==null?"Device with Serial No "+tmsDevice.getSerialNo() + " does not have an outlet attached to it":"User not attached to an outlet");
                return responseWrapper;
            }

        }else {

            responseWrapper.setMessage("No Tms device found with the serial number");
            // common to all methods so lets set them at a common place
            auditLog.setNotes("Serial number Not existing");
            auditLog.setDescription("No Tms device found with the serial number "+ wrapper.getSerialNumber());
            responseWrapper.setCode(404);
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

        auditLog.setOccurenceTime(new Date());
        auditLog.setActivityType("Deletion");
        setLogIpAddress(auditLog); // log ip address

        try{

            if(validate.getError()){
                auditLogRepository.save(auditLog); // save any logging up to this point and terminate execution by returning
                return validate;
            }
            UfsPosUser posUser = ufsPosUserRepository.findByUsernameIgnoreCaseAndIntrash(wrapper.getUsername(), "NO");

            posUser.setIntrash("YES");
            posUser.setActiveStatus(AppConstants.STATUS_INACTIVE);
            //UfsCustomerOutlet posCustomerOutlet = posUser.getOutletId();
//            if(posCustomerOutlet != null){
//                Date terminationDate = new Date();
//                UfsCustomer ufsCustomer = posCustomerOutlet.getCustomerId();
//                ufsCustomer.setTerminationReason(wrapper.getTerminationReason());
//                ufsCustomer.setTerminationDate(terminationDate);
//            } else{
//                responseWrapper.setCode(400);
//                responseWrapper.setMessage("User not attached to an outlet");
//            }


        }catch (Exception ex){
            ex.printStackTrace();
            auditLog.setNotes("System Error, User could not be deleted");
            responseWrapper.setMessage("System Error, User could not be deleted");
            responseWrapper.setCode(06);
        }
        auditLog.setStatus(AppConstants.STATUS_COMPLETED);
        responseWrapper.setCode(200);
        responseWrapper.setMessage("User deleted successfully");
        auditLog.setNotes(wrapper.getUsername() +" deleted successfully");
        auditLogRepository.save(auditLog);
        return responseWrapper;
    }
    public ResponseWrapper logout(PosUserWrapper wrapper){
        UfsPosAuditLog auditLog = new UfsPosAuditLog();
        ResponseWrapper validate = validatePosRequest(wrapper, true, auditLog);
        ResponseWrapper responseWrapper = new ResponseWrapper();

        setLogIpAddress(auditLog); // log ip address

        auditLog.setOccurenceTime(new Date());
        auditLog.setActivityType("Logout");


        if (validate.getError()){
            return responseWrapper;
        }
        auditLog.setStatus(AppConstants.STATUS_COMPLETED);
        responseWrapper.setCode(200);
        responseWrapper.setMessage("log out was successful");
        auditLog.setNotes(wrapper.getUsername() +" log out was successful");
        auditLogRepository.save(auditLog);
        return responseWrapper;
    }

}
