package ke.co.tra.ufs.tms.service.templates;

import ke.co.tra.ufs.tms.entities.*;
import ke.co.tra.ufs.tms.repository.*;
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;
import ke.co.tra.ufs.tms.service.NotificationService;
import ke.co.tra.ufs.tms.service.NotifyService;
import ke.co.tra.ufs.tms.service.PosUserService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.wrappers.*;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Service
@CommonsLog
public class PosUserServiceTemplate implements PosUserService {

    private final UfsPosUserRepository ufsPosUserRepository;
    private final UfsUserWorkgroupRepository ufsUserWorkgroupRepository;
    private final UfsWorkgroupRepository workgroupRepository;
    private final UfsDepartmentRepository ufsDepartmentRepository;
    private final UfsUserRepository ufsUserRepository;
    private final UfsGenderRepository ufsGenderRepository;
    private final UfsUserTypeRepository userTypeRepository;
    private final SysConfigRepository sysConfigRepository;
    private final TmsDeviceTidMidRepository tidRepository;
    private final TmsDeviceRepository tmsDeviceRepository;
    private final PasswordEncoder encoder;
    private final LoggerServiceLocal loggerService;

    public PosUserServiceTemplate(UfsPosUserRepository ufsPosUserRepository, UfsUserWorkgroupRepository ufsUserWorkgroupRepository, UfsWorkgroupRepository workgroupRepository, UfsDepartmentRepository ufsDepartmentRepository, UfsUserRepository ufsUserRepository, UfsGenderRepository ufsGenderRepository, UfsUserTypeRepository userTypeRepository, SysConfigRepository sysConfigRepository, TmsDeviceTidMidRepository tidRepository, TmsDeviceRepository tmsDeviceRepository, PasswordEncoder encoder
                                 ,LoggerServiceLocal loggerService) {
        this.ufsPosUserRepository = ufsPosUserRepository;
        this.ufsUserWorkgroupRepository = ufsUserWorkgroupRepository;
        this.workgroupRepository = workgroupRepository;
        this.ufsDepartmentRepository = ufsDepartmentRepository;
        this.ufsUserRepository = ufsUserRepository;
        this.ufsGenderRepository = ufsGenderRepository;
        this.userTypeRepository = userTypeRepository;
        this.sysConfigRepository = sysConfigRepository;
        this.tidRepository = tidRepository;
        this.tmsDeviceRepository = tmsDeviceRepository;
        this.encoder = encoder;
        this.loggerService = loggerService;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper> resetPin(ActionWrapper<BigDecimal> ids) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        ArrayList<String> errors = new ArrayList<>();

        for (BigDecimal id : ids.getIds()) {

            UfsPosUser ufsPosUser = ufsPosUserRepository.findByPosUserIdAndIntrash(id,AppConstants.NO);

            if (ufsPosUser == null) {
                log.info("Pos User Does Not Exist");
                errors.add(id.toString());
                continue;
            }
            ufsPosUser.setActionStatus(AppConstants.STATUS_UNAPPROVED);
            ufsPosUser.setAction(AppConstants.ACTIVITY_RESET_POS_PIN);
            ufsPosUserRepository.save(ufsPosUser);
            loggerService.posPinReset("Pos User Password Reset (" + ufsPosUser.getUsername() + ")",
                    UfsPosUser.class.getSimpleName(), ufsPosUser.getPosUserId(), AppConstants.STATUS_COMPLETED);
        }
        if (errors.size() > 0) {
            responseWrapper.setCode(HttpStatus.MULTI_STATUS.value());
            responseWrapper.setMessage("Pos User Does Not Exist" + errors);
            responseWrapper.setData(errors);
            return new ResponseEntity(responseWrapper, HttpStatus.MULTI_STATUS);
        } else {
            return new ResponseEntity(responseWrapper, HttpStatus.OK);
        }
    }

