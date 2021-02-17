package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.TmsDevice;
import ke.tra.ufs.webportal.entities.TmsEstateItem;
import ke.tra.ufs.webportal.entities.UfsDeviceModel;
import org.springframework.data.jpa.repository.Query;
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

    List<TmsDevice> findByOutletIdsInAndIntrash(List<BigDecimal> outletIds,String intrash);

    /**
     * @param status
     * @param intrash
     * @return
     */
    @Query("SELECT COUNT(*) FROM TmsDevice u WHERE u.status LIKE ?1% AND lower(u.intrash) = lower(?2) AND u.action!='Release'")
    Integer findAllActiveDevices(String status, String intrash);

    /**
     * @param modelId
     * @param unitItemId
     * @param intrash
     * @return
     */
    List<TmsDevice> findByModelIdAndEstateIdAndIntrash(UfsDeviceModel modelId, TmsEstateItem unitItemId, String intrash);
    /**
     * @param estateId
     * @return
     */
    public List<TmsDevice> findByestateId(TmsEstateItem estateId);


}
