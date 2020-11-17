package repository;

import entities.UfsCustomerOwners;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UfsCustomerOwnersRepository extends CrudRepository<UfsCustomerOwners, Long> {
    Optional<UfsCustomerOwners>findByCustomerIds(Long customerIds);
}
