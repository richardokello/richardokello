package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.TmsDevice;
import ke.tra.ufs.webportal.entities.TmsDeviceTidCurrency;
import ke.tra.ufs.webportal.entities.TmsDeviceTids;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TmsDeviceTidCurrencyRepository extends CrudRepository<TmsDeviceTidCurrency, Long> {
    @Query("SELECT u from TmsDeviceTidCurrency u where u.deviceId=?1")
    List<TmsDeviceTidCurrency> findAllByDeviceId(TmsDevice device);
}
