package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import java.util.List;
import ke.co.tra.ufs.tms.entities.TmsParamDefinition;
import ke.co.tra.ufs.tms.entities.UfsProduct;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Owori Juma
 */
public interface TmsParamDefinitionRepository extends CrudRepository<TmsParamDefinition, BigDecimal> {

    /**
     *
     * @param productId
     * @return
     */
    public List<TmsParamDefinition> findByproductId(UfsProduct productId);

}
