/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import ke.co.tra.ufs.tms.entities.TmsDevice;
import ke.co.tra.ufs.tms.entities.TmsDeviceSimcard;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Owori Juma
 */
public interface DeviceSimcardRepository extends CrudRepository<TmsDeviceSimcard, BigDecimal> {

    /**
     *
     * @param deviceId
     * @return
     */
    TmsDeviceSimcard findBydeviceId(TmsDevice deviceId);
}
