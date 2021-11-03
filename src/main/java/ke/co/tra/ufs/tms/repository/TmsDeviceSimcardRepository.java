package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.TmsDevice;
import ke.co.tra.ufs.tms.entities.TmsDeviceSimcard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TmsDeviceSimcardRepository extends CrudRepository<TmsDeviceSimcard, BigDecimal> {
    TmsDeviceSimcard findByDeviceId(BigDecimal id);
    void deleteAllByDeviceId(TmsDevice tmsDevice);
}
