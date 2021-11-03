package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<UfsUser, Long> {

    public List<UfsUser> findByUserTypeIdAndIntrash(BigDecimal userTypeId, String intrash);

    public UfsUser findByUserId(Long userId);

    public List<UfsUser> findByIntrash(String intrash);
}
