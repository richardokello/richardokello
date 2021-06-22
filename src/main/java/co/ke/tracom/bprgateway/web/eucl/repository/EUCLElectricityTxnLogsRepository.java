package co.ke.tracom.bprgateway.web.eucl.repository;

import co.ke.tracom.bprgateway.web.eucl.entities.EUCLElectricityTxnLogs;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EUCLElectricityTxnLogsRepository extends CrudRepository<EUCLElectricityTxnLogs, Long> {
}
