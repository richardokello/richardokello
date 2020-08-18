package ke.tra.com.tsync.repository;

import ke.tra.com.tsync.entities.UfsContactPerson;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UfsContactPersonRepository extends CrudRepository<UfsContactPerson,Long> {
    Optional<UfsContactPerson> findByOutletIdsAndIntrashAndActionStatus(Long outletIds, String intrash, String actionStatus);
}
