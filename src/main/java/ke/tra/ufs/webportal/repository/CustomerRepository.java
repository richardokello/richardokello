package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsCustomer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<UfsCustomer,Long> {

    public List<UfsCustomer> findByIntrash(String intrash);

    public UfsCustomer findByCustomerId(Long id);
}
