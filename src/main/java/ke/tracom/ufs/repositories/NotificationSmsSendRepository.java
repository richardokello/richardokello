package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.NotificationSmsSend;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationSmsSendRepository extends CrudRepository<NotificationSmsSend,Long> {

}
