package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsContactPerson;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface UfsContactPersonRepository extends CrudRepository<UfsContactPerson, Long> {

    List<UfsContactPerson> findByCustomerOutletIdIsInAndIntrash(List<Long> outletIds, String intrash);

    List<UfsContactPerson> findByIdInAndIntrash(List<Long> id, String intrash);

    UfsContactPerson findByUserNameAndIntrash(String username, String intrash);

    UfsContactPerson findByIdAndIntrash(Long id, String intrash);

    List<UfsContactPerson> findByUserNameInAndIntrash(Set<String> usernames, String intrash);
}
