package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UfsEdittedRecordRepository extends CrudRepository<UfsEdittedRecord, Long> {

    /**
     * Used to fetch edit entity
     * @param entity
     * @param entityId
     * @return
     */
    public UfsEdittedRecord findByUfsEntityAndEntityId(String entity, String entityId);

    /**
     * Used to delete record by entity and entityId
     * @param entity
     * @param entityId
     */
    public void deleteByUfsEntityAndEntityId(String entity, String entityId);
}
