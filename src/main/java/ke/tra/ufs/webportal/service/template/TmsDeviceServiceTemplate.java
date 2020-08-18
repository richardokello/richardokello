package ke.tra.ufs.webportal.service.template;

import ke.tra.ufs.webportal.entities.TmsDevice;
import ke.tra.ufs.webportal.repository.TmsDeviceRepository;
import ke.tra.ufs.webportal.service.TmsDeviceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

    /*@Override
    public TmsDevice findByCustomerIds(BigDecimal customerIds) {
        return tmsDeviceRepository.findByCustomerIds(customerIds);
    }*/
}
