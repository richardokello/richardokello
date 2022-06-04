/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.services.template;

import ke.axle.chassis.exceptions.GeneralBadRequest;
import ke.tracom.ufs.entities.UfsAuthentication;
import ke.tracom.ufs.entities.UfsPasswordHistory;
import ke.tracom.ufs.entities.UfsUser;
import ke.tracom.ufs.entities.wrapper.UfsSysConfigWrapper;
import ke.tracom.ufs.repositories.AuthenticationRepository;
import ke.tracom.ufs.repositories.UfsPasswordHistoryRepository;
import ke.tracom.ufs.repositories.UfsSysConfigRepository;
import ke.tracom.ufs.utils.AppConstants;
import ke.tracom.ufs.utils.PasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author kmwangi
 */
@Service
public class UserAccountService {

    @Autowired
    PasswordGenerator config;

    private final UfsSysConfigRepository configRepo;
    private final UfsPasswordHistoryRepository passHistoryRepo;
    private final PasswordEncoder passEncoder;
    private final LoggerServiceTemplate loggerService;
    private final AuthenticationRepository authRepository;

    private final Logger log;

    public UserAccountService(UfsSysConfigRepository configRepo, UfsPasswordHistoryRepository passHistoryRepo,
                              PasswordEncoder passEncoder, LoggerServiceTemplate loggerService, AuthenticationRepository authRepository) {
        this.loggerService = loggerService;
        this.authRepository = authRepository;
        this.log = LoggerFactory.getLogger(this.getClass());
        this.configRepo = configRepo;
        this.passHistoryRepo = passHistoryRepo;
        this.passEncoder = passEncoder;
    }
    /*
    public boolean isPasswordValid(String password) {
        return true;
    }
     */


    public boolean isPasswordValid(String password, UfsUser user) throws GeneralBadRequest {
        List<UfsSysConfigWrapper> configs = configRepo.findByEntity(AppConstants.ENTITY_PASSWORD_POLICY);
        UfsAuthentication ufsAuthentication = authRepository.findByuserId(user.getUserId());

        int length = 0, charLength = 0, numericLength = 0, reuseCount = 0, lowerCase = 0,
                upperCase = 0, specialCharacter = 0;
        log.info("Password length is: " + password.length() + " and size of config " + configs.size());
        for (UfsSysConfigWrapper config : configs) {
            if (config.getParameter().equalsIgnoreCase("passwordLength")) {
                length = Integer.valueOf(config.getValue());
            } else if (config.getParameter().equalsIgnoreCase("charLength")) {
                charLength = Integer.valueOf(config.getValue());
            } else if (config.getParameter().equalsIgnoreCase("numericLength")) {
                numericLength = Integer.valueOf(config.getValue());

            } else if (config.getParameter().equalsIgnoreCase("reuseCount")) {
                reuseCount = Integer.valueOf(config.getValue());
            } else if (config.getParameter().equalsIgnoreCase("lowerCase")) {
                lowerCase = Integer.valueOf(config.getValue());
            } else if (config.getParameter().equalsIgnoreCase("upperCase")) {
                upperCase = Integer.valueOf(config.getValue());
            } else if (config.getParameter().equalsIgnoreCase("specialCharacter")) {
                specialCharacter = Integer.valueOf(config.getValue());
            }
        }
        String regex = "^(?=.*[0-9]{" + numericLength + "})(?=.*[a-z]{" + lowerCase + "})(?=.*[A-Z]{" + upperCase + "})(?=.*[.*?@#$%^&+=:;/><\\-_!\\]\\[`~]{" + specialCharacter + "})(?=\\S+$).{" + length + ",}$";
        if (!password.matches(regex)) {
            loggerService.log("Changing Password Failed,Password Requirements Not Met", UfsAuthentication.class.getSimpleName(), ufsAuthentication.getAuthenticationId(), user.getUserId(),
                    AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.ACTIVITY_STATUS_FAILED, "Password Requirements Not Met");

            throw new GeneralBadRequest("Sorry password  validation failed. The password should have "
                    + " atleast " + length + " alphanumeric characters, " + charLength +
                    " letter(s), " + numericLength + " number(s), " + lowerCase + " lowercase character(s) "
                    + ", " + specialCharacter + " special character and " + upperCase + " uppercase character(s)");
        }
        //check password history
        List<UfsPasswordHistory> passHistories = passHistoryRepo.findByuserId(user, new PageRequest(0, reuseCount, Sort.Direction.DESC, "changeDate"));
        for (UfsPasswordHistory passHistory : passHistories) {
            if (passEncoder.matches(password, passHistory.getPassword())) {

                loggerService.log("Changing Password Failed,Password has already been used previously", UfsAuthentication.class.getSimpleName(), ufsAuthentication.getAuthenticationId(), user.getUserId(),
                        AppConstants.ACTIVITY_AUTHENTICATION, AppConstants.ACTIVITY_STATUS_FAILED, "Password has already been used previously");

                throw new GeneralBadRequest("Sorry the password has already been used previously");
            }
        }

        return true;
    }


    public boolean hasLowercase(String password) {
        int count = 0;
        for (int i = 0; i >= password.length(); i++) {
            if (String.valueOf(password.charAt(i)) == String.valueOf(password.charAt(i)).toLowerCase()) {
                count++;
            }
        }
        if (count != 0) {
            return false;
        } else {
            return true;
        }
    }

}
