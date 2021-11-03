/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service.templates;

import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import ke.co.tra.ufs.tms.entities.UfsSysConfig;
import ke.co.tra.ufs.tms.entities.wrappers.PasswordConfig;
import ke.co.tra.ufs.tms.entities.wrappers.UfsSysConfigWrapper;
import ke.co.tra.ufs.tms.repository.ConfigRepository;
import ke.co.tra.ufs.tms.repository.SysConfigRepository;
import ke.co.tra.ufs.tms.service.SysConfigService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import ke.co.tra.ufs.tms.utils.SharedMethods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Owori Juma
 */
@Service
@Transactional
public class SysConfigServiceTemplate implements SysConfigService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SysConfigRepository sysConfRepo;
    private final ConfigRepository configRepo;
    private final String key = "Trx#279!DTCeioc?";
    private final Key aesKey;
    private Cipher cipher;

    public SysConfigServiceTemplate(SysConfigRepository sysConfRepo, ConfigRepository configRepo) {
        this.sysConfRepo = sysConfRepo;
        this.aesKey = new SecretKeySpec(key.getBytes(), "AES");
        this.configRepo = configRepo;
        try {
            this.cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            log.error(AppConstants.AUDIT_LOG, "Failed to intialize AES cipher: " + ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UfsSysConfig> fetchSysConfigs(Pageable pg) {
        Page<UfsSysConfig> configs = sysConfRepo.findAll(pg);
        configs.forEach(config -> {
            if (config.getValueType().equalsIgnoreCase(AppConstants.PARAM_TYPE_PASSWORD)) {
                config.setValue(this.decryptText(config.getValue()));
            }
        });
        return configs;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UfsSysConfig> getSysConfigs(Pageable pg, boolean excludePasswordConfig) {
        Page<UfsSysConfig> configs = excludePasswordConfig ? sysConfRepo.findByentityNot(AppConstants.ENTITY_PASSWORD_POLICY, pg)
                : sysConfRepo.findAll(pg);
        configs.forEach(config -> {
            if (config.getValueType().equalsIgnoreCase(AppConstants.PARAM_TYPE_PASSWORD)) {
                config.setValue(this.decryptText(config.getValue()));
            }
        });
        return configs;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UfsSysConfig> searchSysConfigs(String needle, Pageable pg) {
        Page<UfsSysConfig> configs = sysConfRepo.searchConfigs(needle.toLowerCase(), pg);
        configs.forEach(config -> {
            if (config.getValueType().equalsIgnoreCase(AppConstants.PARAM_TYPE_PASSWORD)) {
                config.setValue(this.decryptText(config.getValue()));
            }
        });
        return configs;
    }

    @Override
    @Transactional(readOnly = true)
    public UfsSysConfig fetchSysConfigById(BigDecimal sysConfig) {
        Optional<UfsSysConfig> config = sysConfRepo.findById(sysConfig);
        if (config.get().getValueType().equalsIgnoreCase(AppConstants.PARAM_TYPE_PASSWORD)) {
            config.get().setValue(this.decryptText(config.get().getValue()));
        }
        return config.get();
    }

    @Override
    @Transactional(readOnly = true)
    public UfsSysConfig fetchSysConfig(String entity, String parameter) {
        UfsSysConfig config = sysConfRepo.findByEntityAndParameterAllIgnoreCase(entity, parameter);
        if(config == null){
            return config;
        }
        if (config.getValueType().equalsIgnoreCase(AppConstants.PARAM_TYPE_PASSWORD)) {
            config.setValue(this.decryptText(config.getValue()));
        }
        return config;
    }

    @Override
    @Transactional(readOnly = true)
    public UfsSysConfig fetchSysConfigByEntityAndParameter(BigDecimal id, String entity, String parameter) {
//        return sysConfRepo.findByEntityAndParameter(id, entity, parameter);
        UfsSysConfig config = sysConfRepo.findByEntityAndParameter(id, entity, parameter);
        if (config.getValueType().equalsIgnoreCase(AppConstants.PARAM_TYPE_PASSWORD)) {
            config.setValue(this.decryptText(config.getValue()));
        }
        return config;
    }

    @Override
    @Transactional
    public UfsSysConfig saveSysConfig(UfsSysConfig sysCon) {
        if (sysCon.getValueType().equalsIgnoreCase(AppConstants.PARAM_TYPE_PASSWORD)) {
            sysCon.setValue(this.encryptText(sysCon.getValue()));
        }
        return sysConfRepo.save(sysCon);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UfsSysConfig> getSysConfigs(Pageable pg, String entity, boolean excludePasswordConfig) {
        Page<UfsSysConfig> configs = excludePasswordConfig ? sysConfRepo.findByentityIgnoreCaseAndEntityNot(entity, AppConstants.ENTITY_PASSWORD_POLICY, pg) : sysConfRepo.findByentityIgnoreCase(entity, pg);
        configs.forEach(config -> {
            if (config.getValueType().equalsIgnoreCase(AppConstants.PARAM_TYPE_PASSWORD)) {
                config.setValue(this.decryptText(config.getValue()));
            }
        });
        return configs;
    }


    @Override
    public String previewTemplateMessage(String message, Map<String, Object> variables) {
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            message = message.replace(entry.getKey(), "" + ((entry.getValue() == null) ? "" : entry.getValue()));
        }
        return message;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UfsSysConfigWrapper> getMailConfig() {
        List<String> params = new ArrayList<String>() {
            {
                add("mailHost");
                add("mailPort");
                add("mailUsername");
                add("mailPassword");
                add("mailSSL");
            }
        };
        List<UfsSysConfigWrapper> configs = configRepo.findByentityAndParameterIn(AppConstants.ENTITY_SYSTEM_INTEGRATION, params);
        configs.forEach(config -> {
            if (config.getValueType().equalsIgnoreCase(AppConstants.PARAM_TYPE_PASSWORD)) {
                config.setValue(this.decryptText(config.getValue()));
            }
        });
        return configs;
    }

    @Override
    @Transactional(readOnly = true)
    public PasswordConfig passwordConfigs() {
        List<UfsSysConfigWrapper> configs = configRepo.findByentity(AppConstants.ENTITY_PASSWORD_POLICY);
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
                case AppConstants.PARAMETER_OTP_ATTEMPTS:
                    passConfig.otpAttempts = Integer.valueOf(config.getValue());
                    break;
                case AppConstants.PARAMETER_OTP_EXPIRY:
                    passConfig.otpExpiry = Long.valueOf(config.getValue());
                    break;
            }
        });
        return passConfig;
    }

    @Override
    public UfsSysConfig findByEntityAndParameter(String entity, String parameter) {
        return sysConfRepo.findByEntityAndParameter(entity, parameter);
    }

    /**
     * Encrypt text
     *
     * @param text
     * @return base64 encoded string
     */
    private String encryptText(String text) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(text.getBytes());
//            return new String(encrypted);
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NullPointerException ex) {
            java.util.logging.Logger.getLogger(SharedMethods.class.getName()).log(Level.SEVERE, null, ex);
            log.error(AppConstants.AUDIT_LOG, "Failed to encrypt text:  " + ex.getMessage());
            return text;
        }
    }

    /**
     * Decrypt text
     *
     * @param encryptedText base64 encoded
     * @return
     */
    private String decryptText(String encryptedText) {

        try {
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
//            return new String(cipher.doFinal(encryptedText.getBytes()));
            return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
        } catch (IllegalArgumentException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NullPointerException ex) {
            log.error(AppConstants.AUDIT_LOG, "Failed to decrypt text:  " + ex.getMessage());
            return encryptedText;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UfsSysConfig> getSysConfigs(String actionStatus, String entity, String needle, Pageable pg) {
        Page<UfsSysConfig> configs = this.configRepo.findAll(actionStatus, entity.toLowerCase(), needle.toLowerCase(), pg);
        configs.forEach(config -> {
            if (config.getValueType().equalsIgnoreCase(AppConstants.PARAM_TYPE_PASSWORD)) {
                config.setValue(this.decryptText(config.getValue()));
            }
        });
        return configs;
    }

}
