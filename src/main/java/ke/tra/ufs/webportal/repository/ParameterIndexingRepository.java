package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.ParameterIndex;
import ke.tra.ufs.webportal.entities.enums.ParameterCategory;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public interface ParameterIndexingRepository extends CrudRepository<ParameterIndex, BigDecimal> {
    List<ParameterIndex> findAllByCategory(@NotNull ParameterCategory category);
}
