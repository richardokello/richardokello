package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.ParDeviceOptionsIndices;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ParDeviceOptionsIndicesRepository extends CrudRepository<ParDeviceOptionsIndices, BigDecimal> {
    List<ParDeviceOptionsIndices> findAll(Sort sort);
}
