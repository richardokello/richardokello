/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.resources;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ke.axle.chassis.exceptions.ExpectationFailed;
import ke.axle.chassis.exceptions.GeneralBadRequest;
import ke.axle.chassis.exceptions.NotFoundException;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tracom.ufs.entities.*;
import ke.tracom.ufs.entities.wrapper.OtpConfigWrapper;
import ke.tracom.ufs.repositories.*;
import ke.tracom.ufs.security.CustomUserDetails;
import ke.tracom.ufs.security.OtpOAuth2AccessToken;
import ke.tracom.ufs.services.*;
import ke.tracom.ufs.services.template.FileStorageService;
import ke.tracom.ufs.services.template.LoggerServiceTemplate;
import ke.tracom.ufs.services.template.NotifyServiceTemplate;
import ke.tracom.ufs.services.template.UserAccountService;
import ke.tracom.ufs.utils.AppConstants;
import ke.tracom.ufs.utils.PasswordGenerator;
import ke.tracom.ufs.wrappers.ChangePasswordWrapper;
import ke.tracom.ufs.wrappers.OtpResponse;
import ke.tracom.ufs.wrappers.ResetPasswordWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author cornelius
 * @sub-author eli
 * @sub-author kmwangi
 */
@RestController
public class AuthorizationResource {

    private final TokenStore tokenStore;
    private final UserService userService;
    private final UserRepository userRepository;
    private final LoggerServiceTemplate loggerService;
    private final PasswordEncoder encoder;
    private final CustomUserService customUserService;
    private final AuthenticationRepository authRepository;
    private final NotifyServiceTemplate notifyService;
    private final OTPRepository otpRepository;

    private final WorkGroupService workGroupService;
    private final UserWorkGroupService userWorkGroupService;

    private final AuditLogService auditLogService;

    private final OauthAccessTokenRepository oauthAccessTokenRepository;

    @Autowired
    UserAccountService accService;
    @Autowired
    PasswordGenerator passGen;
    @Autowired
    UfsSysConfigRepository configRepo;
    @Autowired
    UserRepository urepo;
    @Autowired
    UfsUserTypeRepository typeRepository;
    @Autowired
    FileStorageService sservice;
    @Autowired
    private AuthTokenReplication authTokenReplicationService;

    public AuthorizationResource(TokenStore tokenStore, @Qualifier("mainUserService") UserService userService,
                                 UserRepository userRepository, LoggerServiceTemplate loggerService, PasswordEncoder encoder, CustomUserService customUserService, AuthenticationRepository authRepository,
                                 NotifyServiceTemplate notifyService, OTPRepository otpRepository, WorkGroupService workGroupService, UserWorkGroupService userWorkGroupService, AuditLogService auditLogService,OauthAccessTokenRepository oauthAccessTokenRepository) {
        this.tokenStore = tokenStore;
        this.userService = userService;
        this.userRepository = userRepository;
        this.loggerService = loggerService;
        this.encoder = encoder;
        this.customUserService = customUserService;
        this.authRepository = authRepository;
        this.notifyService = notifyService;
        this.otpRepository = otpRepository;
        this.workGroupService = workGroupService;
        this.userWorkGroupService = userWorkGroupService;
        this.auditLogService = auditLogService;
        this.oauthAccessTokenRepository = oauthAccessTokenRepository;
    }

