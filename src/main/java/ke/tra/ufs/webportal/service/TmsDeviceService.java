package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.TmsDevice;

import java.math.BigDecimal;

public interface TmsDeviceService {

    TmsDevice findByDeviceId(BigDecimal deviceId);

    //TmsDevice findByCustomerIds(BigDecimal customerIds);

}
