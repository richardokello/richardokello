package ke.tra.com.tsync.repository;

import ke.tra.com.tsync.entities.UfsCustomerOwners;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UfsCustomerOwnersRepository extends CrudRepository<UfsCustomerOwners, Long> {
    Optional<UfsCustomerOwners>findByCustomerIds(Long customerIds);
}
