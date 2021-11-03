package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParDeviceOptionsIndices;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ParDeviceOptionsIndicesRepository extends CrudRepository<ParDeviceOptionsIndices, BigDecimal> {
    List<ParDeviceOptionsIndices> findAll(Sort sort);
}
