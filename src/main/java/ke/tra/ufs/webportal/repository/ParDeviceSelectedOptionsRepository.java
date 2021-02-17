package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.ParDeviceSelectedOptions;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface ParDeviceSelectedOptionsRepository extends CrudRepository<ParDeviceSelectedOptions, BigDecimal> {
    void deleteAllByDeviceId(BigDecimal id);
}
