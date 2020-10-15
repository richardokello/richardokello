package ke.tra.ufs.webportal.service.template;

import ke.axle.chassis.utils.AppConstants;
import ke.tra.ufs.webportal.entities.TmsDevice;
import ke.tra.ufs.webportal.repository.TmsDeviceRepository;
import ke.tra.ufs.webportal.service.TmsDeviceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TmsDeviceServiceTemplate implements TmsDeviceService {

    private final TmsDeviceRepository tmsDeviceRepository;

    public TmsDeviceServiceTemplate(TmsDeviceRepository tmsDeviceRepository) {
        this.tmsDeviceRepository = tmsDeviceRepository;
    }

    @Override
    public TmsDevice findByDeviceId(BigDecimal deviceId) {
        return tmsDeviceRepository.findByDeviceId(deviceId);
    }

    @Override
    public List<TmsDevice> findByOutletIds(List<BigDecimal> outletIds) {
        return tmsDeviceRepository.findByOutletIdsInAndIntrash(outletIds,AppConstants.NO);
    }

    @Override
    public TmsDevice findByDeviceIdAndIntrash(BigDecimal id) {
        return tmsDeviceRepository.findByDeviceIdAndIntrash(id, AppConstants.NO);
    }


}