    @Override
    @Transactional
    public void createPosUser(TmsDevice entity) {
        //pos system configurations
        UfsSysConfig ufsSysConfig = sysConfigRepository.findByEntityAndParameter("Pos Configuration", "posPin");


        //check if user already exists
        /*UfsPosUser posUser = ufsPosUserRepository.findByUsername(entity.getOutletId().);

        if (posUser != null ) {
            return;
        }*/

        //generate random pin
        String randomPin = RandomStringUtils.random(Integer.parseInt(ufsSysConfig.getValue()), false, true);

        log.info("The generated pin is: " + randomPin);

        //get Username
        //String username = entity.getOutletId().getPhoneNumber();

        //create UfsUser
        UfsUser ufsUser = createUfsUser(entity);

        //create the user
        UfsPosUser user = new UfsPosUser();
        // user.setUsername(username);
        user.setPin(encoder.encode(randomPin));
        user.setActiveStatus(AppConstants.STATUS_INACTIVE);
        user.setPinStatus(AppConstants.PIN_STATUS_INACTIVE);
        user.setTmsDeviceId(entity.getDeviceId());

        ufsPosUserRepository.save(user);

        // TODO update the message send to phone number
//        String phoneNumber = entity.getOutletId().getPhoneNumber();
        String message = "You username is: 2547220000000 and your pin is: " + randomPin;

        sendSmsMessage("2547220000000", message);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper> changePin(UserPinWrapper wrapper) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        UfsPosUser ufsPosUser = ufsPosUserRepository.findByUsername(wrapper.getUsername());

        if (ufsPosUser.equals(null)) {
            responseWrapper.setMessage("Username doesnt Exist");
            responseWrapper.setCode(HttpStatus.NOT_FOUND.value());

            return new ResponseEntity(responseWrapper, HttpStatus.NOT_FOUND);
        }

        if (ufsPosUser.getPinStatus().equalsIgnoreCase(AppConstants.PIN_STATUS_INACTIVE) || ufsPosUser.getActiveStatus().equalsIgnoreCase(AppConstants.STATUS_INACTIVE)) {
            responseWrapper.setMessage("Please Login For The First Time");
            responseWrapper.setCode(HttpStatus.BAD_REQUEST.value());

            return new ResponseEntity(responseWrapper, HttpStatus.FORBIDDEN);
        }

        if (!encoder.matches(wrapper.getPin(), ufsPosUser.getPin())) {
            responseWrapper.setCode(HttpStatus.BAD_REQUEST.value());
            responseWrapper.setMessage("Invalid Old Password");
            return new ResponseEntity(responseWrapper, HttpStatus.FORBIDDEN);
        }

        ufsPosUser.setPin(encoder.encode(wrapper.getNewpin()));
        ufsPosUser.setPinChangeDate(Calendar.getInstance().getTime());
        ufsPosUserRepository.save(ufsPosUser);
        responseWrapper.setMessage("Pin reset successfully.");

        return new ResponseEntity(responseWrapper, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper> firstTimeLogin(UserPinWrapper wrapper) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        UfsPosUser ufsPosUser = ufsPosUserRepository.findByUsername(wrapper.getUsername());

        if (!encoder.matches(wrapper.getPin(), ufsPosUser.getPin())) {
            responseWrapper.setCode(HttpStatus.BAD_REQUEST.value());
            responseWrapper.setMessage("Wrong Password");
            return new ResponseEntity(responseWrapper, HttpStatus.FORBIDDEN);
        }

        ufsPosUser.setPinStatus(AppConstants.PIN_STATUS_ACTIVE);
        ufsPosUser.setActiveStatus(AppConstants.STATUS_ACTIVE);
        ufsPosUser.setPinLastLogin(Calendar.getInstance().getTime());

        ufsPosUserRepository.save(ufsPosUser);

        return new ResponseEntity(responseWrapper, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper> lockUser(ActionWrapper<BigDecimal> accounts) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        for (BigDecimal id : accounts.getIds()) {
            UfsPosUser user = ufsPosUserRepository.findById(id).get();
            user.setActiveStatus(AppConstants.PASS_LOCKED_STATUS);
            ufsPosUserRepository.save(user);
        }
        responseWrapper.setMessage("User account(s) locked successfully");

        return new ResponseEntity(responseWrapper, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper> unLockUser(ActionWrapper<BigDecimal> accounts) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        for (BigDecimal id : accounts.getIds()) {
            UfsPosUser user = ufsPosUserRepository.findById(id).get();
            user.setActiveStatus(AppConstants.ACTIVITY_UNLOCK);
            ufsPosUserRepository.save(user);

        }

        responseWrapper.setMessage("User account(s) unlocked successfully");

        return new ResponseEntity(responseWrapper, HttpStatus.OK);
    }

    @Override
    @Transactional
    @Async
    public ResponseEntity<ResponseWrapper> sendSmsMessage(String phoneNumber, String message) {
        ResponseWrapper responseWrapper = new ResponseWrapper();

        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseWrapper> login(UserPinWrapper wrapper) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        UserDetailsWrapper detailsWrapper = new UserDetailsWrapper();
        List<PosUserWrapper> list = new ArrayList<>();
        System.out.println("88888888888888888888888888888888888888888888888" + wrapper.getUsername());

        UfsPosUser ufsPosUser = ufsPosUserRepository.findByUsername(wrapper.getUsername());

        if (ufsPosUser.getActiveStatus().equalsIgnoreCase(AppConstants.PASS_LOCKED_STATUS)) {
            responseWrapper.setCode(HttpStatus.LOCKED.value());
            responseWrapper.setMessage("Account is Locked");
            return new ResponseEntity(responseWrapper, HttpStatus.LOCKED);
        }

        if (!encoder.matches(wrapper.getPin(), ufsPosUser.getPin())) {
            BigInteger attempts = ufsPosUser.getPinLoginAttemtps();

            ufsPosUser.setPinLoginAttemtps((attempts != null) ? attempts.add(new BigInteger("1")) : new BigInteger("1"));

            if (ufsPosUser.getPinLoginAttemtps().intValue() == 3) {
                ufsPosUser.setActiveStatus(AppConstants.PASS_LOCKED_STATUS);
            }

            ufsPosUserRepository.save(ufsPosUser);
            responseWrapper.setCode(HttpStatus.BAD_REQUEST.value());
            responseWrapper.setMessage("Wrong Password");
            return new ResponseEntity(responseWrapper, HttpStatus.FORBIDDEN);
        }

        //reset pin attempts after successfully login
        if (ufsPosUser.getPinLoginAttemtps() != null) {
            ufsPosUser.setPinLoginAttemtps(null);
            ufsPosUserRepository.save(ufsPosUser);
        }

        /*
        //fetch Workgroup
        UfsUserWorkgroup ufsUserWorkgroup = ufsUserWorkgroupRepository.findByUserId(ufsPosUser.getUserIds());

        //fetch all device id by outletCode
        tmsDeviceRepository.findAllByOutletIds(BigDecimal.valueOf(ufsPosUser.getOutletIds()))
                .forEach(x -> {
                    PosUserWrapper userWrapper = new PosUserWrapper();
                    Set<TidMid> midList = new HashSet<>();
                    userWrapper.setSerialnumber(x.getSerialNo());
                    detailsWrapper.setAccountNumber(x.getCustomerId().getAccountNumber());
                    detailsWrapper.setAssistantRole(x.getOutletId().getAssistantRole());
                    detailsWrapper.setContactPerson(x.getOutletId().getContactPerson());
                    detailsWrapper.setCustomerName(x.getCustomerId().getCustomerName());
                    detailsWrapper.setLocalRegNumber(x.getCustomerId().getLocalRegNumber());
                    detailsWrapper.setPhoneNumber(x.getCustomerId().getPhonenumber());
                    detailsWrapper.setOutletCode(x.getOutletId().getOutletCode());
                    detailsWrapper.setIdNumber(x.getOutletId().getIdNumber());
                    detailsWrapper.setOutletName(x.getOutletId().getOutletName());

                    Long devicesIds = x.getDeviceId().longValue();
                    tidRepository.findAllByDeviceIds(devicesIds).forEach(d -> {
                        TidMid details = new TidMid();
                        details.setMids(d.getMid());
                        details.setTids(d.getTid());

                        midList.add(details);
                    });


                    userWrapper.setTidMids(midList);
                    list.add(userWrapper);
                    detailsWrapper.setPosUserWrapper(list);
                    detailsWrapper.setUfsWorkgroup(ufsUserWorkgroup.getWorkgroup());
                });

        responseWrapper.setMessage("Login Successfully");
        responseWrapper.setCode(HttpStatus.OK.value());
        responseWrapper.setData(detailsWrapper); */

        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    @Override
    @Transactional
    public UfsUser createUfsUser(TmsDevice entity) {
        UfsUser user = new UfsUser();
        UfsCustomerOutlet outlet = entity.getOutletId();
        //user.setFullName(outlet.getContactPerson());
        //user.setPhoneNumber(outlet.getPhoneNumber());
        user.setUserTypeId(userTypeRepository.findByUserType(AppConstants.POS_USER_TYPE).getTypeId());
        user.setGenderId(ufsGenderRepository.findByGender(AppConstants.POS_USER_GENDER).getGenderId());
        user.setTenantIds(outlet.getCustomerId().getTenantIds());
        user.setDepartmentIds(ufsDepartmentRepository.findByDepartmentName(AppConstants.POS_AGENT_DEPARTMENT).getId());
        user.setStatus(Short.parseShort("2"));

        UfsUser user1 = ufsUserRepository.save(user);

        //create Workgroup
        UfsWorkgroup ufsWorkgroup = workgroupRepository.findByGroupName(AppConstants.POS_USER_WORKGROUP);

        UfsUserWorkgroup workgroup = new UfsUserWorkgroup();
        workgroup.setGroupId(BigDecimal.valueOf(ufsWorkgroup.getGroupId()));
        workgroup.setUserId(user1.getUserId());

        ufsUserWorkgroupRepository.save(workgroup);

        return user1;
    }

    @Override
    public UfsPosUser findByUsername(String username, BigDecimal deviceId) {
        return ufsPosUserRepository.findByUsernameAndIntrashAndTmsDeviceId(username, AppConstants.NO, deviceId);
    }

    @Override
    public UfsPosUser savePosUser(UfsPosUser ufsPosUser) {
        return ufsPosUserRepository.save(ufsPosUser);
    }

    @Override
    public UfsPosUser findByContactPersonIdAndDeviceId(Long contactPersonId, BigDecimal tmsDeviceId) {
        return ufsPosUserRepository.findByContactPersonIdAndTmsDeviceIdAndIntrash(contactPersonId, tmsDeviceId, AppConstants.NO);
    }

    @Override
    public UfsPosUser findByCustomerOwnersIdAndDeviceId(Long customerOwnersId, BigDecimal tmsDeviceId) {
        return ufsPosUserRepository.findByCustomerOwnersIdAndTmsDeviceIdAndIntrash(customerOwnersId, tmsDeviceId, AppConstants.NO);
    }

    @Override
    public UfsPosUser findByDeviceIdAndFirstTime(BigDecimal tmsDeviceId, Short firstTimeUser) {
        return ufsPosUserRepository.findByTmsDeviceIdAndFirstTimeUserAndIntrash(tmsDeviceId,firstTimeUser,AppConstants.NO);
    }


    @Override
    public Page<UfsPosUser> getPosUsers(String action, String actionStatus, String needle, Pageable pg) {
         return ufsPosUserRepository.findAll(action, actionStatus, needle, AppConstants.NO, pg);
    }

    @Override
    public Page<UfsPosUser> getPosUsers(String action, String actionStatus, String needle, BigDecimal tmsDeviceId, Long customerOwnersId, Pageable pg) {
        return ufsPosUserRepository.findAll(action, actionStatus, needle, AppConstants.NO,tmsDeviceId,customerOwnersId, pg);
    }

    @Override
    public UfsPosUser findByPosUserId(BigDecimal posUserId) {
        return ufsPosUserRepository.findByPosUserIdAndIntrash(posUserId,AppConstants.NO);
    }

    @Override
    public List<UfsPosUser> findByContactPersonId(Long contactPersonId) {
        return ufsPosUserRepository.findByContactPersonIdAndIntrash(contactPersonId,AppConstants.NO);
    }

}
