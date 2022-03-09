/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.services.template;

import com.netflix.discovery.converters.Auto;
import ke.axle.chassis.exceptions.ExpectationFailed;
import ke.axle.chassis.exceptions.NotFoundException;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tracom.ufs.config.multitenancy.ThreadLocalStorage;
import ke.tracom.ufs.entities.*;
import ke.tracom.ufs.repositories.*;
import ke.tracom.ufs.services.UserService;
import ke.tracom.ufs.services.UserWorkGroupService;
import ke.tracom.ufs.services.WorkGroupService;
import ke.tracom.ufs.utils.AppConstants;
import ke.tracom.ufs.utils.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

/**
 * Performs various user related tasks such as
 * <ul>
 * <li> Validate OTP</li>
 * </ul>
 *
 * @author eli.muraya
 */
@Service
@Transactional
@Qualifier("mainUserService")
public class UserServiceTemplate implements UserService {

    private final OTPRepository otpRepo;
    private final PasswordEncoder passEncoder;
    private final UserRepository userRepo;
    private final AuthenticationRepository authRepo;
    private final UfsUserWorkgroupRepository userWorkgroupRepo;
    private final UfsUserWorkgroupRepository usrWorkgroup;
    private final UfsUserTypeRepository typeRepo;
    private final UserRepository urepo;
    private final PasswordGenerator gen;
    private final PasswordEncoder passwordEncoder;
    private final UfsAuthTypeRepository authtyperepo;
    private final FileStorageService fileStorageService;
    private final NotifyServiceTemplate notifyService;
    private final UserWorkGroupService userWorkGroupService;
    private final WorkGroupService workGroupService;
    private final UserService userService;

    @Autowired
    DataSource dataSource;


    public UserServiceTemplate(OTPRepository otpRepo, PasswordEncoder passEncoder, UserRepository userRepo, AuthenticationRepository authRepo, UfsUserWorkgroupRepository userWorkgroupRepo,
                               UfsUserWorkgroupRepository usrWorkgroup, UfsUserTypeRepository typeRepo, UserRepository urepo, PasswordGenerator gen, PasswordEncoder passwordEncoder,
                               UfsAuthTypeRepository authtyperepo, FileStorageService fileStorageService, NotifyServiceTemplate notifyService, @Lazy UserWorkGroupService userWorkGroupService,
                               @Lazy WorkGroupService workGroupService, @Lazy UserService userService) {
        this.otpRepo = otpRepo;
        this.passEncoder = passEncoder;
        this.userRepo = userRepo;
        this.authRepo = authRepo;
        this.userWorkgroupRepo = userWorkgroupRepo;
        this.usrWorkgroup = usrWorkgroup;
        this.typeRepo = typeRepo;
        this.urepo = urepo;
        this.gen = gen;
        this.passwordEncoder = passwordEncoder;
        this.authtyperepo = authtyperepo;
        this.fileStorageService = fileStorageService;
        this.notifyService = notifyService;
        this.userService = userService;
        this.userWorkGroupService = userWorkGroupService;
        this.workGroupService = workGroupService;
    }

    @Override
    public void authenticateOTP(String code, String userId, Integer expiry) throws NotFoundException, ExpectationFailed {
        List<UfsOtp> authOtp = otpRepo.findByuserIdAndOtpCategory(userId, UfsOtpCategory.AUTH_OTP);
        if (authOtp.size() < 1) {
            UfsUser user = urepo.findByuserId(Long.valueOf(userId));
            if (Objects.nonNull(user)) {
                if (user.getStatus() == AppConstants.STATUS_LOCKED) {
                    throw new LockedException("User account is Locked");
                }
            } else {
                throw new NotFoundException("Sorry invalid OTP Code");
            }
        } else {
            if (!this.passEncoder.matches(code, authOtp.get(0).getOtp())) {
                throw new NotFoundException("Sorry invalid OTP Code");
            }
            //check if otp has expired
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(authOtp.get(0).getCreationDate());
            calendar.add(Calendar.SECOND, expiry);
            if (new Date().after(calendar.getTime())) {
                throw new ExpectationFailed("Sorry OTP has already expired");
            }
            //clear the otp
            otpRepo.delete(authOtp.get(0));
        }
    }

    @Override
    public ResponseWrapper editUser(UfsUser user) {
        /**
         * check if email is editted
         * edit user workgroups
         * */
        UfsUser originalUser = userRepo.findByuserId(user.getUserId());
        if (user.getEmail().equals(originalUser.getEmail())) {
//            continue editing
            originalUser.setFullName(user.getFullName());
            originalUser.setEmail(user.getEmail());
            originalUser.setPhoneNumber(user.getPhoneNumber());
            originalUser.setDateOfBirth(user.getDateOfBirth());
            originalUser.setGenderId(user.getGenderId());


        }
        return null;
    }

    @Override
    public UfsUser findByUserId(Long id) {

        return userRepo.findByUserId(id);
    }

    @Override
    public UfsUser findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public UfsUser findByUserIdAndIntrash(Long id) {
        return userRepo.findByUserIdAndIntrash(id, ke.axle.chassis.utils.AppConstants.NO);
    }

