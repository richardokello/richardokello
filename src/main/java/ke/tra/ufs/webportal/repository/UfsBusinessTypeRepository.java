package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsBusinessType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UfsBusinessTypeRepository extends CrudRepository<UfsBusinessType,Long> {
    UfsBusinessType findByBusinessTypeAndIntrash(String businessType,String intrash);
}
