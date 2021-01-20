package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParBinProfile;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface ParBinProfileRepository extends CrudRepository<ParBinProfile, BigDecimal> {
}
