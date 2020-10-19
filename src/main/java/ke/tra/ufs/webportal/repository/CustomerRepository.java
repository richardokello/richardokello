package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsCustomer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<UfsCustomer,Long> {

    public List<UfsCustomer> findByIntrash(String intrash);

    public UfsCustomer findByCustomerId(Long id);

    public List<UfsCustomer> findByActionAndActionStatus(String action, String actionStatus);

    Optional<UfsCustomer> findByBusinessNameAndIntrash(String businessName,String intrash);

    Optional<UfsCustomer> findByBusinessLicenceNumberAndIntrash(String businessLicenceNumber,String intrash);

    Optional<UfsCustomer> findByLocalRegistrationNumberAndIntrash(String localRegistrationNumber,String intrash);
}
