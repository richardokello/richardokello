package ke.tracom.ufs.config;


import ke.tracom.ufs.entities.UfsAuthentication;
import ke.tracom.ufs.entities.UfsOtpCategory;
import ke.tracom.ufs.entities.UfsUser;
import ke.tracom.ufs.repositories.UserRepository;
import ke.tracom.ufs.services.AuthTokenReplication;
import ke.tracom.ufs.services.SysConfigService;
import ke.tracom.ufs.services.template.LoggerServiceTemplate;
import ke.tracom.ufs.services.template.NotifyServiceTemplate;
import ke.tracom.ufs.repositories.AuthenticationRepository;
import ke.tracom.ufs.repositories.OTPRepository;
import ke.tracom.ufs.services.CustomUserService;
import ke.tracom.ufs.utils.AppConstants;
import ke.tracom.ufs.utils.enums.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Date;

@Component
@Transactional(noRollbackFor = {BadCredentialsException.class})
public class AppAuthenticationProvider extends DaoAuthenticationProvider {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final AuthenticationRepository authRepository;
    private final UserRepository userRepository;
    private final OTPRepository otpRepository;
    private final CustomUserService userService;
    private final NotifyServiceTemplate notifyService;
    private final LoggerServiceTemplate loggerService;
    private final UserDetailsService userDetailsService;
    private final SysConfigService configService;
    private final AuthTokenReplication authTokenReplicationService;

    public AppAuthenticationProvider(@Qualifier("userDetailsServiceTemplate") UserDetailsService userDetailsService,
                                     AuthenticationRepository authRepository, PasswordEncoder passwordEncoder,
                                     UserRepository userRepository, OTPRepository otpRepository, @Qualifier("commonUserService") CustomUserService userService, NotifyServiceTemplate notifyService,
                                     LoggerServiceTemplate loggerService,AuthTokenReplication authTokenReplicationService, SysConfigService configService) {
        super();
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.configService = configService;
        super.setUserDetailsService(userDetailsService);
        this.authRepository = authRepository;
        super.setPasswordEncoder(passwordEncoder);
        this.otpRepository = otpRepository;
        this.userService = userService;
        this.notifyService = notifyService;
        this.loggerService = loggerService;
        this.authTokenReplicationService = authTokenReplicationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String lockedError = "Sorry account is locked";
        UfsAuthentication dbAuth = authRepository.findByusername(authentication.getName());
        try {
            Authentication auth = super.authenticate(authentication);
            log.info("Done Authenticating user using AppAuthenticationProvider");

            // check that the user has multi-tenancy permission and if yes, then replicate the
            // authentication token to all the other schemas. This ensures that the user is not
            // de-authenticated when selecting a different schema.
            boolean isSuperUser = auth.getAuthorities().contains(new SimpleGrantedAuthority("MULTITENANCY PERMISSION"));

            if (isSuperUser){
                log.info(">>>>>>>>>>>> Multitenant Superuser found!\n******************\n Replicating user token to other schemas \n *****************");
                // fetch the username for the current logged in user
                String username = ((UserDetails)auth.getPrincipal()).getUsername();
                // replicate the authentication token to other schemas
                authTokenReplicationService.replicateAuthToken(username);
            }

            //reset login attempts
            dbAuth.setLoginAttempts((short) 0);
            dbAuth.setLastLogin(new Date());
            otpRepository.deleteByuserIdAndOtpCategory(dbAuth.getUser().getUserId().toString(), UfsOtpCategory.AUTH_OTP);
            String code = userService.generateOTP(dbAuth.getUser(), UfsOtpCategory.AUTH_OTP);
            System.out.println("OTP >>>>>> "+code);

            this.notifyService.sendEmail(dbAuth.getUsername(), "One Time Password", "OTP: " + code);
            // todo uncomment this.notifyService.sendSms(dbAuth.getUser().getPhoneNumber(),  "OTP: " + code);

            loggerService.log("Logged in successfully", UfsAuthentication.class.getSimpleName(), dbAuth.getAuthenticationId(), dbAuth.getUserId(),
                    AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.STATUS_COMPLETED, "Logged in successfully");
            return auth;
        } catch (BadCredentialsException e) {
            log.warn("Custom authorization handler using default attempts ({}). Consider retrieving from the configuration", 10);
            int attempts = Integer.parseInt(configService.fetchSysConfigById(new BigDecimal(2)).getValue());
            //CmsUser auser = userService.updateFailedAttempts(authentication.getName(), attempts);
            String error;
            if (dbAuth != null) {
                int userAttempts = dbAuth.getLoginAttempts() + 1;
                log.info("Updating authentication attempts from {} to {}", dbAuth.getLoginAttempts(), userAttempts);
                dbAuth.setLoginAttempts((short) userAttempts);
                loggerService.log("Authentication failed due to wrong credentials", UfsAuthentication.class.getSimpleName(),
                        dbAuth.getAuthenticationId(), dbAuth.getUserId(), AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.ACTIVITY_STATUS_FAILED, "Failed log in");
                int remainingAttempts = attempts - dbAuth.getLoginAttempts();
                if (remainingAttempts > 0) {
                    error = "Sorry wrong password or username (remaining " + remainingAttempts + " attempt(s))";
                } else if (remainingAttempts == 0) {
                    error = lockedError;
                    UfsUser ufsUser = userRepository.findByuserId(dbAuth.getUserId());
                    ufsUser.setStatus(AppConstants.STATUS_LOCKED);
                    userRepository.save(ufsUser);
                } else {
                    error = "Sorry wrong password or username";
                }
            } else {
                error = "Username does not exist";
                loggerService.log("Authentication failed due to wrong credentials", UfsAuthentication.class.getSimpleName(), null, null,
                        AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.ACTIVITY_STATUS_FAILED, error);

            }
            throw new BadCredentialsException(error);
        } catch (LockedException e) {
            loggerService.log("Authentication failed due to account locked", UfsAuthentication.class.getSimpleName(), dbAuth.getAuthenticationId(), dbAuth.getUserId(), AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.ACTIVITY_STATUS_FAILED, "Locked account");
            throw new LockedException(lockedError);
        } catch (DisabledException en) {
            loggerService.log("Authentication failed due to account is disabled", UfsAuthentication.class.getSimpleName(), dbAuth.getAuthenticationId(), dbAuth.getUserId(), AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.ACTIVITY_STATUS_FAILED, "Disabled account");
            throw new DisabledException("Sorry account is disabled");
        } catch (CredentialsExpiredException ex) {
            loggerService.log("Authentication failed due to credentials expired", UfsAuthentication.class.getSimpleName(), dbAuth.getAuthenticationId(), dbAuth.getUserId(), AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.ACTIVITY_STATUS_FAILED, "Account credentials expired");
            throw new CredentialsExpiredException("Sorry password has expired");
        }
    }
}
