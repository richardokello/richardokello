package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.UfsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UfsUserRepository extends JpaRepository<UfsUser,Long> {

}
