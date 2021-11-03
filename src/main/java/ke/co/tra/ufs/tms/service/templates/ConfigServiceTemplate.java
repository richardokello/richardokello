/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service.templates;

import java.util.ArrayList;
import java.util.List;
import ke.co.tra.ufs.tms.entities.UfsSysConfig;
import ke.co.tra.ufs.tms.entities.wrappers.PasswordConfig;
import ke.co.tra.ufs.tms.entities.wrappers.UfsSysConfigWrapper;
import ke.co.tra.ufs.tms.repository.ConfigRepository;
import ke.co.tra.ufs.tms.service.ConfigService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ke.co.tra.ufs.tms.service.LoggerServiceLocal;

/**
 *
 * @author Owori Juma
 */
@Service
@Transactional
public class ConfigServiceTemplate implements ConfigService {

    private final ConfigRepository configRepo;
    private final Logger log;

    public ConfigServiceTemplate(ConfigRepository configRepo) {
        this.configRepo = configRepo;
        this.log = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public Integer passwordExpiry() {
        UfsSysConfig config = configRepo.findByentityAndParameter(AppConstants.ENTITY_PASSWORD_POLICY, AppConstants.PARAMETER_EXPIRY);
        return Integer.valueOf(config.getValue());
    }

    @Override
    public Integer passwordAttempts() {
        UfsSysConfig config = configRepo.findByentityAndParameter(AppConstants.ENTITY_PASSWORD_POLICY, AppConstants.PARAMETER_ATTEMPTS);
        return Integer.valueOf(config.getValue());
    }

    @Override
    public Integer otpAttempts() {
        UfsSysConfig config = configRepo.findByentityAndParameter(AppConstants.ENTITY_PASSWORD_POLICY, AppConstants.PARAMETER_OTP_ATTEMPTS);
        return Integer.valueOf(config.getValue());
    }

    @Override
    public List<UfsSysConfigWrapper> passwordConfigurations() {
        List<UfsSysConfigWrapper> configs = configRepo.findByentity(AppConstants.ENTITY_PASSWORD_POLICY);
        return configs;
    }

    @Override
    public PasswordConfig passwordConfigs() {
        List<UfsSysConfigWrapper> configs = this.passwordConfigurations();
        PasswordConfig passConfig = new PasswordConfig();
        configs.forEach(config -> {
            switch (config.getParameter()) {
                case AppConstants.PARAMETER_ATTEMPTS:
                    passConfig.attempts = Integer.valueOf(config.getValue());
                    break;
                case AppConstants.PARAMETER_EXPIRY:
                    passConfig.expiry = Long.valueOf(config.getValue());
                    break;
                case AppConstants.PARAMETER_CHAR_NUMBER:
                    passConfig.characters = Integer.valueOf(config.getValue());
                    break;
                case AppConstants.PARAMETER_NUMERIC_NUMBER:
                    passConfig.numeric = Integer.valueOf(config.getValue());
                    break;
                case AppConstants.PARAMETER_PASSWORD_LENGTH:
                    passConfig.length = Integer.valueOf(config.getValue());
                    break;
                case AppConstants.PARAMETER_LOWERCASE_NUMBER:
                    passConfig.lowerCase = Integer.valueOf(config.getValue());
                    break;
                case AppConstants.PARAMETER_UPPERCASE_NUMBER:
                    passConfig.upperCase = Integer.valueOf(config.getValue());
                    break;
                case AppConstants.PARAMETER_SPECIAL_CHAR_NUMBER:
                    passConfig.specialChar = Integer.valueOf(config.getValue());
                    break;
            }
        });
        return passConfig;
    }

    @Override
    public List<UfsSysConfigWrapper> getScapiConfig() {
        List<UfsSysConfigWrapper> configs = configRepo.findByentity(AppConstants.ENTITY_SYSTEM_INTEGRATION);
        return configs;
    }

    @Override
    public List<UfsSysConfigWrapper> getSmsConfig() {
        List<String> params = new ArrayList<String>() {
            {
                add("smsApiUrl");
                add("smsPassword");
                add("smsUsername");
            }
        };
        return configRepo.findByentityAndParameterIn(AppConstants.ENTITY_SYSTEM_INTEGRATION, params);
    }

    @Override
    public void savePasswordConfig(PasswordConfig passConfig, LoggerServiceLocal loggerService) {
        List<UfsSysConfigWrapper> configs = this.passwordConfigurations();
        configs.forEach(config -> {
            switch (config.getParameter()) {
                case AppConstants.PARAMETER_ATTEMPTS:
                    config.setValue(passConfig.attempts.toString());
                    break;
                case AppConstants.PARAMETER_EXPIRY:
                    config.setValue(passConfig.expiry.toString());
                    break;
                case AppConstants.PARAMETER_CHAR_NUMBER:
                    config.setValue(passConfig.characters.toString());
                    break;
                case AppConstants.PARAMETER_NUMERIC_NUMBER:
                    config.setValue(passConfig.numeric.toString());
                    break;
                case AppConstants.PARAMETER_PASSWORD_LENGTH:
                    config.setValue(passConfig.length.toString());
                    break;
                case AppConstants.PARAMETER_LOWERCASE_NUMBER:
                    config.setValue(passConfig.lowerCase.toString());
                    break;
                case AppConstants.PARAMETER_UPPERCASE_NUMBER:
                    config.setValue(passConfig.upperCase.toString());
                    break;
                case AppConstants.PARAMETER_SPECIAL_CHAR_NUMBER:
                    config.setValue(passConfig.specialChar.toString());
                    break;
                case AppConstants.PARAMETER_OTP_ATTEMPTS:
                    config.setValue(passConfig.otpAttempts.toString());
                    break;
                case AppConstants.PARAMETER_OTP_EXPIRY:
                    config.setValue(passConfig.otpExpiry.toString());
                    break;
            }
            loggerService.logUpdate("Done Updating password configurations", SharedMethods.getEntityName(UfsSysConfig.class), config.getId().toString(), AppConstants.STATUS_UNAPPROVED);
        });
    }

    @Override
    public String getFileUploadDir() {
        return configRepo.findByentityAndParameter(AppConstants.ENTITY_GLOBAL_INTEGRATION,
                AppConstants.PARAMETER_UPLOAD_DIR).getValue();
    }

}
