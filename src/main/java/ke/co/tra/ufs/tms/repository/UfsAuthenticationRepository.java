package ke.co.tra.ufs.tms.repository;

import ke.co.tra.ufs.tms.entities.UfsAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UfsAuthenticationRepository extends JpaRepository<UfsAuthentication,Long> {

    UfsAuthentication findByUsername(String username);
}
