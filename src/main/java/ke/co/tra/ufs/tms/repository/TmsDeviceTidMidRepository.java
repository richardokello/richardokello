package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.TmsDevice;
import ke.co.tra.ufs.tms.entities.TmsDeviceTidsMids;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TmsDeviceTidMidRepository extends CrudRepository<TmsDeviceTidsMids, Long> {

    List<TmsDeviceTidsMids> findAllByDeviceIds(Long id);

    void deleteAllByDeviceId(TmsDevice tmsDevice);

    @Query("SELECT COUNT(t.id) FROM TmsDeviceTidsMids t WHERE t.tid = ?1 OR t.mid = ?2")
    Integer getTmsDeviceTidsMids(String tid, String mid);

    @Query("SELECT COUNT(t.id) FROM TmsDeviceTidsMids t WHERE (t.tid = ?1 OR t.mid = ?2) and t.deviceIds=?3")
    Integer getTmsDeviceTidsMidsByDeviceIds(String tid, String mid, Long deviceIds);

}
