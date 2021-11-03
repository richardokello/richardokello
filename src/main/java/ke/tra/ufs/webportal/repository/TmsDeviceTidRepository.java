package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.TmsDevice;
import ke.tra.ufs.webportal.entities.TmsDeviceTids;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface TmsDeviceTidRepository extends CrudRepository<TmsDeviceTids, Long> {

    List<TmsDeviceTids> findAllByDeviceIds(Long id);

    Set<TmsDeviceTids> findAllByDeviceIdsIn(Set<Long> ids);

    @Query("SELECT u from TmsDeviceTids u where u.deviceId=?1")
    List<TmsDeviceTids> findAllByDeviceId(TmsDevice device);

    @Modifying
    @Transactional
    @Query(value = "delete from TmsDeviceTidsMids u where u.deviceIds IN (?1)")
    void deleteAllByDeviceId(List<Long> tmsDevice);

}
