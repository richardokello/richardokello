package ke.tra.ufs.webportal.service;


import ke.tra.ufs.webportal.entities.TmsDevice;

import java.math.BigDecimal;

public interface CustomerConfigFileService {
    void generateCustomerFile(BigDecimal deviceId, String filePath);

    TmsDevice getDevice(BigDecimal id);
}
