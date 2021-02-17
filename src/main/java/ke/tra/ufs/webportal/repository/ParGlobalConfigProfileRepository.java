package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.ParGlobalConfigProfile;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface ParGlobalConfigProfileRepository extends CrudRepository<ParGlobalConfigProfile, BigDecimal> {
}
