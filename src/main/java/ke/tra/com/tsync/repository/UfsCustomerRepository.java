package ke.tra.com.tsync.repository;


import ke.tra.com.tsync.entities.UfsCustomer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UfsCustomerRepository extends CrudRepository<UfsCustomer,Long> {


}
