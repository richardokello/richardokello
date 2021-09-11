package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsBankBranches;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UfsBankBranchesRepository extends CrudRepository<UfsBankBranches,Long> {

    public List<UfsBankBranches> findByIntrash(String intrash);

    @Query("SELECT u FROM UfsBankBranches u WHERE u.id =?2")
    public UfsBankBranches findByBranchId(Long id);

    UfsBankBranches findByNameAndIntrash(String name,String intrash);

}
