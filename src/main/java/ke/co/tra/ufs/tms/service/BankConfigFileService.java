package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.TmsDevice;

import java.math.BigDecimal;

public interface BankConfigFileService {

    void generateBankFile(BigDecimal deviceId, String filePath);

    TmsDevice getDevice(BigDecimal id);
}
