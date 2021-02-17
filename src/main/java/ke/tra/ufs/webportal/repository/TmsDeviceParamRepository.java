package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.TmsDevice;
import ke.tra.ufs.webportal.entities.TmsDeviceParam;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

/**
 *
 * @author Owori Juma
 */
public interface TmsDeviceParamRepository extends CrudRepository<TmsDeviceParam, BigDecimal> {

    /**
     *
     * @param deviceId
     * @return
     */
    public TmsDeviceParam findBydeviceId(TmsDevice deviceId);
}
