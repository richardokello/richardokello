package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.ParMenuIndices;
import ke.co.tra.ufs.tms.entities.ParReceiptIndices;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface ParReceiptIndicesRepository extends CrudRepository<ParReceiptIndices, BigDecimal> {
    List<ParReceiptIndices> findAllByCustomerTypeId(BigDecimal customerType, Sort sort);

}
