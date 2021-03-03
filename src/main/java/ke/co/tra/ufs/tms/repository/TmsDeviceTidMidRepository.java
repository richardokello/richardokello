package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.TmsDevice;
import ke.co.tra.ufs.tms.entities.TmsDeviceTidsMids;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface TmsDeviceTidMidRepository extends CrudRepository<TmsDeviceTidsMids, Long> {

    List<TmsDeviceTidsMids> findAllByDeviceIds(Long id);

    @Modifying
    @Transactional
    @Query(value = "delete from TmsDeviceTidsMids u where u.deviceId = ?1")
    void deleteAllByDeviceId(TmsDevice tmsDevice);

    @Modifying
    @Transactional
    @Query(value = "delete from TmsDeviceTidsMids u where u.deviceIds IN (?1)")
    void deleteAllByDeviceId(List<Long> tmsDevice);

    @Query("SELECT COUNT(t.id) FROM TmsDeviceTidsMids t WHERE t.tid = ?1 OR t.mid = ?2")
    Integer getTmsDeviceTidsMids(String tid, String mid);

    @Query("SELECT COUNT(t.id) FROM TmsDeviceTidsMids t WHERE (t.tid = ?1 OR t.mid = ?2) and t.deviceIds NOT IN (?3)")
    Integer getTmsDeviceTidsMidsByDeviceIds(String tid, String mid, Long deviceIds);

    @Query("SELECT COUNT(t.id) FROM TmsDeviceTidsMids t WHERE t.tid = ?1")
    Integer getTmsDeviceTids(String tid);

    @Query("SELECT COUNT(t.id) FROM TmsDeviceTidsMids t WHERE t.tid  IN (?1)")
    Integer getTmsDeviceTidsIn(Set<String> tid);

    @Query("SELECT COUNT(t.id) FROM TmsDeviceTidsMids t WHERE t.tid = ?1 and t.deviceIds NOT IN (?2)")
    Integer getTmsDeviceTidsByDeviceIds(String tid, Long deviceIds);

    @Query("SELECT u from TmsDeviceTidsMids u where u.deviceId=?1")
    List<TmsDeviceTidsMids> findAllByDeviceId(TmsDevice device);

    @Query("SELECT u from TmsDeviceTidsMids u where u.deviceIds IN (?1)")
    List<TmsDeviceTidsMids> findAllByDeviceIdIn(List<Long> device);

    @Query("SELECT u from TmsDeviceTidsMids u where u.mid=?1")
    List<TmsDeviceTidsMids> findAllByMid(String mid);

    @Query("SELECT u from TmsDeviceTidsMids u where u.mid IN (?1)")
    List<TmsDeviceTidsMids> findAllByMidIn(Set<String> mid);

    @Query("SELECT u from TmsDeviceTidsMids u where u.mid = ?1 AND u.currencyIds NOT IN (?2)")
    List<TmsDeviceTidsMids> findAllByMidInAndCurrencyId(String mid, List<BigDecimal> currencyIds);

    @Query("SELECT u from TmsDeviceTidsMids u where u.mid = ?1 AND u.currencyIds <>?2")
    List<TmsDeviceTidsMids> findAllByMidAndCurrencyIdsIsNot(String mid, BigDecimal currencyIds);
    @Query("SELECT COUNT(u.id) from TmsDeviceTidsMids u where u.mid=?1 AND u.currencyIds <>?2")
    Integer countAllByMidAndCurrencyIdsIsNot(String mid, BigDecimal currencyIds);

}
