package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsCustomerOutlet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface UfsCustomerOutletRepository extends CrudRepository<UfsCustomerOutlet,Long> {

    public List<UfsCustomerOutlet> findByIntrash(String intrash);

    public UfsCustomerOutlet findByCustomerIds(BigDecimal customerId);

    public UfsCustomerOutlet findByIdAndIntrash(Long outletId, String intrash);

    public UfsCustomerOutlet findByOutletCode(String outletCode);

   public UfsCustomerOutlet findByCustomerIdsAndIntrash(BigDecimal customerId,String intrash);

    List<UfsCustomerOutlet> findOutletsByCustomerIdsAndIntrash(BigDecimal customerId, String intrash);

    List<UfsCustomerOutlet> findOutletsByCustomerIdsInAndIntrash(List<BigDecimal> customerId, String intrash);

    @Query("SELECT u FROM #{#entityName} u WHERE u.actionStatus like ?1% "
            + "AND STR(COALESCE(u.customerIds, -1)) LIKE ?2% AND u.createdAt BETWEEN ?3 AND ?4 AND "
            + "(u.outletName LIKE %?5% OR u.outletCode LIKE %?5% OR u.latitude LIKE %?5% OR u.longitude LIKE %?5%) AND lower(u.intrash) = lower(?6) ")
    Page<UfsCustomerOutlet> findAllByCustomerId(String actionStatus, String customerIds, Date from, Date to, String toLowerCase, String no, Pageable pg);
}
