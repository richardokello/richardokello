/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.service;

import java.util.List;
import ke.co.tra.ufs.tms.entities.wrappers.PasswordConfig;
import ke.co.tra.ufs.tms.entities.wrappers.UfsSysConfigWrapper;

/**
 *
 * @author Owori Juma
 */
@Deprecated
public interface ConfigService {
    /**
     * Used to fetch password expiry seconds
     * @return expiry in seconds
     */
    public Integer passwordExpiry();
    /**
     * Used to get number of password attempts
     * @return 
     */
    public Integer passwordAttempts();
    /**
     * Used to get number of OTP attempts
     * @return 
     */
    public Integer otpAttempts();
    /**
     * Fetch all password configurations
     * @return 
     */
    public List<UfsSysConfigWrapper> passwordConfigurations();
    /**
     * Used to fetch password configurations
     * @return 
     */
    public PasswordConfig passwordConfigs();
    /**
     * Used to get scapi integration configurations
     * @return 
     */
    public List<UfsSysConfigWrapper> getScapiConfig();
    /**
     * Used to get sms configuration
     * @return 
     */
    public List<UfsSysConfigWrapper> getSmsConfig();
    /**
     * Used to save password configuration
     * @param config
     * @param loggerService
     * @return 
     */
    public void savePasswordConfig(PasswordConfig config, LoggerServiceLocal loggerService);
    /**
     * Get file upload directory path
     * @return 
     */
    public String getFileUploadDir();

}
