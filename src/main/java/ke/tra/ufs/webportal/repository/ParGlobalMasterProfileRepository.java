package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.ParGlobalMasterProfile;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface ParGlobalMasterProfileRepository extends CrudRepository<ParGlobalMasterProfile, BigDecimal> {
}
