package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsCustomerOwners;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UfsCustomerOwnerRepository extends CrudRepository<UfsCustomerOwners,Long> {

    public UfsCustomerOwners findByCustomerIdsAndIntrash(BigDecimal customerIds,String intrash);

    List<UfsCustomerOwners> findByIntrashAndCustomerIds(String intrash,BigDecimal customerIds);
}
