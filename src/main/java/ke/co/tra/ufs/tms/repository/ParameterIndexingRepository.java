package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParameterIndex;
import ke.co.tra.ufs.tms.entities.enums.ParameterCategory;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public interface ParameterIndexingRepository extends CrudRepository<ParameterIndex, BigDecimal> {
    List<ParameterIndex> findAllByCategory(@NotNull ParameterCategory category);
}
