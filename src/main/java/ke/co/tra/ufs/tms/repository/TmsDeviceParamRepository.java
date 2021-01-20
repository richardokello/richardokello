package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import ke.co.tra.ufs.tms.entities.TmsDevice;
import ke.co.tra.ufs.tms.entities.TmsDeviceParam;
import org.springframework.data.repository.CrudRepository;

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
