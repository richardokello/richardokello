package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.ParBinProfile;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface ParBinProfileRepository extends CrudRepository<ParBinProfile, BigDecimal> {
}
