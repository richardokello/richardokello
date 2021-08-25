package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.CrdbBillers;
import ke.co.tra.ufs.tms.entities.UfsDeviceMake;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CrdbBillersRepository extends CrudRepository<CrdbBillers,Long> {

    /**
     * Filter crdb billers by code,biller,requestDirection, status, search key and date
     * @param pg
     * @param needle
     * @return
     */

    @Query("SELECT c FROM CrdbBillers c WHERE c.status !='200' AND c.code LIKE ?1% AND c.biller LIKE ?2% AND c.requestDirection LIKE ?3% AND (c.tid LIKE %?4% OR c.mid LIKE %?4% OR c.amount LIKE %?4% OR c.owner LIKE %?4%) "
            + "AND c.insertTime BETWEEN ?5 AND ?6")
    public Page<CrdbBillers> findAllPendingRetries(String code,String biller,String requestDirection,String needle,Date from,Date to,Pageable pg);

}
