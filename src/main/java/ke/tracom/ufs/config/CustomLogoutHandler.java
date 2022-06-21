package ke.tracom.ufs.config;

import ke.axle.chassis.utils.LoggerService;
import ke.tracom.ufs.entities.UfsAuthentication;
import ke.tracom.ufs.repositories.AuthenticationRepository;
import ke.tracom.ufs.utils.AppConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLogoutHandler extends SimpleUrlLogoutSuccessHandler {

    private final LoggerService loggerService;
    private final AuthenticationRepository authRepository;

    public CustomLogoutHandler(LoggerService loggerService, AuthenticationRepository authRepository) {
        this.loggerService = loggerService;
        this.authRepository = authRepository;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("Logged out >>>>  is null " +  (authentication == null));
        UfsAuthentication ufsAuthentication = authRepository.findByusernameIgnoreCase(authentication.getName());
        loggerService.log("Logged out successfully", UfsAuthentication.class.getSimpleName(), ufsAuthentication.getAuthenticationId(), ufsAuthentication.getUserId(),
                AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.STATUS_COMPLETED, "Logged out successfully");
        super.onLogoutSuccess(request, response, authentication);
    }
}
