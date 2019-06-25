package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsCustomerOwners;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UfsCustomerOwnerRepository extends CrudRepository<UfsCustomerOwners,Long> {


}
