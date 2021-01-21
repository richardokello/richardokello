package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsEdittedRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UfsEdittedRecordRepository extends JpaRepository<UfsEdittedRecord,Long> {
}
