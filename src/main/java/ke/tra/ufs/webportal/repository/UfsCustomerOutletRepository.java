package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsCustomerOutlet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UfsCustomerOutletRepository extends CrudRepository<UfsCustomerOutlet,Long> {

    public List<UfsCustomerOutlet> findByIntrash(String intrash);

    public UfsCustomerOutlet findByCustomerIds(BigDecimal customerId);

    public UfsCustomerOutlet findByOutletCode(String outletCode);

}
