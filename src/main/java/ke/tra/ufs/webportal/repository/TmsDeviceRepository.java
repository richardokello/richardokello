package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.TmsDevice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface TmsDeviceRepository extends CrudRepository<TmsDevice, BigDecimal> {

    List<TmsDevice> findByIntrash(String intrash);

    Set<TmsDevice> findAllByGeographicalRegionIds(BigDecimal ids);
}
