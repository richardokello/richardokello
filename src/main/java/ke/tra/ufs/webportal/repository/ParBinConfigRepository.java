package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.ParBinConfig;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface ParBinConfigRepository extends CrudRepository<ParBinConfig, BigDecimal> {
}
