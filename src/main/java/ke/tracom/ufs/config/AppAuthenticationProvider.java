package ke.tracom.ufs.config;


import ke.tracom.ufs.entities.UfsAuditLog;
import ke.tracom.ufs.entities.UfsAuthentication;
import ke.tracom.ufs.entities.UfsOtpCategory;
import ke.tracom.ufs.entities.UfsUser;
import ke.tracom.ufs.repositories.UserRepository;
import ke.tracom.ufs.services.AuditLogService;
import ke.tracom.ufs.services.SysConfigService;
import ke.tracom.ufs.services.template.LoggerServiceTemplate;
import ke.tracom.ufs.services.template.NotifyServiceTemplate;
import ke.tracom.ufs.repositories.AuthenticationRepository;
import ke.tracom.ufs.repositories.OTPRepository;
import ke.tracom.ufs.services.CustomUserService;
import ke.tracom.ufs.utils.AppConstants;
import ke.tracom.ufs.utils.exceptions.UserAlreadyLoggedInException;
import ke.tracom.ufs.wrappers.LogExtras;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private TokenStore tokenStore;
    private HttpServletRequest request;
    private final AuditLogService auditLogService;
    private final LogExtras extras;

    @Value("${client_id}")
    private String client_id;

    public AppAuthenticationProvider(@Qualifier("userDetailsServiceTemplate") UserDetailsService userDetailsService,
                                     AuthenticationRepository authRepository, PasswordEncoder passwordEncoder,
                                     UserRepository userRepository, OTPRepository otpRepository, @Qualifier("commonUserService") CustomUserService userService, NotifyServiceTemplate notifyService,
                                     LoggerServiceTemplate loggerService, SysConfigService configService, TokenStore tokenStore, HttpServletRequest request, AuditLogService auditLogService, LogExtras extras) {
        super();
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.configService = configService;
        this.tokenStore = tokenStore;
        this.request = request;
        this.auditLogService = auditLogService;
        this.extras = extras;
        super.setUserDetailsService(userDetailsService);
        this.authRepository = authRepository;
        super.setPasswordEncoder(passwordEncoder);
        this.otpRepository = otpRepository;
        this.userService = userService;
        this.notifyService = notifyService;
        this.loggerService = loggerService;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String lockedError = "Sorry account is locked";
        UfsAuthentication dbAuth = authRepository.findByusername(authentication.getName());
        try {
            Collection<OAuth2AccessToken> accesToken = tokenStore.findTokensByClientIdAndUserName(client_id, authentication.getName());
            List<OAuth2AccessToken> activeToken = accesToken.stream().filter(token -> !token.isExpired()).collect(Collectors.toList());
            if (activeToken.size() > 0) {
                String ip = extras.getIpAddress();
                String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);

                List<UfsAuditLog> audits = auditLogService.findByUserIdAndIpAndSource(dbAuth.getUserId().toString(), AppConstants.STATUS_COMPLETED, AppConstants.ACTIVITY_AUTHENTICATION, source, ip);
                log.error("Other IP===>{}", ip);
                log.error("Other Source===>{}", source);
                log.error("Other userId===>{}", dbAuth.getUserId().toString());
                log.error("Other Logs===>{}", audits.size());
                if (audits.size() < 1)
                    loggerService.log("Another user with same credentials has already logged into the system", UfsAuthentication.class.getSimpleName(), null, null,
                            AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.ACTIVITY_STATUS_FAILED, "Another user with same credentials already logged in");
                    //assumption is that access token will always be one
                    StringBuilder stringBuilder = new StringBuilder("Another user with same credentials has already logged into the system ")
                            .append("[")
                            .append(activeToken.get(0).getValue())
                            .append("]");
                throw new UserAlreadyLoggedInException(stringBuilder.toString());

            }
            Authentication auth = super.authenticate(authentication);
            log.info("Done Authenticating user using AppAuthenticationProvider");
            //reset login attempts
            dbAuth.setLoginAttempts((short) 0);
            dbAuth.setLastLogin(new Date());
            otpRepository.deleteByuserIdAndOtpCategory(dbAuth.getUser().getUserId().toString(), UfsOtpCategory.AUTH_OTP);
            String code = userService.generateOTP(dbAuth.getUser(), UfsOtpCategory.AUTH_OTP);
            System.out.println("OTP >>>>>>"+code);

            this.notifyService.sendEmail(dbAuth.getUsername(), "One Time Password", "OTP: " + code);
            this.notifyService.sendSms(dbAuth.getUser().getPhoneNumber(),  "OTP: " + code);

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
