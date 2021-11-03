package ke.co.tra.ufs.tms.repository;



import ke.co.tra.ufs.tms.entities.UfsSwitches;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UfsSwitchesRepository extends CrudRepository<UfsSwitches,Long> {
}