    //@RequestMapping("/test")
    public ResponseEntity<ResponseWrapper> getUser(Authentication a) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(a);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/otp/verification", method = RequestMethod.POST)
    @ApiOperation(value = "Verify OTP", notes = "Used to handle OTP Verification. Returns permissions after verifying permissions")
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Invalid or expired OTP"),
            @ApiResponse(code = 423, message = "Account locked due to many attempts")
    })
    public ResponseEntity<ResponseWrapper<OtpResponse>> OTPVerification(OAuth2Authentication a, @RequestParam("otp") String otp) {
        ResponseWrapper response = new ResponseWrapper();
//        //CmsUser user = userService.findByUsername(a.getName());
        String otpAttempts = configRepo.getConfiguration("Password Policy", "otpAttempts").getValue();
        int result = Integer.parseInt(otpAttempts);
        CustomUserDetails user = (CustomUserDetails) a.getPrincipal();
        OAuth2AccessToken token = tokenStore.getAccessToken(a);
        String tenantIds = authRepository.findByusernameIgnoreCase(a.getName()).getUser().getTenantIds();
        String userType = authRepository.findByusernameIgnoreCase(a.getName()).getUser().getUserType().getUserType();
        String countyId = null;
        String customerId = authRepository.findByusernameIgnoreCase(a.getName()).getUser().getCustomerId().toString();
        if (Objects.nonNull(authRepository.findByusernameIgnoreCase(a.getName()).getUser().getCountyId())) {
            countyId = authRepository.findByusernameIgnoreCase(a.getName()).getUser().getCountyId();
        }
        try {
            UfsUser ufsUser = userRepository.findByuserId(Long.valueOf(user.getUserId()));
            if (ufsUser.getStatus() == AppConstants.STATUS_LOCKED) {
                throw new LockedException("Account is locked");
            }

            Integer expiry = Integer.valueOf(configRepo.getConfiguration(AppConstants.ENTITY_PASSWORD_POLICY,
                    AppConstants.PARAMETER_OTP_EXPIRY).getValue());
            userService.authenticateOTP(otp, user.getUserId().toString(), expiry);
            OAuth2AccessToken otpToken = new OtpOAuth2AccessToken(token);
            tokenStore.removeAccessToken(token);
            tokenStore.storeAccessToken(otpToken, a);

            // check that the user has multi-tenancy permission and if yes, then replicate the
            // authentication token to all the other schemas. This ensures that the user is not
            // de-authenticated when selecting a different schema on the UI.

            // check for multi-tenant superuser role.
            boolean isSuperUser = a.getAuthorities().contains(new SimpleGrantedAuthority("MULTITENANCY PERMISSION"));

            // todo uncomment
            if (isSuperUser) {
                System.err.println("** found multi-tenant superuser. Replicating authentication token...");
                // get the username of the authenticated user.
                String username = ((UserDetails) a.getPrincipal()).getUsername();
                // replicate the authentication token to other schemas
                authTokenReplicationService.replicateAuthToken(username);
            }

            OtpResponse data = new OtpResponse();
            data.permissions = a.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
            data.userDetails = user;
            data.tenantIds = tenantIds;
            data.userType = userType;
            data.countyId = countyId;
            data.customerId=customerId;



            response.setData(data);

            //reset Otp attempts
            UfsOtp ufsOtp = otpRepository.findByUserId(user.getUserId().toString());

            if (Objects.nonNull(ufsOtp)) {
                if (Objects.nonNull(ufsOtp.getOtpAttempts())) {
                    ufsOtp.setOtpAttempts(null);
                    otpRepository.save(ufsOtp);
                }
            }

            loggerService.log("OTP verified successfully", UfsOtp.class.getSimpleName(), null, user.getUserId(),
                    AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.STATUS_COMPLETED, "OTP verified successfully");

            return new ResponseEntity(response, HttpStatus.OK);
        } catch (NotFoundException ex) {

            UfsOtp ufsOtp = otpRepository.findByUserId(user.getUserId().toString());
            System.out.println(ufsOtp);

            if (Objects.nonNull(ufsOtp)) {

                if (Objects.nonNull(ufsOtp.getOtpAttempts())) {
                    if (ufsOtp.getOtpAttempts() == 0) {
                        UfsUser ufsUser = userRepository.findByuserId(Long.valueOf(ufsOtp.getUserId()));
                        ufsUser.setStatus(AppConstants.STATUS_LOCKED);
                        userRepository.save(ufsUser);

                        loggerService.log("Account Locked for user  " + a.getName() + " due to wrong OTP",
                                UfsOtp.class.getSimpleName(), null, authRepository.findByusernameIgnoreCase(a.getName()).getUserId(), AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.ACTIVITY_DECLINE, "Wrong OTP");

                        otpRepository.delete(ufsOtp);
                        response.setCode(401);
                        response.setMessage("Account Locked for user  " + a.getName() + " due to wrong OTP");
                        return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
                    }
                }

                if (Objects.isNull(ufsOtp.getOtpAttempts())) {
                    ufsOtp.setOtpAttempts((short) (result - 1));
                    otpRepository.save(ufsOtp);
                } else {
                    ufsOtp.setOtpAttempts((short) (ufsOtp.getOtpAttempts() - 1));
                    otpRepository.save(ufsOtp);
                }
            }

            loggerService.log("OTP Authentication failed for user  " + a.getName() + " due to wrong OTP " + ufsOtp.getOtpAttempts() + " attempts left",
                    UfsOtp.class.getSimpleName(), null, authRepository.findByusernameIgnoreCase(a.getName()).getUserId(), AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.ACTIVITY_DECLINE, "Wrong OTP");
            response.setCode(401);
            response.setMessage("OTP Authentication failed for user  " + a.getName() + " due to wrong OTP " + ufsOtp.getOtpAttempts() + " attempts left");
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
        } catch (ExpectationFailed ex) {
            response.setMessage(ex.getMessage());
            response.setCode(401);
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
        } catch (LockedException ex) {
            response.setMessage(ex.getMessage());
            response.setCode(401);
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/encode")
    protected ResponseEntity<ResponseWrapper> encodePassword(@RequestParam("password") String password) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(encoder.encode(password));
        return ResponseEntity.ok(response);
    }

    @Transactional
    @RequestMapping(value = "/otp/resend", method = RequestMethod.GET)
    @ApiOperation(value = "Resend OTP", notes = "Used to resend otp to user")
    public ResponseEntity<ResponseWrapper> resendOtp(Authentication a) {
        ResponseWrapper response = new ResponseWrapper();
        UfsAuthentication dbAuth = authRepository.findByusernameIgnoreCase(a.getName());
        otpRepository.deleteByuserIdAndOtpCategory(dbAuth.getUserId().toString(), UfsOtpCategory.AUTH_OTP);
        String code = customUserService.generateOTP(dbAuth.getUser(), UfsOtpCategory.AUTH_OTP);
        try {
            this.notifyService.sendEmail(dbAuth.getUsername(), "One Time Password", "OTP: " + code);
            this.notifyService.sendSms(dbAuth.getUser().getPhoneNumber(), "OTP: " + code);
        } catch (Exception e) {
            String error = "OTP Resend failed due to smtp/mail server configurations";
            loggerService.log(error, UfsOtp.class.getSimpleName(), null, null,
                    AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.ACTIVITY_STATUS_FAILED, error);
            response.setMessage(error);
            return new ResponseEntity(error + " " + e.getMessage(), HttpStatus.MULTI_STATUS);
        }
        response.setMessage("OTP resent successfully");
        loggerService.log("OTP resent successfully", UfsAuthentication.class.getSimpleName(), dbAuth.getAuthenticationId(), dbAuth.getUserId(),
                AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.STATUS_COMPLETED, "OTP resent successfully");

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    @ApiOperation(value = "Reset user password at login", notes = "Used when --forgot password-- is used. "
            + "Fetches user by email, resets password and send to email")
    public ResponseEntity<ResponseWrapper> forgotPassword(@RequestParam("email") String email) {
        String phoneNo = configRepo.getConfiguration("RA Bio", "raPhoneNumber").getValue();
        ResponseWrapper response = new ResponseWrapper();
        UfsAuthentication dbAuth = authRepository.findByusername(email);

        if (dbAuth != null) {
            String password = passGen.generateRandomPassword();
            dbAuth.setPassword(encoder.encode(password));
            dbAuth.setPasswordStatus(AppConstants.STATUS_EXPIRED);
            authRepository.save(dbAuth);
            try {
                this.notifyService.sendEmail(dbAuth.getUsername(), "PASSWORD CHANGE REQUEST", "Use this  generate password to access your account: " + password + " If this wasn't you, kindly contact us on " + phoneNo);
                this.notifyService.sendSms(dbAuth.getUser().getPhoneNumber(), "Use this  generate password to access your account: " + password);
            } catch (Exception e) {
                String error = "Password reset failed due to smtp/mail server configurations";
                loggerService.log(error, UfsAuthentication.class.getSimpleName(), null, null,
                        AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.ACTIVITY_STATUS_FAILED, error);
                response.setMessage(error);
                return new ResponseEntity(error + " " + e.getMessage(), HttpStatus.MULTI_STATUS);
            }

            loggerService.log("Password reset successfully ", UfsAuthentication.class.getSimpleName(), dbAuth.getAuthenticationId(), dbAuth.getUserId(),
                    AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.STATUS_COMPLETED, "Password reset successfully");
            response.setMessage("Password reset successfully. Check your email for new credentials");

            return new ResponseEntity(response, HttpStatus.OK);

        }
        response.setCode(404);
        response.setMessage("Provided email address doesnt exist. Check if the email provided is correct");
        loggerService.log("Password Reset Failed,Provided Email doesnt exist " + email, UfsAuthentication.class.getSimpleName(), null, null,
                AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.ACTIVITY_STATUS_FAILED, "Provided Email doesnt exist");

        return new ResponseEntity(response, HttpStatus.OK);

    }

    @Transactional
    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    @ApiOperation(value = "Change user password", notes = "Used when user is already logged in.")
    public ResponseEntity<ResponseWrapper> resetPassword(Authentication a, @Valid ResetPasswordWrapper reset) throws GeneralBadRequest {
        ResponseWrapper response = new ResponseWrapper();
        UfsAuthentication dbAuth = authRepository.findByusernameIgnoreCase(a.getName()); // find user

        if (!encoder.matches(reset.getOldPassword(), dbAuth.getPassword())) { //password matches
            response.setCode(400);
            response.setMessage("Invalid Old Password");
            loggerService.log("Changing Password Failed,Invalid Old Password Provided", UfsAuthentication.class.getSimpleName(), dbAuth.getAuthenticationId(), dbAuth.getUserId(),
                    AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.ACTIVITY_STATUS_FAILED, "Invalid Old Password");

            return new ResponseEntity(response, HttpStatus.FORBIDDEN);

        }

        //to check password meets policy
        if (accService.isPasswordValid(reset.getNewPassword(), dbAuth.getUser()) == false) {
            response.setCode(400);
            return new ResponseEntity(response, HttpStatus.FORBIDDEN);

        }
        dbAuth.setPassword(encoder.encode(reset.getNewPassword())); //set encoded password
        authRepository
                .save(dbAuth);

        response.setMessage("Password reset successfully. You can now login to your account");
        loggerService.log("Password changed successfully ", UfsAuthentication.class.getSimpleName(), dbAuth.getAuthenticationId(), dbAuth.getUserId(),
                AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.STATUS_COMPLETED, "Password changed successfully");

        return new ResponseEntity(response, HttpStatus.OK);

    }

    @Transactional
    @RequestMapping(value = "/change-password/first-time", method = RequestMethod.POST)
    @ApiOperation(value = "Change user password", notes = "Used to change user  password when user doesnt have a token.")
    public ResponseEntity<ResponseWrapper> changePassword(@Valid ChangePasswordWrapper changePassword) throws GeneralBadRequest {
        ResponseWrapper response = new ResponseWrapper();
        UfsAuthentication dbAuth = authRepository.findByusernameIgnoreCase(changePassword.getEmail());

        if (dbAuth == null) {
            response.setCode(404);
            response.setMessage("Provided Email doesnt exist");
            loggerService.log("Changing First Time Password Failed,Provided Email doesnt exist " + changePassword.getEmail(), UfsAuthentication.class.getSimpleName(), null, null,
                    AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.ACTIVITY_STATUS_FAILED, "Provided Email doesnt exist");

            return new ResponseEntity(response, HttpStatus.NOT_FOUND);

        }

        //password matches
        if (!encoder.matches(changePassword.getOldPassword(), dbAuth.getPassword())) {
            response.setCode(400);
            response.setMessage("Invalid Old Password");

            loggerService.log("Changing First Time Password Failed,Invalid Old Password Provided", UfsAuthentication.class.getSimpleName(), dbAuth.getAuthenticationId(), dbAuth.getUserId(),
                    AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.ACTIVITY_STATUS_FAILED, "Invalid Old Password");

            return new ResponseEntity(response, HttpStatus.FORBIDDEN);

        }
        if (!accService.isPasswordValid(changePassword.getNewPassword(), dbAuth.getUser())) { //to check password meets policy
            response.setCode(400);
            response.setMessage("Password must have atleast 1 special character, 1 uppercase letter, 1 lowercase letter and 1 number. Kindly input a password meeting the policy"); //to fetch the policy from db
            loggerService.log("Changing First Time Password Failed,Password Requirements Not Met", UfsAuthentication.class.getSimpleName(), dbAuth.getAuthenticationId(), dbAuth.getUserId(),
                    AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.ACTIVITY_STATUS_FAILED, "Password Requirements Not Met");

            return new ResponseEntity(response, HttpStatus.FORBIDDEN);
        }
        String encodedPassword = encoder.encode(changePassword.getNewPassword());
        dbAuth.setPassword(encodedPassword); //set encoded password
        dbAuth.setPasswordStatus(AppConstants.STATUS_ACTIVE);
        authRepository.save(dbAuth);

        // replicate user information if the user belongs to superuser workgroup
        List<UfsUserWorkgroup> userWorkgroupList = userWorkGroupService.findAllByUserId(dbAuth.getUserId());
        userWorkgroupList.forEach(ufsUserWorkgroup -> {
            BigDecimal workGroupId = ufsUserWorkgroup.getGroupId();
            String workGroupName = workGroupService.findWorkgroupById(workGroupId.longValue()).getGroupName();
            if (workGroupName.equalsIgnoreCase(AppConstants.SUPERVIEWER)) {
                userService.replicateUserCredentialsInfo(dbAuth.getUsername(), encodedPassword, dbAuth.getPasswordStatus());
            }
        });
        response.setMessage("Password changed successfully. You can now login to your account");
        loggerService.log("First Time Password changed successfully ", UfsAuthentication.class.getSimpleName(), dbAuth.getAuthenticationId(), dbAuth.getUserId(),
                AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.STATUS_COMPLETED, "First Time Password changed successfully");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/me")
    public ResponseEntity<ResponseWrapper> user(Principal principal) {
        ResponseWrapper wrapper = new ResponseWrapper();
        if (principal == null) {
            wrapper.setCode(401);
            wrapper.setMessage("Unauthorized");
            return new ResponseEntity(wrapper, HttpStatus.UNAUTHORIZED);

        }
        wrapper.setMessage("Logged in user ");
        wrapper.setData(authRepository.findByusernameIgnoreCase(principal.getName()).getUser());
        return ResponseEntity.ok(wrapper);
    }

    @GetMapping("/profile-picture/{filename:.+}")
    public ResponseEntity<Resource> getProfilePicture(@PathVariable("filename") String filename) {
        Resource file;
        try {
            file = sservice.loadFileAsResource(filename);
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .contentType(MediaType.IMAGE_PNG)
                .contentType(MediaType.APPLICATION_PDF)
                .body(file);
    }

    //used by other services requiring OTP configuration
    @RequestMapping(value = "/otp-configs", method = RequestMethod.GET)
    @ApiOperation(value = "Verify OTP", notes = "Used to return number of OTP attenpts and expiry time")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public ResponseEntity<ResponseWrapper<OtpConfigWrapper>> otpConfigs() {
        ResponseWrapper response = new ResponseWrapper();
        String otpAttempts = configRepo.getConfiguration("Password Policy", "otpAttempts").getValue();
        int result = Integer.parseInt(otpAttempts);
        Integer expiry = Integer.valueOf(configRepo.getConfiguration(AppConstants.ENTITY_PASSWORD_POLICY,
                AppConstants.PARAMETER_OTP_EXPIRY).getValue());
        OtpConfigWrapper otpConfigWrapper = new OtpConfigWrapper();
        otpConfigWrapper.setExpiry(expiry);
        otpConfigWrapper.setOtpAttempts(otpAttempts);

        response.setData(otpConfigWrapper);
        response.setCode(200);
        response.setMessage("Otp Configs Returned Successfully");

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/logout", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<OtpResponse>> logout(Authentication auth) throws InterruptedException {
        ResponseWrapper response = new ResponseWrapper();
        OAuth2Authentication a = (OAuth2Authentication) auth;
        OAuth2AccessToken accessToken = tokenStore.getAccessToken(a);
        tokenStore.removeAccessToken(accessToken);


        UfsAuthentication ufsAuthentication = authRepository.findByusernameIgnoreCase(a.getName());
        // TODO this log causes race condition: check on UfsAuditLogRepository for more info - OpenID Connect should be implemented to resolve this issue
//        loggerService.log("Logged out successfully", UfsAuthentication.class.getSimpleName(), ufsAuthentication.getAuthenticationId(), ufsAuthentication.getUserId(),
//                AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.STATUS_COMPLETED, "Logged out successfully");

        auditLogService.createLog(createNew(ufsAuthentication));

        response.setMessage("Logged out successfully");
        return new ResponseEntity(response, HttpStatus.OK);
    }


    private UfsAuditLog createNew(UfsAuthentication authentication) {
        UfsAuditLog log = new UfsAuditLog();
        log.setEntityId(authentication.getAuthenticationId().toString());
        log.setUser(authentication.getUserId());
        log.setEntityName(UfsAuthentication.class.getSimpleName());
        log.setActivityType(AppConstants.ACTIVITY_AUTHENTICATION);
        log.setStatus(AppConstants.STATUS_COMPLETED);
        log.setActivityType(AppConstants.ACTIVITY_AUTHENTICATION);
        log.setDescription("Logged out successfully");
        return log;
    }
    @Transactional
    @RequestMapping(value = "/user/logout/login-time", method = RequestMethod.POST)
    @ApiOperation(value = "Logout user at login", notes = "Used when --logout-- is used. "
            + "Fetches user by email, clears already logged in user token")
    public ResponseEntity<ResponseWrapper> logoutDuringLogin(@RequestParam("email") String email) {
        ResponseWrapper response = new ResponseWrapper();
        oauthAccessTokenRepository.deleteAllByUsername(email);

        UfsAuthentication ufsAuthentication = authRepository.findByusernameIgnoreCase(email);
        loggerService.log("Logged out successfully", UfsAuthentication.class.getSimpleName(), ufsAuthentication.getAuthenticationId(), ufsAuthentication.getUserId(),
                AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.STATUS_COMPLETED, "Logged out successfully");

        response.setMessage("Logged out successfully.Please Login again");
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
