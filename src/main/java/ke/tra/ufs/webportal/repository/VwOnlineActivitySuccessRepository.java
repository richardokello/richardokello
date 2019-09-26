package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.views.VwOnlineActivitySuccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;


@Repository
public interface VwOnlineActivitySuccessRepository extends JpaRepository<VwOnlineActivitySuccess, BigInteger> {

}
