package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsGlsBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UfsGlsBatchRepository extends JpaRepository<UfsGlsBatch,Long> {
}
