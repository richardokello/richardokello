package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsContactPerson;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UfsContactPersonRepository extends CrudRepository<UfsContactPerson,Long> {

    List<UfsContactPerson> findByCustomerOutletIdIsInAndIntrash(List<Long> outletIds,String intrash);

}
