package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsBankBranches;
<<<<<<< HEAD
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
=======
>>>>>>> brb-webportal
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UfsBankBranchesRepository extends CrudRepository<UfsBankBranches,Long> {

    public List<UfsBankBranches> findByIntrash(String intrash);

    @Query("SELECT u FROM UfsBankBranches u WHERE u.id =?1")
    public UfsBankBranches findByBranchId(Long id);

    UfsBankBranches findByNameAndIntrash(String name,String intrash);

    @Query("SELECT u FROM #{#entityName} u WHERE u.actionStatus LIKE ?1%"
            + " AND (u.name LIKE %?2% OR u.code LIKE %?2% OR u.tenantIds LIKE %?2%) AND lower(u.intrash) = lower(?3)"+
            "AND u.createdAt BETWEEN ?4 AND ?5 AND  u.action!='Suspend'")
    Page<UfsBankBranches> findAll(String actionStatus, String needle, String intrash, Date from, Date to, Pageable pg);

}
