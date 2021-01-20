package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParGlobalConfigProfile;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface ParGlobalConfigProfileRepository extends CrudRepository<ParGlobalConfigProfile, BigDecimal> {
}
