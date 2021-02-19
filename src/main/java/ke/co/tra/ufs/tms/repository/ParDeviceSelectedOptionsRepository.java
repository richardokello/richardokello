package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParDeviceSelectedOptions;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ParDeviceSelectedOptionsRepository extends CrudRepository<ParDeviceSelectedOptions, BigDecimal> {
    void deleteAllByDeviceId(BigDecimal id);

    List<ParDeviceSelectedOptions> findAllByDeviceId(BigDecimal deviceId);
}
