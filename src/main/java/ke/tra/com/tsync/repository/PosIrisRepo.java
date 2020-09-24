package ke.tra.com.tsync.repository;

import ke.tra.com.tsync.entities.PosIris;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PosIrisRepo extends JpaRepository<PosIris, Long> {
}
