package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsTrainedAgents;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UfsTrainedAgentsRepository extends CrudRepository<UfsTrainedAgents,Long> {

}
