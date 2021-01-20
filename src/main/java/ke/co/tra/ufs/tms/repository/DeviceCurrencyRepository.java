/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import ke.co.tra.ufs.tms.entities.TmsDevice;
import ke.co.tra.ufs.tms.entities.TmsDeviceCurrency;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Owori Juma
 */
public interface DeviceCurrencyRepository extends CrudRepository<TmsDeviceCurrency, BigDecimal> {

    /**
     *
     * @param deviceId
     * @return
     */
    TmsDeviceCurrency findBydeviceId(TmsDevice deviceId);
}
