package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.UfsCustomer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UfsCustomerRepository extends CrudRepository<UfsCustomer,Long> {

}
