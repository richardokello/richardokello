package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsCustomerOwners;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface UfsCustomerOwnerRepository extends CrudRepository<UfsCustomerOwners, Long> {

    public UfsCustomerOwners findByCustomerIdsAndIntrash(BigDecimal customerIds, String intrash);

    public UfsCustomerOwners findByUserNameAndIntrash(String username, String intrash);

    public List<UfsCustomerOwners> findByUserNameInAndIntrash(Set<String> username, String intrash);

    List<UfsCustomerOwners> findByIntrashAndCustomerIds(String intrash, BigDecimal customerIds);


    @Query("SELECT u FROM #{#entityName} u WHERE u.actionStatus like ?1% "
            + "AND STR(COALESCE(u.customerIds, -1)) LIKE ?2% AND "
            + "(u.directorName LIKE %?3% OR u.directorPrimaryContactNumber LIKE %?3% OR u.directorIdNumber LIKE %?3% OR u.directorEmailAddress LIKE %?3% OR u.userName LIKE %?3%) AND lower(u.intrash) = lower(?4) ")
    Page<UfsCustomerOwners> findAllByCustomerId(String actionStatus, String customerIds,  String toLowerCase, String no, Pageable pg);

}
