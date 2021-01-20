package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.UfsTerminalHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UfsTerminalHistoryRepository extends CrudRepository<UfsTerminalHistory,Long> {

}
