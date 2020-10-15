package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.TmsDevice;

import java.math.BigDecimal;
import java.util.List;

public interface TmsDeviceService {

    TmsDevice findByDeviceId(BigDecimal deviceId);

    List<TmsDevice> findByOutletIds(List<BigDecimal> outletIds);

    public TmsDevice findByDeviceIdAndIntrash(BigDecimal id);


}
