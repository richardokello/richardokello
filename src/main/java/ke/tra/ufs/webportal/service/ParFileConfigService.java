package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.ParGlobalConfigProfile;
import ke.tra.ufs.webportal.entities.wrapper.GlobalConfigFileRequest;

import java.math.BigDecimal;

public interface ParFileConfigService {
    void generateGlobalConfigFile(GlobalConfigFileRequest request, String filePath);
    void generateGlobalConfigFileAsync(ParGlobalConfigProfile profile, BigDecimal modelType, String filePath);
}

