package ke.co.tra.ufs.tms.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import ke.co.tra.ufs.tms.entities.TmsEstateHierarchy;
import ke.co.tra.ufs.tms.entities.UfsProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Owori Juma
 */
public interface BusinessUnitRepository extends CrudRepository<TmsEstateHierarchy, BigDecimal> {

    /**
     *
     * @param unitName
     * @param productId
     * @param intrash
     * @return
     */
    public TmsEstateHierarchy findByunitNameAndProductIdAndIntrash(String unitName, UfsProduct productId, String intrash);

    /**
     *
     * @param unitId
     * @param productId
     * @param intrash
     * @return
     */
    public TmsEstateHierarchy findByunitIdAndProductIdAndIntrash(BigDecimal unitId, UfsProduct productId, String intrash);
    /**
     *
     * @param status
     * @param actionStatus
     * @param from
     * @param to
     * @param intrash
     * @param productId
     * @param pg
     * @return
     */
    @Query("SELECT u FROM #{#entityName} u WHERE u.status like ?1% AND u.actionStatus like ?2% "
            + "AND u.creationDate BETWEEN ?3 AND ?4 AND  STR(COALESCE(u.productId, -1)) LIKE ?6% AND"
            + " lower(u.intrash) = lower(?5)")
    public Page<TmsEstateHierarchy> findAll(String status, String actionStatus, Date from, Date to, String intrash, String productId, Pageable pg);

    /**
     *
     * @param intrash
     * @param pg
     * @return
     */
    public Page<TmsEstateHierarchy> findByintrash(String intrash, Pageable pg);

    /**
     *
     * @param levelNo
     * @param productId
     * @return
     */
    public TmsEstateHierarchy findBylevelNoAndProductId(BigInteger levelNo, UfsProduct productId);

    /**
     *
     * @param intrash
     * @return
     */
    public List<TmsEstateHierarchy> findByintrash(String intrash);

    /**
     *
     * @param productId
     * @param pg
     * @return
     */
    public Page<TmsEstateHierarchy> findByproductIdOrderByUnitIdAsc(UfsProduct productId, Pageable pg);

    /**
     *
     * @param productId
     * @return
     */
    public List<TmsEstateHierarchy> findByproductIdOrderByLevelNoDesc(UfsProduct productId);

}
