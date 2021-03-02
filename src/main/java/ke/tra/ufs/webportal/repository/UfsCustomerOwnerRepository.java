package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsCustomerOwners;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface UfsCustomerOwnerRepository extends CrudRepository<UfsCustomerOwners, Long> {

    public UfsCustomerOwners findByCustomerIdsAndIntrash(BigDecimal customerIds, String intrash);

    public UfsCustomerOwners findByUserNameAndIntrash(String username, String intrash);

    public List<UfsCustomerOwners> findByUserNameInAndIntrash(Set<String> username, String intrash);

    List<UfsCustomerOwners> findByIntrashAndCustomerIds(String intrash, BigDecimal customerIds);
}
