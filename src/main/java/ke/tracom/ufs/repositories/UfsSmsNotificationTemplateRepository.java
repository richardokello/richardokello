package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsSmsNotificationTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UfsSmsNotificationTemplateRepository extends CrudRepository<UfsSmsNotificationTemplate,Long> {

}
