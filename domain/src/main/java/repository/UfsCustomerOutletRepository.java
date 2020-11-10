package repository;

import entities.UfsCustomerOutlet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UfsCustomerOutletRepository extends CrudRepository<UfsCustomerOutlet,Long> {

    public Optional<UfsCustomerOutlet> findById(Long id);
}
