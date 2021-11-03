package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParGlobalMasterProfile;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface ParGlobalMasterProfileRepository extends CrudRepository<ParGlobalMasterProfile, BigDecimal> {
}
