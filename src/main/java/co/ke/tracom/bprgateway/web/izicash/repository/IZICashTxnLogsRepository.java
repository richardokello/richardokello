package co.ke.tracom.bprgateway.web.izicash.repository;

import co.ke.tracom.bprgateway.web.izicash.entities.IZICashTxnLogs;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IZICashTxnLogsRepository extends CrudRepository<IZICashTxnLogs, Long> {
}
