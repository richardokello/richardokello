package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.TmsDevice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface TmsDeviceRepository extends CrudRepository<TmsDevice, BigDecimal> {


//    Set<TmsDevice> findAllByGeographicalRegionIds(BigDecimal ids);

    public List<TmsDevice> findByIntrash(String intrash);

    public TmsDevice findByDeviceId(BigDecimal deviceId);

    public TmsDevice findByDeviceIdAndIntrash(BigDecimal deviceId,String intrash);


}
