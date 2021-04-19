package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsPosUserIdTracker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UfsPosUserIdTrackerRepository extends CrudRepository<UfsPosUserIdTracker, Long> {
    /**
     * @return
     */
//    @Query(value = "SELECT u from UfsPosUserIdTracker u order by u.Id desc LIMIT 1", nativeQuery = true)
//    UfsPosUserIdTracker findTopByOrderByIdDesc();
    UfsPosUserIdTracker findTopByOrderByCurrentPosUserIdDesc();
}