    @Override
    public UfsUser findByfullName(String name) {

        return userRepo.findByfullName(name);
    }

    @Override
    public void save(UfsUser there) {

        userRepo.save(there);
    }

    @Override
    public Optional<UfsUser> findById(Long id) {

        return userRepo.findById(id);
    }

    @Override
    @Async
    public void processApprove(List<UfsUser> users) {
        users.parallelStream().forEach(isThere -> {
            if (isThere.getAction().equals(AppConstants.ACTIVITY_CREATE)) {
                String password = gen.generateRandomPassword();
                String encodedPassword = passwordEncoder.encode(password);

                UfsAuthentication auth = authRepo.findByuserId(isThere.getUserId());
                System.out.println("User Object>>>>>  " + isThere);
                System.out.println("Authentication record == null " + (auth == null));
                auth.setPassword(encodedPassword);
                auth.setPasswordStatus(AppConstants.STATUS_EXPIRED);
                authRepo.save(auth);

                isThere.setActionStatus(AppConstants.STATUS_APPROVED);
                isThere.setAction(AppConstants.ACTIVITY_APPROVE);
                isThere.setStatus(AppConstants.STATUS_ACTIVE);
                urepo.save(isThere);

                // replicate user information if the user belongs to superuser workgroup
                List<UfsUserWorkgroup> userWorkgroupList = userWorkGroupService.findAllByUserId(isThere.getUserId());
                userWorkgroupList.forEach(ufsUserWorkgroup -> {
                    BigDecimal workGroupId = ufsUserWorkgroup.getGroupId();
                    String workGroupName = workGroupService.findWorkgroupById(workGroupId.longValue()).getGroupName();
                    if (workGroupName.equalsIgnoreCase(AppConstants.SUPERVIEWER)) {
                        userService.replicateUserInfo(isThere.getEmail(), encodedPassword); // send the password so it update user with the new password
                    }
                });
                this.notifyService.sendEmail(auth.getUsername(), "LOGIN CREDENTIALS", "Use the following credentials to login: Username : " + auth.getUsername() + " \n \nPassword : " + password);
                //todo uncomment this.notifyService.sendSms(auth.getUser().getPhoneNumber(), "Use the following credentials to login: Username : " + auth.getUsername() + " \n \nPassword : " + password);
            }
        });
    }

    @Override
    public UfsAuthentication findByusernameIgnoreCase(String username) {
        return authRepo.findByusernameIgnoreCase(username);
    }

    @Override
    public UfsAuthentication findByusername(String username) {
        return authRepo.findByusername(username);
    }

    @Override
    public Page<UfsUser> fetchUsersExclude(UfsUser user, String actionStatus, Date from, Date to, String needle, Pageable pg) {
        return userRepo.findAll(actionStatus, from, to, needle.toLowerCase(), AppConstants.NO, user, pg);
    }

    @Override
    public Page<UfsUser> fetchUsersExcludes(UfsUser user, String actionStatus, Date from, Date to, String needle, Short status, Pageable pg) {
        return userRepo.findAllByStatus(actionStatus, from, to, needle.toLowerCase(), AppConstants.NO, user, status, pg);
    }

    @Override
    public Page<UfsUser> fetchUsersAction(UfsUser user, String actionStatus, Date from, Date to, String needle, String action, Pageable pg) {
        return userRepo.findAllByAction(actionStatus, from, to, needle.toLowerCase(), AppConstants.NO, user, action, pg);
    }

    @Override
    public Page<UfsUser> fetchUsersUserType(UfsUser user, String actionStatus, Date from, Date to, String needle, BigDecimal userTypeId, Pageable pg) {
        return userRepo.findAllByUserTypeId(actionStatus, from, to, needle.toLowerCase(), AppConstants.NO, user, userTypeId, pg);
    }

    @Override
    public UfsAuthentication findByuserId(Long userId) {
        return authRepo.findByuserId(userId);
    }

    @Override
    public UfsAuthentication findByauthenticationId(Long id) {
        return authRepo.findByauthenticationId(id);
    }

    @Override
    @Async
    public void replicateUserInfo(String email, String password) {
        try {
            System.out.println(">>>>>>>>>>>> Replicating user info: procedure calling >>>> tenant id >>>> " + ThreadLocalStorage.getTenantName()
             + " >>>>> schema " + dataSource.getConnection().getSchema());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        userRepo.replicateUserInfo(email, password);
    }

    @Override
    public void replicateUserCredentialsInfo(String email, String hashedPassword, int status) {
        try {
            System.out.println(">>>>>>>>>>>> Replicating user info: procedure calling >>>> tenant id >>>> " + ThreadLocalStorage.getTenantName()
                    + " >>>>> schema " + dataSource.getConnection().getSchema());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        userRepo.replicateUserCredentialsInfo(email, hashedPassword, status);
    }

    @Override
    public UfsAuthentication saveAuthentication(UfsAuthentication authentication) {
        return authRepo.save(authentication);
    }
}
