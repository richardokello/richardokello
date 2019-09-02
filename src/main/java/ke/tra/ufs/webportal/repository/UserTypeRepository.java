package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsUserType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface UserTypeRepository extends CrudRepository<UfsUserType, BigDecimal> {

    public UfsUserType findByUserType(String userType);

}
