package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.UfsCustomerOutlet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UfsCustomerOutletRepository extends CrudRepository<UfsCustomerOutlet, Long> {

    public UfsCustomerOutlet findBycustomerIds(BigDecimal customerId);

    public UfsCustomerOutlet findByIdAndIntrash(Long id, String intrash);

    public UfsCustomerOutlet findByCustomerIdsAndIntrash(BigDecimal customerId, String intrash);

    List<UfsCustomerOutlet> findOutletsByCustomerIdsAndIntrash(BigDecimal customerId, String intrash);

    List<UfsCustomerOutlet> findAllByIdIn(List<Long> id);
}
