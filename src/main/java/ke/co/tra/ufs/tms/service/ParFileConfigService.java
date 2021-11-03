package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.ParGlobalConfigProfile;
import ke.co.tra.ufs.tms.entities.wrappers.GlobalConfigFileRequest;

import java.math.BigDecimal;

public interface ParFileConfigService {
    void generateGlobalConfigFile(GlobalConfigFileRequest request, String filePath);
    void generateGlobalConfigFileAsync(ParGlobalConfigProfile profile, BigDecimal modelType, String filePath);
}

