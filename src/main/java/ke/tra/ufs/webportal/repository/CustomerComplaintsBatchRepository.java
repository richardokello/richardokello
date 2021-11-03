package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsCustomerComplaintsBatch;
import ke.tra.ufs.webportal.entities.UfsTrainedAgentsBatch;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerComplaintsBatchRepository extends CrudRepository<UfsCustomerComplaintsBatch,Long> {

}
