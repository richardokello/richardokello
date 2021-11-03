package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParBinConfig;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface ParBinConfigRepository extends CrudRepository<ParBinConfig, BigDecimal> {
}
