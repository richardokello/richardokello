package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.NotificationEmailSend;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NotificationEmailSendRepository extends CrudRepository<NotificationEmailSend,Long> {

}
