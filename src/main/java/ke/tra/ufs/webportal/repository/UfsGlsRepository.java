package ke.tra.ufs.webportal.repository;

import ke.tra.ufs.webportal.entities.UfsGls;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UfsGlsRepository extends CrudRepository<UfsGls, Long> {

    UfsGls findByGlCodeAndIntrash(String code, String intrash);
}
