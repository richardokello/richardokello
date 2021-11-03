package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.CustomerOwnersCrime;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UfsCustomerOwnerCrimeRepository extends CrudRepository<CustomerOwnersCrime,Long> {


}
