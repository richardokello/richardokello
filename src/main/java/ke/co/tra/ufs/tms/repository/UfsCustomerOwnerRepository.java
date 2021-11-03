package ke.co.tra.ufs.tms.repository;


import ke.co.tra.ufs.tms.entities.UfsCustomerOwners;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UfsCustomerOwnerRepository extends CrudRepository<UfsCustomerOwners, Long> {

    public UfsCustomerOwners findByCustomerIdsAndIntrash(BigDecimal customerIds, String intrash);

    public UfsCustomerOwners findByIdAndIntrash(Long id, String intrash);

    List<UfsCustomerOwners> findAllByCustomerIdsAndIntrash(BigDecimal customerIds, String intrash);
}
