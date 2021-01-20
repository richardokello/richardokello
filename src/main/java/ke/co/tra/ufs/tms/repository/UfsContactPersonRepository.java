package ke.co.tra.ufs.tms.repository;


import ke.co.tra.ufs.tms.entities.UfsContactPerson;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UfsContactPersonRepository extends CrudRepository<UfsContactPerson,Long> {

    List<UfsContactPerson> findByCustomerOutletIdIsInAndIntrash(List<Long> outletIds, String intrash);

    List<UfsContactPerson> findByCustomerOutletIdAndIntrash(Long outletId, String intrash);

}
