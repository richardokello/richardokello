package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.TmsDevice;
import ke.co.tra.ufs.tms.entities.TmsDeviceTidsMids;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TmsDeviceTidMidRepository extends CrudRepository<TmsDeviceTidsMids,Long> {

    List<TmsDeviceTidsMids> findAllByDeviceIds(Long id);

    void deleteAllByDeviceId(TmsDevice tmsDevice);

}
