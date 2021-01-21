package ke.tracom.ufs.repositories;

import ke.tracom.ufs.entities.UfsPasswordHistory;
import ke.tracom.ufs.entities.UfsUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UfsPasswordHistoryRepository extends CrudRepository<UfsPasswordHistory,Long> {

    List<UfsPasswordHistory> findByuserId(UfsUser user, Pageable pg);
}
