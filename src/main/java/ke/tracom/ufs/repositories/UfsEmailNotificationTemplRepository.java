package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsEmailNotificationTempl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UfsEmailNotificationTemplRepository extends CrudRepository<UfsEmailNotificationTempl,Long> {

}
