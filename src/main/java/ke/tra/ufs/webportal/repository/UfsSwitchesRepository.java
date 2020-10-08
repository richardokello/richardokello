package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsSwitches;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UfsSwitchesRepository extends CrudRepository<UfsSwitches,Long> {

    UfsSwitches findBySwitchName(String switchName);
}
