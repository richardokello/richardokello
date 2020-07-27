package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsContactPerson;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UfsContactPersonRepository extends CrudRepository<UfsContactPerson,Long> {
}
